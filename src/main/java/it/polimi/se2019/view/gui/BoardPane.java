package it.polimi.se2019.view.gui;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.Tile;
import it.polimi.se2019.model.board.TileColor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class BoardPane {
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

    private static final int SKULL_HEIGHT = 41;
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

    private MainScreen mMainController;

    private Board mBoard;
    private Button[][] mInteractiveButtons = new Button[BOARD_COLUMNS][BOARD_ROWS];

    private List<ImageView> mKilltrack = new ArrayList<>();
    private EnumMap<PlayerColor, Circle> mPawns = new EnumMap<>(PlayerColor.class);
    private EnumMap<TileColor, HBox> mSpawnBoxes = new EnumMap<>(TileColor.class);
    //private GridPane[][] mInternalCellGrid = new GridPane[BOARD_COLUMNS][BOARD_ROWS];
    private BoardSquare[][] mSquareControllers = new BoardSquare[BOARD_COLUMNS][BOARD_ROWS];


    public MainScreen getMainController() {
        return mMainController;
    }

    public void setMainController(MainScreen mainController) {
        mMainController = mainController;
    }

    public void addTargetDeath (int value) {
        Image skullImage = new Image(GuiResourcePaths.SKULL);
        for (int i = 0; i < value; i++) {
            mKilltrack.add(GuiUtils.addImageViewToBox(deathsBox, SKULL_HEIGHT, SKULL_WIDTH, skullImage));
        }
    }

    public void addPawnToCoordinate (Position pos, PlayerColor color) {
        mSquareControllers[pos.getX()][pos.getY()].addPawn(mPawns.get(color));
    }

    public void updateWeaponsInSpawn (HBox spawn, String[] weaponIds) {
        if (weaponIds.length != 3) {
            throw new IllegalArgumentException("Weapon ids are not 3");
        }

        for (int i = 0; i < weaponIds.length; i++) {
            if (weaponIds[i] != null) {
                Image weaponImage = new Image(GuiResourcePaths.WEAPON_CARD + weaponIds[i] + ".png");
                ((ImageView)spawn.getChildren().get(i)).setImage(weaponImage);
            }
            else {
                ((ImageView)spawn.getChildren().get(i)).setImage(null);
            }
        }
    }

    public void switchButtonGrid(boolean value) {
        buttonGrid.setVisible(value);
    }

    public void initialize (Board board) throws IOException {
        mBoard = board;
        createPawns();
        createSpawnEnumMap();
        createBoardElements();
    }

    private void createSpawnEnumMap () {
        mSpawnBoxes.put(TileColor.BLUE, blueSpawnWeaponBox);
        mSpawnBoxes.put(TileColor.RED, redSpawnWeaponBox);
        mSpawnBoxes.put(TileColor.YELLOW, yellowSpawnWeaponBox);
    }

    public void handleClickedPos (int x, int y) {
        System.out.println("You clicked (" + x + ", " + y + ")");
        switchButtonGrid(false);
    }

    private void createBoardElements () throws IOException {
        for (int x = 0; x < BOARD_COLUMNS; x++) {
            for (int y = 0; y < BOARD_ROWS; y++) {
                if (mBoard.getTileAt(new Position(x, y)) != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/boardSquare.fxml"));
                    Pane newLoadedPane =  loader.load();

                    BoardSquare squareController = loader.getController();
                    mSquareControllers[x][y] = squareController;

                    /* TODO: delete image and pawns in fxml, add image only in normalTile and
                     try to check if pawns are correctly placed inside squareGrid */
                    Tile tile = mBoard.getTileAt(new Position(x, y));
                    if (tile.getTileType().equals("normal")) {
                        // TODO: get correct ammoCard id from tile
                        squareController.addAmmoCardImage("042");
                        squareController.addPawn(new Circle(9));
                        squareController.addPawn(new Circle(9));
                        squareController.addPawn(new Circle(9));
                    }
                    else {
                        squareController.addPawn(new Circle(9));
                        // TODO: get correct weaponCard ids from tile
                        String[] ids = {"022", "023", "024"};
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

        // from script dynamic instantiation (Problems with anchorPane probably)
        /*for (int x = 0; x < BOARD_COLUMNS; x++) {
            for (int y = 0; y < BOARD_ROWS; y++) {
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setPrefWidth(GRID_WIDTH);
                anchorPane.setPrefHeight(GRID_HEIGHT);
                anchorPane.setMinSize(GRID_WIDTH, GRID_HEIGHT);
                anchorPane.setPadding(new Insets(15));

                GridPane gridPane = new GridPane();

                // 3*3 grid
                for (int i = 0; i < 3; i++) {
                    RowConstraints rowConstraints = new RowConstraints();
                    rowConstraints.setValignment(VPos.CENTER);
                    rowConstraints.setFillHeight(true);
                    rowConstraints.setVgrow(Priority.SOMETIMES);
                    gridPane.getRowConstraints().add(rowConstraints);
                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    columnConstraints.setHalignment(HPos.CENTER);
                    columnConstraints.setFillWidth(true);
                    columnConstraints.setHgrow(Priority.SOMETIMES);
                    gridPane.getColumnConstraints().add(columnConstraints);

                    //gridPane.addColumn(i);
                    //gridPane.addRow(i);
                    // DEBUG: add circles on diagonal to see if instantiation is correct
                    gridPane.add(new Circle(10), i, i);
                }

                mInternalCellGrid[x][y] = gridPane;

                anchorPane.getChildren().add(gridPane);
                boardGrid.add(anchorPane, x, y);
                System.out.println("Size: " + anchorPane.getWidth() + "*" + anchorPane.getHeight());
            }
        }*/
    }

    private void addGridButton (int x, int y) {
        Button button = new Button("");
        button.setPrefHeight(GRID_HEIGHT);
        button.setPrefWidth(GRID_WIDTH);
        button.setOpacity(0.2);
        button.setOnMouseClicked(event -> {
            handleClickedPos(x, y);
        });

        mInteractiveButtons[x][y] = button;
        buttonGrid.add(button, x, y);
    }

    private Circle createCircle (String color) {
        Circle circle = new Circle(CIRCLE_RADIUS);
        circle.setFill(Paint.valueOf(color));
        circle.setStroke(Paint.valueOf("black"));
        circle.setVisible(false);

        return circle;
    }

    private void createPawns () {
        mPawns.put(PlayerColor.GREEN, createCircle(GREEN_PAWN_COLOR));
        mPawns.put(PlayerColor.BLUE, createCircle(BLUE_PAWN_COLOR));
        mPawns.put(PlayerColor.YELLOW, createCircle(YELLOW_PAWN_COLOR));
        mPawns.put(PlayerColor.GREY, createCircle(GREY_PAWN_COLOR));
        mPawns.put(PlayerColor.PURPLE, createCircle(PURPLE_PAWN_COLOR));
    }

    public Board getBoard() {
        return mBoard;
    }
}
