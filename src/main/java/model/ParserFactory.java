package main.java.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Factory for returning the correct {@link SchemaParser}
 * implementation based on schema format (e.g., JSON, XML).
 */
public class ParserFactory {
    private static final Logger logger = LogManager.getLogger(ParserFactory.class);

    public static SchemaParser get(String format) {
        if (format == null || format.trim().isEmpty()) {
            logger.error("Schema format is null or empty.");
            throw new IllegalArgumentException(
                "No schema format specified. Please provide a valid format (json or xml)."
            );
        }

        logger.info("Schema format requested: {}", format);

        switch (format.toLowerCase()) {
            case "xml":
                return new XmlParser();
            case "json":
                return new JSONParser();
            default:
                logger.error("Unsupported schema format requested: {}", format);
                throw new IllegalArgumentException(
                    "Unsupported schema format: '" + format +
                    "'. Supported formats are: json, xml."
                );
        }
    }
}