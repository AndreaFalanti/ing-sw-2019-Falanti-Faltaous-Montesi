package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.action.MoveShootAction;
import it.polimi.se2019.model.action.ReloadAction;
import it.polimi.se2019.model.board.*;
import it.polimi.se2019.util.Jsons;
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
    @FXML
    private Button powerUpDiscardButton;
    @FXML
    private TabPane tabPane;
    @FXML
    private Pane directionButtonsPane;
    @FXML
    private Pane roomColorButtonsPane;
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


    private static final Logger logger = Logger.getLogger(MainScreen.class.getName());

    private static final int ACTIONS_TAB = 0;
    private static final int PLAYERS_TAB = 1;
    private static final int EFFECTS_TAB = 2;
    private static final int DIRECTION_TAB = 3;
    private static final int TARGETS_TAB = 4;
    private static final int ROOM_TAB = 5;

    private static final double LOADED_OPACITY = 1;
    private static final double UNLOADED_OPACITY = 0.4;

    private GraphicView mView;
    private PlayerColor mClientColor;

    private BoardPane mBoardController;
    private EnumMap<PlayerColor, PlayerPane> mPlayerControllers = new EnumMap<>(PlayerColor.class);

    private boolean[] mDiscardedPowerUpsCache = new boolean[3];
    private boolean[] mTargetSelectedCache;
    private int mActualEffectIndex;
    private List<Effect> mEffectsCache;
    private List<Effect> mMandatoryEffectsCache;

    private List<Button> mUndoWeaponButtons;
    private EnumMap<TileColor, Button> mRoomButtons = new EnumMap<>(TileColor.class);


    public BoardPane getBoardController() {
        return mBoardController;
    }

    public PlayerColor getClientColor() {
        return mClientColor;
    }

    public GraphicView getView() {
        return mView;
    }

    public Button getUndoButton() {
        return undoButton;
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

        setupDirectionButtonsBehaviour();
        setupRoomColorButtonsBehaviour();
        setWeaponTabsUndoButtonsBehaviour();
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
        List<Player> players = new ArrayList<>();
        players.add(new Player("aaa", PlayerColor.YELLOW));
        players.add(new Player("bbb", PlayerColor.GREY));
        players.add(new Player("ccc", PlayerColor.BLUE));

        for (Player player : players) {
            loader = new FXMLLoader(getClass().getResource("/fxml/otherPlayerPane.fxml"));
            newLoadedPane =  loader.load();

            PlayerPane otherPlayerController = loader.getController();
            mPlayerControllers.put(player.getColor(), otherPlayerController);

            otherPlayerController.setMainScreen(this);
            otherPlayerController.setupBoardImage(player.getColor());
            otherPlayerBoardsBox.getChildren().add(newLoadedPane);

            // test methods
            otherPlayerController.updateAmmo(new AmmoValue(2,1,1));
            otherPlayerController.addDamageTokens(PlayerColor.YELLOW, 5);

            Weapon[] weapons = {
                    new Weapon(),
                    new Weapon(),
                    null
            };
            weapons[0].setLoaded(true);
            weapons[0].setGuiID("022");
            weapons[1].setGuiID("023");

            ((OtherPlayerPane)otherPlayerController).updatePlayerWeapons(weapons);
            ((OtherPlayerPane)otherPlayerController).updatePowerUpNum(3);
        }
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
        GuiUtils.setBoxEnableStatus(weaponBox,true);
        setEnableStatusActionButtonBox(false);

        for (int i = 0; i < weaponBox.getChildren().size(); i++) {
            setShootingBehaviourOnWeapon(weaponBox.getChildren().get(i), i, pos);
        }

        undoButton.setOnMouseClicked(event -> {
            GuiUtils.setBoxEnableStatus(weaponBox,false);
            setEnableStatusActionButtonBox(true);
        });
    }

    /**
     * Enable weapon box and set reload behaviour on weapon images
     */
    public void setReloadOnWeapon () {
        GuiUtils.setBoxEnableStatus(weaponBox,true);
        setEnableStatusActionButtonBox(false);

        for (int i = 0; i < weaponBox.getChildren().size(); i++) {
            setReloadBehaviourOnWeapon(weaponBox.getChildren().get(i), i);
        }

        undoButton.setOnMouseClicked(event -> {
            GuiUtils.setBoxEnableStatus(weaponBox,false);
            setEnableStatusActionButtonBox(true);
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

        undoButton.setOnMouseClicked(event -> {
            GuiUtils.setBoxEnableStatus(weaponBox,false);
            setEnableStatusActionButtonBox(true);
        });
    }

    /**
     * Enable powerUp box for sending notification about what powerUps players want to discard
     */
    public void enablePowerUpBoxForSendingDiscardedIndex () {
        GuiUtils.setBoxEnableStatus(powerUpGrid, true);
        setEnableStatusActionButtonBox(false);
        powerUpDiscardButton.setDisable(false);

        for (int i = 0; i < mDiscardedPowerUpsCache.length; i++) {
            mDiscardedPowerUpsCache[i] = false;
            final int index = i;

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

        powerUpDiscardButton.setOnMouseClicked(event -> {
            notify(new PowerUpDiscardedRequest(mDiscardedPowerUpsCache, mView.getOwnerColor()));
            powerUpDiscardButton.setDisable(true);
        });

        undoButton.setOnMouseClicked(event -> {
            GuiUtils.setBoxEnableStatus(powerUpGrid,false);
            setEnableStatusActionButtonBox(true);
            for (Node node : powerUpGrid.getChildren()) {
                node.setOpacity(LOADED_OPACITY);
            }
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
                GuiUtils.setBoxEnableStatus(weaponBox,false);
                setEnableStatusActionButtonBox(true);

                notify(new ActionRequest(new MoveShootAction(mClientColor, pos, index), mView.getOwnerColor()));
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
                GuiUtils.setBoxEnableStatus(weaponBox,false);
                setEnableStatusActionButtonBox(true);

                notify(new ActionRequest(new ReloadAction(index), mView.getOwnerColor()));
            }
            else {
                logToChat("Can't reload an already loaded weapon");
            }
        });
    }

    /**
     * Set weapon image to send notification that contains its index when clicked
     * @param weapon Weapon imageView
     * @param index Index to set in notification
     */
    private void setIndexForwardingOnWeapon(Node weapon, int index) {
        weapon.setOnMouseClicked(event ->
            notify(new WeaponSelectedRequest(index, mView.getOwnerColor()))
        );
    }

    /**
     * Setup directions tab button behaviours. When clicked they will notify controller with selected direction.
     */
    private void setupDirectionButtonsBehaviour () {
        Direction[] directions = Direction.values();
        for (int i = 0; i < directions.length; i++) {
            final int index = i;
            directionButtonsPane.getChildren().get(i).setOnMouseClicked(event -> {
                logToChat("Selected direction: " + directions[index].toString());
                notify(new DirectionSelectedRequest(directions[index], mView.getOwnerColor()));
            });
        }
    }

    private void setupRoomColorButtonsBehaviour () {
        TileColor[] tileColors = TileColor.values();
        for (int i = 0; i < tileColors.length; i++) {
            final int index = i;
            mRoomButtons.put(tileColors[i], (Button)roomColorButtonsPane.getChildren().get(i));

            roomColorButtonsPane.getChildren().get(i).setOnMouseClicked(event -> {
                logToChat("Selected room: " + tileColors[index].toString());
                notify(new RoomSelectedRequest(tileColors[index], mView.getOwnerColor()));
            });
        }
    }

    public void activateDirectionTab () {
        tabPane.getSelectionModel().select(DIRECTION_TAB);
    }

    public void activateRoomTab (Set<TileColor> possibleColors) {
        tabPane.getSelectionModel().select(ROOM_TAB);
        TileColor[] tileColors = TileColor.values();

        for (TileColor tileColor : tileColors) {
            for (Button button : mRoomButtons.values()) {
                button.setDisable(!possibleColors.contains(tileColor));
            }
        }
    }

    public void activateTargetsTab (Set<PlayerColor> possibleTargets, int minTargets, int maxTargets) {
        tabPane.getSelectionModel().select(TARGETS_TAB);
        targetsBox.getChildren().clear();
        List<PlayerColor> playerTargets = new ArrayList<>(possibleTargets);
        // all false by default value
        mTargetSelectedCache = new boolean[possibleTargets.size()];
        targetsOkButton.setDisable(true);

        Label title;
        if (minTargets == maxTargets) {
            title = new Label("Select " + minTargets + "target");
        }
        else {
            title = new Label("Select from " + minTargets + " to " + maxTargets + " targets");
        }
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
                    logToChat(text + radioButton.getText());
                }
                else {
                    mTargetSelectedCache[index] = false;
                    logToChat("Deselected: " + radioButton.getText());
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

    public void activateEffectsTabForWeaponMode (Effect effect1, Effect effect2) {
        tabPane.getSelectionModel().select(EFFECTS_TAB);
        effectsBox.getChildren().clear();
        effectsOkButton.setDisable(true);

        ToggleGroup toggleGroup = new ToggleGroup();

        for(Effect effect : new Effect[] {effect1, effect2}) {
            AnchorPane child = createEffectPane(effect, true);

            RadioButton radioButton = new RadioButton();
            radioButton.setUserData(effect.getId());
            radioButton.setToggleGroup(toggleGroup);

            addToggleAndInsertInEffectsPane(child, radioButton);
        }

        effectsOkButton.setOnMouseClicked(event ->
            notify(new WeaponModeSelectedRequest(
                    (String) toggleGroup.getSelectedToggle().getUserData(), mView.getOwnerColor()))
        );
    }

    public void activateEffectsTabForEffects(SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {
        tabPane.getSelectionModel().select(EFFECTS_TAB);
        effectsBox.getChildren().clear();
        effectsOkButton.setDisable(true);

        mActualEffectIndex = 0;
        mEffectsCache = new ArrayList<>();
        mMandatoryEffectsCache = new ArrayList<>();

        for (Map.Entry<Integer, Set<Effect>> entry : priorityMap.entrySet()) {
            boolean enableEffectPane = true;//entry.getKey() == currentPriority;

            for (Effect effect : entry.getValue()) {
                if (!effect.isOptional()) {
                    mMandatoryEffectsCache.add(effect);
                }

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
                    }

                    effectsOkButton.setDisable(!checkMandatoryEffectsAreSelected());
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

    private void addToggleAndInsertInEffectsPane(AnchorPane anchorPane, Node toggle) {
        anchorPane.getChildren().add(toggle);
        AnchorPane.setBottomAnchor(toggle, 5d);
        AnchorPane.setRightAnchor(toggle, 5d);

        effectsBox.getChildren().add(anchorPane);
        VBox.setVgrow(anchorPane, Priority.ALWAYS);
    }

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

    private void setWeaponTabsUndoButtonsBehaviour () {
        for (Button button : mUndoWeaponButtons) {
            button.setOnMouseClicked(event -> {
                returnToActionTab();
                notify(new UndoWeaponInteractionRequest(mView.getOwnerColor()));
            });
        }
    }

    private void returnToActionTab() {
        for (int i = 0; i < tabPane.getTabs().size(); i++) {
            tabPane.getTabs().get(i).setDisable(i != ACTIONS_TAB && i != PLAYERS_TAB);
        }
        tabPane.getSelectionModel().selectFirst();
    }

    // TODO: use this in weapon tabs activation after testing is over
    private void activateWeaponRelatedTab (int index) {
        for (int i = 0; i < tabPane.getTabs().size(); i++) {
            tabPane.getTabs().get(i).setDisable(i != index);
        }
        tabPane.getSelectionModel().select(index);
    }

    private boolean checkMandatoryEffectsAreSelected () {
        return mEffectsCache.containsAll(mMandatoryEffectsCache);
    }
}
