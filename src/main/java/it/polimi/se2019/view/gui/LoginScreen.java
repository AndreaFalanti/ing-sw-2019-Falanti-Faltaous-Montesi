package it.polimi.se2019.view.gui;

import it.polimi.se2019.network.client.ClientNetworkHandler;
import it.polimi.se2019.network.client.SocketNetworkHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private ClientNetworkHandler mNetworkHandler;
    private int mActualType = -1;

    public GraphicView getView() {
        return mView;
    }

    public void setView(GraphicView view) {
        mView = view;
    }

    @FXML
    public void initialize () {
        socketRadioButton.setUserData(SOCKET_TYPE);
        rmiRadioButton.setUserData(RMI_TYPE);
    }

    public void login () throws IOException {
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
                    mNetworkHandler = new SocketNetworkHandler(mView,
                            new Socket("localhost", 4567));
                }

                if (mNetworkHandler.sendUsername(username)) {
                    mView.setNetworkHandler(mNetworkHandler);
                    waitingForPlayers();
                }
                else {
                    errorLabel.setText(USERNAME_ALREADY_TAKEN);
                }
                break;
            case RMI_TYPE:
                // TODO
                break;
            default:
                throw new IllegalArgumentException("Connection type not recognised");
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
