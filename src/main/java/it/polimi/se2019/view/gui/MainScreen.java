package it.polimi.se2019.view.gui;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainScreen {
    @FXML
    private Pane playerPane;
    @FXML
    private Pane boardPane;

    private BoardPane mBoardController;

    public void loadPlayerBoard(PlayerColor color) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/playerPane.fxml"));
        Pane newLoadedPane =  loader.load();

        PlayerPane playerController = loader.getController();
        playerController.changeBoardImage(color.getPascalName());
        //testing various methods, they will be used in observer update methods
        playerController.addDamageTokens(PlayerColor.PURPLE, 3);
        playerController.addDeath();

        playerPane.getChildren().add(newLoadedPane);
    }

    public void loadBoard () throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/boardPane.fxml"));
        Pane newLoadedPane =  loader.load();

        mBoardController = loader.getController();
        Board board = Board.fromJson(Jsons.get("boards/game/board1"));
        mBoardController.initialize(board);
        mBoardController.addTargetDeath(5);

        boardPane.getChildren().add(newLoadedPane);
    }

    public void activateButtonGrid () {
        mBoardController.switchButtonGrid(true);
    }

    public void deactivateButtonGrid () {
        mBoardController.switchButtonGrid(false);
    }
}
