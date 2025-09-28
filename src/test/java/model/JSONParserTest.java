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
        assertThrows(SchemaParsingException.class,() -> parser.parse(file));
    }

    @Test
    void testInvalidExtension() throws Exception {
        File file = File.createTempFile("schhema", "txt");
        try(FileWriter writer = new FileWriter(file)) {
            writer.write("{\"name\": \"Person\", \"fields\": [\"id\"]}");
        }
        assertThrows(SchemaParsingException.class,() -> parser.parse(file));
    }

    @Test
    void testValidJsonSchema() throws Exception {
        File file = File.createTempFile("schema","json");
        try(FileWriter writer = new FileWriter(file)) {
            writer.write("{\"name\":\"Person\",\"fields\":[\"id\",\"firstName\"]}");
        }

        SchemaObject result = parser.parse(file);

        assertNotNull(result);
        assertEquals("Person", result.getName());
        assertEquals(2,result.getFields().size());
    }
}
