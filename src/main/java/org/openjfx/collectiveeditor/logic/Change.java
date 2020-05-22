/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.collectiveeditor.logic;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author max
 */
public class Change {
    
    public HashSet<Integer> deletions;
    public HashMap<Integer, String> additions;
    
    public Change() {
        deletions = new HashSet<>();
        additions = new HashMap<>();
    }
    
    public void addDeletion(int pos) {
        deletions.add(pos);
    }
    
    /**
     * Creates an addition change. If the position was already used, the method
     * <b>appends</b> the new insertion to the old one.
     * @param pos the position in the original text of the insertion
     * @param text the text to insert
     * @return true if no inserts were made before in that position, false if the
     * insert was appended to the last one in that position.
     */
    public boolean addInsertion(int pos, String text) {
        if (additions.containsKey(pos)) {
            String add = additions.get(pos);
            additions.put(pos, add + text);
            return false;
        } else {
            additions.put(pos, text);
            return true;
        }
    }
}
