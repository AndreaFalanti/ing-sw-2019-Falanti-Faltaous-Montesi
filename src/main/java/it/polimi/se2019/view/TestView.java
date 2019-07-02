package it.polimi.se2019.view;

import com.google.gson.GsonBuilder;
import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.MoveAction;
import it.polimi.se2019.model.action.MoveGrabAction;
import it.polimi.se2019.model.action.ShootAction;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.model.update.*;
import it.polimi.se2019.model.update.serialization.UpdateFactory;
import it.polimi.se2019.util.Observer;
import it.polimi.se2019.view.request.ActionRequest;
import it.polimi.se2019.view.request.Request;
import it.polimi.se2019.view.request.TurnEndRequest;
import it.polimi.se2019.view.request.WeaponSelectedRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static it.polimi.se2019.util.InteractionUtils.input;
import static it.polimi.se2019.util.InteractionUtils.println;

public class TestView extends View {
    private Game mGame = null;

    private static Game makeGame(InitializationInfo info) {
        // TODO: make this better
        Game result = new Game(
                info.getBoard(),
                info.getPlayers(),
                8
        );

        System.out.println(result.getPlayers());

        result.register(update -> {});

        return result;
    }

    private static String head(String command) {
        return command.split("\\s+")[0];
    }

    private static String tail(String command) {
        String[] commands = command.split("\\s+");

        return Arrays.stream(commands, 1, commands.length)
                .collect(Collectors.joining(" "));
    }

    private static Position parsePosition(String toParse) {
        return new Position(
                Integer.parseInt(toParse.split("\\s+")[0]),
                Integer.parseInt(toParse.split("\\s+")[1])
        );
    }

    private Request parseCommand(String command) {
        if (head(command).equals("move"))
            return new ActionRequest(
                    new MoveAction(mOwnerColor, parsePosition(tail(command))),
                    mOwnerColor
            );

        else if (head(command).equals("grab"))
            return new ActionRequest(
                    new MoveGrabAction(mOwnerColor, parsePosition(tail(command))),
                    mOwnerColor
            );

        else if (head(command).equals("shoot"))
            return new ActionRequest(
                    new ShootAction(Integer.parseInt(tail(command))),
                    mOwnerColor
            );

        else if (head(command).equals("pass"))
            return new TurnEndRequest(mOwnerColor);

        else
            throw new IllegalArgumentException("Illegal command");
    }

    public TestView(PlayerColor ownerColor) {
        super(ownerColor, null);

        mUpdateHandler = new UpdateHandler() {
            @Override
            public void handle(PlayerPositionUpdate update) {
                mGame.getPlayerFromColor(update.getPlayerColor())
                        .move(update.getPlayerPos());
            }

            @Override
            public void handle(PlayerWeaponsUpdate update) {
                Arrays.stream(update.getWeapons())
                        .forEach(weapon ->
                                mGame.getPlayerFromColor(update.getPlayerColor()).addWeapon(weapon)
                        );
            }

            @Override
            public void handle(PlayerDamageUpdate update) {
                mGame.handleDamageInteraction(
                        update.getShooterPlayerColor(),
                        update.getDamagedPlayerColor(),
                        new Damage(update.getDamageTaken(), 0)
                );
            }

            @Override
            public void handle(PlayerMarksUpdate update) {
                mGame.handleDamageInteraction(
                        update.getShooterPlayerColor(),
                        update.getTargetPlayerColor(),
                        new Damage(0, update.getMarks())
                );
            }

            @Override
            public void fallbackHandle(Update update) {
                println(String.format(
                        "Received unhandled update: %s",
                        new GsonBuilder()
                                .setPrettyPrinting()
                                .create()
                                .toJson(UpdateFactory.toJsonTree(update))
                ));
            }
        };
    }

    @Override
    public void showMessage(String message) {
        println("MESSAGE: " + message);
    }

    @Override
    public void reportError(String error) {
        println("ERROR: " + error);
    }

    @Override
    public void showPowerUpsDiscardView() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showWeaponSelectionView(TileColor spawnColor) {
        Weapon[] weapons = mGame.getPlayerFromColor(mOwnerColor).getWeapons();

        println("Here are your weapons:");

        IntStream.range(0, weapons.length)
                .forEach(i ->
                        println(i + ": " + weapons[i].getName())
                );

        notify(new WeaponSelectedRequest(
                Integer.parseInt(input("choose one: ")),
                mOwnerColor
        ));
    }

    @Override
    public void showValidPositions(List<Position> positions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showPowerUpSelectionView(List<Integer> indexes) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void showRoomColorSelectionView(Set<TileColor> possibleColors) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void showDirectionSelectionView() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showPositionSelectionView(Set<Position> possiblePositions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void showWeaponModeSelectionView(Effect effect1, Effect effect2) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void showRespawnPowerUpDiscardView() {
        throw new UnsupportedOperationException();

    }

    @Override
    public void showAmmoColorSelectionView(Set<TileColor> possibleColors) {

    }

    @Override
    public void reinitialize(InitializationInfo initInfo) {
        mGame = makeGame(initInfo);
    }

    @Override
    public void registerAll(Observer<Request> observer) {
        register(observer);
    }

    public void startInteraction() {
        println("hello! Welcome to Adrenaline!");

        parseCommand(input("> "));
    }
}
