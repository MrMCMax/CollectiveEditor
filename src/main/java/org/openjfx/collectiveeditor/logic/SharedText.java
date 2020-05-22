/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.collectiveeditor.logic;

import java.util.LinkedList;
import javafx.scene.control.TextArea;
import org.openjfx.collectiveeditor.diff.diff_match_patch;
import org.openjfx.collectiveeditor.diff.diff_match_patch.Diff;
import org.openjfx.collectiveeditor.diff.diff_match_patch.Operation;

/**
 *
 * @author max
 */
public class SharedText {
    
    private TextArea textArea;
    private int myCaret;
    private String oldText;
    private SocketListener socket;
    private boolean connected = false;
    
    public SharedText(TextArea ta) {
        textArea = ta;
        myCaret = ta.getCaretPosition();
        connected = false;
        oldText = ta.getText();
    }
    
    /**
     * Calculates changes based on the last version of the text. Also updates oldText
     * @return a Change object representing the changes (deletions and insertions)
     */
    public Change calculateChanges() {
        Change c = new Change();
        String newText = getAllText();
        diff_match_patch dmp = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> changes = dmp.diff_main(oldText, newText);
        oldText = newText;
        dmp.diff_cleanupSemantic(changes);
        int start = 0;
        for (Diff d : changes) {
            if (d.operation == Operation.DELETE) {
                //int relativePos = start == myCaret ? d.text.length() : start - myCaret;
                //c = new Change.DeletionChange(relativePos);
            } else if (d.operation == Operation.INSERT) {
                //c = new Change.AdditionChange(d.text);
            }
            start += d.text.length();
        }
        System.out.println(changes);
        return c;
    }
    
    /**
     * Merges the changes in c with the current text.
     * @param c the changes to merge
     */
    public void computeChanges(Change c) {
        //...
    }
    
    public void openConnection(int port) {
        socket = new SocketListener(port);
        connected = true;
    }
    
    public void addCaretChange(int pos) {
        myCaret = pos;
    }
    
    public String getAllText() {
        return textArea.getText();
    }
}
