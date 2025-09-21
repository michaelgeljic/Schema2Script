package model;

import java.io.File;
import java.util.Arrays;

public class XmlParser implements SchemaParser {

    @Override
    public SchemaObject parse(File schemaFile) throws SchemaParsingException{
        // Simulate reading the XML file by logging its path
        System.out.println("Simulating parsing of XML schema file: " + schemaFile.getAbsolutePath());


        if (!schemaFile.exists()) {
            throw new SchemaParsingException("XML file not found: " + schemaFile.getAbsolutePath());
        }
        if (!schemaFile.getName().toLowerCase().endsWith(".xml")) {
            throw new SchemaParsingException("Invalid file format. Expected a .xml file.");
        }

        
        // Return a mock model.SchemaObject (hardcoded)
        return createMockSchemaObject();
    }

    /**
     * Creates a mock model.SchemaObject that mimics a parsed schema.
     */
    private SchemaObject createMockSchemaObject() {
        // Hardcode some fields to simulate schema fields
        return new SchemaObject(
                "MockSchemaObject",
                Arrays.asList("field1", "field2", "field3")
        );
    }
}
