package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.AmmoCard;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.MoveAction;
import it.polimi.se2019.model.action.MoveGrabAction;
import it.polimi.se2019.model.board.*;
import it.polimi.se2019.util.Observable;
import it.polimi.se2019.view.request.ActionRequest;
import it.polimi.se2019.view.request.Request;
import it.polimi.se2019.view.request.UndoWeaponInteractionRequest;
import it.polimi.se2019.view.request.WeaponSelectedRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;

public class BoardPane extends Observable<Request> {
    @FXML
    private HBox redSpawnWeaponBox;
    @FXML
    private HBox blueSpawnWeaponBox;
    @FXML
    private HBox yellowSpawnWeaponBox;
    @FXML
    private HBox deathsBox;
    @FXML
    private GridPane boardGrid;
    @FXML
    private GridPane buttonGrid;
    @FXML
    private Label activePlayerLabel;
    @FXML
    private GridPane frenzyKilltrackGridPane;
    @FXML
    private Label remainingActionsLabel;
    @FXML
    private Label turnLabel;

    private static final int SKULL_HEIGHT = 40;
    private static final int SKULL_WIDTH = 32;

    private static final String GREEN_PAWN_COLOR = "#09cd02";
    private static final String BLUE_PAWN_COLOR = "#35f8de";
    private static final String YELLOW_PAWN_COLOR = "#dff800";
    private static final String GREY_PAWN_COLOR = "#979797";
    private static final String PURPLE_PAWN_COLOR = "#ac27ff";
    private static final int CIRCLE_RADIUS = 9;

    private static final int BOARD_COLUMNS = 4;
    private static final int BOARD_ROWS = 3;

    private static final int GRID_WIDTH = 122;
    private static final int GRID_HEIGHT = 122;

    private static final int FRENZY_KILLTRACK_TOKEN_WIDTH = 27;
    private static final int FRENZY_KILLTRACK_TOKEN_HEIGHT = 36;
    private static final int FRENZY_KILLTRACK_GRID_COLUMNS = 3;
    private static final int FRENZY_KILLTRACK_GRID_ROWS = 2;

    private static final String ACTIVE_PLAYER_PREFIX = "Active player: ";

    private MainScreen mMainController;
    private EnumMap<PlayerColor, Label> mFrenzyKilltrackLabels = new EnumMap<>(PlayerColor.class);
    private EnumMap<PlayerColor, Integer> mFrenzyKilltrackKillCounts = new EnumMap<>(PlayerColor.class);

    private Button[][] mInteractiveButtons = new Button[BOARD_COLUMNS][BOARD_ROWS];
    private List<StackPane> mKilltrack = new ArrayList<>();
    private EnumMap<PlayerColor, Circle> mPawns = new EnumMap<>(PlayerColor.class);
    private EnumMap<TileColor, HBox> mSpawnBoxes = new EnumMap<>(TileColor.class);
    private BoardSquare[][] mSquareControllers = new BoardSquare[BOARD_COLUMNS][BOARD_ROWS];

    private int mKilltrackTargetKills;
    private int mKilltrackActualIndex;


    public MainScreen getMainController() {
        return mMainController;
    }

    public void setMainController(MainScreen mainController) {
        mMainController = mainController;
    }

    /**
     * Add skulls to killtrack, based on kill targets of the game
     * @param value Kills target
     */
    public void addTargetDeath (int value) {
        Image skullImage = new Image(GuiResourcePaths.SKULL);
        for (int i = 0; i < value; i++) {
            StackPane stackPane = new StackPane();
            GuiUtils.addImageViewToBox(stackPane, SKULL_HEIGHT, SKULL_WIDTH, skullImage);
            stackPane.setAlignment(Pos.CENTER);

            deathsBox.getChildren().add(stackPane);
            mKilltrack.add(stackPane);
        }

        mKilltrackTargetKills = value;
        mKilltrackActualIndex = 0;
    }

