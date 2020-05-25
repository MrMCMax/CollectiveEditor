package org.openjfx.collectiveeditor.gui;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.openjfx.collectiveeditor.App;

public class ConnectToFormController implements Initializable {

    @FXML
    private TextField ipTextField;
    @FXML
    private Spinner<Integer> portField;
    @FXML
    private Button acceptButton;
    @FXML
    private Button cancelButton;
    
    private boolean opened;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        portField.setValueFactory(new IntegerSpinnerValueFactory(1024, 65535));
    }

    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void acceptButtonHandler(ActionEvent event) {
        //Check that the IP address is correct
        try {
            InetAddress address = InetAddress.getByName(ipTextField.getText());
            opened=true;
            close();
        } catch (UnknownHostException e) {
            Alert alert = new Alert(AlertType.ERROR, "Host no encontrado, compruebe la IP", ButtonType.OK);
            alert.showAndWait();
            opened=false;
        }
    }

    @FXML
    private void cancelButtonHandler(ActionEvent event) {
        opened=true;
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
    
    public String getIP() {
        return ipTextField.getText();
    }
}