package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Parses a JSON schema file using Jackson and generates a {@link SchemaObject}.
 */
public class JSONParser implements SchemaParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SchemaObject parse(File schemaFile) throws SchemaParsingException {
        System.out.println("Parsing JSON schema file: " + schemaFile.getAbsolutePath());

        if (!schemaFile.exists()) {
            throw new SchemaParsingException("JSON file not found: " + schemaFile.getAbsolutePath());
        }
        if (!schemaFile.getName().toLowerCase().endsWith(".json")) {
            throw new SchemaParsingException("Invalid file format. Expected a .json file.");
        }

        try {
            // Read the JSON tree
            JsonNode rootNode = objectMapper.readTree(schemaFile);

            // Validate basic structure: e.g., must have "name" and "fields"
            if (!rootNode.has("name") || !rootNode.has("fields")) {
                throw new SchemaParsingException("JSON schema missing required 'name' or 'fields'.");
            }

            String name = rootNode.get("name").asText();

            List<String> fields = new ArrayList<>();
            JsonNode fieldsNode = rootNode.get("fields");
            if (!fieldsNode.isArray()) {
                throw new SchemaParsingException("'fields' must be an array.");
            }

            Iterator<JsonNode> it = fieldsNode.elements();
            while (it.hasNext()) {
                fields.add(it.next().asText());
            }

            return new SchemaObject(name, fields);

        } catch (IOException e) {
            throw new SchemaParsingException("Error reading JSON file: " + e.getMessage(), e);
        }
    }
}
