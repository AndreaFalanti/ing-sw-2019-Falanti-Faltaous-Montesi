package it.polimi.se2019.view.gui;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.logging.Logger;

public class MainScreen {
    @FXML
    private Pane playerPane;
    @FXML
    private Pane boardPane;
    @FXML
    private HBox weaponBox;
    @FXML
    private HBox powerUpBox;
    @FXML
    private Button undoButton;
    @FXML
    private VBox chatBox;

    private static final Logger logger = Logger.getLogger(MainScreen.class.getName());
    
    private static final double LOADED_OPACITY = 1;
    private static final double UNLOADED_OPACITY = 0.4;

    private BoardPane mBoardController;

    public void loadPlayerBoard(PlayerColor color) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/playerPane.fxml"));
        Pane newLoadedPane =  loader.load();

        PlayerPane playerController = loader.getController();
        playerController.changeBoardImage(color.getPascalName());
        //testing various methods, they will be used in observer update methods
        playerController.addDamageTokens(PlayerColor.PURPLE, 3);
        playerController.addDeath();

        playerPane.getChildren().add(newLoadedPane);
    }

    public void loadBoard () throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/boardPane.fxml"));
        Pane newLoadedPane =  loader.load();

        mBoardController = loader.getController();
        Board board = Board.fromJson(Jsons.get("boards/game/board1"));
        mBoardController.initialize(board);
        mBoardController.addTargetDeath(5);

        boardPane.getChildren().add(newLoadedPane);
    }

    public void setWeaponBoxStatus (boolean enable) {
        if (enable) {
            weaponBox.setDisable(false);
            weaponBox.setStyle("-fx-background-color: RED");
        }
        else {
            weaponBox.setDisable(true);
            weaponBox.setStyle("-fx-background-color: rgba(255, 0, 0, 0.0)");
        }
    }

    public void setWeaponLoadStatus (int index, boolean value) {
        ImageView weaponView = (ImageView)weaponBox.getChildren().get(index);
        if (value) {
            weaponView.setOpacity(1);
        }
        else {
            weaponView.setOpacity(0.4);
        }
    }

    public void setShootOnWeapon () {
        setWeaponBoxStatus(true);
        for (int i = 0; i < weaponBox.getChildren().size(); i++) {
            setShootingBehaviourOnWeapon(weaponBox.getChildren().get(i), i);
        }
    }

    public void setReloadOnWeapon () {
        setWeaponBoxStatus(true);
        for (int i = 0; i < weaponBox.getChildren().size(); i++) {
            setReloadBehaviourOnWeapon(weaponBox.getChildren().get(i), i);
        }
    }

    private void setShootingBehaviourOnWeapon (Node weapon, int index) {
        weapon.setOnMouseClicked(event -> {
            if (weapon.getOpacity() == LOADED_OPACITY) {
                logToChat("Shooting with weapon of index: " + index);
                setWeaponLoadStatus(index, false);
                setWeaponBoxStatus(false);
            }
            else {
                logToChat("Can't shoot with unloaded weapon");
            }
        });
    }

    private void setReloadBehaviourOnWeapon (Node weapon, int index) {
        weapon.setOnMouseClicked(event -> {
            if (weapon.getOpacity() == UNLOADED_OPACITY) {
                logToChat("Reload weapon of index: " + index);
                setWeaponLoadStatus(index, true);
                setWeaponBoxStatus(false);
            }
            else {
                logToChat("Can't reload an already loaded weapon");
            }
        });
    }

    public void logToChat (String message) {
        logger.info(message);
        Label label = new Label(message);
        chatBox.getChildren().add(label);
    }

    public void activateButtonGrid () {
        mBoardController.switchButtonGrid(true);
    }

    public void deactivateButtonGrid () {
        mBoardController.switchButtonGrid(false);
    }
}
