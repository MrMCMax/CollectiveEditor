package org.openjfx.collectiveeditor.gui;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import org.openjfx.collectiveeditor.App;
import org.openjfx.collectiveeditor.diff.diff_match_patch;
import org.openjfx.collectiveeditor.diff.diff_match_patch.Diff;

import org.zeromq.ZMQ;

public class PrimaryController implements Initializable {

    private org.openjfx.collectiveeditor.logic.Application logic;

    private Tab selectedTab;

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
    private MenuItem openConnectionButton;
    @FXML
    private TabPane tabPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("sources controller");
        logic = new org.openjfx.collectiveeditor.logic.Application();
        
        //Create first textArea
        SharedTextArea sta = new SharedTextArea(logic);
        logic.newTextHandler(sta);
        newFileTab.setContent(sta);
        
        //Set the selected tab listener
        selectedTab = newFileTab;
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Tab> ov, Tab t, Tab t1) -> {
                    selectedTab = t1;
        });
    }

    private void createNewTab(String name) {
        SharedTextArea ta = new SharedTextArea(logic);
        Tab newTab = new Tab(name, ta);
        logic.newTextHandler(ta);
        tabPane.getTabs().add(newTab);
    }

    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void fileButtonHandle(ActionEvent event) {
        logic.outputChanges((TextArea) selectedTab.getContent());
    }

    @FXML
    private void exitHandle(ActionEvent event) {
    }
}
