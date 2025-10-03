package main.java.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SchemaValidator {

    public void validate(SchemaModel schemaModel) {
        if (schemaModel == null || schemaModel.getSchema() == null) {
            throw new IllegalArgumentException("SchemaObject is null");
        }

        SchemaObject schema = schemaModel.getSchema();

        if (schema.getName() == null || schema.getName().isEmpty()) {
            throw new IllegalArgumentException("Schema name is required");
        }

        List<String> fields = schema.getFields();
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("At least one field is required for " + schema.getName());
        }

        Set<String> seen = new HashSet<>();
        for (String field : fields) {
            if (!seen.add(field)) {
                throw new IllegalArgumentException("Duplicate field found: " + field);
            }
        }
    }
}
