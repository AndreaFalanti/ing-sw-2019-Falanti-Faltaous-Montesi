package it.polimi.se2019.view.gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

/**
 * GUI controller of the content of a board tile, contained in board pane
 *
 * @author Andrea Falanti
 */
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

    public void updateAmmoCard (String id) {
        if (id != null) {
            mAmmoCardView.setImage(new Image(GuiResourcePaths.AMMO_CARD + id + ".png"));
        }
        else {
            mAmmoCardView.setImage(new Image(GuiResourcePaths.AMMO_CARD + "04" + ".png"));
        }
    }

    public void addPawn (Circle circle) {
        // check if selected pawn is already in this square, in that case do nothing
        for (Node node : squareGrid.getChildren()) {
            if (circle.equals(node)) {
                return;
            }
        }


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
