package org.example.backend.server;

import org.example.common.wrappers.SocketMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackendServer {
    private final int port;
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private volatile boolean running = false;

    public BackendServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        System.out.println("BackendServer started on port " + port);

        // Accept loop
        while (running) {
            Socket socket = serverSocket.accept();
            System.out.println("New connection from " + socket.getRemoteSocketAddress());
            ClientHandler handler = new ClientHandler(socket, this);
            clients.add(handler);
            Thread t = new Thread(handler);
            t.start();
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (clients) {
            for (ClientHandler c : clients) {
                c.shutdown();
            }
            clients.clear();
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public void broadcast(SocketMessage msg) {
        synchronized (clients) {
            for (ClientHandler c : new ArrayList<>(clients)) {
                try {
                    c.sendMessage(msg);
                } catch (Exception e) {
                    System.err.println("Failed to send message to client " + c.getClientId() + ": " + e.getMessage());
                }
            }
        }
    }

    public boolean sendToUser(String userId, SocketMessage msg) {
        synchronized (clients) {
            for (ClientHandler c : clients) {
                if (userId != null && userId.equals(c.getClientId())) {
                    try {
                        c.sendMessage(msg);
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public List<String> getActiveUserIds() {
        List<String> ids = new ArrayList<>();
        synchronized (clients) {
            for (ClientHandler c : clients) {
                if (c.getClientId() != null) ids.add(c.getClientId());
            }
        }
        return ids;
    }

    // Send an ACTIVE_USERS update with payload in format id:name,id:name,...
    public void broadcastActiveUsers() {
        StringBuilder sb = new StringBuilder();
        synchronized (clients) {
            for (ClientHandler c : clients) {
                String id = c.getClientId();
                if (id != null) {
                    String name = c.getClientName();
                    if (name == null) name = "";
                    if (sb.length() > 0) sb.append(",");
                    sb.append(id).append(":").append(name);
                }
            }
        }
        SocketMessage msg = new SocketMessage();
        msg.setType(SocketMessage.MessageType.ACTIVE_USERS);
        msg.setFromId("server");
        msg.setPayload(sb.toString());
        broadcast(msg);
    }

    // Small runnable main for manual testing
    public static void main(String[] args) {
        int port = 55555;
        BackendServer server = new BackendServer(port);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
