package it.polimi.se2019.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginScreen {
    @FXML
    private TextField usernameTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;
    @FXML
    private ToggleGroup network;
    @FXML
    private Pane infoPane;

    private static final Logger logger = Logger.getLogger(LoginScreen.class.getName());
    private static final String USERNAME_ERROR = "Insert a username before logging";
    private static final String CONNECTION_ERROR = "Select a connection before logging";

    private GraphicView mView;

    public GraphicView getView() {
        return mView;
    }

    public void setView(GraphicView view) {
        mView = view;
    }

    public void login () {
        String username = usernameTextField.getText();
        RadioButton radioButton = (RadioButton) network.getSelectedToggle();

        if (username == null || username.isEmpty()) {
            errorLabel.setText(USERNAME_ERROR);
            return;
        }
        if (radioButton == null) {
            errorLabel.setText(CONNECTION_ERROR);
            return;
        }

        String connection = radioButton.getText();

        Object[] logObjects = {username, connection};
        logger.log(Level.INFO, "Login:\nUsername: {0}\nConnection: {1}", logObjects);

        waitingForPlayers();
        openMainScreen();
    }

    private void waitingForPlayers () {
        infoPane.getChildren().clear();

        Label label = new Label("Waiting for players...");
        label.setStyle("-fx-font: 20 segoe; -fx-font-weight: bold");

        infoPane.getChildren().add(label);
        label.setLayoutX(infoPane.getWidth() / 2 - 50);
        label.setLayoutY(infoPane.getHeight() / 2);
    }

    private void openMainScreen () {
        /*try {
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
            PlayerColor clientColor = PlayerColor.GREEN;
            GraphicView guiView = new GraphicView(clientColor, controller);
            controller.setView(guiView);
            controller.setClientColor(clientColor);


            controller.loadPlayerBoard(clientColor);
            controller.loadBoard();
            String[] ids = {"022", "023", "024"};
            controller.updatePowerUpGrid(ids);
            controller.updateWeaponBox(ids);

            stage.show();

            // TODO: delete this tests
            //controller.activateDirectionTab();

            Set<PlayerColor> set = new HashSet<>();
            set.add(PlayerColor.YELLOW);
            set.add(PlayerColor.GREY);
            set.add(PlayerColor.BLUE);
            controller.activateTargetsTab(set, 1, 2);

            SortedMap<Integer, Set<Effect>> sortedMap = new TreeMap<>();
            Set<Effect> effectsSet1 = new HashSet<>();
            Effect effect1 = new Effect("Effect1", 0, false, new AmmoValue(0,0,0));
            Effect effect2 = new Effect("Effect2", 0, true, new AmmoValue(1,0,1));
            Effect effect3 = new Effect("Effect3", 1, true, new AmmoValue(0,0,1));

            effectsSet1.add(effect1);
            effectsSet1.add(effect2);
            sortedMap.put(0, effectsSet1);
            Set<Effect> effectsSet2 = new HashSet<>();
            effectsSet2.add(effect3);
            sortedMap.put(1, effectsSet2);
            controller.activateEffectsTabForEffects(sortedMap, effectsSet1);

            infoPane.getScene().getWindow().hide();
        }
        catch (IOException e) {
            logger.severe(e.getMessage());
        }*/
    }
}
