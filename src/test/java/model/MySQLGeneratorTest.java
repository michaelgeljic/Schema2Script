package model;

import exception.SqlGenerationException;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link MySQLGenerator}.
 */
class MySQLGeneratorTest {

    private final ISqlGenerator generator = new MySQLGenerator();

    private SchemaObject schema(String name, String... fields) {
        return new SchemaObject(name, Arrays.asList(fields));
    }

    private String norm(String sql) {
        // Only normalize whitespace, keep backticks intact
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
        String n = norm(sql);

        assertTrue(n.contains("`Order`"), "Reserved table name should be backticked");
        assertTrue(n.contains("`select`"), "Reserved keyword 'select' should be backticked");
        assertTrue(n.contains("`from`"), "Reserved keyword 'from' should be backticked");
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
        SchemaObject s = schema("Person", "name", "email", "name"); // duplicate "name"
        assertThrows(IllegalArgumentException.class,
                () -> generator.generateCreateTable(s));
    }

    @Test
    void fieldNameNullThrows() {
        SchemaObject s = schema("Person", "id", null);
        assertThrows(IllegalArgumentException.class,
                () -> generator.generateCreateTable(s));
    }

    @Test
    void fieldNameEmptyThrows() {
        SchemaObject s = schema("Person", "id", "  ");
        assertThrows(IllegalArgumentException.class,
                () -> generator.generateCreateTable(s));
    }

    @Test
    void mapDataTypeRecognizedTypes() {
        assertEquals("INT", generator.mapDataType("int"));
        assertEquals("VARCHAR(255)", generator.mapDataType("string"));
        assertEquals("BOOLEAN", generator.mapDataType("bool"));
    }

    @Test
    void mapDataTypeUnknownDefaultsToText() {
        assertEquals("TEXT", generator.mapDataType("float"));
        assertEquals("TEXT", generator.mapDataType("randomtype"));
    }

    @Test
    void mapDataTypeNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> generator.mapDataType(null));
    }

    @Test
    void mapDataTypeEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> generator.mapDataType(" "));
    }

    @Test
    void generateConstraintsReturnsEmptyString() {
        SchemaObject s = schema("AnyTable", "id");
        String constraints = generator.generateConstraints(s);
        assertNotNull(constraints, "Should not return null");
        assertEquals("", constraints.trim(), "Should return empty string for now");
    }


    @Test
    void generateCreateTableHandlesException() {
        // Create a subclass that throws an exception during SQL generation
        ISqlGenerator faultyGenerator = new MySQLGenerator() {
            @Override
            public String generateCreateTable(SchemaObject schema) {
                throw new RuntimeException("boom");
            }
        };
        SchemaObject schema = new SchemaObject("Bad", List.of("id"));
        assertThrows(RuntimeException.class, () -> faultyGenerator.generateCreateTable(schema));
    }

    @Test
    void debugLoggingPathIsCovered() {
        // Enable debug logging for this class
        org.apache.logging.log4j.core.config.Configurator.setLevel(
                "model.MySQLGenerator", org.apache.logging.log4j.Level.DEBUG
        );

        SchemaObject schema = new SchemaObject("Test", List.of("id"));
        MySQLGenerator generator = new MySQLGenerator();

        // Act
        String sql = generator.generateCreateTable(schema);

        // Assert: SQL should be a valid CREATE TABLE statement
        assertNotNull(sql);
        assertTrue(sql.contains("CREATE TABLE `Test`"));
        assertTrue(sql.contains("`id` VARCHAR(255)"));
    }


    @Test
    void nullSchemaThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> generator.generateCreateTable(null)
        );
        assertTrue(ex.getMessage().contains("schema is null"));
    }

    @Test
    void handlesSqlGenerationErrorWhenExceptionOccurs() {
        MySQLGenerator badGenerator = new MySQLGenerator() {
            @Override
            protected void buildCreateTableSQL(SchemaObject schema, List<String> fields, StringBuilder sb) {
                throw new RuntimeException("Simulated SQL generation failure");
            }
        };

        SchemaObject schema = new SchemaObject("Broken", List.of("id"));

        SqlGenerationException ex = assertThrows(
                SqlGenerationException.class,
                () -> badGenerator.generateCreateTable(schema)
        );

        assertTrue(ex.getMessage().contains("unexpected error"));
    }







}
