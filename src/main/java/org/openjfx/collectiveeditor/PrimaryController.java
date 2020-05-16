package org.openjfx.collectiveeditor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import org.zeromq.ZMQ;

public class PrimaryController implements Initializable {

    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem newButton;
    @FXML
    private MenuItem openButton;
    @FXML
    private MenuItem saveButton;
    @FXML
    private MenuItem saveAsButton;
    @FXML
    private MenuItem closeButton;
    @FXML
    private MenuItem exitButton;
    @FXML
    private MenuItem connectButton;
    @FXML
    private MenuItem netOptionsButton;
    @FXML
    private HBox bottomBar;
    @FXML
    private Label lineLabel;
    @FXML
    private Label colLabel;
    @FXML
    private ChoiceBox<?> tabChoiceBox;
    @FXML
    private Spinner<Integer> spacesSpinner;
    @FXML
    private Tab newFileTab;
    @FXML
    private TextArea newFileTextArea;
    @FXML
    private MenuItem openConnectionButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("sources controller");
    }  
    
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
