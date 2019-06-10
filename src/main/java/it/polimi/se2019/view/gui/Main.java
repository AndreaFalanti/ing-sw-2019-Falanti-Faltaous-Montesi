package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // initialize javafx stuff
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
        scene.getStylesheets().add(getClass().getResource("/css/tabPaneBar.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

        // initialize test game
        Game game = new Game(
                Board.fromJson(Jsons.get("boards/game/board1")),
                new ArrayList<>(Arrays.asList(
                        new Player("Mario", PlayerColor.PURPLE, new Position(0, 0)),
                        new Player("Luigi", PlayerColor.GREEN, new Position(0, 0)),
                        new Player("Dorian", PlayerColor.GREY, new Position(0, 0)),
                        new Player("Smurfette", PlayerColor.BLUE, new Position(0, 0)),
                        new Player("Banano", PlayerColor.YELLOW, new Position(0, 0))
                )),
                1
        );

        // initialize mainScreen and view and link them
        MainScreen mainScreen = loader.getController();
        GraphicView guiView = new GraphicView(mainScreen);
        mainScreen.setView(guiView);
        mainScreen.setClientColor(PlayerColor.PURPLE);
        mainScreen.loadPlayerBoard(PlayerColor.GREY);
        mainScreen.loadBoard();
        String[] ids = {"022", "023", "024"};
        mainScreen.updatePowerUpGrid(ids);
        mainScreen.updateWeaponBox(ids);

        // initialize controller and link it to view
        Controller controller = new Controller(game);
        mainScreen.register(controller);

        // show
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
