package test.java.model;

import org.junit.jupiter.api.Test;

import main.java.model.JSONParser;
import main.java.model.SchemaObject;
import main.java.model.SchemaParsingException;

import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;

public class JSONParserTest {
    private final JSONParser parser = new JSONParser();

    @Test
    void testFileDoesNotExist() {
        File file = new File("does_not_exist.json");
        SchemaParsingException ex = assertThrows(SchemaParsingException.class, () -> parser.parse(file));
        assertTrue(ex.getMessage().contains("file could not be found"));
    }

    @Test
    void testInvalidExtension() throws Exception {
        File file = File.createTempFile("schema", ".txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("{\"name\": \"Person\", \"fields\": [\"id\"]}");
        }
        SchemaParsingException ex = assertThrows(SchemaParsingException.class, () -> parser.parse(file));
        assertTrue(ex.getMessage().contains("Invalid file format"));
    }

    @Test
    void testValidJsonSchema() throws Exception {
        File file = File.createTempFile("schema", ".json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("{\"name\":\"Person\",\"fields\":[\"id\",\"firstName\"]}");
        }

        SchemaObject result = parser.parse(file);

        assertNotNull(result);
        assertEquals("Person", result.getName());
        assertEquals(2, result.getFields().size());
    }

    @Test
    void testMissingFieldsMessage() throws Exception {
        File tempFile = File.createTempFile("invalid", ".json");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("{ \"name\": \"Person\" }");
        }

        SchemaParsingException ex = assertThrows(SchemaParsingException.class, () -> parser.parse(tempFile));
        assertTrue(ex.getMessage().contains("missing required property 'name' or 'fields'"));
    }
}