/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.collectiveeditor.logic;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;
/**
 *
 * @author max
 */
public class SocketListener {
    
    
    
    public SocketListener(int port) {
        
    }
    
    public SocketListener(String IP, int port) {
        
    }
    
    public void connect() {
        try (ZContext context = new ZContext()) {
            ZMQ.Socket rep = context.createSocket(SocketType.REP);
            
        }
    }
}
