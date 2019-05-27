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

    private ImageView mAmmoCardView = null;

    public void addAmmoCardImage (String id) {
        Image ammoCardImage = new Image(GuiResourcePaths.AMMO_CARD + id + ".png");
        mAmmoCardView = new ImageView(ammoCardImage);
        mAmmoCardView.setPreserveRatio(true);

        squareGrid.add(mAmmoCardView, 0, 2);
    }

    public void onAmmoCardRefill (String id) {
        mAmmoCardView.setImage(new Image(GuiResourcePaths.AMMO_CARD + id + ".png"));
    }

    public void onAmmoCardGrab () {
        onAmmoCardRefill("04");
    }

    public void addPawn (Circle circle) {
        for (int x = 0; x < GRID_COLUMNS; x++) {
            for (int y = 1; y < GRID_ROWS; y++) {
                // avoid cell (2,1)
                if (x == 2) {
                    y = 2;
                }

                if (GuiUtils.getNodeFromGridPane(squareGrid, x, y) == null) {
                    squareGrid.add(circle, x, y);
                    return;
                }
            }
        }

        System.out.println("Error in script");
    }
}