    /**
     * Add token with killer color to killtrack, if overkill add a label that indicate that 2 tokens are added.
     * If in frenzy, it display a new box and add tokens to correct label.
     * @param color Killer color
     * @param overkill Indicate if kill is an overkill
     */
    public void addKillToKilltrack (PlayerColor color, boolean overkill) {
        if (mKilltrackActualIndex < mKilltrackTargetKills) {
            Image token = new Image(GuiResourcePaths.DAMAGE_TOKEN + color.getPascalName() + ".png");
            ((ImageView)mKilltrack.get(mKilltrackActualIndex).getChildren().get(0)).setImage(token);
            if (overkill) {
                Label label = new Label("x2");
                label.setTextFill(Paint.valueOf("white"));
                mKilltrack.get(mKilltrackActualIndex).getChildren().add(label);
            }

        }
        else {
            frenzyKilltrackGridPane.setVisible(true);
            int valueToAdd = (overkill) ? 2 : 1;
            int actualValue = mFrenzyKilltrackKillCounts.merge(color, valueToAdd, Integer::sum);
            mFrenzyKilltrackLabels.get(color).setText("x" + actualValue);
        }

        mKilltrackActualIndex++;
    }

    /**
     * Update active player text
     * @param color Active player color
     */
    public void updateActivePlayerText (PlayerColor color) {
        activePlayerLabel.setText(ACTIVE_PLAYER_PREFIX + color.toString());
    }

    public void updateRemainingActionsText (int value) {
        remainingActionsLabel.setText("Remaining actions: " + value);
    }

    public void updateTurnText (int value) {
        turnLabel.setText("Turn: " + value);
    }

    /**
     * Move player pawn of desired color to given position
     * @param pos Position where the player is moved
     * @param color Player color that is moved
     */
    public void movePawnToCoordinate(Position pos, PlayerColor color) {
        Circle pawn = mPawns.get(color);
        pawn.setVisible(true);
        mSquareControllers[pos.getX()][pos.getY()].addPawn(pawn);
    }

    /**
     * Set visibility of player pawn of given color
     * @param color Player color
     * @param value Value to set
     */
    public void setPlayerPawnVisibility (PlayerColor color, boolean value) {
        mPawns.get(color).setVisible(value);
    }

    /**
     * Update weapon images in desired spawn box
     * @param spawnBox Spawn's box to update
     * @param weaponIds GUI ids of weapons
     */
    public void updateWeaponsInSpawn (HBox spawnBox, String[] weaponIds) {
        if (weaponIds.length != 3) {
            throw new IllegalArgumentException("Weapon ids are not 3");
        }

        for (int i = 0; i < weaponIds.length; i++) {
            if (weaponIds[i] != null) {
                Image weaponImage = new Image(GuiResourcePaths.WEAPON_CARD + weaponIds[i] + ".png");
                ((ImageView)spawnBox.getChildren().get(i)).setImage(weaponImage);
            }
            else {
                ((ImageView)spawnBox.getChildren().get(i)).setImage(null);
            }
        }
    }

    /**
     * Switch board overlay button grid enable status
     * @param value Enable status
     */
    public void switchButtonGridEnableStatus(boolean value) {
        buttonGrid.setVisible(value);
    }

    /**
     * Initialize board pane
     * @param board Game board
     * @throws IOException Thrown if square fxml is not found
     */
    public void initialize (Board board) throws IOException {
        createPawns();
        createSpawnEnumMap();
        createBoardElements(board);
        initializeFrenzyKilltrack();
    }

    /**
     * Create enum spawn map
     */
    private void createSpawnEnumMap () {
        mSpawnBoxes.put(TileColor.BLUE, blueSpawnWeaponBox);
        mSpawnBoxes.put(TileColor.RED, redSpawnWeaponBox);
        mSpawnBoxes.put(TileColor.YELLOW, yellowSpawnWeaponBox);
    }

    /**
     * Test behavior of grid overlay buttons
     * @param x Coordinate x in grid
     * @param y Coordinate y in grid
     */
    public void handleClickedPos (int x, int y) {
        mMainController.logToChat("You clicked (" + x + ", " + y + ")");
        switchButtonGridEnableStatus(false);
    }

    /**
     * Create and initialize frenzy killtrack bonus box. It's set invisible but it will be activated when
     * first kill in frenzy mode is scored.
     */
    private void initializeFrenzyKilltrack () {
        int x = 0;
        int y = 0;

        for (PlayerColor color : PlayerColor.values()) {
            StackPane stackPane = new StackPane();

            Image image = new Image(GuiResourcePaths.DAMAGE_TOKEN + color.getPascalName() + ".png");
            Label label = new Label("x0");
            label.setTextFill(Paint.valueOf("white"));

            GuiUtils.addImageViewToBox(stackPane, FRENZY_KILLTRACK_TOKEN_HEIGHT, FRENZY_KILLTRACK_TOKEN_WIDTH, image);
            stackPane.getChildren().add(label);
            stackPane.setAlignment(Pos.CENTER);

            frenzyKilltrackGridPane.add(stackPane, x, y);
            mFrenzyKilltrackLabels.put(color, label);
            mFrenzyKilltrackKillCounts.put(color, 0);

            x++;
            if (x == FRENZY_KILLTRACK_GRID_COLUMNS) {
                x = 0;
                y++;
            }
        }

        frenzyKilltrackGridPane.setVisible(false);
    }

