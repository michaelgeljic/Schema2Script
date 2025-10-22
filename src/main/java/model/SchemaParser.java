package model;

import java.io.File;

import exception.SchemaParsingException;
import org.apache.logging.log4j.*;

/**
 * Defines the contract for schema parsers that convert schema files
 * (e.g., JSON, XML) into {@link SchemaObject} instances.
 * <p>
 * Each implementation of this interface should provide its own logic
 * for reading and validating a schema file in a specific format.
 * </p>
 */
public interface SchemaParser {

    Logger logger = LogManager.getLogger(SchemaParser.class);

    /**
     * Parses the given schema file and produces a {@link SchemaObject}.
     *
     * @param schemaFile the {@link File} representing the schema to parse
     * @return a {@link SchemaObject} created from the parsed schema
     * @throws SchemaParsingException if parsing fails due to invalid format,
     *                                missing fields, or unexpected errors
     */
    SchemaObject parse(File schemaFile) throws SchemaParsingException;

    /**
     * Logs the start of schema parsing.
     */
    default void logStart(File schemaFile) {
        if (schemaFile != null) {
            logger.info("Starting schema parsing for file: {}", schemaFile.getAbsolutePath());
        } else {
            logger.warn("Attempted to start parsing with a null file reference.");
        }
    }

    /**
     * Logs a successful schema parse.
     */
    default void logSuccess(SchemaObject schemaObject) {
        if (schemaObject != null) {
            logger.debug("Parsed SchemaObject successfully: {}", schemaObject);
        } else {
            logger.warn("Schema parsing reported success, but the SchemaObject was null.");
        }
    }

    /**
     * Logs an error that occurred during schema parsing.
     */
    default void logError(String message, Throwable t) {
        if (t != null) {
            logger.error("Schema parsing error: {}", message, t);
        } else {
            logger.error("Schema parsing error: {}" , message);
        }
    }
}