/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.collectiveeditor.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author max
 */
public class SharedTextArea extends TextArea {
    
    org.openjfx.collectiveeditor.logic.Application app;

    public SharedTextArea(org.openjfx.collectiveeditor.logic.Application app) {
        this.app = app;
        
        this.setOnKeyPressed((KeyEvent t) -> {
            if (t.getCode().equals(KeyCode.ENTER)) {
                tabComplete();
                t.consume();
            }
        });

        System.out.println("What");
        this.caretPositionProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) -> {
            app.addCaretChange(SharedTextArea.this, (int) t1);
        });
    }

    private void tabComplete() {
        int pos = this.getCaretPosition() - 1;
        int nTabs = 0;
        String text = getText();
        StringBuilder newline = new StringBuilder("\n");
        while (pos > 0 && text.charAt(pos) != '\n') pos--;
        while (pos < text.length() && (text.charAt(pos) == ' ' || text.charAt(pos) == '\t'))
            newline.append(text.charAt(pos++));
        this.insertText(pos, newline.toString());
    }
}
