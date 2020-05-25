/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.collectiveeditor.logic;

import javafx.scene.control.TextArea;

/**
 *
 * @author max
 */
public interface LogicInterface {
    
    void createNewFile();
    
    String openFile(String path);
    
    void newTextHandler(TextArea ta);
    
    void addCaretChange(TextArea ta, int pos);
    
    boolean saveFile();
    
    void startConnection(TextArea ta, String IP, int port);
    
    void openConnection(TextArea ta, int port);
    
    /**
     * Test methods
     */
    
    /**
     * Test method to output changes of current text area
     * @param ta The text area to show the changes of
     */
    void outputChanges(TextArea ta);
}
