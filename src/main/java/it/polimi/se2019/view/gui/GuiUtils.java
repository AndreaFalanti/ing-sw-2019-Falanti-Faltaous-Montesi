package it.polimi.se2019.view.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public final class GuiUtils {
    private GuiUtils () {}

    public static ImageView addImageToBox (Pane box, int height, int width, Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        imageView.setPreserveRatio(true);

        box.getChildren().add(imageView);
        return imageView;
    }
}
