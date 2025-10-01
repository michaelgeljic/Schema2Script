package main.java.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParserFactory {
    private static final Logger logger = LogManager.getLogger();

    public static SchemaParser get(String format) {
        if (format == null || format.isEmpty()) {
            logger.error("Schema format is null or empty");
        }

        logger.info("Schema format requested: " + format);

        switch (format.toLowerCase()) {
            case "xml":
                return new XmlParser();
            case "json":
                return new JSONParser();
            default:
                throw new IllegalArgumentException("Unsupported schema format: " + format);
        }
    }
}