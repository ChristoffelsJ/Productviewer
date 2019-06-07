package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Viewer.fxml"));
        Scene scene = new Scene(root, 1010, 500);
        stage.setTitle("ProductViewer");
        stage.setScene(scene);
        stage.show();

    }
}