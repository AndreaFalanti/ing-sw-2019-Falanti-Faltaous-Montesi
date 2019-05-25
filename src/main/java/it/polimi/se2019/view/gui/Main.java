package it.polimi.se2019.view.gui;

import it.polimi.se2019.model.PlayerColor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainScreen.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Adrenalina");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        primaryStage.centerOnScreen();

        MainScreen controller = loader.getController();
        controller.loadPlayerBoard(PlayerColor.GREY);
        controller.loadBoard();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
