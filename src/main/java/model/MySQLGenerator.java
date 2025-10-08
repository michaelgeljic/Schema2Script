package main.java.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A generator that produces MySQL-compatible SQL statements
 * from a given {@link SchemaObject}.
 * <p>
 * This class currently supports:
 * <ul>
 *     <li>Generating a basic {@code CREATE TABLE} statement
 *         with all fields mapped to {@code VARCHAR(255)} by default.</li>
 *     <li>Mapping simple generic types (e.g., {@code int}, {@code string}, {@code bool})
 *         to corresponding MySQL data types.</li>
 * </ul>
 * <p>
 * Future enhancements may include richer type mapping and constraint generation.
 * </p>
 */
public class MySQLGenerator implements ISqlGenerator {

    private static final Logger logger = LogManager.getLogger(MySQLGenerator.class);

    /**
     * Generates a {@code CREATE TABLE} SQL statement for the given schema.
     * <p>
     * Each field in the schema is represented as a column with type
     * {@code VARCHAR(255)} by default. More advanced type inference can
     * be handled using {@link #mapDataType(String)} in future iterations.
     * </p>
     *
     * @param schema the {@link SchemaObject} containing the table name and fields
     * @return a valid MySQL {@code CREATE TABLE} statement as a {@link String}
     * @throws IllegalArgumentException if the schema is {@code null},
     *                                  has no name, or contains no fields
     * @throws RuntimeException if an unexpected error occurs while building the SQL
     */
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


        Set<String> seen = new HashSet<>();
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                throw new IllegalArgumentException("Field names cannot be null or empty.");
            }
            String normalized = field.toLowerCase();
            if (!seen.add(normalized)) {
                logger.error("Duplicate field '{}' detected in schema '{}'", field, schema.getName());
                throw new IllegalArgumentException(
                        "Cannot generate SQL: duplicate field '" + field + "' in schema '" + schema.getName() + "'."
                );
            }
        }

        logger.info("Starting CREATE TABLE generation for schema: {}", schema.getName());
        logger.debug("Schema '{}' has {} fields: {}", schema.getName(), fields.size(), fields);

        StringBuilder sb = new StringBuilder();
        try {

            sb.append("CREATE TABLE `").append(schema.getName()).append("` (\n");

            for (int i = 0; i < fields.size(); i++) {
                String field = fields.get(i);

                logger.debug("Adding field to CREATE TABLE statement: {}", field);


                sb.append("    `").append(field).append("` VARCHAR(255)");

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


    /**
     * Maps a generic data type (e.g., {@code int}, {@code string}, {@code bool})
     * to a MySQL-compatible data type.
     *
     * @param genericType the generic type name
     * @return the corresponding MySQL data type
     * @throws IllegalArgumentException if {@code genericType} is {@code null} or empty
     */
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

    /**
     * Generates SQL constraints for the given schema.
     * <p>
     * This is currently a placeholder for future work,
     * such as adding primary keys, foreign keys, or uniqueness constraints.
     * </p>
     *
     * @param schema the schema object for which to generate constraints
     * @return a {@link String} containing SQL constraints, or an empty string
     */
    @Override
    public String generateConstraints(SchemaObject schema) {
        // Placeholder for future constraint generation
        return "";
    }
}
