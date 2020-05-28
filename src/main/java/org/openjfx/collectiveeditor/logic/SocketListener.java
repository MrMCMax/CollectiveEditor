/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openjfx.collectiveeditor.logic;

import java.util.InputMismatchException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

/**
 *
 * @author max
 */
public class SocketListener {

    private boolean host;
    private String IP;
    private int port;
    private SharedText st;
    private ZMQ.Socket socket;
    private Thread thread;
    private String initialText;

    public static final String CONNECT_MSG = "connect";
    public static final String BLANK_MSG = "blank";

    public SocketListener(SharedText st, int port) {
        this.port = port;
        this.host = true;
    }

    public SocketListener(SharedText st, String IP, int port) {
        this.IP = IP;
        this.port = port;
        this.host = false;
    }

    public void connectTo() {
        thread = new Thread(this::connectCode);
        thread.setDaemon(true);
        thread.start();
    }

    public void openConnection(String initialText) {
        this.initialText = initialText;
        //Now we start the thread to send and receive messages
        thread = new Thread(this::openCode);
        thread.setDaemon(true);
        thread.start();
    }

    private void connectCode() {
        try (ZContext context = new ZContext()) {
            socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://" + IP + ":" + port);

            //Connect msg
            socket.send(CONNECT_MSG);
            //Receives the text from the host
            String text = socket.recvStr();
            st.setInitialText(text); //Will call fx thread
            //Sends blank message so that the host can start with the updates
            socket.send(BLANK_MSG);
            //Start cycle
            while (!thread.isInterrupted()) {
                byte[] change = socket.recv();
                if (thread.isInterrupted()) {
                    break;
                }
                Change extChange = Change.fromBytes(change);
                Thread.sleep(300);
                Platform.runLater(() -> st.cycle(extChange));
                Change localChanges = st.getLocalChanges();
                if (localChanges == null) {
                    break;
                }
                socket.send(localChanges.toBytes());
            }
        } catch (InterruptedException e) {
            socket.disconnect("tcp://" + IP + ":" + port);
        }
        Platform.runLater(() -> st.finishConnection());

    }

    private void openCode() {
        try (ZContext context = new ZContext()) {
            socket = context.createSocket(SocketType.REP);
            socket.bind("tcp://*:" + port);

            String msg = socket.recvStr();
            if (msg.equals(CONNECT_MSG)) {
                socket.send(initialText);
                if (socket.recvStr().equals(BLANK_MSG)) {
                    //Start loop
                    while (!thread.isInterrupted()) {
                        byte[] change = socket.recv();
                        if (thread.isInterrupted()) {
                            break;
                        }
                        Change extChange = Change.fromBytes(change);
                        Thread.sleep(300);
                        Platform.runLater(() -> st.cycle(extChange));
                        Change localChanges = st.getLocalChanges();
                        if (localChanges == null) {
                            break;
                        }
                        socket.send(localChanges.toBytes());
                    }
                    Platform.runLater(() -> st.finishConnection());
                } else {
                    throw new InputMismatchException("Bad connection");
                }
            } else {
                throw new InputMismatchException("Bad connection");
            }
        } catch (InterruptedException e) {
            socket.disconnect("tcp://*:" + port);
            Platform.runLater(() -> st.finishConnection());
        }
    }

    public Thread getThread() {
        return thread;
    }

    public void stopConnection() {
        thread.interrupt();
    }
}
