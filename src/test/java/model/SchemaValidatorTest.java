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

        assertThrows(IllegalArgumentException.class, () -> validator.validate(model));
    }

    @Test
    void testNoFields() {
        SchemaObject schema = new SchemaObject("EmptySchema", List.of());
        SchemaModel model = new SchemaModel();
        model.setSchema(schema);

        assertThrows(IllegalArgumentException.class, () -> validator.validate(model));
    }

    @Test
    void testDuplicateFields() {
        SchemaObject schema = new SchemaObject("DuplicateSchema", List.of("id", "id"));
        SchemaModel model = new SchemaModel();
        model.setSchema(schema);

        assertThrows(IllegalArgumentException.class, () -> validator.validate(model));
    }
}
