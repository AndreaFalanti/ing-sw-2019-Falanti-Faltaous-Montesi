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
        Pane newLoadedPane =  FXMLLoader.load(getClass().getResource("/fxml/playerPane.fxml"));
        playerPane.getChildren().add(newLoadedPane);
    }

}
