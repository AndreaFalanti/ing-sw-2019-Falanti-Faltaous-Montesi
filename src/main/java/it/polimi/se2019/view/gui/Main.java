package it.polimi.se2019.view.gui;

import it.polimi.se2019.model.PlayerColor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainScreen.fxml"));
        Pane root = loader.load();
        primaryStage.setTitle("Adrenalina");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);

        BackgroundImage backgroundImage = new BackgroundImage(new Image(GuiResourcePaths.BACKGROUND + "bg.jpg"),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        root.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        primaryStage.centerOnScreen();

        MainScreen controller = loader.getController();
        controller.loadPlayerBoard(PlayerColor.PURPLE);
        controller.loadBoard();
        String[] ids = {"022", "023", "024"};
        controller.updatePowerUpGrid(ids);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