    /**
     * Update tile in board
     * @param tile Tile with updated info
     * @param pos Tile position
     */
    public void updateBoardTile (Tile tile, Position pos) {
        if (tile.getTileType().equals("normal")) {
            AmmoCard ammoCard = ((NormalTile)tile).getAmmoCard();
            BoardSquare tileController = mSquareControllers[pos.getX()][pos.getY()];
            if (ammoCard != null) {
                tileController.updateAmmoCard(ammoCard.getGuiID());
            }
            else {
                tileController.updateAmmoCard(null);
            }
        }
        else {
            HBox weaponBox = mSpawnBoxes.get(tile.getColor());
            Weapon[] weapons = ((SpawnTile)tile).getWeapons();

            String[] ids = new String[3];
            for (int i = 0; i < weapons.length; i++) {
                if (weapons[i] != null) {
                    ids[i] = weapons[i].getGuiID();
                }
                else {
                    ids[i] = null;
                }
            }
            updateWeaponsInSpawn(weaponBox, ids);
        }
    }

    /**
     * Create board elements from board set in the controller
     * @param board Board to initialize
     * @throws IOException Thrown if square fxml is not found
     */
    private void createBoardElements (Board board) throws IOException {
        for (int x = 0; x < BOARD_COLUMNS; x++) {
            for (int y = 0; y < BOARD_ROWS; y++) {
                Tile tile = board.getTileAt(new Position(x, y));

                if (tile != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/boardSquare.fxml"));
                    Pane newLoadedPane =  loader.load();

                    BoardSquare squareController = loader.getController();
                    mSquareControllers[x][y] = squareController;

                    if (tile.getTileType().equals("normal")) {
                        String guiId = ((NormalTile)tile).getAmmoCard().getGuiID();
                        squareController.addAmmoCardImage(guiId);
                    }
                    else {
                        Weapon[] weapons = ((SpawnTile)tile).getWeapons();
                        String[] ids = new String[3];
                        for (int i = 0; i < weapons.length; i++) {
                            ids[i] = (weapons[i] != null) ? weapons[i].getGuiID() : null;
                        }

                        updateWeaponsInSpawn(mSpawnBoxes.get(tile.getColor()), ids);
                    }

                    boardGrid.add(newLoadedPane, x, y);

                    // set button in buttonGrid
                    addGridButton(x, y);
                }
                else {
                    mSquareControllers[x][y] = null;
                }
            }
        }

        buttonGrid.setVisible(false);
    }

    /**
     * Add grid button in board overlay in given position
     * @param x Coordinate x
     * @param y Coordinate y
     */
    private void addGridButton (int x, int y) {
        Button button = new Button("");
        button.setPrefHeight(GRID_HEIGHT);
        button.setPrefWidth(GRID_WIDTH);
        button.setOpacity(0.2);
        button.setOnMouseClicked(event ->
            handleClickedPos(x, y)
        );

        mInteractiveButtons[x][y] = button;
        buttonGrid.add(button, x, y);
    }

    /**
     * Method for creating circle shapes (pawns) of given color
     * @param color Color to use for fill
     * @return Circle created
     */
    private Circle createCircle (String color) {
        Circle circle = new Circle(CIRCLE_RADIUS);
        circle.setFill(Paint.valueOf(color));
        circle.setStroke(Paint.valueOf("black"));
        circle.setVisible(false);

        return circle;
    }

    /**
     * Create board player pawns
     */
    private void createPawns () {
        mPawns.put(PlayerColor.GREEN, createCircle(GREEN_PAWN_COLOR));
        mPawns.put(PlayerColor.BLUE, createCircle(BLUE_PAWN_COLOR));
        mPawns.put(PlayerColor.YELLOW, createCircle(YELLOW_PAWN_COLOR));
        mPawns.put(PlayerColor.GREY, createCircle(GREY_PAWN_COLOR));
        mPawns.put(PlayerColor.PURPLE, createCircle(PURPLE_PAWN_COLOR));
    }

