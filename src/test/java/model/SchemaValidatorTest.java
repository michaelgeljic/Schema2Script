package test.java.model;

import main.java.model.SchemaModel;
import main.java.model.SchemaObject;
import main.java.model.SchemaValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SchemaValidatorTest {

    private final SchemaValidator validator = new SchemaValidator();

    @Test
    void testValidSchema() {
        SchemaObject schema = new SchemaObject("Person", List.of("id", "firstName"));
        SchemaModel model = new SchemaModel();
        model.setSchema(schema);

        assertDoesNotThrow(() -> validator.validate(model));
    }

    @Test
    void testMissingName() {
        SchemaObject schema = new SchemaObject("", List.of("id"));
        SchemaModel model = new SchemaModel();
        model.setSchema(schema);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(model));
        assertTrue(ex.getMessage().contains("schema name is missing or empty"));
    }

    @Test
    void testNoFields() {
        SchemaObject schema = new SchemaObject("Person", List.of());
        SchemaModel model = new SchemaModel();
        model.setSchema(schema);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(model));
        assertTrue(ex.getMessage().contains("must contain at least one field"));
    }

    @Test
    void testDuplicateFields() {
        SchemaObject schema = new SchemaObject("DuplicateSchema", List.of("id", "id"));
        SchemaModel model = new SchemaModel();
        model.setSchema(schema);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(model));
        assertTrue(ex.getMessage().contains("contains duplicate field"));
    }
}