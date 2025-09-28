package main.java.model;

import java.util.List;

public class MySQLGenerator implements ISqlGenerator {

    public String generateCreateTable(SchemaObject schema) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(schema.getName()).append(" (\n");

        List<String> fields = schema.getFields();
        for (int i = 0; i < fields.size(); i++) {
            String field = fields.get(i);

            sb.append("    ").append(field).append(" VARCHAR(255)");

            if (i < fields.size() - 1) {
                sb.append(",\n");
            }
        }
        sb.append("\n);");
        return sb.toString();
    }

    @Override
    public String mapDataType(String genericType) {
        switch (genericType.toLowerCase()) {
            case "int":
                return "INT";
            case "string":
                return "VARCHAR(255)";
            case "bool":
                return "BOOLEAN";
            default:
                return "TEXT";
        }
    }

    @Override
    public String generateConstraints(SchemaObject schema) {
        return ""; // no constraints yet
    }

}
