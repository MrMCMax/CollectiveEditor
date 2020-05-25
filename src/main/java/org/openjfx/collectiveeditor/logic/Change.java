/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.collectiveeditor.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.zeromq.ZMQ;

/**
 *
 * @author max
 */
public class Change implements Serializable {

    private static final long serialVersionUID = 1L;

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
     *
     * @param pos the position in the original text of the insertion
     * @param text the text to insert
     * @return true if no inserts were made before in that position, false if
     * the insert was appended to the last one in that position.
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

    
    public byte[] toBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] res = null;
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)){
            oos.writeObject(this);
            res = baos.toByteArray();
        } catch (IOException e) {
            res = null;
        } finally {
            try {
                baos.close();
            } catch (IOException e) {}
            return res;
        }
    }

    public static Change fromBytes(byte[] b) {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        Change c = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            c = (Change) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e);
            c = null;
        } finally {
            try {
                bais.close();
            } catch (IOException e) {}
            return c;
        }
    }
}
