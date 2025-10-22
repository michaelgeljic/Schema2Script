package view;

import model.SchemaObject;

public abstract class SchemaView {
    public abstract void showSuccess(String message);
    public abstract void showError(String message);
    public abstract void showParsedSummary(SchemaObject schema);
}
