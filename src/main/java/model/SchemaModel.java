package model;

/**
 * Holds the current {@link SchemaObject} being worked with in the application.
 * <p>
 * Acts as the "Model" in the MVC pattern, storing the parsed schema
 * that is passed between controller and view.
 * </p>
 */
public class SchemaModel {
    private SchemaObject schemaObject;

    /**
     * Sets the active schema object in the model.
     *
     * @param schemaObject the schema to store (cannot be null)
     * @throws IllegalArgumentException if schemaObject is null
     */
    public void setSchema(SchemaObject schemaObject) {
        if (schemaObject == null) {
            throw new IllegalArgumentException("SchemaModel cannot store a null schema.");
        }
        this.schemaObject = schemaObject;
    }

    /**
     * Retrieves the active schema object stored in the model.
     *
     * @return the schema object, or null if none has been set
     */
    public SchemaObject getSchema() {
        return schemaObject;
    }
}