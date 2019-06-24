package it.polimi.se2019.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginScreen.fxml"));
        Pane root = loader.load();
        primaryStage.setTitle("Adrenalina");
        primaryStage.setResizable(false);

        BackgroundImage backgroundImage = new BackgroundImage(new Image(GuiResourcePaths.BACKGROUND + "intro.jpg"),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        root.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
