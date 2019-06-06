package it.polimi.se2019.view.gui;

import it.polimi.se2019.model.AmmoCard;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.MoveShootAction;
import it.polimi.se2019.model.action.ReloadAction;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.NormalTile;
import it.polimi.se2019.model.board.SpawnTile;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.util.Observable;
import it.polimi.se2019.view.request.ActionRequest;
import it.polimi.se2019.view.request.Request;
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

public class MainScreen extends Observable<Request> {
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
    @FXML
    private VBox buttonBox;

    private static final Logger logger = Logger.getLogger(MainScreen.class.getName());
    
    private static final double LOADED_OPACITY = 1;
    private static final double UNLOADED_OPACITY = 0.4;

    private PlayerColor mClientColor;

    private BoardPane mBoardController;
    private EnumMap<PlayerColor, PlayerPane> mPlayerControllers = new EnumMap<>(PlayerColor.class);

    public BoardPane getBoardController() {
        return mBoardController;
    }

    public PlayerColor getClientColor() {
        return mClientColor;
    }

    public Button getUndoButton() {
        return undoButton;
    }

    public void setClientColor (PlayerColor color) {
        mClientColor = color;
    }

    public PlayerPane getPlayerControllerFromColor(PlayerColor color) {
        return mPlayerControllers.get(color);
    }

    /**
     * Load player board of given color
     * @param color Player color
     * @throws IOException Thrown if fxml is not found
     */
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
        playerController.updateMarkLabel(PlayerColor.YELLOW, 3);
        playerController.updateMarkLabel(PlayerColor.BLUE, 1);
        //playerController.flipBoard();
        //playerController.eraseDamage();

