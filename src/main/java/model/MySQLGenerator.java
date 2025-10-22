package model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import exception.SqlGenerationException;

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
     * Performs validation on the schema and fields, constructs a properly formatted
     * SQL statement, and logs detailed progress. Handles duplicate or invalid field names,
     * and wraps any unexpected exceptions in a {@link SqlGenerationException}.
     * </p>
     *
     * @param schema the {@link SchemaObject} containing the table name and fields
     * @return a valid MySQL {@code CREATE TABLE} statement as a {@link String}
     * @throws IllegalArgumentException if the schema is {@code null}, has no name, or contains no valid fields
     * @throws SqlGenerationException if an unexpected error occurs during SQL construction
     */
    @Override
    public String generateCreateTable(SchemaObject schema) {
        validateSchema(schema);
        List<String> fields = schema.getFields();
        validateFields(schema.getName(), fields);

        logger.info("Starting CREATE TABLE generation for schema: {}", schema.getName());
        logger.debug("Schema '{}' has {} fields: {}", schema.getName(), fields.size(), fields);

        StringBuilder sb = new StringBuilder();
        try {
            buildCreateTableSQL(schema, fields, sb);

            logger.info("Successfully generated CREATE TABLE statement for schema: {}", schema.getName());
            if (logger.isDebugEnabled()) {
                logger.debug("Generated SQL:\n{}", sb);
            }

        } catch (Exception e) {
            handleSqlGenerationError(schema, e);
        }

        return sb.toString();
    }

    /**
     * Validates that the given {@link SchemaObject} is not null and has a valid table name.
     * <p>
     * Throws an exception if the schema is missing, unnamed, or invalid.
     * </p>
     *
     * @param schema the {@link SchemaObject} to validate
     * @throws IllegalArgumentException if the schema is {@code null} or has no valid name
     */
    private void validateSchema(SchemaObject schema) {
        if (schema == null) {
            logger.error("SchemaObject is null, cannot generate CREATE TABLE statement.");
            throw new IllegalArgumentException("Cannot generate SQL: schema is null.");
        }

        String name = schema.getName();
        if (name == null || name.trim().isEmpty()) {
            logger.error("Schema name is missing.");
            throw new IllegalArgumentException("Cannot generate SQL: schema name is missing or empty.");
        }
    }

    /**
     * Validates the list of field names within a schema.
     * <p>
     * Ensures that the list is not null or empty, and that no field name is null, empty,
     * or duplicated (case-insensitive). Logs errors when invalid input is detected.
     * </p>
     *
     * @param schemaName the name of the schema (used for error messages)
     * @param fields the list of field names to validate
     * @throws IllegalArgumentException if the field list is null, empty, or contains duplicates or invalid names
     */
    private void validateFields(String schemaName, List<String> fields) {
        if (fields == null || fields.isEmpty()) {
            logger.error("Schema '{}' has no fields defined.", schemaName);
            throw new IllegalArgumentException(
                    "Cannot generate SQL: schema '" + schemaName + "' must contain at least one field."
            );
        }

        Set<String> seen = new HashSet<>();
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                throw new IllegalArgumentException("Field names cannot be null or empty.");
            }

            String normalized = field.toLowerCase();
            if (!seen.add(normalized)) {
                logger.error("Duplicate field '{}' detected in schema '{}'", field, schemaName);
                throw new IllegalArgumentException(
                        "Cannot generate SQL: duplicate field '" + field + "' in schema '" + schemaName + "'."
                );
            }
        }
    }

    /**
     * Builds the actual {@code CREATE TABLE} SQL statement using the provided schema name and fields.
     * <p>
     * Each field is mapped to {@code VARCHAR(255)} by default.
     * </p>
     *
     * @param schema the {@link SchemaObject} containing the table name
     * @param fields the list of validated field names
     * @param sb a {@link StringBuilder} used to accumulate the SQL statement
     */
    private void buildCreateTableSQL(SchemaObject schema, List<String> fields, StringBuilder sb) {
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
    }

    /**
     * Handles unexpected errors that occur during SQL generation.
     * <p>
     * Logs the error with details and wraps it in a {@link SqlGenerationException}.
     * </p>
     *
     * @param schema the schema being processed when the error occurred
     * @param e the original {@link Exception} thrown
     * @throws SqlGenerationException a wrapped exception with contextual information
     */
    private void handleSqlGenerationError(SchemaObject schema, Exception e) {
        logger.error("Error while generating CREATE TABLE statement for schema: {}", schema.getName(), e);
        throw new SqlGenerationException(
                "An unexpected error occurred while generating CREATE TABLE for schema '"
                        + schema.getName() + "': " + e.getMessage(),
                e
        );
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
