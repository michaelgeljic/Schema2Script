package main.java.model;

public class ParserFactory {
    public SchemaParser get(String format) {
        if (format == null || format.isEmpty()) {
            throw new IllegalArgumentException("Format cant be null or empty");
        }

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