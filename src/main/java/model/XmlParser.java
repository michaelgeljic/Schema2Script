package main.java.model;

import java.io.File;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XmlParser implements SchemaParser {

    // Define a logger for this class
    private static final Logger logger = LogManager.getLogger(XmlParser.class);

    @Override
    public SchemaObject parse(File schemaFile) throws SchemaParsingException {
        logger.info("Starting XML schema parsing for file: {}", schemaFile.getAbsolutePath());

        if (!schemaFile.exists()) {
            logger.error("XML file not found: {}", schemaFile.getAbsolutePath());
            throw new SchemaParsingException("XML file not found: " + schemaFile.getAbsolutePath());
        }

        if (!schemaFile.getName().toLowerCase().endsWith(".xml")) {
            logger.warn("Invalid file format detected: {}", schemaFile.getName());
            throw new SchemaParsingException("Invalid file format. Expected a .xml file.");
        }

        logger.debug("Successfully validated XML file: {}", schemaFile.getName());

        // Return a mock SchemaObject (hardcoded)
        SchemaObject schemaObject = createMockSchemaObject();
        logger.info("Parsed SchemaObject created: {}", schemaObject.getName());
        return schemaObject;
    }

    /**
     * Creates a mock SchemaObject that mimics a parsed schema.
     */
    private SchemaObject createMockSchemaObject() {
        logger.debug("Creating mock SchemaObject with fields [field1, field2, field3]");
        return new SchemaObject(
                "MockSchemaObject",
                Arrays.asList("field1", "field2", "field3")
        );
    }
}
