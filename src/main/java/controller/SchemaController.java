package controller;

import model.*;
import view.SchemaView;
import exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Controller class in the MVC architecture that mediates between the
 * {@link SchemaModel} (application data and logic) and the {@link SchemaView} (UI layer).
 * <p>
 * It is responsible for handling user actions related to schema file uploads,
 * validating files, detecting file formats, delegating parsing to the correct
 * {@link SchemaParser}, and updating both the model and the view.
 * </p>
 */
public class SchemaController {
    private final SchemaModel model;
    private final SchemaView view;
    private static final Logger logger = LogManager.getLogger(SchemaController.class);

    /**
     * Constructs a new {@code SchemaController}.
     *
     * @param model the {@link SchemaModel} that holds the application state
     * @param view  the {@link SchemaView} responsible for displaying messages and results
     */
    public SchemaController(SchemaModel model, SchemaView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Handles the upload of a schema file by:
     * <ul>
     *     <li>Validating the file (existence, readability, format)</li>
     *     <li>Determining the format (JSON or XML)</li>
     *     <li>Delegating parsing to the correct {@link SchemaParser}</li>
     *     <li>Updating the {@link SchemaModel} with the parsed schema</li>
     *     <li>Notifying the {@link SchemaView} of the outcome</li>
     * </ul>
     *
     * @param schemaFile the schema file chosen by the user
     */
    public void handleSchemaUpload(File schemaFile) {
        try {
            validateFile(schemaFile);

            String format = detectFormat(schemaFile);
            SchemaParser parser = ParserFactory.get(format);

            SchemaObject schemaObject = parser.parse(schemaFile);
            model.setSchema(schemaObject);

            view.showParsedSummary(schemaObject);

        } catch (SchemaParsingException e) {
            view.showError("Schema could not be parsed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            view.showError("Invalid input: " + e.getMessage());
        } catch (FileUploadException e) {
            view.showError("File upload failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            view.showError("An unexpected error occurred. Please try again.");
        }
    }

    /**
     * Validates that the given file exists, is a regular file, and is readable.
     *
     * @param file the file to validate
     * @throws FileUploadException if the file is {@code null}, missing, invalid, or unreadable
     */
    private void validateFile(File file) {
        if (file == null) {
            throw new FileUploadException("No file provided. Please select a schema file (.json or .xml).");
        }
        if (!file.exists()) {
            throw new FileUploadException("File not found: " + file.getAbsolutePath());
        }
        if (!file.isFile()) {
            throw new FileUploadException("Not a valid file: " + file.getAbsolutePath());
        }
        if (!file.canRead()) {
            throw new FileUploadException("File cannot be read: " + file.getAbsolutePath());
        }
    }

    /**
     * Detects the schema format based on the file extension.
     *
     * @param file the schema file
     * @return the detected format: {@code "xml"} or {@code "json"}
     * @throws IllegalArgumentException if the file type is unsupported
     */
    private String detectFormat(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".xml")) {
            return "xml";
        } else if (name.endsWith(".json")) {
            return "json";
        } else {
            throw new IllegalArgumentException(
                    "Unsupported file type: " + name + ". Please upload a .json or .xml schema file."
            );
        }
    }
}
