package it.polimi.se2019.view.gui;

import it.polimi.se2019.model.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

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

    private static final int DAMAGE_TOKEN_HEIGHT = 55;
    private static final int DAMAGE_TOKEN_WIDTH = 40;

    private static final int SKULL_HEIGHT = 52;
    private static final int SKULL_WIDTH = 46;


    public void changeBoardImage (String color) throws IOException {
        Image boardImage = new Image(GuiResourcePaths.PLAYER_BOARD_UP + color + ".png");
        Image actionImage = new Image(GuiResourcePaths.ACTION_TILE_UP + color + ".png");
        playerBoard.setImage(boardImage);
        actionTile.setImage(actionImage);
    }

    public void addDamageTokens (PlayerColor color, int quantity) {
        Image tokenImage = new Image(GuiResourcePaths.DAMAGE_TOKEN + color.getPascalName() + ".png");

        for (int i = 0; i < quantity; i++) {
            addImageToBox(damageTokensBox, DAMAGE_TOKEN_HEIGHT, DAMAGE_TOKEN_WIDTH, tokenImage);
        }
    }

    public void addDeath () {
        Image skullImage = new Image(GuiResourcePaths.SKULL);
        addImageToBox(deathsBox, SKULL_HEIGHT, SKULL_WIDTH, skullImage);
    }

    private void addImageToBox (Pane box, int height, int width, Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        imageView.setPreserveRatio(true);

        box.getChildren().add(imageView);
    }
}
