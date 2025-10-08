package main.java.view;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.model.SchemaObject;

/**
 * Handles all user-facing messages and interactions for schema parsing.
 * This class represents the "View" in the MVC pattern.
 */
public class SchemaView {

    /**
     * Displays a success message to the user.
     *
     * @param message the success message
     */
    public void showSuccess(String message) {
        System.out.println("[SUCCESS] " + message);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message
     */
    public void showError(String message) {
        System.err.println("[ERROR] " + message);
    }

    /**
     * Opens a file chooser dialog to allow the user to select a schema file.
     *
     * @return the chosen file, or null if none was selected
     */
    public File chooseSchemaFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a schema file (.json or .xml)");
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Schema files (.json, .xml)", "json", "xml"));

        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Displays a summary of the parsed schema.
     *
     * @param schema the parsed schema object
     */
    public void showParsedSummary(SchemaObject schema) {
        if (schema == null) {
            showError("Parsed schema is empty. Please provide a valid schema file.");
        } else {
            showSuccess("Schema parsed and loaded successfully:\n" + schema.toString());
        }
    }
}