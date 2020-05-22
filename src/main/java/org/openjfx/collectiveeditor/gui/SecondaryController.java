package org.openjfx.collectiveeditor.gui;

import java.io.IOException;
import javafx.fxml.FXML;
import org.openjfx.collectiveeditor.App;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}