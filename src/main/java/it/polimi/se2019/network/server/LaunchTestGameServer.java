package it.polimi.se2019.network.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.controller.weapon.Weapons;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.update.ActivePlayerUpdate;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.UpdateHandler;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.util.Pair;
import it.polimi.se2019.view.VirtualView;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.cli.CLIView;
import sun.management.counter.perf.PerfLongCounter;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LaunchTestGameServer {
    public static final int SOCKET_PORT = 3456;
    public static final int RMI_PORT = 1111;

    public static final Game testGame = new Game(
            Board.fromJson(Jsons.get("boards/game/board1")),
            new ArrayList<>(Arrays.asList(
                    new Player("Mario", PlayerColor.PURPLE, new Position(2, 0)),
                    new Player("Luigi", PlayerColor.GREEN, new Position(2, 0)),
                    new Player("Smurfette", PlayerColor.BLUE, new Position(3, 2))
            )),
            8
    );

    public static void main(String[] args) {
        // initialize RMI
        try {
            LocateRegistry.createRegistry(RMI_PORT);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Controller controller = null;
        try (
                ServerSocket serverSocket = new ServerSocket(SOCKET_PORT)
        ) {
            controller = new Controller(
                    testGame,
                    Stream.of(
                            new Pair<>(
                                    PlayerColor.PURPLE,
                                    new CLIView(null)
                            ),
                            new Pair<>(
                                    PlayerColor.GREEN,
                                    new CLIView(null)
                            ),
                            new Pair<>(
                                    PlayerColor.BLUE,
                                    new VirtualView(
                                            PlayerColor.BLUE,
                                            // RmiConnection.create(RMI_PORT, PlayerColor.BLUE.getPascalName())
                                            SocketConnection.accept(serverSocket)
                                    )
                            )
                    )
                            .collect(Collectors.toMap(
                                    Pair::getFirst,
                                    Pair::getSecond
                            ))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        ((VirtualView) controller.getPlayerViews().get(PlayerColor.BLUE)).startReceivingRequests();

        controller.getPlayerViews().entrySet()
                .forEach(entry -> entry.getValue().reinitialize(testGame.extractViewInitializationInfo(entry.getKey())));

        testGame.startNextTurn();
        testGame.startNextTurn();
        testGame.startNextTurn();

        /*
        controller.startShootInteraction(PlayerColor.BLUE, Weapons.get("heatseeker").getBehaviour());
        while(controller.isHandlingShootInteraction()) {
            synchronized (controller.getShootInteraction().getLock()) {
                try {
                    controller.getShootInteraction().getLock().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(Arrays.stream(testGame.getPlayerFromColor(PlayerColor.GREEN).getDamageTaken())
                .collect(Collectors.toList()));
                */
    }
}
