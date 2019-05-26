package it.polimi.se2019.view.gui;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

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

    private static final int SKULL_HEIGHT = 41;
    private static final int SKULL_WIDTH = 32;
    private static final String GREEN_PAWN_COLOR = "#09cd02";
    private static final String BLUE_PAWN_COLOR = "#35f8de";
    private static final String YELLOW_PAWN_COLOR = "#dff800";
    private static final String GREY_PAWN_COLOR = "#979797";
    private static final String PURPLE_PAWN_COLOR = "#ac27ff";

    private List<ImageView> mKilltrack = new ArrayList<>();
    private EnumMap<PlayerColor, Circle> mPawns = new EnumMap<>(PlayerColor.class);

    public void addTargetDeath (int value) {
        Image skullImage = new Image(GuiResourcePaths.SKULL);
        for (int i = 0; i < value; i++) {
            mKilltrack.add(GuiUtils.addImageToBox(deathsBox, SKULL_HEIGHT, SKULL_WIDTH, skullImage));
        }
    }

    public void addPawnToCoordinate (Position pos, PlayerColor color) {

    }

    private Circle createCircle (String color) {
        Circle circle = new Circle(10);
        circle.setFill(Paint.valueOf(color));
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
}
