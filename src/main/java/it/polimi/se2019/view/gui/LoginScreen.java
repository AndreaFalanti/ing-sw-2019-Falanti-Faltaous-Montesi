package it.polimi.se2019.view.gui;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.se2019.network.client.NetworkHandler;
import it.polimi.se2019.network.connection.RmiConnection;
import it.polimi.se2019.network.connection.SocketConnection;
import it.polimi.se2019.util.JarPath;
import it.polimi.se2019.util.Jsons;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GUI login screen, starting scene of the GUI application
 *
 * @author Andrea Falanti
 */
public class LoginScreen {
    @FXML
    private TextField usernameTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private ToggleGroup network;
    @FXML
    private Pane infoPane;
    @FXML
    private RadioButton socketRadioButton;
    @FXML
    private RadioButton rmiRadioButton;

    private static final Logger logger = Logger.getLogger(LoginScreen.class.getName());
    private static final String USERNAME_ERROR = "Insert a username before logging";
    private static final String CONNECTION_ERROR = "Select a connection before logging";
    private static final String USERNAME_ALREADY_TAKEN = "That username is already taken";

    private static final int SOCKET_TYPE = 0;
    private static final int RMI_TYPE = 1;

    private GraphicView mView;
    private NetworkHandler mNetworkHandler;
    private int mActualType = -1;
    private NetworkSettings mNetworkSettings;

    public GraphicView getView() {
        return mView;
    }

    public void setView(GraphicView view) {
        mView = view;
    }

    private class NetworkSettings {
        String host;
        int socketPort;
        int rmiPort;
    }

    @FXML
    public void initialize () {
        socketRadioButton.setUserData(SOCKET_TYPE);
        rmiRadioButton.setUserData(RMI_TYPE);

        Gson gson = new Gson();
        String jarPath = JarPath.getJarPath();

        try {
            JsonReader jsonReader = new JsonReader(new FileReader(jarPath + "connection.json"));
            mNetworkSettings = gson.fromJson(jsonReader, NetworkSettings.class);
            jsonReader.close();
        } catch (FileNotFoundException e) {
            mNetworkSettings = gson.fromJson(Jsons.get("configurations/connection"), NetworkSettings.class);
            try (FileWriter fileWriter = new FileWriter(jarPath + "connections.json")) {
                fileWriter.write(Jsons.get("configurations/connection"));
            }
            catch (IOException e1) {
                logger.severe(e1.getMessage());
            }
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
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

        switch ((int)radioButton.getUserData()) {
            case SOCKET_TYPE:
                if (mNetworkHandler == null || mActualType != SOCKET_TYPE) {
                    mNetworkHandler = new NetworkHandler(
                            mView,
                            SocketConnection.establish(mNetworkSettings.host, mNetworkSettings.socketPort)
                    );
                    mActualType = SOCKET_TYPE;
                }
                break;
            case RMI_TYPE:
                if (mNetworkHandler == null || mActualType != RMI_TYPE) {
                    mNetworkHandler = new NetworkHandler(
                            mView,
                            RmiConnection.establish(mNetworkSettings.host, mNetworkSettings.rmiPort)
                    );
                }
                mActualType = RMI_TYPE;
                break;
            default:
                throw new IllegalArgumentException("Connection type not recognised");
        }

        if (mNetworkHandler.sendUsername(username)) {
            mView.setNetworkHandler(mNetworkHandler);
            mNetworkHandler.startReceivingMessages();

            waitingForPlayers();
        }
        else {
            errorLabel.setText(USERNAME_ALREADY_TAKEN);
        }
    }

    private void waitingForPlayers () {
        infoPane.getChildren().clear();

        Label label = new Label("Waiting for players...");
        label.setStyle("-fx-font: 20 segoe; -fx-font-weight: bold");

        infoPane.getChildren().add(label);
        label.setLayoutX(infoPane.getWidth() / 2 - 50);
        label.setLayoutY(infoPane.getHeight() / 2);
    }
}
