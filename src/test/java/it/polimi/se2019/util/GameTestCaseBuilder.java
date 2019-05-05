package it.polimi.se2019.util;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.board.Board;

import java.util.ArrayList;
import java.util.List;

public final class GameTestCaseBuilder {
    /**
     * Used to fill a list with valid players. [ORDER: blue, yellow, grey]
     */
    public static List<Player> generatePlayerList() {
        List<Player> players = new ArrayList<>();

        Player p1 = new Player("a", PlayerColor.BLUE);
        Player p2 = new Player("b", PlayerColor.YELLOW);
        Player p3 = new Player("c", PlayerColor.GREY);

        players.add(p1);
        players.add(p2);
        players.add(p3);

        return players;
    }

    public static PowerUpCard generateTeleportCard () {
        return new PowerUpCard("Teleport", new AmmoValue(0,1,0), new TeleportBehaviour());
    }

    //TODO: implement other cards generation methods

    public static Game generateGameWithAllPowerUpsToPlayers() {
        Board board = Board.fromJson(Jsons.get("boards/game/board1"));
        List<Player> players = generatePlayerList();

        for (Player player : players) {
            try {
                player.addPowerUp(generateTeleportCard());
            }
            catch (FullHandException e) {
                e.printStackTrace();
            }
        }

        return new Game(board, players, 3);
    }
}
