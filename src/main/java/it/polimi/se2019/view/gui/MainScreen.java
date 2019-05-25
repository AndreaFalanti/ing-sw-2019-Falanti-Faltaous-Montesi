package it.polimi.se2019.view.gui;

import it.polimi.se2019.model.PlayerColor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainScreen {
    @FXML
    private Pane playerPane;

    public void loadBoard (PlayerColor color) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/playerPane.fxml"));
        Pane newLoadedPane =  loader.load();

        PlayerPane playerController = loader.getController();
        playerController.changeBoardImage(color.getPascalName());
        //testing various methods, they will be used in observer update methods
        playerController.addDamageTokens(PlayerColor.PURPLE, 3);
        playerController.addDeath();

        playerPane.getChildren().add(newLoadedPane);
    }

}
