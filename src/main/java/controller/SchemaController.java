package main.java.controller;

import main.java.model.*;
import main.java.view.SchemaApp;

import java.io.File;

public class SchemaController {
    private final SchemaModel model;
    private final SchemaApp view;

    public SchemaController(SchemaModel model, SchemaApp view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Handles the upload of a schema file:
     *  - chooses parser by file extension
     *  - updates model with parsed schema
     *  - notifies view of success or error
     */
    public void handleSchemaUpload(File schemaFile) {
        try {
            validateFile(schemaFile);

            String format = detectFormat(schemaFile);
            SchemaParser parser = ParserFactory.get(format);

            SchemaObject schemaObject = parser.parse(schemaFile);

            model.setSchema(schemaObject);

        } catch (SchemaParsingException e) {
            view.showError("Schema parsing failed: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            view.showError("Invalid input: " + e.getMessage());

        } catch (FileUploadException e) {
            view.showError("File upload error: " + e.getMessage());

        } catch (Exception e) {
            view.showError("Unexpected error occurred. Details: " + e.getMessage());
        }
    }

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
     * Determine the schema format based on file extension.
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