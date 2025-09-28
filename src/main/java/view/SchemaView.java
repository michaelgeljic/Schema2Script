package main.java.view;

public class SchemaView {

    public void showSuccess(String message) {
        System.out.println("SUCCESS: " + message);
    }

    public void showError(String message) {
        System.err.println("ERROR: " + message);
    }
}
