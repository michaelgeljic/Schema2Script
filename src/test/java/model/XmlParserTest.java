package test.java.model;

import main.java.model.SchemaObject;
import main.java.model.SchemaParsingException;
import main.java.model.XmlParser;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class XmlParserTest {

    private final XmlParser parser = new XmlParser();

    @Test
    void testFileDoesNotExist() {
        File file = new File("does_not_exist.xml");
        SchemaParsingException ex = assertThrows(SchemaParsingException.class, () -> parser.parse(file));
        assertTrue(ex.getMessage().contains("could not be found"));
        assertTrue(ex.getMessage().contains("Please check the file path"));
    }

    @Test
    void testInvalidExtension() throws Exception {
        File file = File.createTempFile("schema", ".txt");
        SchemaParsingException ex = assertThrows(SchemaParsingException.class, () -> parser.parse(file));
        assertTrue(ex.getMessage().contains("Invalid file format"));
        assertTrue(ex.getMessage().contains("Please provide a valid XML schema file"));
    }

    @Test
    void testMockReturnsSchemaObject() throws Exception {
        File file = File.createTempFile("schema", ".xml");
        SchemaObject result = parser.parse(file);

        assertNotNull(result);
        assertEquals("MockSchemaObject", result.getName());
        assertEquals(3, result.getFields().size());
    }
}