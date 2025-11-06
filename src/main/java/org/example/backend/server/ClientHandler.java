package org.example.backend.server;

import org.example.common.wrappers.SocketMessage;
import org.example.common.wrappers.userWrapper;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final BackendServer server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private volatile boolean running = false;
    private String clientId; // username/id of connected user
    private String clientName; // human-readable name

    public ClientHandler(Socket socket, BackendServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // Important: create ObjectOutputStream before ObjectInputStream to avoid stream deadlock
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            running = true;

            while (running) {
                Object obj;
                try {
                    obj = in.readObject();
                } catch (EOFException eof) {
                    // client closed connection
                    break;
                }
                if (obj instanceof SocketMessage) {
                    SocketMessage msg = (SocketMessage) obj;
                    handleMessage(msg);
                } else {
                    System.err.println("Received unknown object from client: " + obj);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("ClientHandler error: " + e.getMessage());
        } finally {
            cleanupAndNotify();
        }
    }

    private void handleMessage(SocketMessage msg) {
        switch (msg.getType()) {
            case LOGIN -> handleLogin(msg);
            case LOGOUT -> handleLogout(msg);
            case CHAT -> handleChat(msg);
            case PING -> sendPong();
            case SYSTEM -> System.out.println("SYSTEM message: " + msg.getPayload());
            default -> System.out.println("Unhandled message type: " + msg.getType());
        }
    }

    private void handleLogin(SocketMessage msg) {
        userWrapper user = msg.getUser();
        if (user != null) {
            this.clientId = user.getId();
            this.clientName = user.getName();
            System.out.println("User logged in: " + clientId);
            // broadcast to others that this user logged in
            SocketMessage notify = new SocketMessage();
            notify.setType(SocketMessage.MessageType.LOGIN);
            notify.setFromId(clientId);
            notify.setUser(user);
            notify.setPayload(user.getName());
            server.broadcast(notify);

            // send updated active users list to everyone
            server.broadcastActiveUsers();
        }
    }

    private void handleLogout(SocketMessage msg) {
        System.out.println("User requested logout: " + clientId);
        // broadcast logout
        SocketMessage notify = new SocketMessage();
        notify.setType(SocketMessage.MessageType.LOGOUT);
        notify.setFromId(clientId);
        notify.setPayload(msg.getPayload());
        server.broadcast(notify);
        // then shutdown this handler; cleanupAndNotify will remove and broadcast active users
        running = false;
        shutdown();
    }

    private void handleChat(SocketMessage msg) {
        String to = msg.getToId();
        msg.setFromId(clientId);
        if (to == null || to.isEmpty()) {
            // broadcast
            server.broadcast(msg);
        } else {
            // direct
            boolean ok = server.sendToUser(to, msg);
            if (!ok) {
                // optionally reply to sender that user not found
                SocketMessage reply = new SocketMessage();
                reply.setType(SocketMessage.MessageType.SYSTEM);
                reply.setFromId("server");
                reply.setToId(clientId);
                reply.setPayload("User '" + to + "' not available");
                try {
                    sendMessage(reply);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendPong() {
        SocketMessage pong = new SocketMessage();
        pong.setType(SocketMessage.MessageType.PING);
        pong.setFromId("server");
        try {
            sendMessage(pong);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessage(SocketMessage msg) throws IOException {
        if (out == null) throw new IOException("Output stream not initialized");
        out.writeObject(msg);
        out.flush();
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientName() { return clientName; }

    public void shutdown() {
        running = false;
        try {
            if (in != null) in.close();
        } catch (IOException ignored) {}
        try {
            if (out != null) out.close();
        } catch (IOException ignored) {}
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
    }

    private void cleanupAndNotify() {
        // broadcast logout if logged in
        if (clientId != null) {
            SocketMessage notify = new SocketMessage();
            notify.setType(SocketMessage.MessageType.LOGOUT);
            notify.setFromId(clientId);
            notify.setPayload("disconnect");
            server.broadcast(notify);
        }
        server.removeClient(this);
        // After removing, broadcast the updated active user list
        server.broadcastActiveUsers();
        shutdown();
    }
}
