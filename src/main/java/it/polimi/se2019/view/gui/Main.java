package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.PlayerColor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainScreen.fxml"));
        Pane root = loader.load();
        primaryStage.setTitle("Adrenalina");
        primaryStage.setResizable(false);
        //primaryStage.setAlwaysOnTop(true);

        BackgroundImage backgroundImage = new BackgroundImage(new Image(GuiResourcePaths.BACKGROUND + "bg.jpg"),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        root.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/css/tabPaneBar.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

        MainScreen controller = loader.getController();
        GraphicView guiView = new GraphicView(controller);
        controller.setView(guiView);
        controller.setClientColor(PlayerColor.GREEN);


        controller.loadPlayerBoard(PlayerColor.GREEN);
        controller.loadBoard();
        String[] ids = {"022", "023", "024"};
        controller.updatePowerUpGrid(ids);
        controller.updateWeaponBox(ids);

        primaryStage.show();

        // TODO: delete this tests
        //controller.activateDirectionTab();

        Set<PlayerColor> set = new HashSet<>();
        set.add(PlayerColor.YELLOW);
        set.add(PlayerColor.GREY);
        set.add(PlayerColor.BLUE);
        controller.activateTargetsTab(set, 1, 2);

        SortedMap<Integer, Set<Effect>> sortedMap = new TreeMap<>();
        Set<Effect> effectsSet1 = new HashSet<>();
        effectsSet1.add(new Effect("Effect1", 0, false, new AmmoValue(0,0,0)));
        effectsSet1.add(new Effect("Effect2", 0, true, new AmmoValue(1,0,1)));
        sortedMap.put(0, effectsSet1);
        Set<Effect> effectsSet2 = new HashSet<>();
        effectsSet2.add(new Effect("Effect3", 1, true, new AmmoValue(0,0,1)));
        sortedMap.put(1, effectsSet2);
        controller.activateEffectsTabForEffects(sortedMap, 0);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
