/**
 * Package for schema parsing utilities.
 */
package parser;

import java.io.File;
import java.util.Arrays;

/**
 * Parses a JSON schema file and generates a {@link SchemaObject}.
 * This class implements the {@link SchemaParser} interface to provide
 * specific functionality for JSON schema files.
 */
public class JSONParser implements SchemaParser {

    /**
     * Parses the specified JSON schema file.
     * <p>
     * This method simulates the parsing of a JSON schema file.
     * In a real-world scenario, it would read and validate the file's contents
     * to construct a {@link SchemaObject} that represents the schema.
     * </p>
     *
     * @param schemaFile The {@link File} object representing the JSON schema to be
     *                   parsed.
     * @return A {@link SchemaObject} representing the parsed schema.
     */
    @Override
    public SchemaObject parse(File schemaFile) throws SchemaParsingException{
        System.out.println("Simulating parsing of a JSON schema file: " + schemaFile.getAbsolutePath());

        if (!schemaFile.exists()) {
            throw new SchemaParsingException("JSON file not found: " + schemaFile.getAbsolutePath());
        }
        if (!schemaFile.getName().toLowerCase().endsWith(".json")) {
            throw new SchemaParsingException("Invalid file format. Expected a .json file.");
        }

        return createMockSchemaObject();
    }

    /**
     * Creates and returns a mock {@link SchemaObject}.
     * <p>
     * This is a helper method used for demonstration purposes. It
     * generates a dummy schema object with predefined fields.
     * </p>
     *
     * @return A new {@link SchemaObject} instance for testing or demonstration.
     */
    private SchemaObject createMockSchemaObject() {
        return new SchemaObject(
                "MockSchemaObject",
                Arrays.asList("field1", "field2", "field3"));
    }

}