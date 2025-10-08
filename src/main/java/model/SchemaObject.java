package main.java.model;

import java.util.List;

/**
 * Represents a schema definition with a name and a list of fields.
 */
public class SchemaObject {
    private final String name;
    private final List<String> fields;

    /**
     * Creates a new SchemaObject.
     *
     * @param name   the schema name (cannot be null or empty)
     * @param fields the list of fields (cannot be null)
     * @throws IllegalArgumentException if name or fields are invalid
     */
    public SchemaObject(String name, List<String> fields) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Schema name cannot be null or empty.");
        }
        if (fields == null) {
            throw new IllegalArgumentException("Schema fields cannot be null.");
        }

        this.name = name;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public List<String> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return "SchemaObject{name='" + name + "', fields=" + fields + "}";
    }
}