    public void setupInteractiveGridForMoveAction () {
        for (int i = 0; i < BOARD_COLUMNS; i++) {
            for (int j = 0; j < BOARD_ROWS; j++) {
                int x = i;
                int y = j;

                if (mInteractiveButtons[i][j] != null) {
                    mInteractiveButtons[i][j].setOnMouseClicked(event -> {
                        mMainController.logToChat("Move Action in pos (" + x + ", " + y + ")");
                        switchButtonGridEnableStatus(false);
                        mMainController.setEnableStatusActionButtonBox(true);

                        notify(new ActionRequest(
                                new MoveAction(mMainController.getClientColor(), new Position(x, y), true),
                                mMainController.getView().getOwnerColor()));
                    });
                }
            }
        }
    }

    public void setupInteractiveGridForGrabAction () {
        for (int i = 0; i < BOARD_COLUMNS; i++) {
            for (int j = 0; j < BOARD_ROWS; j++) {
                int x = i;
                int y = j;

                if (mInteractiveButtons[i][j] != null) {
                    mInteractiveButtons[i][j].setOnMouseClicked(event -> {
                        mMainController.logToChat("Grab Action in pos (" + x + ", " + y + ")");
                        switchButtonGridEnableStatus(false);
                        mMainController.setEnableStatusActionButtonBox(true);

                        notify(new ActionRequest(
                                new MoveGrabAction(mMainController.getClientColor(), new Position(x, y)),
                                mMainController.getView().getOwnerColor()));
                    });
                }
            }
        }
    }

    public void setupInteractiveGridForShootAction (Node weaponBox) {
        for (int i = 0; i < BOARD_COLUMNS; i++) {
            for (int j = 0; j < BOARD_ROWS; j++) {
                int x = i;
                int y = j;

                if (mInteractiveButtons[i][j] != null) {
                    mInteractiveButtons[i][j].setOnMouseClicked(event -> {
                        mMainController.logToChat("Shoot Action pos set to: (" + x + ", " + y + ")");
                        switchButtonGridEnableStatus(false);
                        mMainController.setShootOnWeapon(new Position(x, y));
                    });
                }

                mMainController.getUndoButton().setOnMouseClicked(event -> {
                    GuiUtils.setBoxEnableStatus(weaponBox, false);
                    mMainController.setEnableStatusActionButtonBox(true);
                });
            }
        }
    }

    public void setupInteractiveGridForChoosingPosition (Set<Position> possiblePosition) {
        switchButtonGridEnableStatus(true);

        for (int i = 0; i < BOARD_COLUMNS; i++) {
            for (int j = 0; j < BOARD_ROWS; j++) {
                int x = i;
                int y = j;

                if (mInteractiveButtons[i][j] != null) {
                    mInteractiveButtons[i][j].setDisable(!possiblePosition.contains(new Position(i, j)));

                    mInteractiveButtons[i][j].setOnMouseClicked(event -> {
                        mMainController.logToChat("Selected position: (" + x + ", " + y + ")");
                        switchButtonGridEnableStatus(false);
                        for (Button[] buttons : mInteractiveButtons) {
                            for (Button button : buttons) {
                                if (button != null) {
                                    button.setDisable(false);
                                }
                            }
                        }
                    });
                }

                mMainController.getUndoButton().setOnMouseClicked(event -> {
                    switchButtonGridEnableStatus(false);
                    mMainController.returnToActionTab();
                    notify(new UndoWeaponInteractionRequest(mMainController.getView().getOwnerColor()));
                });
            }
        }
    }

    public void enableSpawnWeaponBoxForSendingIndex(TileColor spawnColor) {
        HBox selectedSpawn = mSpawnBoxes.get(spawnColor);

        GuiUtils.setBoxEnableStatus(selectedSpawn,true);
        mMainController.setEnableStatusActionButtonBox(false);

        for (int i = 0; i < selectedSpawn.getChildren().size(); i++) {
            final int index = i;
            selectedSpawn.getChildren().get(i).setOnMouseClicked(event -> {
                notify(new WeaponSelectedRequest(index, mMainController.getView().getOwnerColor()));
                resetWeaponBoxToDefault(selectedSpawn);
            });
        }

        mMainController.getUndoButton().setOnMouseClicked(event -> resetWeaponBoxToDefault(selectedSpawn));
    }

    private void resetWeaponBoxToDefault (Node weaponBox) {
        GuiUtils.setBoxEnableStatus(weaponBox,false);
        mMainController.setEnableStatusActionButtonBox(true);
    }
}
