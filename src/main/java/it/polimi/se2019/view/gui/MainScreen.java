package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.PowerUpCard;
import it.polimi.se2019.model.action.MoveReloadShootAction;
import it.polimi.se2019.model.action.MoveShootAction;
import it.polimi.se2019.model.action.ReloadAction;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.util.Observable;
import it.polimi.se2019.view.request.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * GUI controller of the main game scene, interact with all sub-controllers of the scene to handle game
 * interactions and display correct data on the screen
 *
 * @author Andrea Falanti
 */
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
    private ScrollPane scrollLogger;
    @FXML
    private VBox otherPlayerBoardsBox;
    @FXML
    private Pane buttonBox;
    @FXML
    private Pane normalActionsBox;
    @FXML
    private Pane frenzyActionsBox;
    @FXML
    private Button powerUpDiscardButton;
    @FXML
    private TabPane tabPane;
    @FXML
    private Pane directionButtonsPane;
    @FXML
    private Pane roomColorButtonsPane;
    @FXML
    private Pane ammoColorButtonsPane;
    @FXML
    private VBox effectsBox;
    @FXML
    private VBox targetsBox;
    @FXML
    private Button targetsOkButton;
    @FXML
    private Button effectsOkButton;
    @FXML
    private Button effectsUndoButton;
    @FXML
    private Button directionsUndoButton;
    @FXML
    private Button targetsUndoButton;
    @FXML
    private Button roomsUndoButton;
    @FXML
    private Button ammoUndoButton;


    private static final Logger logger = Logger.getLogger(MainScreen.class.getName());

    private static final int ACTIONS_TAB = 0;
    private static final int PLAYERS_TAB = 1;
    private static final int EFFECTS_TAB = 2;
    private static final int DIRECTION_TAB = 3;
    private static final int TARGETS_TAB = 4;
    private static final int ROOM_TAB = 5;
    private static final int AMMO_TAB = 6;

    private static final double LOADED_OPACITY = 1;
    private static final double UNLOADED_OPACITY = 0.4;

    private static final int POWER_UPS_GRID_ROWS = 2;
    private static final int POWER_UPS_GRID_COLUMNS = 2;

    private GraphicView mView;
    private PlayerColor mClientColor;

    private BoardPane mBoardController;
    private EnumMap<PlayerColor, PlayerPane> mPlayerControllers = new EnumMap<>(PlayerColor.class);

    private boolean[] mDiscardedPowerUpsCache = new boolean[3];
    private boolean[] mTargetSelectedCache;
    private int mActualEffectIndex;
    private List<Effect> mEffectsCache;
    private List<Integer> mPowerUpUsedCache;

    private List<Button> mUndoWeaponButtons;
    private EnumMap<TileColor, Button> mRoomButtons = new EnumMap<>(TileColor.class);
    private EnumMap<TileColor, Button> mAmmoButtons = new EnumMap<>(TileColor.class);


    public BoardPane getBoardController() {
        return mBoardController;
    }

    public PlayerColor getClientColor() {
        return mClientColor;
    }

    public GraphicView getView() {
        return mView;
    }

    public void setClientColor (PlayerColor color) {
        mClientColor = color;
    }

    public void setView(GraphicView view) {
        mView = view;
    }

    public PlayerPane getPlayerControllerFromColor(PlayerColor color) {
        return mPlayerControllers.get(color);
    }


    @FXML
    public void initialize () {
        mUndoWeaponButtons = new ArrayList<>();
        mUndoWeaponButtons.add(effectsUndoButton);
        mUndoWeaponButtons.add(targetsUndoButton);
        mUndoWeaponButtons.add(directionsUndoButton);
        mUndoWeaponButtons.add(roomsUndoButton);
        mUndoWeaponButtons.add(ammoUndoButton);

        setupDirectionButtonsBehaviour();
        setupRoomColorButtonsBehaviour();
        setupAmmoColorButtonsBehaviour();
        setWeaponTabsUndoButtonsBehaviour();

        resetAllPowerUpsBehaviourToDefault();
    }

    public void activateUndoButtonWithBehaviour (Runnable executableFunction) {
        undoButton.setDisable(false);
        undoButton.setOnMouseClicked(event -> {
            executableFunction.run();
            undoButton.setDisable(true);
        });
    }


    /**
     * Load player board of given color
     * @param ownerColor View owner player color
     * @param players Players to initialize
     * @throws IOException Thrown if fxml is not found
     */
    public void loadPlayerBoards(PlayerColor ownerColor, List<Player> players) throws IOException {
        for (Player player : players) {
            if (player.getColor() == ownerColor) {
                initializeMainPlayerBoard(ownerColor, player);
            }
            else {
                initializeOpponentPlayerBoard(player);
            }
        }
    }

    /**
     * Initialize player board of the owner of the view
     * @param ownerColor Owner player color
     * @param player Owner's player data
     * @throws IOException Thrown if fxml is not found
     */
    private void initializeMainPlayerBoard (PlayerColor ownerColor, Player player) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/playerPane.fxml"));
        Pane newLoadedPane =  loader.load();

        PlayerPane playerController = loader.getController();
        mPlayerControllers.put(ownerColor, playerController);
        playerController.setMainScreen(this);

        playerController.setupBoardImage(ownerColor);
        initializeCommonPlayerBoardInfo(player, playerController);

        // update damage, marks and deaths
        updateDamageMarksAndDeaths(player, playerController);

        playerPane.getChildren().add(newLoadedPane);
    }

    /**
     * Initialize opponent player board
     * @param player Opponent player data
     * @throws IOException Thrown if fxml is not found
     */
    private void initializeOpponentPlayerBoard (Player player) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/otherPlayerPane.fxml"));
        Pane newLoadedPane =  loader.load();

        PlayerPane otherPlayerController = loader.getController();
        mPlayerControllers.put(player.getColor(), otherPlayerController);

        otherPlayerController.setMainScreen(this);

        otherPlayerController.setupBoardImage(player.getColor());
        initializeCommonPlayerBoardInfo(player, otherPlayerController);
        updateDamageMarksAndDeaths(player, otherPlayerController);

        otherPlayerBoardsBox.getChildren().add(newLoadedPane);

        ((OtherPlayerPane)otherPlayerController).updatePlayerWeapons(player.getWeapons());

        int actualCardsNum = 0;
        for (PowerUpCard powerUpCard : player.getPowerUps()) {
            if (powerUpCard != null) {
                actualCardsNum++;
            }
        }
        ((OtherPlayerPane)otherPlayerController).updatePowerUpNum(actualCardsNum);
    }

    /**
     * Set various player board info, like name, ammo, score and also flip board if necessary
     * @param player Player data
     * @param playerController Player board controller
     */
    private void initializeCommonPlayerBoardInfo (Player player, PlayerPane playerController) {
        playerController.setPlayerName(player.getName());
        playerController.updateAmmo(player.getAmmo());
        playerController.setScore(player.getScore());
        playerController.eraseDamage();

        if (player.isBoardFlipped()) {
            playerController.flipBoard();
        }
    }

    /**
     * Update player board damage, marks and deaths on initialization
     * @param player Player data
     * @param playerController Player board controller
     */
    private void updateDamageMarksAndDeaths (Player player, PlayerPane playerController) {
        playerController.updateDamageTokens(player.getDamageTaken());

        for (int i = 0; i < player.getDeathsNum(); i++) {
            playerController.addDeath();
        }

        Map<PlayerColor, Integer> marksMap = player.getMarks();
        for (Map.Entry<PlayerColor, Integer> entry : marksMap.entrySet()) {
            playerController.updateMarkLabel(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Load game board
     * @param board Board to initialize
     * @param targetKills Kills target of the game
     * @param activePlayerColor Actual active player
     * @param remainingActions Remaining actions in this turn
     * @param turnNumber Actual turn number
     * @param players Players in the game
     * @param kills Actual kills done
     * @param overkills Actual overkills done
     * @throws IOException Thrown if fxml is not found
     */
    public void initializeBoardAndInfo(Board board, int targetKills, PlayerColor activePlayerColor,
                                       int remainingActions, int turnNumber, List<Player> players,
                                       List<PlayerColor> kills, List<PlayerColor> overkills) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/boardPane.fxml"));
        Pane newLoadedPane =  loader.load();

        mBoardController = loader.getController();
        mBoardController.setMainController(this);

        mBoardController.initialize(board);
        mBoardController.addTargetDeath(targetKills);
        mBoardController.updateActivePlayerText(activePlayerColor);
        mBoardController.updateRemainingActionsText(remainingActions);
        mBoardController.updateTurnText(turnNumber);

        int overkillsCounter = 0;
        for (PlayerColor kill : kills) {
            // assign overkill to first kill that is valid (less fidelity but same result). This could be
            // easily adjusted with a refactor in game class, replacing the two lists with a single custom list.
            if (overkills.get(overkillsCounter) == kill) {
                mBoardController.addKillToKilltrack(kill, true);
                overkillsCounter++;
            }
            else {
                mBoardController.addKillToKilltrack(kill, false);
            }

        }

        // update player positions
        for (Player player : players) {
            if (player.getPos() != null) {
                mBoardController.movePawnToCoordinate(player.getPos(), player.getColor());
            }
        }

        boardPane.getChildren().add(newLoadedPane);
    }

    /**
     * Update spawn's weapon box with latest model changes
     * @param ids Weapon ids
     * @param loaded Weapon's load status
     */
    public void updateWeaponBox (String[] ids, boolean[] loaded) {
        for (int i = 0; i < ids.length; i++) {
            ImageView weaponImageView = (ImageView)weaponBox.getChildren().get(i);

            if (ids[i] != null) {
                Image weaponImage = new Image(GuiResourcePaths.WEAPON_CARD + ids[i] + ".png");
                weaponImageView.setImage(weaponImage);
                double opacity = loaded[i] ? LOADED_OPACITY : UNLOADED_OPACITY;
                weaponImageView.setOpacity(opacity);
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
        for (int i = 0; i < ids.length; i++) {
            ImageView powerUpImageView = (ImageView)powerUpGrid.getChildren().get(i);

            if (ids[i] != null) {
                Image powerUpImage = new Image(GuiResourcePaths.POWER_UP_CARD + ids[i] + ".png");
                powerUpImageView.setImage(powerUpImage);
                powerUpImageView.setDisable(false);
            }
            else {
                powerUpImageView.setImage(null);
                powerUpImageView.setDisable(true);
            }
        }
    }

    /**
     * Reset all powerUps behaviour to default regular powerUp use
     */
    private void resetAllPowerUpsBehaviourToDefault () {
        for (int i = 0; i < powerUpGrid.getChildren().size(); i++) {
            setPowerUpDefaultBehaviour((ImageView) powerUpGrid.getChildren().get(i), i);
        }
    }

    /**
     * Set default behaviour on powerUp images
     * @param powerUp PowerUp image
     * @param index PowerUp index
     */
    private void setPowerUpDefaultBehaviour(ImageView powerUp, int index) {
        powerUp.setOnMouseClicked(event -> {
            if (powerUp.getImage() != null) {
                logToChat("Using powerUp with index: " + index, false);
                notify(new UsePowerUpRequest(index, mView.getOwnerColor()));
                powerUp.setDisable(true);
            }
        });
    }

    /**
     * Log message to both GUI and console
     * @param message Message to log
     * @param error Is this message an error?
     */
    public void logToChat(String message, boolean error) {
        logger.info(message);
        Label label = new Label(message);
        if (error) {
            label.setTextFill(Paint.valueOf("RED"));
        }

        chatBox.getChildren().add(label);
        //automatically scroll to last element
        scrollLogger.setVvalue(1d);
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

        activateUndoButtonWithBehaviour(() -> {
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

        activateUndoButtonWithBehaviour(() -> {
            disableBoardButtonGrid();
            setEnableStatusActionButtonBox(true);
        });
    }

    /**
     * Activate interactive board button grid for shoot action
     * @param frenzy Is a frenzy reload shoot?
     */
    public void activateButtonGridForShoot (boolean frenzy) {
        activateBoardButtonGrid();
        setEnableStatusActionButtonBox(false);

        mBoardController.setupInteractiveGridForShootAction(weaponBox, frenzy);

        activateUndoButtonWithBehaviour(() -> {
            disableBoardButtonGrid();
            setEnableStatusActionButtonBox(true);
        });
    }

    /**
     * Activate board button grid for normal shoot
     */
    public void activateButtonGridForNormalShoot () {
        activateButtonGridForShoot(false);
    }

    /**
     * Activate board button grid for frenzy reload shoot
     */
    public void activateButtonGridForFrenzyReloadShoot () {
        activateButtonGridForShoot(true);
    }

    /**
     * Enable weapon box and set shoot behaviour on weapon images
     * @param pos Selected position for ShootAction
     */
    public void setShootOnWeapon (Position pos) {
        logToChat("Select weapon for shoot", false);
        GuiUtils.setBoxEnableStatus(weaponBox,true);
        setEnableStatusActionButtonBox(false);

        for (int i = 0; i < weaponBox.getChildren().size(); i++) {
            setShootingBehaviourOnWeapon(weaponBox.getChildren().get(i), i, pos);
        }

        activateUndoButtonWithBehaviour(() -> {
            GuiUtils.setBoxEnableStatus(weaponBox,false);
            setEnableStatusActionButtonBox(true);
        });
    }

    /**
     * Enable weapon box and set reload behaviour on weapon images
     */
    public void setReloadOnWeapon () {
        logToChat("Select weapon to reload", false);
        GuiUtils.setBoxEnableStatus(weaponBox,true);
        setEnableStatusActionButtonBox(false);

        for (int i = 0; i < weaponBox.getChildren().size(); i++) {
            setReloadBehaviourOnWeapon(weaponBox.getChildren().get(i), i);
        }

        activateUndoButtonWithBehaviour(() -> {
            GuiUtils.setBoxEnableStatus(weaponBox,false);
            setEnableStatusActionButtonBox(true);
        });
    }

    public void setReloadShootOnWeapon (Position pos) {
        setReloadOnWeapon();

        // override on click behaviour
        for (int i = 0; i < weaponBox.getChildren().size(); i++) {
            setReloadShootBehaviourOnWeapon(weaponBox.getChildren().get(i), i, pos);
        }
    }

    /**
     * Set shoot behaviour on weapon images
     * @param weapon Weapon image
     * @param index Weapon index
     * @param pos ShootAction position
     */
    private void setShootingBehaviourOnWeapon (Node weapon, int index, Position pos) {
        weapon.setOnMouseClicked(event -> {
            logToChat("Shooting with weapon of index: " + index, false);
            GuiUtils.setBoxEnableStatus(weaponBox,false);
            setEnableStatusActionButtonBox(true);

            notify(new ActionRequest(new MoveShootAction(mClientColor, pos, index), mView.getOwnerColor()));
        });
    }

    /**
     * Set reload behaviour on weapon images
     * @param weapon Weapon image
     * @param index Weapon index
     */
    private void setReloadBehaviourOnWeapon (Node weapon, int index) {
        weapon.setOnMouseClicked(event -> {
            logToChat("Reload weapon of index: " + index, false);
            GuiUtils.setBoxEnableStatus(weaponBox,false);
            setEnableStatusActionButtonBox(true);

            notify(new ActionRequest(new ReloadAction(index), mView.getOwnerColor()));
        });
    }

    private void setReloadShootBehaviourOnWeapon (Node weapon, int reloadIndex, Position pos) {
        weapon.setOnMouseClicked(event -> {
            logToChat("Reload weapon of index: " + reloadIndex, false);

            logToChat("Select weapon for shoot", false);
            for (int i = 0; i < weaponBox.getChildren().size(); i++) {
                int shootIndex = i;
                weaponBox.getChildren().get(i).setOnMouseClicked(event1 -> {
                    logToChat("Complete Reload-Shoot with weapon of index: " + reloadIndex, false);
                    GuiUtils.setBoxEnableStatus(weaponBox,false);
                    setEnableStatusActionButtonBox(true);

                    notify(new ActionRequest(new MoveReloadShootAction(mClientColor, pos, shootIndex, reloadIndex),
                            mView.getOwnerColor()));
                });
            }
        });
    }

    /**
     * Enable player weapon box for sending notification about clicked weapon index
     */
    public void enableWeaponBoxForSendingIndex () {
        GuiUtils.setBoxEnableStatus(weaponBox,true);
        setEnableStatusActionButtonBox(false);

        for (int i = 0; i < weaponBox.getChildren().size(); i++) {
            setIndexForwardingOnWeapon(weaponBox.getChildren().get(i), i);
        }

        activateUndoButtonWithBehaviour(() -> {
            GuiUtils.setBoxEnableStatus(weaponBox,false);
            setEnableStatusActionButtonBox(true);
        });
    }

    /**
     * Enable powerUp box for sending notification about what powerUps players want to discard
     */
    public void enablePowerUpBoxForSendingDiscardedIndex () {
        initializePowerUpInteraction();

        for (int i = 0; i < mDiscardedPowerUpsCache.length; i++) {
            mDiscardedPowerUpsCache[i] = false;
            final int index = i;

            if (i < powerUpGrid.getChildren().size()) {
                Node powerUp = powerUpGrid.getChildren().get(i);
                powerUp.setOpacity(UNLOADED_OPACITY);

                powerUp.setOnMouseClicked(event -> {
                    mDiscardedPowerUpsCache[index] = !mDiscardedPowerUpsCache[index];
                    if (powerUp.getOpacity() == LOADED_OPACITY) {
                        powerUp.setOpacity(UNLOADED_OPACITY);
                    }
                    else {
                        powerUp.setOpacity(LOADED_OPACITY);
                    }
                });
            }
        }

        powerUpDiscardButton.setOnMouseClicked(event -> {
            notify(new PowerUpDiscardedRequest(mDiscardedPowerUpsCache, mView.getOwnerColor()));
            finalizePowerUpInteraction();
        });

        activateUndoButtonWithBehaviour(this::finalizePowerUpInteraction);
    }

    /**
     * Method called at start of every powerUp interaction
     */
    private void initializePowerUpInteraction () {
        returnToActionTab();
        GuiUtils.setBoxEnableStatus(powerUpGrid, true);
        setEnableStatusActionButtonBox(false);
        powerUpDiscardButton.setDisable(false);
    }

    /**
     * Method called at end of every powerUp interaction
     */
    private void finalizePowerUpInteraction () {
        GuiUtils.setBoxEnableStatus(powerUpGrid,false);
        powerUpGrid.setDisable(false);
        setEnableStatusActionButtonBox(true);
        resetAllPowerUpsBehaviourToDefault();
        powerUpDiscardButton.setDisable(true);

        for (Node node : powerUpGrid.getChildren()) {
            node.setOpacity(LOADED_OPACITY);
        }
    }

    /**
     * Setup powerUp grid to start a multiple selection of powerUps (Targeting scopes and Tagback grenade usages)
     * @param indexes Valid powerUp indexes
     */
    public void setupPowerUpSelection (List<Integer> indexes) {
        initializePowerUpInteraction();
        powerUpDiscardButton.setText("Use");
        mPowerUpUsedCache = new ArrayList<>();

        for (Node node : powerUpGrid.getChildren()) {
            node.setOpacity(UNLOADED_OPACITY);
            node.setDisable(true);
        }

        for (Integer index : indexes) {
            Node powerUp = GuiUtils.getNodeFromGridPane(powerUpGrid, index%2, index/2);
            if (powerUp != null) {
                powerUp.setDisable(false);
                powerUp.setOnMouseClicked(event -> {
                    if (powerUp.getOpacity() == LOADED_OPACITY) {
                        powerUp.setOpacity(UNLOADED_OPACITY);
                        mPowerUpUsedCache.remove(index);
                    }
                    else {
                        powerUp.setOpacity(LOADED_OPACITY);
                        mPowerUpUsedCache.add(index);
                    }
                });
            }
        }

        powerUpDiscardButton.setOnMouseClicked(event -> {
            notify(new PowerUpsSelectedRequest(mPowerUpUsedCache, mView.getOwnerColor()));
            powerUpDiscardButton.setText("Discard");
            finalizePowerUpInteraction();
        });

        undoButton.setDisable(true);
    }

    /**
     * Setup powerUp grid for selecting a powerUp card discarded to respawn
     */
    public void setupRespawnPowerUpSelection () {
        initializePowerUpInteraction();
        powerUpDiscardButton.setDisable(true);

        for (int x = 0; x < POWER_UPS_GRID_COLUMNS; x++) {
            for (int y = 0; y < POWER_UPS_GRID_ROWS; y++) {
                Node powerUp = GuiUtils.getNodeFromGridPane(powerUpGrid, x, y);
                if (powerUp != null) {
                    int index = x + y * POWER_UPS_GRID_COLUMNS;

                    powerUp.setOnMouseClicked(event -> {
                        logToChat("Using powerUp of index " + index + " for respawn", false);
                        notify(new RespawnPowerUpRequest(index, mView.getOwnerColor()));
                        finalizePowerUpInteraction();
                    });
                }
            }
        }
    }

    /**
     * Set weapon image to send notification that contains its index when clicked
     * @param weapon Weapon imageView
     * @param index Index to set in notification
     */
    private void setIndexForwardingOnWeapon(Node weapon, int index) {
        weapon.setOnMouseClicked(event -> {
            logToChat("Switching weapon of index: " + index , false);
            GuiUtils.setBoxEnableStatus(weaponBox,false);
            setEnableStatusActionButtonBox(true);

            notify(new WeaponSelectedRequest(index, mView.getOwnerColor()));
        });
    }

    /**
     * Setup directions tab button behaviours. When clicked they will notify controller with selected direction.
     */
    private void setupDirectionButtonsBehaviour () {
        Direction[] directions = Direction.values();
        for (int i = 0; i < directions.length; i++) {
            final int index = i;
            directionButtonsPane.getChildren().get(i).setOnMouseClicked(event -> {
                logToChat("Selected direction: " + directions[index].toString(), false);
                notify(new DirectionSelectedRequest(directions[index], mView.getOwnerColor()));
                returnToActionTab();
            });
        }
    }

    /**
     * Setup room tab button behaviours. When clicked they will notify controller with selected room color.
     */
    private void setupRoomColorButtonsBehaviour () {
        TileColor[] tileColors = TileColor.values();
        for (int i = 0; i < tileColors.length; i++) {
            final int index = i;
            mRoomButtons.put(tileColors[i], (Button)roomColorButtonsPane.getChildren().get(i));

            roomColorButtonsPane.getChildren().get(i).setOnMouseClicked(event -> {
                logToChat("Selected room: " + tileColors[index].toString(), false);
                notify(new RoomSelectedRequest(tileColors[index], mView.getOwnerColor()));
                returnToActionTab();
            });
        }
    }

    /**
     * Setup ammo tab button behaviours. When clicked they will notify controller with selected ammo color.
     */
    private void setupAmmoColorButtonsBehaviour () {
        TileColor[] ammoColors = {TileColor.RED, TileColor.YELLOW, TileColor.BLUE};
        for (int i = 0; i < ammoColors.length; i++) {
            final int index = i;
            mAmmoButtons.put(ammoColors[i], (Button)ammoColorButtonsPane.getChildren().get(i));

            ammoColorButtonsPane.getChildren().get(i).setOnMouseClicked(event -> {
                logToChat("Selected ammo color: " + ammoColors[index].toString(), false);
                notify(new AmmoColorSelectedRequest(ammoColors[index], mView.getOwnerColor()));
                returnToActionTab();
            });
        }
    }

    /**
     * Activate direction tab
     */
    public void activateDirectionTab () {
        activateWeaponRelatedTab(DIRECTION_TAB);
    }

    /**
     * Activate room tab, deactivate all color buttons that are not valid in the interaction
     * @param possibleColors Valid room colors
     */
    public void activateRoomTab (Set<TileColor> possibleColors) {
        activateWeaponRelatedTab(ROOM_TAB);

        for (Map.Entry<TileColor, Button> entry : mRoomButtons.entrySet()) {
            entry.getValue().setDisable(!possibleColors.contains(entry.getKey()));
        }
    }

    /**
     * Activate ammo tab, deactivate all color buttons that are not valid in the interaction
     * @param possibleColors Valid ammo colors
     */
    public void activateAmmoTab (Set<TileColor> possibleColors) {
        activateWeaponRelatedTab(AMMO_TAB);

        for (Map.Entry<TileColor, Button> entry : mAmmoButtons.entrySet()) {
            entry.getValue().setDisable(!possibleColors.contains(entry.getKey()));
        }
    }

    /**
     * Activate target tab and customize it with received data
     * @param possibleTargets Valid targets
     * @param minTargets Min targets to select
     * @param maxTargets Max targets to select
     */
    public void activateTargetsTab (Set<PlayerColor> possibleTargets, int minTargets, int maxTargets) {
        activateWeaponRelatedTab(TARGETS_TAB);
        targetsBox.getChildren().clear();

        List<PlayerColor> playerTargets = new ArrayList<>(possibleTargets);
        // all false by default value
        mTargetSelectedCache = new boolean[possibleTargets.size()];
        targetsOkButton.setDisable(true);

        Label title;
        String labelText = (minTargets == maxTargets) ? "Select " + minTargets + " target" :
                "Select from " + minTargets + " to " + maxTargets + " targets";
        title = new Label(labelText);

        title.setTextFill(Paint.valueOf("white"));
        title.setStyle("-fx-font: 20 segoe; -fx-font-weight: bold");
        targetsBox.getChildren().add(title);

        for (int i = 0; i < playerTargets.size(); i++) {
            PlayerColor color = playerTargets.get(i);

            RadioButton radioButton = new RadioButton(
                    getPlayerControllerFromColor(color).getPlayerUsername() + " [" + color.getPascalName() + "]");
            radioButton.setTextFill(Paint.valueOf("white"));

            final int index = i;
            radioButton.setOnMouseClicked(event -> {
                // javafx is stupid and switch selected status before calling this, so logic is actually inverted
                if (radioButton.isSelected()) {
                    boolean select = canSelectAnotherTarget(maxTargets);
                    radioButton.setSelected(select);
                    mTargetSelectedCache[index] = select;
                    String text = select ? "Selected: " : "Deselected: ";
                    logToChat(text + radioButton.getText(), false);
                }
                else {
                    mTargetSelectedCache[index] = false;
                    logToChat("Deselected: " + radioButton.getText(), false);
                }

                targetsOkButton.setDisable(!checkTargetSelectionRestriction(minTargets, maxTargets));
            });

            targetsBox.getChildren().add(radioButton);
        }

        targetsOkButton.setOnMouseClicked(event -> {
            Set<PlayerColor> set = new HashSet<>();
            for (int i = 0; i < mTargetSelectedCache.length; i++) {
                if (mTargetSelectedCache[i]) {
                    set.add(playerTargets.get(i));
                }
            }

            notify(new TargetsSelectedRequest(set, mView.getOwnerColor()));
            returnToActionTab();
        });
    }

    /**
     * Activate effect tab for choosing a weapon mode in a shoot interaction
     * @param effect1 First shoot mode
     * @param effect2 Second shoot mode
     */
    public void activateEffectsTabForWeaponMode (Effect effect1, Effect effect2) {
        activateWeaponRelatedTab(EFFECTS_TAB);
        effectsBox.getChildren().clear();
        effectsOkButton.setDisable(true);

        ToggleGroup toggleGroup = new ToggleGroup();

        for(Effect effect : new Effect[] {effect1, effect2}) {
            AnchorPane child = createEffectPane(effect, true);

            RadioButton radioButton = new RadioButton();
            radioButton.setUserData(effect.getId());
            radioButton.setToggleGroup(toggleGroup);

            radioButton.setOnMouseClicked(event -> effectsOkButton.setDisable(false));

            addToggleAndInsertInEffectsPane(child, radioButton);
        }

        effectsOkButton.setOnMouseClicked(event -> {
            notify(new WeaponModeSelectedRequest(
                    (String) toggleGroup.getSelectedToggle().getUserData(), mView.getOwnerColor()));
            returnToActionTab();
        });
    }

    /**
     * Activate effects tab for choosing secondary effects
     * @param priorityMap Map of all effects, associated with their priority
     * @param possibleEffects Valid effects to use right now
     */
    public void activateEffectsTabForEffects(SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {
        activateWeaponRelatedTab(EFFECTS_TAB);
        effectsBox.getChildren().clear();

        mActualEffectIndex = 0;
        mEffectsCache = new ArrayList<>();

        for (Map.Entry<Integer, Set<Effect>> entry : priorityMap.entrySet()) {
            for (Effect effect : entry.getValue()) {
                boolean enableEffectPane = possibleEffects.contains(effect);

                AnchorPane child = createEffectPane(effect, enableEffectPane);

                CheckBox checkBox = new CheckBox();
                checkBox.setOnAction(event -> {
                    if (checkBox.isSelected()) {
                        checkBox.setText(Integer.toString(mActualEffectIndex));
                        mEffectsCache.add(effect);
                        mActualEffectIndex++;
                    }
                    else {
                        checkBox.setText("");
                        mEffectsCache.remove(effect);
                        if (mEffectsCache.isEmpty()) {
                            mActualEffectIndex = 0;
                        }
                    }
                });

                addToggleAndInsertInEffectsPane(child, checkBox);
            }
        }

        effectsOkButton.setOnMouseClicked(event -> {
            List<String> ids = new ArrayList<>();
            for (Effect effect : mEffectsCache) {
                ids.add(effect.getId());
            }

            notify(new EffectsSelectedRequest(ids, mView.getOwnerColor()));
            returnToActionTab();
        });
    }

    /**
     * Add toggle to effect pane and finalize its creation
     * @param anchorPane Effect pane
     * @param toggle Toggle to insert
     */
    private void addToggleAndInsertInEffectsPane(AnchorPane anchorPane, Node toggle) {
        anchorPane.getChildren().add(toggle);
        AnchorPane.setBottomAnchor(toggle, 5d);
        AnchorPane.setRightAnchor(toggle, 5d);

        effectsBox.getChildren().add(anchorPane);
        VBox.setVgrow(anchorPane, Priority.ALWAYS);
    }

    /**
     * Create an effect pane, customized with effect info received
     * @param effect Effect data
     * @param enabled Should this pane be enabled?
     * @return Created effect pane
     */
    private AnchorPane createEffectPane (Effect effect, boolean enabled) {
        AnchorPane anchorPane = new AnchorPane();

        String name = effect.isOptional() ? effect.getName() : effect.getName() + " [MANDATORY]";
        Label nameLabel = new Label(name);

        nameLabel.setStyle("-fx-font: 16 segoe; -fx-font-weight: bold; -fx-font-style: italic");

        Label costLabel = new Label(String.format("(%d, %d, %d)", effect.getCost().getRed(),
                effect.getCost().getYellow(), effect.getCost().getBlue()));

        anchorPane.getChildren().add(nameLabel);
        anchorPane.getChildren().add(costLabel);
        AnchorPane.setTopAnchor(nameLabel, 5d);
        AnchorPane.setTopAnchor(costLabel, 5d);
        AnchorPane.setLeftAnchor(nameLabel, 5d);
        AnchorPane.setRightAnchor(costLabel, 5d);

        anchorPane.setStyle("-fx-background-color: #eeeeee");

        if (!enabled) {
            anchorPane.setDisable(true);
            anchorPane.setOpacity(0.4);
        }

        return anchorPane;
    }

    /**
     * Activate board interactive button grid for a position selection
     * @param possiblePositions Valid positions to select
     */
    public void activatePositionSelection (Set<Position> possiblePositions) {
        logToChat("Choose a position", false);
        mBoardController.setupInteractiveGridForChoosingPosition(possiblePositions);
    }

    private boolean canSelectAnotherTarget (int max) {
        return getCurrentTargetCount() < max;
    }

    private boolean checkTargetSelectionRestriction (int min, int max) {
        int count = getCurrentTargetCount();

        return count >= min && count <= max;
    }

    private int getCurrentTargetCount() {
        int count = 0;
        for (int i = 0; i < mTargetSelectedCache.length; i++) {
            if (mTargetSelectedCache[i]) {
                count++;
            }
        }
        return count;
    }

    /**
     * Set default undo behaviour to all undo buttons in weapons related tabs
     */
    private void setWeaponTabsUndoButtonsBehaviour () {
        for (Button button : mUndoWeaponButtons) {
            button.setOnMouseClicked(event -> {
                returnToActionTab();
                notify(new UndoWeaponInteractionRequest(mView.getOwnerColor()));
            });
        }
    }

    /**
     * Return to action tab, deactivating all other panes except player info tab
     */
    public void returnToActionTab() {
        for (int i = 0; i < tabPane.getTabs().size(); i++) {
            tabPane.getTabs().get(i).setDisable(i != ACTIONS_TAB && i != PLAYERS_TAB);
        }

        if (!tabPane.getSelectionModel().isSelected(PLAYERS_TAB)) {
            tabPane.getSelectionModel().selectFirst();
        }
    }

    /**
     * Activate a specific weapon related tab, deactivating all the others in the process,
     * except player info tab (easier to check target for strategy)
     * @param index Tab index to activate
     */
    private void activateWeaponRelatedTab (int index) {
        for (int i = 0; i < tabPane.getTabs().size(); i++) {
            tabPane.getTabs().get(i).setDisable(i != index && i != PLAYERS_TAB);
        }
        tabPane.getSelectionModel().select(index);
    }

    /**
     * Send turn end notification
     */
    public void endTurn () {
        logToChat("Ending turn", false);
        notify(new TurnEndRequest(mView.getOwnerColor()));
    }

    /**
     * Set action box accordingly to game status (frenzy or normal)
     * @param isFrenzy Is game in frenzy mode?
     */
    public void switchActionBox (boolean isFrenzy) {
        normalActionsBox.setVisible(!isFrenzy);
        frenzyActionsBox.setVisible(isFrenzy);

        if (isFrenzy) {
            for (PlayerPane pane : mPlayerControllers.values()) {
                pane.setFrenzyActionTile();
            }
        }
    }

    /**
     * Enable view for active playing
     */
    public void activateView () {
        returnToActionTab();
        setEnableStatusActionButtonBox(true);
        if (powerUpGrid.isDisabled()) {
            resetAllPowerUpsBehaviourToDefault();
            powerUpGrid.setDisable(false);
        }
        undoButton.setDisable(true);
    }

    /**
     * Disable view while waiting for owner's turn
     */
    public void deactivateView () {
        returnToActionTab();
        GuiUtils.setBoxEnableStatus(powerUpGrid, false);
        GuiUtils.setBoxEnableStatus(weaponBox, false);
        setEnableStatusActionButtonBox(false);
        powerUpDiscardButton.setDisable(true);
        undoButton.setDisable(true);
    }
}
