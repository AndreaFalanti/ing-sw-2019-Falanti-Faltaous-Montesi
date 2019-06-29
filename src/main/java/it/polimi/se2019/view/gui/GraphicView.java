package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.network.client.ClientNetworkHandler;
import it.polimi.se2019.view.InitializationInfo;
import it.polimi.se2019.view.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.logging.Logger;

public class GraphicView extends View {
    private static final Logger logger = Logger.getLogger(GraphicView.class.getName());

    private MainScreen mMainFrameController;
    private Scene mActuallyDisplayedScene;

    private ClientNetworkHandler mNetworkHandler;


    public GraphicView(PlayerColor color, MainScreen mainFrameController) {
        super(color, new GraphicUpdateHandler(mainFrameController));
        mMainFrameController = mainFrameController;
    }

    public void setMainFrameController(MainScreen mainFrameController) {
        mMainFrameController = mainFrameController;
    }

    public void setActuallyDisplayedScene(Scene actuallyDisplayedScene) {
        mActuallyDisplayedScene = actuallyDisplayedScene;
    }

    public void setNetworkHandler(ClientNetworkHandler networkHandler) {
        mNetworkHandler = networkHandler;
        register(mNetworkHandler);
    }

    @Override
    public void showMessage(String message){
        mMainFrameController.logToChat(message);
    }

    @Override
    public void reportError(String error){
        mMainFrameController.logToChat(error);
    }

    @Override
    public void showPowerUpsDiscardView() {
        mMainFrameController.enablePowerUpBoxForSendingDiscardedIndex();
    }

    @Override
    public void showWeaponSelectionView(TileColor spawnColor) {
        if (spawnColor == null) {
            mMainFrameController.enableWeaponBoxForSendingIndex();
        }
        else {
            mMainFrameController.getBoardController().enableSpawnWeaponBoxForSendingIndex(spawnColor);
        }
    }

    @Override
    public void showValidPositions(List<Position> positions) {
        //TODO maybe implement later
    }

    @Override
    public void showPowerUpSelectionView(List<Integer> indexes) {
        mMainFrameController.setupPowerUpSelection(indexes);
    }

    @Override
    public void showRoomColorSelectionView(Set<TileColor> possibleColors) {
        mMainFrameController.activateRoomTab(possibleColors);
    }

    @Override
    public void showDirectionSelectionView() {
        mMainFrameController.activateDirectionTab();
    }

    @Override
    public void showPositionSelectionView(Set<Position> possiblePositions) {
        mMainFrameController.activatePositionSelection(possiblePositions);
    }

    @Override
    public void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets) {
        mMainFrameController.activateTargetsTab(possibleTargets, minToSelect, maxToSelect);
    }

    @Override
    public void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {
        mMainFrameController.activateEffectsTabForEffects(priorityMap, possibleEffects);
    }

    @Override
    public void showWeaponModeSelectionView(Effect effect1, Effect effect2) {
        mMainFrameController.activateEffectsTabForWeaponMode(effect1, effect2);
    }

    @Override
    public void showRespawnPowerUpDiscardView() {
        mMainFrameController.setupRespawnPowerUpSelection();
    }

    @Override
    public void reinitialize(InitializationInfo initInfo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainScreen.fxml"));
            Stage stage = new Stage();

            Pane root = loader.load();
            stage.setTitle("Adrenalina");
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);

            BackgroundImage backgroundImage = new BackgroundImage(new Image(GuiResourcePaths.BACKGROUND + "bg.jpg"),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            root.setBackground(new Background(backgroundImage));

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/css/tabPaneBar.css").toExternalForm());
            stage.setScene(scene);
            stage.centerOnScreen();

            MainScreen controller = loader.getController();

            PlayerColor clientColor = initInfo.getOwnerColor();
            setMainFrameController(controller);
            setOwnerColor(clientColor);
            controller.setView(this);
            controller.setClientColor(clientColor);


            controller.loadPlayerBoards(clientColor, initInfo.getPlayers());
            controller.initializeBoardAndInfo(initInfo.getBoard(), initInfo.getSkullNum(),
                    initInfo.getActivePlayerColor(), initInfo.getRemainingActions(), initInfo.getTurnNumber(),
                    initInfo.getPlayers(), initInfo.getKills(), initInfo.getOverkills());

            Player owner = getOwnerPlayerFromList(initInfo.getPlayers());
            String[] ids = new String[3];
            for (int i = 0; i < owner.getWeapons().length; i++) {
                if (owner.getWeapon(i) != null) {
                    ids[i] = owner.getWeapon(i).getGuiID();
                }
                else {
                    ids[i] = null;
                }
            }
            controller.updateWeaponBox(ids);

            for (int i = 0; i < owner.getPowerUps().length; i++) {
                if (owner.getPowerUpCard(i) != null) {
                    ids[i] = owner.getPowerUpCard(i).getGuiID();
                }
                else {
                    ids[i] = null;
                }
            }
            controller.updatePowerUpGrid(ids);

            mActuallyDisplayedScene.getWindow().hide();
            stage.show();
            mActuallyDisplayedScene = scene;
        }
        catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    private Player getOwnerPlayerFromList (List<Player> players) {
        for (Player player : players) {
            if (player.getColor() == mOwnerColor) {
                return player;
            }
        }

        throw new IllegalArgumentException("Player that own this view is not present in the list!");
    }
}