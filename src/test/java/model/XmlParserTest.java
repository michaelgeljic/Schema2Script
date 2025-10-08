package test.java.model;

import main.java.model.SchemaObject;
import main.java.model.SchemaParsingException;
import main.java.model.XmlParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link XmlParser}.
 */
class XmlParserTest {

    private final XmlParser parser = new XmlParser();

    @Test
    void testFileDoesNotExist() {
        File file = new File("does_not_exist.xml");
        SchemaParsingException ex = assertThrows(SchemaParsingException.class, () -> parser.parse(file));
        assertTrue(ex.getMessage().contains("File not found"));
    }

    @Test
    void testInvalidExtension() throws Exception {
        File file = File.createTempFile("schema", ".txt");
        SchemaParsingException ex = assertThrows(SchemaParsingException.class, () -> parser.parse(file));
        assertTrue(ex.getMessage().contains("Invalid file format"));
    }

    @Test
    void testParsesValidXmlSchema() throws Exception {
        // Create a temporary XML schema file
        File file = File.createTempFile("schema", ".xml");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("""
                <schema name="Person">
                    <fields>
                        <field>id</field>
                        <field>firstName</field>
                        <field>lastName</field>
                        <field>age</field>
                    </fields>
                </schema>
                """);
        }

        SchemaObject result = parser.parse(file);

        assertNotNull(result, "Parser should return a SchemaObject");
        assertEquals("Person", result.getName(), "Schema name should match the XML attribute");
        assertEquals(4, result.getFields().size(), "Should parse all fields");
        assertTrue(result.getFields().contains("id"));
        assertTrue(result.getFields().contains("firstName"));
        assertTrue(result.getFields().contains("lastName"));
        assertTrue(result.getFields().contains("age"));
    }
}
