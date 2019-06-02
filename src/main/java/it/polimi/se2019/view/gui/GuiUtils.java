package it.polimi.se2019.view.gui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public final class GuiUtils {
    private GuiUtils () {}

    /**
     * Add an imageView to box parameter with given size
     * @param box Target box
     * @param height ImageView height
     * @param width ImageView width
     * @param image Image to assign to ImageView
     * @return ImageView created and added to given box
     */
    public static ImageView addImageViewToBox(Pane box, int height, int width, Image image) {
        ImageView imageView = new ImageView(image);
        setupImageViewSize(imageView, height, width);

        box.getChildren().add(imageView);
        return imageView;
    }

    public static void setupImageViewSize(ImageView imageView, int height, int width) {
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        imageView.setPreserveRatio(true);
    }

    /**
     * Get GridPane cell content at given position
     * @param gridPane GridPane to check
     * @param col Cell column index
     * @param row Cell row index
     * @return Cell content, null if not present
     */
    public static Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}
