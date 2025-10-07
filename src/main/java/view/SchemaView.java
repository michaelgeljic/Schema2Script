package main.java.view;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.model.SchemaObject;

public class SchemaView {

    public void showSuccess(String message) {
        System.out.println("SUCCESS: " + message);
    }

    public void showError(String message) {
        System.err.println("ERROR: " + message);
    }

    public File chooseSchemaFile(){
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a schema file (.json or .xml)");
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Schema files (.json, .xml)", "json", "xml"));
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION){
            return chooser.getSelectedFile();
        }
        return null;
    }
    public void showParsedSummary(SchemaObject schema){
        if(schema == null){
            showError("Parsed schema is empty.");
        } else {
            showSuccess("Schema parsed and loaded into the model:" + schema.toString());
        }
    }
}
