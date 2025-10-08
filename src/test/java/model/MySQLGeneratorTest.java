package test.java.model;

import org.junit.jupiter.api.Test;

import main.java.model.ISqlGenerator;
import main.java.model.MySQLGenerator;
import main.java.model.SchemaObject;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MySQLGeneratorTest {

    private final ISqlGenerator generator = new MySQLGenerator();

    private SchemaObject schema(String name, String... fields) {
        return new SchemaObject(name, Arrays.asList(fields));
    }

    private String norm(String sql) {
        return sql.replaceAll("\\s+", " ").trim();
    }

    @Test
    void generatesBasicCreateTable() {
        SchemaObject s = schema("Person", "id", "name");
        String sql = generator.generateCreateTable(s);
        String n = norm(sql);

        
        assertTrue(n.startsWith("CREATE TABLE"), "Should start with CREATE TABLE");
        assertTrue(n.contains("`Person`"), "Table name should be backticked");
        assertTrue(n.contains("`id`"), "Field 'id' should be present and backticked");
        assertTrue(n.contains("`name`"), "Field 'name' should be present and backticked");
        assertTrue(n.endsWith(";"), "Should end with semicolon");

        // has parentheses around the column list
        assertTrue(n.contains("(") && n.contains(")"), "Should have parentheses around columns");
    }

    @Test
    void quotesReservedWords() {
        SchemaObject s = schema("Order", "select", "from");
        String sql = generator.generateCreateTable(s);
        assertTrue(sql.contains("`Order`"));
        assertTrue(sql.contains("`select`"));
        assertTrue(sql.contains("`from`"));
    }

    @Test
    void nullSchemaThrows() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateCreateTable(null));
    }

    @Test
    void emptyNameThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> generator.generateCreateTable(new SchemaObject("", List.of("id"))));
    }

    @Test
    void emptyFieldsThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> generator.generateCreateTable(new SchemaObject("Empty", List.of())));
    }

    @Test
    void duplicateFieldsThrow() {
        SchemaObject s = schema("Person", "name", "email", "name");
        assertThrows(IllegalArgumentException.class, () -> generator.generateCreateTable(s));
    }
}
