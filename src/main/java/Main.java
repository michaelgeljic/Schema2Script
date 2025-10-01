package main.java;

import main.java.model.*;
import main.java.view.SchemaView;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Choose which type of schema to parse (hardcoded for testing in IntelliJ)
        String type = "xml"; // or "xml"

        // Point to a file inside your project (adjust path if needed)
        File schemaFile = new File("src/main/resources/person.xml");
        // File schemaFile = new File("src/main/resources/person.xml");

        try {
            // Get the correct parser
            SchemaParser parser = ParserFactory.get(type);

            // Parse schema
            SchemaObject schema = parser.parse(schemaFile);

            // Show results
            SchemaView view = new SchemaView();


        } catch (SchemaParsingException e) {
            System.err.println("Error parsing schema: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
