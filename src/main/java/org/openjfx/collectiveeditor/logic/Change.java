/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.collectiveeditor.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author max
 */
public class Change {
    
    private SortedSet<Integer> deletions;
    private SortedMap<Integer, String> insertions;
    
    public Change() {
        deletions = new TreeSet<>();
        insertions = new TreeMap<>();
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
        if (insertions.containsKey(pos)) {
            String add = insertions.get(pos);
            insertions.put(pos, add + text);
            return false;
        } else {
            insertions.put(pos, text);
            return true;
        }
    }
    
    public SortedSet<Integer> getDeletions() {
        return deletions;
    }
    
    public SortedMap<Integer, String> getInsertions() {
        return insertions;
    }
    
    public void addAllDeletions(Set<Integer> deletions) {
        deletions.addAll(deletions);
    }
    
    public void addAllInsertions(Map<Integer, String> insertions) {
        insertions.forEach((pos, text) -> addInsertion(pos, text));
    }
}
