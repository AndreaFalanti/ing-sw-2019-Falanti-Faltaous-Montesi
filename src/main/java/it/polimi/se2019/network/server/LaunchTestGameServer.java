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


    public static void main(String[] args) {
        // initialize test game
        final Game testGame = new Game(
                Board.fromJson(Jsons.get("boards/game/board1")),
                new ArrayList<>(Arrays.asList(
                        new Player("Mario", PlayerColor.PURPLE, new Position(2, 0)),
                        new Player("Luigi", PlayerColor.GREEN, new Position(2, 0)),
                        new Player("Smurfette", PlayerColor.BLUE, new Position(3, 2))
                )),
                8
        );

        // initialize RMI
        try {
            LocateRegistry.createRegistry(RMI_PORT);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Connection rmiConnection = RmiConnection.create(RMI_PORT, "connection");

        Controller controller = null;
        try (
                ServerSocket serverSocket = new ServerSocket(SOCKET_PORT)
        ) {
            controller = new Controller(
                    testGame,
                    Stream.of(
                            new Pair<>(
                                    PlayerColor.PURPLE,
                                    new VirtualView(
                                            PlayerColor.PURPLE,
                                            SocketConnection.accept(serverSocket)
                                    )
                            ),
                            new Pair<>(
                                    PlayerColor.GREEN,
                                    new VirtualView(
                                            PlayerColor.GREEN,
                                            rmiConnection
                                    )
                            ),
                            new Pair<>(
                                    PlayerColor.BLUE,
                                    new VirtualView(
                                            PlayerColor.BLUE,
                                            RmiConnection.create(RMI_PORT, PlayerColor.BLUE.getPascalName())
                                            // SocketConnection.accept(serverSocket)
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

        controller.getPlayerViews().values().stream()
                .map(view -> (VirtualView) view)
                .forEach(VirtualView::startReceivingRequests);

        controller.getPlayerViews().entrySet()
                .forEach(entry -> entry.getValue().reinitialize(testGame.extractViewInitializationInfo(entry.getKey())));

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
