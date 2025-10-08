package main.java.model;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Generates MySQL SQL statements from a {@link SchemaObject}.
 */
public class MySQLGenerator implements ISqlGenerator {

    private static final Logger logger = LogManager.getLogger(MySQLGenerator.class);

    @Override
    public String generateCreateTable(SchemaObject schema) {
        if (schema == null) {
            logger.error("SchemaObject is null, cannot generate CREATE TABLE statement.");
            throw new IllegalArgumentException("Cannot generate SQL: schema is null.");
        }

        if (schema.getName() == null || schema.getName().trim().isEmpty()) {
            logger.error("Schema name is missing.");
            throw new IllegalArgumentException("Cannot generate SQL: schema name is missing or empty.");
        }

        List<String> fields = schema.getFields();
        if (fields == null || fields.isEmpty()) {
            logger.error("Schema '{}' has no fields defined.", schema.getName());
            throw new IllegalArgumentException(
                "Cannot generate SQL: schema '" + schema.getName() + "' must contain at least one field."
            );
        }

        logger.info("Starting CREATE TABLE generation for schema: {}", schema.getName());
        logger.debug("Schema '{}' has {} fields: {}", schema.getName(), fields.size(), fields);

        StringBuilder sb = new StringBuilder();
        try {
            sb.append("CREATE TABLE ").append(schema.getName()).append(" (\n");

            for (int i = 0; i < fields.size(); i++) {
                String field = fields.get(i);

                if (field == null || field.trim().isEmpty()) {
                    logger.warn("Encountered null or empty field name at index {} in schema '{}'. Skipping.", i, schema.getName());
                    continue;
                }

                logger.debug("Adding field to CREATE TABLE statement: {}", field);
                sb.append("    ").append(field).append(" VARCHAR(255)");

                if (i < fields.size() - 1) {
                    sb.append(",\n");
                }
            }

            sb.append("\n);");

            logger.info("Successfully generated CREATE TABLE statement for schema: {}", schema.getName());
            logger.debug("Generated SQL:\n{}", sb.toString());

        } catch (Exception e) {
            logger.error("Error while generating CREATE TABLE statement for schema: {}", schema.getName(), e);
            throw new RuntimeException(
                "An unexpected error occurred while generating CREATE TABLE for schema '" + schema.getName() + "': " + e.getMessage(),
                e
            );
        }

        return sb.toString();
    }

    @Override
    public String mapDataType(String genericType) {
        if (genericType == null || genericType.trim().isEmpty()) {
            logger.error("Generic type is null or empty.");
            throw new IllegalArgumentException("Cannot map data type: input type is null or empty.");
        }

        switch (genericType.toLowerCase()) {
            case "int":
                return "INT";
            case "string":
                return "VARCHAR(255)";
            case "bool":
                return "BOOLEAN";
            default:
                logger.warn("Unknown generic type '{}'. Defaulting to TEXT.", genericType);
                return "TEXT";
        }
    }

    @Override
    public String generateConstraints(SchemaObject schema) {
        // Placeholder for future constraint generation
        return "";
    }
}