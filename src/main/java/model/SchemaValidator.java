package main.java.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validates {@link SchemaModel} and its contained {@link SchemaObject}.
 * Ensures that schema name and fields are present and consistent.
 */
public class SchemaValidator {

    /**
     * Validates the provided schema model.
     *
     * @param schemaModel the schema model to validate
     * @throws IllegalArgumentException if the schema is invalid
     */
    public void validate(SchemaModel schemaModel) {
        if (schemaModel == null || schemaModel.getSchema() == null) {
            throw new IllegalArgumentException(
                "Schema validation failed: no schema object found in the model."
            );
        }

        SchemaObject schema = schemaModel.getSchema();

        if (schema.getName() == null || schema.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Schema validation failed: schema name is missing or empty."
            );
        }

        List<String> fields = schema.getFields();
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException(
                "Schema validation failed: schema '" + schema.getName() + "' must contain at least one field."
            );
        }

        Set<String> seen = new HashSet<>();
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                throw new IllegalArgumentException(
                    "Schema validation failed: schema '" + schema.getName() + "' contains an empty or null field."
                );
            }
            if (!seen.add(field)) {
                throw new IllegalArgumentException(
                    "Schema validation failed: schema '" + schema.getName() + "' contains duplicate field '" + field + "'."
                );
            }
        }
    }
}