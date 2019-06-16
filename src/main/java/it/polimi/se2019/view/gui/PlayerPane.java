package it.polimi.se2019.view.gui;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.PlayerColor;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.EnumMap;


public class PlayerPane {
    @FXML
    protected ImageView playerBoard;
    @FXML
    protected ImageView actionTile;
    @FXML
    protected HBox deathsBox;
    @FXML
    protected HBox damageTokensBox;
    @FXML
    protected HBox marksBox;
    @FXML
    protected Label nameLabel;
    @FXML
    protected Label scoreLabel;
    @FXML
    protected VBox redAmmoBox;
    @FXML
    protected VBox yellowAmmoBox;
    @FXML
    protected VBox blueAmmoBox;

    protected int mDamageTokenHeight = 55;
    protected int mDamageTokenWidth = 40;

    protected int mSkullHeight = 52;
    protected int mSkullWidth = 46;

    protected int mAmmoSquareSide = 18;

    private MainScreen mMainScreen;
    protected PlayerColor mPlayerBoardColor;
    private EnumMap<PlayerColor, Label> mMarksNumLabels = new EnumMap<>(PlayerColor.class);


    public MainScreen getMainScreen() {
        return mMainScreen;
    }

    public String getPlayerUsername () {
        return nameLabel.getText();
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

    /**
     * Setup a board of given player color
     * @param color Color of the board
     */
    public void setupBoardImage(PlayerColor color) {
        mPlayerBoardColor = color;

        changeBoardImage(color.getPascalName());
        initializeMarksBox();
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
            GuiUtils.addImageViewToBox(damageTokensBox, mDamageTokenHeight, mDamageTokenWidth, tokenImage);
        }
    }

    /**
     * Reset all damage visible on this board
     */
    public void eraseDamage () {
        damageTokensBox.getChildren().clear();
    }

    /**
     * Setup mark images and labels, will instantiate all marks color that are not equal to this board one.
     * This method also initialize a map for easier label updates.
     */
    public void initializeMarksBox () {
        for (PlayerColor color : PlayerColor.values()) {
            if (color != mPlayerBoardColor) {
                Image markImage = new Image(GuiResourcePaths.DAMAGE_TOKEN + color.getPascalName() + ".png");
                Label label = new Label("x0");
                label.setTextFill(Paint.valueOf("white"));

                StackPane stackPane = new StackPane();
                GuiUtils.addImageViewToBox(stackPane, mDamageTokenHeight, mDamageTokenWidth, markImage);
                stackPane.getChildren().add(label);
                stackPane.setAlignment(Pos.CENTER);

                mMarksNumLabels.put(color, label);

                marksBox.getChildren().add(stackPane);
            }
        }
    }

    /**
     * Update mark displayed value with given parameter
     * @param color Mark color to update
     * @param value Marks num to set
     */
    public void updateMarkLabel (PlayerColor color, int value) {
        mMarksNumLabels.get(color).setText("x" + value);
    }

    /**
     * Add a skull to board
     */
    public void addDeath () {
        Image skullImage = new Image(GuiResourcePaths.SKULL);
        GuiUtils.addImageViewToBox(deathsBox, mSkullHeight, mSkullWidth, skullImage);
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
        Rectangle square = new Rectangle(mAmmoSquareSide, mAmmoSquareSide);
        square.setFill(Paint.valueOf(color));
        square.setStroke(Paint.valueOf("black"));

        return square;
    }
}
