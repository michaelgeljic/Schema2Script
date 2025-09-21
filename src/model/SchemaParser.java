package model;

import java.io.File;

/**
 * Defines the contract for schema parsers that convert schema files
 * (e.g., JSON, XML) into {@link SchemaObject} instances.
 * <p>
 * Each implementation of this interface should provide its own logic
 * for reading and validating a schema file in a specific format.
 * </p>
 */
public interface SchemaParser {

    /**
     * Parses the given schema file and produces a {@link SchemaObject}.
     * <p>
     * Implementations should validate the schema file format and
     * throw a {@link SchemaParsingException} if the file is invalid,
     * missing required fields, or cannot be read properly.
     * </p>
     *
     * @param schemaFile the {@link File} representing the schema to parse
     * @return a {@link SchemaObject} created from the parsed schema
     * @throws SchemaParsingException if parsing fails due to invalid format,
     *                                missing fields, or unexpected errors
     */
    SchemaObject parse(File schemaFile) throws SchemaParsingException;
}