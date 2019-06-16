package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.weapon.Weapon;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class OtherPlayerPane extends PlayerPane {
    @FXML
    private Button switchButton;
    @FXML
    private HBox cardDetailBox;
    @FXML
    private Pane boardPane;
    @FXML
    private Label powerUpNumLabel;


    @FXML
    public void initialize () {
        mDamageTokenHeight = 34;
        mDamageTokenWidth = 25;
        mSkullHeight = 32;
        mSkullWidth = 24;
        mAmmoSquareSide = 16;
    }

    /**
     * Switch visible pane between player board and card detail box
     */
    public void switchDetailPane () {
        if (boardPane.isVisible()) {
            boardPane.setVisible(false);
            cardDetailBox.setVisible(true);
            switchButton.setText("Player board");
        }
        else {
            boardPane.setVisible(true);
            cardDetailBox.setVisible(false);
            switchButton.setText("Weapons");
        }
    }

    /**
     * Update player weapons with the ones passed as parameters
     * @param weapons Current weapons
     */
    public void updatePlayerWeapons (Weapon[] weapons) {
        for (int i = 0; i < weapons.length; i++ ) {
            ImageView weaponView = (ImageView) cardDetailBox.getChildren().get(i);
            Image weaponImage = null;

            if (weapons[i] != null) {
                if (weapons[i].isLoaded()) {
                    weaponImage = new Image(GuiResourcePaths.WEAPON_CARD + "02.png");
                }
                else {
                    String id = weapons[i].getGuiID();
                    weaponImage = new Image(GuiResourcePaths.WEAPON_CARD + id + ".png");
                }
            }

            weaponView.setImage(weaponImage);
        }
    }

    /**
     * Update powerUp cards number displayed
     * @param num Actual num of powerUps
     */
    public void updatePowerUpNum (int num) {
        powerUpNumLabel.setText(Integer.toString(num));
    }
}
