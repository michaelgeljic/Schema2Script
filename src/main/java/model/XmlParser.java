package main.java.model;

import java.io.File;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Parses an XML schema file and generates a {@link SchemaObject}.
 * <p>
 * Currently this is a mock implementation returning a hardcoded schema,
 * but it includes validation for file existence and format.
 * </p>
 */
public class XmlParser implements SchemaParser {

    private static final Logger logger = LogManager.getLogger(XmlParser.class);

    @Override
    public SchemaObject parse(File schemaFile) throws SchemaParsingException {
        logger.info("Starting XML schema parsing for file: {}", schemaFile.getAbsolutePath());

        if (!schemaFile.exists()) {
            logger.error("XML file not found: {}", schemaFile.getAbsolutePath());
            throw new SchemaParsingException(
                "The XML file could not be found: " + schemaFile.getAbsolutePath() +
                ". Please check the file path and try again."
            );
        }

        if (!schemaFile.getName().toLowerCase().endsWith(".xml")) {
            logger.warn("Invalid file format detected: {}", schemaFile.getName());
            throw new SchemaParsingException(
                "Invalid file format: " + schemaFile.getName() +
                ". Please provide a valid XML schema file with a .xml extension."
            );
        }

        logger.debug("Successfully validated XML file: {}", schemaFile.getName());

        // Return a mock SchemaObject (hardcoded for now)
        SchemaObject schemaObject = createMockSchemaObject();
        logger.info("Parsed SchemaObject created: {}", schemaObject.getName());
        return schemaObject;
    }

    /**
     * Creates a mock SchemaObject that mimics a parsed schema.
     * This should be replaced with actual XML parsing in the future.
     */
    private SchemaObject createMockSchemaObject() {
        logger.debug("Creating mock SchemaObject with fields [field1, field2, field3]");
        return new SchemaObject(
            "MockSchemaObject",
            Arrays.asList("field1", "field2", "field3")
        );
    }
}