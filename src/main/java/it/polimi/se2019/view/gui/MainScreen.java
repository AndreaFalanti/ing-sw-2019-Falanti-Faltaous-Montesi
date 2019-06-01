package it.polimi.se2019.view.gui;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.EnumMap;
import java.util.logging.Logger;

public class MainScreen {
    @FXML
    private Pane playerPane;
    @FXML
    private Pane boardPane;
    @FXML
    private HBox weaponBox;
    @FXML
    private GridPane powerUpGrid;
    @FXML
    private Button undoButton;
    @FXML
    private VBox chatBox;
    @FXML
    private VBox otherPlayerBoardsBox;

    private static final Logger logger = Logger.getLogger(MainScreen.class.getName());
    
    private static final double LOADED_OPACITY = 1;
    private static final double UNLOADED_OPACITY = 0.4;

    private BoardPane mBoardController;
    private EnumMap<PlayerColor, PlayerPane> mPlayerControllers = new EnumMap<>(PlayerColor.class);

    public BoardPane getBoardController() {
        return mBoardController;
    }

    public PlayerPane getPlayerControllerFromColor(PlayerColor color) {
        return mPlayerControllers.get(color);
    }

    public void loadPlayerBoard(PlayerColor color) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/playerPane.fxml"));
        Pane newLoadedPane =  loader.load();

        PlayerPane playerController = loader.getController();
        mPlayerControllers.put(color, playerController);

        playerController.setMainScreen(this);
        playerController.setupBoardImage(color);
        //testing various methods, they will be used in observer update methods
        playerController.addDamageTokens(PlayerColor.PURPLE, 3);
        playerController.addDeath();
        playerController.setPlayerName("Aldo");
        playerController.updateAmmo(new AmmoValue(1, 2, 3));
        playerController.setScore(3);
        //playerController.flipBoard();

        playerPane.getChildren().add(newLoadedPane);
        // second tab testing
        /*for (int i = 0; i < 4; i++ ) {
            loader = new FXMLLoader(getClass().getResource("/fxml/playerPane.fxml"));
            newLoadedPane =  loader.load();
            otherPlayerBoardsBox.getChildren().add(newLoadedPane);
        }*/
    }

    public void loadBoard () throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/boardPane.fxml"));
        Pane newLoadedPane =  loader.load();

        mBoardController = loader.getController();
        mBoardController.setMainController(this);
        Board board = Board.fromJson(Jsons.get("boards/game/board1"));
        mBoardController.initialize(board);
        mBoardController.addTargetDeath(5);

        boardPane.getChildren().add(newLoadedPane);
    }

    public void setBoxEnableStatus(Node box, boolean enable) {
        if (enable) {
            box.setDisable(false);
            box.setStyle("-fx-background-color: RED");
        }
        else {
            box.setDisable(true);
            box.setStyle("-fx-background-color: rgba(255, 0, 0, 0.0)");
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

    public void enableGridForMove () {

    }

    public void updateWeaponBox (String[] ids) {
        if (ids.length != 3) {
            throw new IllegalArgumentException("need 3 powerUp ids to update");
        }

        for (int i = 0; i < ids.length; i++) {
            ImageView weaponImageView = (ImageView)weaponBox.getChildren().get(i);

            if (ids[i] != null) {
                Image weaponImage = new Image(GuiResourcePaths.WEAPON_CARD + ids[i] + ".png");
                weaponImageView.setImage(weaponImage);
            }
            else {
                weaponImageView.setImage(null);
            }
        }
    }

    public void updatePowerUpGrid (String[] ids) {
        if (ids.length != 3) {
            throw new IllegalArgumentException("need 3 powerUp ids to update");
        }

        for (int i = 0; i < ids.length; i++) {
            ImageView powerUpImageView = (ImageView)powerUpGrid.getChildren().get(i);

            if (ids[i] != null) {
                Image powerUpImage = new Image(GuiResourcePaths.POWER_UP_CARD + ids[i] + ".png");
                powerUpImageView.setImage(powerUpImage);
                setPowerUpBehaviour(powerUpImageView, i);
                powerUpImageView.setDisable(false);
            }
            else {
                powerUpImageView.setImage(null);
                powerUpImageView.setDisable(true);
            }
        }
    }

    public void setShootOnWeapon () {
        setBoxEnableStatus(weaponBox,true);
        for (int i = 0; i < weaponBox.getChildren().size(); i++) {
            setShootingBehaviourOnWeapon(weaponBox.getChildren().get(i), i);
        }

        undoButton.setOnMouseClicked(event -> setBoxEnableStatus(weaponBox,false));
    }

    public void setReloadOnWeapon () {
        setBoxEnableStatus(weaponBox,true);
        for (int i = 0; i < weaponBox.getChildren().size(); i++) {
            setReloadBehaviourOnWeapon(weaponBox.getChildren().get(i), i);
        }

        undoButton.setOnMouseClicked(event -> setBoxEnableStatus(weaponBox,false));
    }

    private void setShootingBehaviourOnWeapon (Node weapon, int index) {
        weapon.setOnMouseClicked(event -> {
            if (weapon.getOpacity() == LOADED_OPACITY) {
                logToChat("Shooting with weapon of index: " + index);
                setWeaponLoadStatus(index, false);
                setBoxEnableStatus(weaponBox,false);
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
                setBoxEnableStatus(weaponBox,false);
            }
            else {
                logToChat("Can't reload an already loaded weapon");
            }
        });
    }

    private void setPowerUpBehaviour (ImageView powerUp, int index) {
        powerUp.setOnMouseClicked(event -> {
            logToChat("Using powerUp with index: " + index);
            powerUp.setImage(null);
            powerUp.setDisable(true);
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
