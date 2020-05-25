/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.collectiveeditor.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
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
     * Calculates changes based on the last version of the text.
     * Does not update oldText.
     * @return a Change object representing the changes (deletions and
     * insertions)
     */
    public Change calculateChanges() {
        Change c = new Change();
        String newText = getAllText();
        diff_match_patch dmp = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> changes = dmp.diff_main(oldText, newText);
        dmp.diff_cleanupSemantic(changes);
        int start = 0;
        for (Diff d : changes) {
            switch (d.operation) {
                case DELETE:
                    for (int i = start; i < start + d.text.length(); i++) {
                        c.addDeletion(i);
                    }
                    start += d.text.length();
                    break;
                case INSERT:
                    c.addInsertion(start, d.text);
                    break;
                default: //Equal
                    start += d.text.length();
                    break;
            }
        }
        System.out.println(changes);
        return c;
    }

    /**
     * Merges two changes that started from the same point into one.
     * Insertions of c1 will appear <b>before</b> the ones of c2
     * @param c1 change 1
     * @param c2 change 2
     * @return A Change object that represents both
     */
    public static Change mergeChanges(Change c1, Change c2) {
        Change c = c1;
        c.addAllDeletions(c2.getDeletions());
        c.addAllInsertions(c2.getInsertions());
        return c;
    }
    /**
     * Merges the changes in c with the current text.
     *
     * @param c the changes to merge
     */
    public void computeChanges(Change c) {
        //We have to delete first and then insert.
        //But we have to move the insertion points with the deletions, and the caret.
        SortedSet<Integer> deletions = c.getDeletions();
        SortedMap<Integer, String> insertions = c.getInsertions();
        Iterator<Integer> del = c.getDeletions().iterator();
        Iterator<Map.Entry<Integer, String>> ins = insertions.entrySet().iterator();
        SortedMap<Integer, String> newIns = new TreeMap<>();
        if (del.hasNext()) {
            //We have to fix the insertions and the caret
            int ndeletions = 0;
            boolean delCaretFound = false;
            int delPos = del.next();
            if (ins.hasNext()) {
                Map.Entry<Integer, String> insPos = ins.next();
                
                while (del.hasNext() && ins.hasNext()) {
                    if (delPos < insPos.getKey()) {
                        ndeletions++;
                        delPos = del.next();
                    } else {
                        //Skew insPos by the number of deletions that happened before
                        newIns.put(insPos.getKey() - ndeletions, insPos.getValue());
                        insPos = ins.next();
                    }
                    if (!delCaretFound && myCaret <= delPos) {
                        myCaret -= ndeletions;
                        delCaretFound = true;
                    }
                }
                //If there are more deletions
                while (del.hasNext() && !delCaretFound) {
                    if (myCaret <= delPos) {
                        myCaret -= ndeletions;
                        delCaretFound = true;
                    } else { 
                        delPos = del.next();
                        ndeletions++;
                    }
                }
                if (!delCaretFound) { myCaret -= ndeletions; }
                while (ins.hasNext()) {
                    newIns.put(insPos.getKey() - ndeletions, insPos.getValue());
                    insPos = ins.next();
                }
            }
        } else {
            newIns = insertions;
        }
        //Calculate the pos of the caret after insertions
        SortedMap<Integer, String> insMap = newIns.headMap(myCaret);
        insMap.values().forEach((s) -> myCaret += s.length());
        //Now we have to apply the changes
        for (int pos : deletions) {
            textArea.deleteText(pos, pos+1);
        }
        for (Map.Entry<Integer, String> pos : newIns.entrySet()) {
            textArea.insertText(pos.getKey(), pos.getValue());
        }
        textArea.positionCaret(myCaret);
    }
    
    private void updateText() {
        oldText = getAllText();
    }

    public void openConnection(int port) {
        socket = new SocketListener(port);
        connected = true;
    }
    
    public void connectTo(String IP, int port) {
        socket = new SocketListener(IP, port);
        connected = true;
    }

    public void addCaretChange(int pos) {
        myCaret = pos;
    }

    public String getAllText() {
        return textArea.getText();
    }
}
