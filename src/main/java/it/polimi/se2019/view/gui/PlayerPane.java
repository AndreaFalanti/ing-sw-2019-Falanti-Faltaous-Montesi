package it.polimi.se2019.view.gui;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.IOException;


public class PlayerPane {
    @FXML
    private ImageView playerBoard;
    @FXML
    private ImageView actionTile;
    @FXML
    private HBox deathsBox;
    @FXML
    private HBox damageTokensBox;
    @FXML
    private HBox marksBox;
    @FXML
    private Label nameLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private VBox redAmmoBox;
    @FXML
    private VBox yellowAmmoBox;
    @FXML
    private VBox blueAmmoBox;

    private static final int DAMAGE_TOKEN_HEIGHT = 55;
    private static final int DAMAGE_TOKEN_WIDTH = 40;

    private static final int SKULL_HEIGHT = 52;
    private static final int SKULL_WIDTH = 46;

    private static final int AMMO_SQUARE_SIDE = 18;

    private MainScreen mMainScreen;
    private PlayerColor mPlayerBoardColor;


    public MainScreen getMainScreen() {
        return mMainScreen;
    }

    /**
     * Set reference to main controller
     * @param mainScreen Main controller reference
     */
    public void setMainScreen(MainScreen mainScreen) {
        mMainScreen = mainScreen;
    }

    /**
     * Change board image based on passed string
     * @param boardString String representing color and mode
     */
    private void changeBoardImage (String boardString) {
        Image boardImage = new Image(GuiResourcePaths.PLAYER_BOARD + boardString + ".png");
        Image actionImage = new Image(GuiResourcePaths.ACTION_TILE + boardString + ".png");
        playerBoard.setImage(boardImage);
        actionTile.setImage(actionImage);
    }

    public void setupBoardImage(PlayerColor color) throws IOException {
        mPlayerBoardColor = color;

        changeBoardImage(color.getPascalName());
    }

    /**
     * Flip board in final frenzy mode
     */
    public void flipBoard () {
        changeBoardImage("Flipped" + mPlayerBoardColor.getPascalName());
        // skulls are set aside when board is flipped
        deathsBox.setVisible(false);
    }

    /**
     * Add damage tokens to this board
     * @param color Color of player that dealt the damage
     * @param quantity Number of tokens to add
     */
    public void addDamageTokens (PlayerColor color, int quantity) {
        Image tokenImage = new Image(GuiResourcePaths.DAMAGE_TOKEN + color.getPascalName() + ".png");

        for (int i = 0; i < quantity; i++) {
            GuiUtils.addImageViewToBox(damageTokensBox, DAMAGE_TOKEN_HEIGHT, DAMAGE_TOKEN_WIDTH, tokenImage);
        }
    }

    /**
     * Add a skull to board
     */
    public void addDeath () {
        Image skullImage = new Image(GuiResourcePaths.SKULL);
        GuiUtils.addImageViewToBox(deathsBox, SKULL_HEIGHT, SKULL_WIDTH, skullImage);
    }

    /**
     * Set player username on board
     * @param username Player name
     */
    public void setPlayerName (String username) {
        nameLabel.setText(username);
    }

    /**
     * Set score value on the board
     * @param score Actual score
     */
    public void setScore (int score) {
        scoreLabel.setText(score + " pts");
    }

    /**
     * Update visible ammo with actual player's ammo
     * @param actualAmmo Current value of ammo
     */
    public void updateAmmo (AmmoValue actualAmmo) {
        updateAmmoBox(redAmmoBox, actualAmmo.getRed(), "red");
        updateAmmoBox(yellowAmmoBox, actualAmmo.getYellow(), "yellow");
        updateAmmoBox(blueAmmoBox, actualAmmo.getBlue(), "blue");
    }

    /**
     * Update a single ammo box color
     * @param ammoBox Ammo box to update
     * @param value Number of ammo of that color
     * @param color Color to set to square ammo
     */
    private void updateAmmoBox (VBox ammoBox, int value, String color) {
        int displayedAmmo = ammoBox.getChildren().size();
        while (displayedAmmo != value) {
            if (displayedAmmo < value) {
                ammoBox.getChildren().add(instantiateAmmoSquare(color));
            }
            else {
                ammoBox.getChildren().remove(displayedAmmo - 1);
            }

            displayedAmmo = ammoBox.getChildren().size();
        }
    }

    /**
     * Instantiate an ammo square of given color
     * @param color Color to use to fill square shape
     * @return Instantiated ammo square
     */
    private Rectangle instantiateAmmoSquare (String color) {
        Rectangle square = new Rectangle(AMMO_SQUARE_SIDE, AMMO_SQUARE_SIDE);
        square.setFill(Paint.valueOf(color));
        square.setStroke(Paint.valueOf("black"));

        return square;
    }
}
