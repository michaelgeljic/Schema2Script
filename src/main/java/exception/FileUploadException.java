package exception;

/**
 * Custom exception for handling errors during file uploads and validation.
 * <p>
 * Thrown when a schema file is missing, unreadable, or invalid
 * (e.g., wrong type, cannot be accessed).
 * </p>
 */
public class FileUploadException extends RuntimeException {

    public FileUploadException(String message) {
        super("File Upload Error: " + message);
    }

    public FileUploadException(String message, Throwable cause) {
        super("File Upload Error: " + message, cause);
    }
}