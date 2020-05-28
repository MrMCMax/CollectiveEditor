/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.collectiveeditor.logic;

/**
 *
 * @author max
 */
public class localChangeMonitor {
    
    private Change localChange;
    
    private boolean dirty;
    
    /**
     * Creates a localChangeMonitor without a change. Get operations will be blocked
     * until a change is set.
     */
    public localChangeMonitor() {
        localChange = null;
        dirty = true;
    }
    
    /**
     * Creates a localChangeMonitor with a Change. Get operations are available since
     * the start
     * @param c the initial Change
     */
    public localChangeMonitor(Change c) {
        this.localChange = c;
        dirty = false;
    }
    
    /**
     * Sets the change. Notifies waiting gets. If there were no gets, new gets are
     * available.
     * @param c the change to set
     */
    public synchronized void setChange(Change c) {
        this.localChange = c;
        dirty = false;
        notifyAll();
    }
    
    /**
     * Blocks until the change is set, then returns it. Subsequent gets will have
     * to wait to another set to proceed.
     * @return The change, or null if the wait is interrupted.
     */
    public synchronized Change getChange() {
        while (dirty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
        dirty = true;
        return localChange;
    }
}
