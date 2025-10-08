package main.java.view;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.java.controller.SchemaController;
import main.java.model.SchemaModel;
import main.java.model.SchemaObject;
import main.java.model.MySQLGenerator;

import java.io.File;

public class SchemaApp extends Application {

    private TextArea outputArea;
    private SchemaController controller;

    @Override
    public void start(Stage primaryStage) {
        SchemaModel model = new SchemaModel();
        controller = new SchemaController(model, new FxSchemaView());

        // UI components
        Button chooseFileBtn = new Button("Choose Schema File");
        chooseFileBtn.setOnAction(e -> chooseSchemaFile(primaryStage));

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(300);
        outputArea.setWrapText(true);

        VBox root = new VBox(10, chooseFileBtn, outputArea);
        root.setStyle("-fx-padding: 15; -fx-background-color: #f9f9f9;");

        Scene scene = new Scene(root, 650, 400);
        primaryStage.setTitle("Schema Parser (MVC)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void chooseSchemaFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Schema File");
        fileChooser.setInitialDirectory(new File("src/main/resources"));
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Schema Files (*.json, *.xml)", "*.json", "*.xml")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            outputArea.appendText("[INFO] Selected file: " + selectedFile.getAbsolutePath() + "\n");
            controller.handleSchemaUpload(selectedFile);
        } else {
            outputArea.appendText("[INFO] No file selected.\n");
        }
    }

    /**
     * JavaFX-based implementation of SchemaView
     */
    private class FxSchemaView extends main.java.view.SchemaView {
        @Override
        public void showSuccess(String message) {
            outputArea.appendText("[SUCCESS] " + message + "\n");
        }

        @Override
        public void showError(String message) {
            outputArea.appendText("[ERROR] " + message + "\n");
        }

        @Override
        public void showParsedSummary(SchemaObject schema) {
            if (schema == null) {
                showError("Parsed schema is empty.");
            } else {
                try {
                    // Generate SQL
                    MySQLGenerator generator = new MySQLGenerator();
                    String sql = generator.generateCreateTable(schema);

                    // Output SQL first
                    outputArea.appendText(sql + "\n");

                    // Then show parsed schema summary
                    showSuccess("Schema parsed and loaded successfully:\n" + schema);
                } catch (Exception e) {
                    showError("Error generating SQL: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}