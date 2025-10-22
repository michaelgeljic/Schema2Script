package model;

import exception.SchemaParsingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A parser that reads XML schema files and converts them into {@link SchemaObject} instances.
 * <p>
 * The parser expects XML files to follow the structure:
 * </p>
 *
 * <pre>{@code
 * <schema name="Person">
 *     <fields>
 *         <field>id</field>
 *         <field>firstName</field>
 *         <field>lastName</field>
 *         <field>age</field>
 *     </fields>
 * </schema>
 * }</pre>
 *
 * <p>
 * From this example, the parser would produce a {@link SchemaObject}
 * with the name {@code "Person"} and fields {@code ["id", "firstName", "lastName", "age"]}.
 * </p>
 *
 * <p>
 * Validation is performed on the file path and extension.
 * Detailed logging is provided for troubleshooting and debugging.
 * </p>
 */
public class XmlParser implements SchemaParser {

    private static final Logger logger = LogManager.getLogger(XmlParser.class);

    /**
     * Parses the provided XML schema file into a {@link SchemaObject}.
     *
     * @param schemaFile the XML file containing schema information
     * @return a populated {@link SchemaObject} with the schema name and field list
     * @throws SchemaParsingException if the file does not exist, has an invalid extension,
     *                                or cannot be parsed successfully
     */
    @Override
    public SchemaObject parse(File schemaFile) throws SchemaParsingException {
        logger.info("Starting XML schema parsing for file: {}", schemaFile.getAbsolutePath());

        if (!schemaFile.exists()) {
            throw new SchemaParsingException("File not found: " + schemaFile.getAbsolutePath());
        }
        if (!schemaFile.getName().toLowerCase().endsWith(".xml")) {
            throw new SchemaParsingException("Invalid file format: must be .xml");
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(schemaFile);
            doc.getDocumentElement().normalize();

            // Extract schema name from <schema name="...">
            Element schemaElement = (Element) doc.getElementsByTagName("schema").item(0);
            String schemaName = schemaElement.getAttribute("name");

            // Extract field names from <fields><field>...</field></fields>
            NodeList fieldNodes = schemaElement.getElementsByTagName("field");
            List<String> fields = new ArrayList<>();
            for (int i = 0; i < fieldNodes.getLength(); i++) {
                fields.add(fieldNodes.item(i).getTextContent().trim());
            }

            logger.info("Parsed schema name: {}", schemaName);
            logger.info("Parsed fields: {}", fields);

            return new SchemaObject(schemaName, fields);

        } catch (Exception e) {
            logger.error("Error parsing XML schema", e);
            throw new SchemaParsingException("Failed to parse XML schema: " + e.getMessage(), e);
        }
    }
}