        playerPane.getChildren().add(newLoadedPane);
        // second tab testing
        /*for (int i = 0; i < 4; i++ ) {
            loader = new FXMLLoader(getClass().getResource("/fxml/playerPane.fxml"));
            newLoadedPane =  loader.load();
            otherPlayerBoardsBox.getChildren().add(newLoadedPane);
        }*/
    }

    /**
     * Load game board
     * @throws IOException Thrown if fxml is not found
     */
    public void loadBoard () throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/boardPane.fxml"));
        Pane newLoadedPane =  loader.load();

        mBoardController = loader.getController();
        mBoardController.setMainController(this);

        Board board = Board.fromJson(Jsons.get("boards/game/board1"));
        mBoardController.initialize(board);
        mBoardController.addTargetDeath(4);
        mBoardController.updateActivePlayerText(PlayerColor.PURPLE);
        mBoardController.updateRemainingActionsText(2);
        mBoardController.updateTurnText(15);
        mBoardController.addKillToKilltrack(PlayerColor.YELLOW, true);
        mBoardController.addKillToKilltrack(PlayerColor.BLUE, false);
        mBoardController.addKillToKilltrack(PlayerColor.BLUE, true);

        mBoardController.movePawnToCoordinate(new Position(1), PlayerColor.BLUE);
        mBoardController.movePawnToCoordinate(new Position(1), PlayerColor.PURPLE);
        mBoardController.movePawnToCoordinate(new Position(1), PlayerColor.YELLOW);
        mBoardController.movePawnToCoordinate(new Position(1), PlayerColor.GREY);
        mBoardController.movePawnToCoordinate(new Position(1), PlayerColor.GREEN);

        mBoardController.movePawnToCoordinate(new Position(2), PlayerColor.PURPLE);

        // Tile update test
        NormalTile normalTile = new NormalTile();
        AmmoCard ammoCard = new AmmoCard();
        ammoCard.setGuiID("043");
        normalTile.setAmmoCard(ammoCard);

        SpawnTile spawnTile = new SpawnTile();
        Weapon weapon = new Weapon();
        weapon.setGuiID("026");
        spawnTile.addWeapon(weapon);

        mBoardController.updateBoardTile(normalTile, new Position(2, 1));
        mBoardController.updateBoardTile(spawnTile, new Position(2, 0));

        boardPane.getChildren().add(newLoadedPane);
    }

    /**
     * Enable or disable selected box
     * @param box Selected box
     * @param enable true to enable box, false otherwise
     */
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

    /**
     * Change weapon image appearence, based on its loaded status
     * @param index Weapon index
     * @param value true if loaded, false otherwise
     */
    public void setWeaponLoadStatus (int index, boolean value) {
        ImageView weaponView = (ImageView)weaponBox.getChildren().get(index);
        if (value) {
            weaponView.setOpacity(1);
        }
        else {
            weaponView.setOpacity(0.4);
        }
    }

    /**
     * Update spawn's weapon box with latest model changes
     * @param ids Weapon ids
     */
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

    /**
     * Update player's powerUps with latest changes
     * @param ids PowerUp ids
     */
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

    /**
     * Set correct behaviours on powerUp images
     * @param powerUp PowerUp image
     * @param index PowerUp index
     */
    private void setPowerUpBehaviour (ImageView powerUp, int index) {
        powerUp.setOnMouseClicked(event -> {
            logToChat("Using powerUp with index: " + index);
            powerUp.setImage(null);
            powerUp.setDisable(true);
        });
    }

    /**
     * Log message to both GUI and console
     * @param message Message to log
     */
    public void logToChat (String message) {
        logger.info(message);
        Label label = new Label(message);
        chatBox.getChildren().add(label);
    }

    /**
     * Activate interactive board button grid
     */
    public void activateBoardButtonGrid() {
        mBoardController.switchButtonGridEnableStatus(true);
    }

    /**
     * Disable interactive board button grid
     */
    public void disableBoardButtonGrid() {
        mBoardController.switchButtonGridEnableStatus(false);
    }

    /**
     * Set enable property on action button box
     * @param value true to enable, false to disable
     */
    public void setEnableStatusActionButtonBox(boolean value) {
        buttonBox.setDisable(!value);
    }

    /**
     * Activate interactive board button grid for move action
     */
    public void activateButtonGridForMove () {
        activateBoardButtonGrid();
        setEnableStatusActionButtonBox(false);

        mBoardController.setupInteractiveGridForMoveAction();

        undoButton.setOnMouseClicked(event -> {
            disableBoardButtonGrid();
            setEnableStatusActionButtonBox(true);
        });
    }

    /**
     * Activate interactive board button grid for grab action
     */
    public void activateButtonGridForGrab () {
        activateBoardButtonGrid();
        setEnableStatusActionButtonBox(false);

        mBoardController.setupInteractiveGridForGrabAction();

        undoButton.setOnMouseClicked(event -> {
            disableBoardButtonGrid();
            setEnableStatusActionButtonBox(true);
        });
    }

    /**
     * Activate interactive board button grid for shoot action
     */
    public void activateButtonGridForShoot () {
        activateBoardButtonGrid();
        setEnableStatusActionButtonBox(false);

        mBoardController.setupInteractiveGridForShootAction(weaponBox);

        undoButton.setOnMouseClicked(event -> {
            disableBoardButtonGrid();
            setEnableStatusActionButtonBox(true);
        });
    }

    /**
     * Enable weapon box and set shoot behaviour on weapon images
     * @param pos Selected position for ShootAction
     */
    public void setShootOnWeapon (Position pos) {
        setBoxEnableStatus(weaponBox,true);
        setEnableStatusActionButtonBox(false);

        for (int i = 0; i < weaponBox.getChildren().size(); i++) {
            setShootingBehaviourOnWeapon(weaponBox.getChildren().get(i), i, pos);
        }

        undoButton.setOnMouseClicked(event -> {
            setBoxEnableStatus(weaponBox,false);
            setEnableStatusActionButtonBox(true);
        });
    }

    /**
     * Enable weapon box and set reload behaviour on weapon images
     */
    public void setReloadOnWeapon () {
        setBoxEnableStatus(weaponBox,true);
        setEnableStatusActionButtonBox(false);

        for (int i = 0; i < weaponBox.getChildren().size(); i++) {
            setReloadBehaviourOnWeapon(weaponBox.getChildren().get(i), i);
        }

        undoButton.setOnMouseClicked(event -> {
            setBoxEnableStatus(weaponBox,false);
            setEnableStatusActionButtonBox(true);
        });
    }

    /**
     * Set shoot behaviour on weapon images
     * @param weapon Weapon image
     * @param index Weapon index
     * @param pos ShootAction position
     */
    private void setShootingBehaviourOnWeapon (Node weapon, int index, Position pos) {
        weapon.setOnMouseClicked(event -> {
            if (weapon.getOpacity() == LOADED_OPACITY) {
                logToChat("Shooting with weapon of index: " + index);
                setWeaponLoadStatus(index, false);
                setBoxEnableStatus(weaponBox,false);
                setEnableStatusActionButtonBox(true);

                notify(new ActionRequest(new MoveShootAction(mClientColor, pos, index)));
            }
            else {
                logToChat("Can't shoot with unloaded weapon");
            }
        });
    }

    /**
     * Set reload behaviour on weapon images
     * @param weapon Weapon image
     * @param index Weapon index
     */
    private void setReloadBehaviourOnWeapon (Node weapon, int index) {
        weapon.setOnMouseClicked(event -> {
            if (weapon.getOpacity() == UNLOADED_OPACITY) {
                logToChat("Reload weapon of index: " + index);
                setWeaponLoadStatus(index, true);
                setBoxEnableStatus(weaponBox,false);
                setEnableStatusActionButtonBox(true);

                notify(new ActionRequest(new ReloadAction(index)));
            }
            else {
                logToChat("Can't reload an already loaded weapon");
            }
        });
    }
}
