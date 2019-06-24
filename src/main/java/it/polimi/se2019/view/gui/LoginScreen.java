package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.PlayerColor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginScreen {
    @FXML
    private TextField usernameTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;
    @FXML
    private ToggleGroup network;
    @FXML
    private Pane infoPane;

    private static final Logger logger = Logger.getLogger(LoginScreen.class.getName());
    private static final String USERNAME_ERROR = "Insert a username before logging";
    private static final String CONNECTION_ERROR = "Select a connection before logging";

    public void login () {
        String username = usernameTextField.getText();
        RadioButton radioButton = (RadioButton) network.getSelectedToggle();

        if (username == null || username.isEmpty()) {
            errorLabel.setText(USERNAME_ERROR);
            return;
        }
        if (radioButton == null) {
            errorLabel.setText(CONNECTION_ERROR);
            return;
        }

        String connection = radioButton.getText();

        Object[] logObjects = {username, connection};
        logger.log(Level.INFO, "Login:\nUsername: {0}\nConnection: {1}", logObjects);

        waitingForPlayers();
        openMainScreen();
    }

    private void waitingForPlayers () {
        infoPane.getChildren().clear();

        Label label = new Label("Waiting for players...");
        label.setStyle("-fx-font: 20 segoe; -fx-font-weight: bold");

        infoPane.getChildren().add(label);
        label.setLayoutX(infoPane.getWidth() / 2 - 50);
        label.setLayoutY(infoPane.getHeight() / 2);
    }

    private void openMainScreen () {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainScreen.fxml"));
            Stage stage = new Stage();

            Pane root = loader.load();
            stage.setTitle("Adrenalina");
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);

            BackgroundImage backgroundImage = new BackgroundImage(new Image(GuiResourcePaths.BACKGROUND + "bg.jpg"),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            root.setBackground(new Background(backgroundImage));

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/css/tabPaneBar.css").toExternalForm());
            stage.setScene(scene);
            stage.centerOnScreen();

            MainScreen controller = loader.getController();
            GraphicView guiView = new GraphicView(controller);
            controller.setView(guiView);
            controller.setClientColor(PlayerColor.GREEN);


            controller.loadPlayerBoard(PlayerColor.GREEN);
            controller.loadBoard();
            String[] ids = {"022", "023", "024"};
            controller.updatePowerUpGrid(ids);
            controller.updateWeaponBox(ids);

            stage.show();

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

            infoPane.getScene().getWindow().hide();
        }
        catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }
}
