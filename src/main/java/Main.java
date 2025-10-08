package main.java;

import main.java.controller.SchemaController;
import main.java.model.*;
import main.java.view.SchemaApp;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // MVC components
        SchemaModel model = new SchemaModel();
        SchemaApp view = new SchemaApp();
        SchemaController controller = new SchemaController(model, view);

        // Choose schema file
        File schemaFile = new File("src/main/resources/person.json");
        // File schemaFile = new File("src/main/resources/person.xml");

        MySQLGenerator generator = new MySQLGenerator();
        try {
            // delegate work to controller
            controller.handleSchemaUpload(schemaFile);
            SchemaObject schema = model.getSchema();
            generator.generateCreateTable(schema);

            // summary after parsing
            if (model.getSchema() != null) {
                view.showParsedSummary(model.getSchema());
            }
        } catch (RuntimeException e) {
            System.err.println("Fatal error: " + e.getMessage());
        }

    }
}
