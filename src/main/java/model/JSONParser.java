package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import exception.SchemaParsingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Parses a simple JSON schema: { "name": "...", "fields": ["..."] }
 */
public class JSONParser implements SchemaParser {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(JSONParser.class);

    @Override
    public SchemaObject parse(File schemaFile) throws SchemaParsingException {
        logger.info("Starting JSON schema parsing for file: {}", schemaFile.getAbsolutePath());

        if (!schemaFile.exists()) {
            throw new SchemaParsingException(
                "The file could not be found: " + schemaFile.getAbsolutePath() +
                ". Please check the path and try again."
            );
        }
        if (!schemaFile.getName().toLowerCase().endsWith(".json")) {
            throw new SchemaParsingException(
                "Invalid file format: " + schemaFile.getName() +
                ". Please upload a valid JSON file with a .json extension."
            );
        }

        try {
            logger.debug("Reading JSON tree from {}", schemaFile.getName());
            JsonNode rootNode = objectMapper.readTree(schemaFile);

            if (rootNode == null) {
                throw new SchemaParsingException(
                    "The JSON file is empty or invalid. Please provide a valid JSON schema."
                );
            }

            // Guard against picking the array-of-tables test file
            if (!rootNode.isObject()) {
                throw new SchemaParsingException(
                    "Invalid JSON schema: expected a single object with 'name' and 'fields' at the root, " +
                    "but found " + rootNode.getNodeType() + ". If you intended to use the array-of-tables schema, " +
                    "either choose a simple schema (person.json) or upgrade the parser to support that format."
                );
            }

            // Validate required keys
            if (!rootNode.hasNonNull("name") || !rootNode.has("fields")) {
                throw new SchemaParsingException(
                    "Invalid JSON schema: missing required property 'name' or 'fields'. " +
                    "Please ensure your schema includes both."
                );
            }

            String name = rootNode.get("name").asText();
            logger.debug("Extracted schema name: {}", name);

            JsonNode fieldsNode = rootNode.get("fields");
            if (fieldsNode == null || !fieldsNode.isArray()) {
                throw new SchemaParsingException(
                    "Invalid JSON schema: 'fields' must be an array of field names."
                );
            }

            List<String> fields = new ArrayList<>();
            Iterator<JsonNode> it = fieldsNode.elements();
            int idx = 0;
            while (it.hasNext()) {
                JsonNode n = it.next();
                String f = n.isTextual() ? n.asText() : n.toString();
                fields.add(f);
                logger.trace("Field[{}] = {}", idx++, f);
            }

            SchemaObject schemaObject = new SchemaObject(name, fields);
            logger.info("Parsed SchemaObject created: {}", schemaObject.getName());
            return schemaObject;

        } catch (IOException e) {
            logger.error("Error reading JSON file {}", schemaFile.getAbsolutePath(), e);
            throw new SchemaParsingException(
                "An error occurred while reading the JSON file: " + e.getMessage() +
                ". Please ensure the file is valid JSON and try again.",
                e
            );
        }
    }
}