package model;

/**
 * Defines the contract for SQL generators that convert {@link SchemaObject}
 * instances into SQL statements (e.g., CREATE TABLE).
 * <p>
 * Implementations should ensure:
 * - Input schemas are valid (non-null, contain fields).
 * - Meaningful exceptions are thrown with descriptive messages
 *   if SQL generation fails.
 * </p>
 */
public interface ISqlGenerator {

    /**
     * Generates a SQL CREATE TABLE statement for the given schema.
     *
     * @param schema the schema object containing table name and fields
     * @return SQL string for creating the table
     * @throws IllegalArgumentException if schema is null or invalid
     */
    String generateCreateTable(SchemaObject schema);

    /**
     * Maps a generic data type (e.g., "string", "int") to a vendor-specific SQL type.
     *
     * @param genericType the generic data type
     * @return mapped SQL type (never null)
     * @throws IllegalArgumentException if type is unsupported or null
     */
    String mapDataType(String genericType);

    /**
     * Generates SQL constraints (e.g., primary keys, foreign keys, unique constraints)
     * for the given schema.
     *
     * @param schema the schema object
     * @return SQL string representing constraints, or empty string if none
     */
    String generateConstraints(SchemaObject schema);
}