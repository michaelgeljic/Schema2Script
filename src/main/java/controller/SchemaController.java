package main.java.controller;

import main.java.model.*;

import main.java.view.SchemaView;

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
     *  - updates main.java.model with parsed schema
     *  - notifies main.java.view of success or error
     */
    public void handleSchemaUpload(File schemaFile) {
        try {
            if (schemaFile == null || !schemaFile.exists() || !schemaFile.canRead()) {
            throw new IllegalArgumentException("File is missing or cannot be read.");
        }
        String format = detectFormat(schemaFile);
        SchemaParser parser = parserFactory.get(format);

        SchemaObject schemaObject = parser.parse(schemaFile);

        model.setSchema(schemaObject);
        view.showSuccess("Schema parsed successfully: " + schemaObject.getName());
    
    } catch(SchemaParsingException e){
        view.showError("Parsing failed:" + e.getMessage());

    } catch (IllegalArgumentException e){
        view.showError("Invalid input:" + e.getMessage());
    } catch (Exception e){
        view.showError("Unexpected error:" + e.getMessage());
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
