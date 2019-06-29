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

        p1.move(new Position(0, 0));
        p2.move(new Position(0, 0));
        p3.move(new Position(0, 0));

        players.add(p1);
        players.add(p2);
        players.add(p3);

        return players;
    }

    public static PowerUpCard generateTeleportCard () {
        return new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(1,0,0));
    }

    public static PowerUpCard generateNewtonCard () {
        return new PowerUpCard(PowerUpType.NEWTON, new AmmoValue(0,1,0));
    }

    public static PowerUpCard generateTagbackGrenadeCard () {
        return new PowerUpCard(PowerUpType.TAGBACK_GRENADE, new AmmoValue(0,0,1));
    }

    public static PowerUpCard generateTargetingScopeCard () {
        return new PowerUpCard(PowerUpType.TARGETING_SCOPE, new AmmoValue(0,1,0));
    }

    /**
     * Generate a not started game in normal status with players without cards in hand. [Turn: 0]
     * @return A not started base game
     */
    public static Game generateNotStartedBaseGame() {
        Board board = Board.fromJson(Jsons.get("boards/game/board1"));
        List<Player> players = generatePlayerList();

        Game game = new Game(board, players, 5);

        return game;
    }

    /**
     * Generate game in normal status with players without cards in hand (typical game start). [Turn: 1]
     * @return A base game
     */
    public static Game generateBaseGame () {
        Board board = Board.fromJson(Jsons.get("boards/game/board1"));
        List<Player> players = generatePlayerList();

        Game game = new Game(board, players, 5);
        game.startNextTurn();

        return game;
    }

    /**
     * Generate game in normal status with players that have various powerUps in hand. [Turn: 1]
     * @return A game with powerUps to players
     */
    public static Game generateGameWithAllPowerUpsToPlayers () {
        Game game = generateBaseGame();

        for (Player player : game.getPlayers()) {
            try {
                player.addPowerUp(generateTeleportCard());
                player.addPowerUp(generateNewtonCard());
                player.addPowerUp(generateTagbackGrenadeCard());
            }
            catch (FullHandException e) {
                e.printStackTrace();
            }
        }

        return game;
    }

    /**
     * Generate game in final frenzy status, active player plays before first player in turn order. [Turn: 3]
     * @return A game in final frenzy mode
     */
    public static Game generateFinalFrenzyGameBeforeFirstPlayer () {
        Board board = Board.fromJson(Jsons.get("boards/game/board1"));
        List<Player> players = generatePlayerList();
        Game game = new Game(board, players, 1);

        game.startNextTurn();
        game.onTurnEnd();

        game.startNextTurn();
        game.handleDamageInteraction(game.getActivePlayer().getColor(), PlayerColor.GREY, new Damage(12,0));
        game.onTurnEnd();
        game.getPlayerFromColor(PlayerColor.GREY).respawn(new Position(0, 0));

        game.startNextTurn();

        return game;
    }

    /**
     * Generate game in final frenzy status, active player plays after first player in turn order. [Turn: 5]
     * @return A game in final frenzy mode
     */
    public static Game generateFinalFrenzyGameAfterFirstPlayer () {
        Game game = generateFinalFrenzyGameBeforeFirstPlayer();

        game.onTurnEnd();

        game.startNextTurn();
        game.onTurnEnd();

        game.startNextTurn();

        return game;
    }
}
