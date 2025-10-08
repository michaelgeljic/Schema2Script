package main.java.model;

/**
 * Custom exception for handling errors during schema parsing.
 * <p>
 * This exception is thrown when a schema file cannot be parsed correctly,
 * due to issues such as:
 * <ul>
 *   <li>Invalid file format (e.g., wrong extension)</li>
 *   <li>Corrupted or unreadable file contents</li>
 *   <li>Missing or unexpected fields in the schema</li>
 *   <li>General parsing errors (I/O issues, libraries failing, etc.)</li>
 * </ul>
 * </p>
 */
public class SchemaParsingException extends Exception {

    /**
     * Creates a new {@code SchemaParsingException} with a descriptive error message.
     *
     * @param message A human-readable description of the error.
     */
    public SchemaParsingException(String message) {
        super("Schema Parsing Error: " + message);
    }

    /**
     * Creates a new {@code SchemaParsingException} with a message and underlying cause.
     * <p>
     * Useful when another exception (like {@link java.io.IOException}
     * or a parsing library error) triggered the parsing failure.
     * </p>
     *
     * @param message A human-readable description of the error.
     * @param cause   The original exception that caused this error (may be {@code null}).
     */
    public SchemaParsingException(String message, Throwable cause) {
        super("Schema Parsing Error: " + message, cause);
    }
}