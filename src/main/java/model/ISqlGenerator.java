package main.java.model;


public interface ISqlGenerator {
    String generateCreateTable(SchemaObject schema);
    String mapDataType(String genericType);
    String generateConstraints(SchemaObject schema);
}