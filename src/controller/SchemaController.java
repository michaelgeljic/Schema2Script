package controller;

import model.*;

import view.SchemaView;

import java.io.File;

public class SchemaController {
    private final SchemaModel model;
    private final SchemaView view;
    private final ParserFactory parserFactory;

    public SchemaController(SchemaModel model, SchemaView view) {
        this.model = model;
        this.view = view;
        this.parserFactory = new ParserFactory();
    }

    /**
     * Handles the upload of a schema file:
     *  - chooses parser by file extension
     *  - updates model with parsed schema
     *  - notifies view of success or error
     */
    public void handleSchemaUpload(File schemaFile) {
        try {
            String format = detectFormat(schemaFile);
            SchemaParser parser = parserFactory.get(format);

            SchemaObject schemaObject = parser.parse(schemaFile);

            // update model
            model.setSchema(schemaObject);

            // notify view
            view.showSuccess("Schema parsed successfully: " + schemaObject.getName());

        } catch (SchemaParsingException e) {
            view.showError("Parsing failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            view.showError("Invalid input: " + e.getMessage());
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
            throw new IllegalArgumentException("Unsupported file extension for: " + name);
        }
    }
}
