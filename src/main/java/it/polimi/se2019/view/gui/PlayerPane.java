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


    public void changeBoardImage (String color) throws IOException {
        Image boardImage = new Image(GuiResourcePaths.PLAYER_BOARD_UP + color + ".png");
        Image actionImage = new Image(GuiResourcePaths.ACTION_TILE_UP + color + ".png");
        playerBoard.setImage(boardImage);
        actionTile.setImage(actionImage);
    }

    public void addDamageTokens (PlayerColor color, int quantity) {
        Image tokenImage = new Image(GuiResourcePaths.DAMAGE_TOKEN + color.getPascalName() + ".png");

        for (int i = 0; i < quantity; i++) {
            GuiUtils.addImageViewToBox(damageTokensBox, DAMAGE_TOKEN_HEIGHT, DAMAGE_TOKEN_WIDTH, tokenImage);
        }
    }

    public void addDeath () {
        Image skullImage = new Image(GuiResourcePaths.SKULL);
        GuiUtils.addImageViewToBox(deathsBox, SKULL_HEIGHT, SKULL_WIDTH, skullImage);
    }

    public void setPlayerName (String username) {
        nameLabel.setText(username);
    }

    public void updateAmmo (AmmoValue actualAmmo) {
        updateAmmoBox(redAmmoBox, actualAmmo.getRed(), "red");
        updateAmmoBox(yellowAmmoBox, actualAmmo.getYellow(), "yellow");
        updateAmmoBox(blueAmmoBox, actualAmmo.getBlue(), "blue");
    }

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

    private Rectangle instantiateAmmoSquare (String color) {
        Rectangle square = new Rectangle(AMMO_SQUARE_SIDE, AMMO_SQUARE_SIDE);
        square.setFill(Paint.valueOf(color));
        square.setStroke(Paint.valueOf("black"));

        return square;
    }
}
