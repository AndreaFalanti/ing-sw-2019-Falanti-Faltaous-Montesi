package it.polimi.se2019.view.gui;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.network.client.ClientNetworkHandler;
import it.polimi.se2019.util.Observer;
import it.polimi.se2019.view.InitializationInfo;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.Request;
import javafx.application.Platform;
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
        ((GraphicUpdateHandler)mUpdateHandler).setMainController(mainFrameController);
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
        Platform.runLater(() -> mMainFrameController.logToChat(message));
    }

    @Override
    public void reportError(String error){
        Platform.runLater(() -> mMainFrameController.logToChat(error));
    }

    @Override
    public void showPowerUpsDiscardView() {
        Platform.runLater(() -> mMainFrameController.enablePowerUpBoxForSendingDiscardedIndex());
    }

    @Override
    public void showWeaponSelectionView(TileColor spawnColor) {
        Platform.runLater(() -> {
            if (spawnColor == null) {
                mMainFrameController.enableWeaponBoxForSendingIndex();
            }
            else {
                mMainFrameController.getBoardController().enableSpawnWeaponBoxForSendingIndex(spawnColor);
            }
        });
    }

    @Override
    public void showValidPositions(List<Position> positions) {
        //TODO maybe implement later
    }

    @Override
    public void showPowerUpSelectionView(List<Integer> indexes) {
        Platform.runLater(() -> mMainFrameController.setupPowerUpSelection(indexes));
    }

    @Override
    public void showRoomColorSelectionView(Set<TileColor> possibleColors) {
        Platform.runLater(() -> mMainFrameController.activateRoomTab(possibleColors));
    }

    @Override
    public void showDirectionSelectionView() {
        Platform.runLater(() -> mMainFrameController.activateDirectionTab());
    }

    @Override
    public void showPositionSelectionView(Set<Position> possiblePositions) {
        Platform.runLater(() -> mMainFrameController.activatePositionSelection(possiblePositions));
    }

    @Override
    public void showTargetsSelectionView(int minToSelect, int maxToSelect, Set<PlayerColor> possibleTargets) {
        Platform.runLater(() -> mMainFrameController.activateTargetsTab(possibleTargets, minToSelect, maxToSelect));
    }

    @Override
    public void showEffectsSelectionView(SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {
        Platform.runLater(() -> mMainFrameController.activateEffectsTabForEffects(priorityMap, possibleEffects));
    }

    @Override
    public void showWeaponModeSelectionView(Effect effect1, Effect effect2) {
        Platform.runLater(() -> mMainFrameController.activateEffectsTabForWeaponMode(effect1, effect2));
    }

    @Override
    public void showRespawnPowerUpDiscardView() {
        Platform.runLater(() -> mMainFrameController.setupRespawnPowerUpSelection());
    }

    @Override
    public void reinitialize(InitializationInfo initInfo) {
        Platform.runLater(() -> {
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
                String[] weaponIds = new String[3];
                for (int i = 0; i < owner.getWeapons().length; i++) {
                    if (owner.getWeapon(i) != null) {
                        weaponIds[i] = owner.getWeapon(i).getGuiID();
                    }
                    else {
                        weaponIds[i] = null;
                    }
                }
                controller.updateWeaponBox(weaponIds);

                String[] powerUpIds = new String[4];
                for (int i = 0; i < owner.getPowerUps().length; i++) {
                    if (owner.getPowerUpCard(i) != null) {
                        powerUpIds[i] = owner.getPowerUpCard(i).getGuiID();
                    }
                    else {
                        powerUpIds[i] = null;
                    }
                }
                controller.updatePowerUpGrid(powerUpIds);

                mActuallyDisplayedScene.getWindow().hide();
                stage.show();
                mActuallyDisplayedScene = scene;

                mNetworkHandler.registerObservablesFromView();
            }
            catch (IOException e) {
                logger.severe(e.getMessage());
            }
        });
    }

    @Override
    public void registerAll(Observer<Request> observer) {
        mMainFrameController.getBoardController().register(observer);
        mMainFrameController.register(observer);
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