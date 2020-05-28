/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.collectiveeditor.gui;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author max
 */
public class OpenConnectionFormController implements Initializable {

    @FXML
    private Spinner<Integer> portField;
    @FXML
    private Button acceptButton;
    @FXML
    private Button cancelButton;

    private boolean opened;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        portField.setValueFactory(new IntegerSpinnerValueFactory(1024, 65535));
        opened=false;
    }

    @FXML
    private void acceptButtonHandler(ActionEvent event) {
        if (available(getPort())) {
            opened = true;
            close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Puerto no disponible", ButtonType.OK);
            alert.showAndWait();
            opened=false;
        }
    }

    @FXML
    private void cancelButtonHandler(ActionEvent event) {
        opened = false;
        close();
    }

    private void close() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    public boolean getAccepted() {
        return opened;
    }

    public int getPort() {
        return portField.getValue();
    }

    public static boolean available(int port) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }
        return false;
    }
}
