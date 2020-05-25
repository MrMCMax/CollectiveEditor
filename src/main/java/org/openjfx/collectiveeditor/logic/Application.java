/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.collectiveeditor.logic;

import java.util.HashMap;
import java.util.LinkedList;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
/**
 *
 * @author max
 */
public class Application implements LogicInterface {
    
    private LinkedList<SharedText> texts;
    private HashMap<TextArea, SharedText> areas;

    public Application() {
        areas = new HashMap<>(4);
        texts = new LinkedList<>();
    }
    
    @Override
    public void createNewFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void newTextHandler(TextArea ta) {
        areas.put(ta, new SharedText(ta));
    }
    
    @Override
    public String openFile(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean saveFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addCaretChange(TextArea ta, int pos) {
        areas.get(ta).addCaretChange(pos);
    }
    
    @Override
    public void startConnection(TextArea ta, String IP, int port) {
        areas.get(ta).connectTo(IP, port);
    }

    @Override
    public void openConnection(TextArea ta, int port) {
        areas.get(ta).openConnection(port);
    }
    
    /**
     * Test methods
     */
    @Override
    public void outputChanges(TextArea ta) {
        areas.get(ta).calculateChanges();
    }

    
}
