package it.polimi.se2019.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainScreen.fxml"));
        primaryStage.setTitle("Adrenalina");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
