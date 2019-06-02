package it.polimi.se2019.view.gui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

public class BoardSquare {
    @FXML
    private GridPane squareGrid;

    private static final int GRID_ROWS = 3;
    private static final int GRID_COLUMNS = 3;
    private static final int AMMO_CARD_WIDTH = 46;
    private static final int AMMO_CARD_HEIGHT = 48;

    private ImageView mAmmoCardView = null;

    public void addAmmoCardImage (String id) {
        Image ammoCardImage = new Image(GuiResourcePaths.AMMO_CARD + id + ".png");
        mAmmoCardView = new ImageView(ammoCardImage);
        mAmmoCardView.setPreserveRatio(true);
        mAmmoCardView.setFitWidth(AMMO_CARD_WIDTH);
        mAmmoCardView.setFitHeight(AMMO_CARD_HEIGHT);

        squareGrid.add(mAmmoCardView, 0, 2);
    }

    public void onAmmoCardRefill (String id) {
        mAmmoCardView.setImage(new Image(GuiResourcePaths.AMMO_CARD + id + ".png"));
    }

    public void onAmmoCardGrab () {
        onAmmoCardRefill("04");
    }

    public void addPawn (Circle circle) {
        for (int y = 0; y < GRID_ROWS; y++) {
            for (int x = 1; x < GRID_COLUMNS; x++) {
                // avoid cell (2,1)
                if (y == 2) {
                    x = 2;
                }

                if (GuiUtils.getNodeFromGridPane(squareGrid, x, y) == null) {
                    squareGrid.add(circle, x, y);
                    return;
                }
            }
        }
    }
}
