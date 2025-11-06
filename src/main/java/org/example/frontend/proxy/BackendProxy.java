package org.example.frontend.proxy;

import org.example.common.wrappers.SocketMessage;
import org.example.common.wrappers.userWrapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class BackendProxy {
    private final String host;
    private final int port;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final List<Consumer<SocketMessage>> listeners = new CopyOnWriteArrayList<>();
    private final ExecutorService readerExecutor = Executors.newSingleThreadExecutor();
    private volatile boolean connected = false;

    public BackendProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public synchronized void connect() throws IOException {
        if (connected) return;
        socket = new Socket(host, port);
        // create output first
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());
        connected = true;
        startReader();
    }

    private void startReader() {
        readerExecutor.submit(() -> {
            try {
                while (connected && !socket.isClosed()) {
                    Object obj = in.readObject();
                    if (obj instanceof SocketMessage) {
                        SocketMessage msg = (SocketMessage) obj;
                        notifyListeners(msg);
                    } else {
                        System.err.println("BackendProxy: received unknown object: " + obj);
                    }
                }
            } catch (Exception e) {
                if (connected) {
                    System.err.println("BackendProxy reader error: " + e.getMessage());
                }
            } finally {
                try {
                    disconnectInternal();
                } catch (IOException ignored) {}
            }
        });
    }

    private void notifyListeners(SocketMessage msg) {
        for (Consumer<SocketMessage> l : listeners) {
            try {
                l.accept(msg);
            } catch (Exception e) {
                System.err.println("Listener error: " + e.getMessage());
            }
        }
    }

    public synchronized void disconnect() throws IOException {
        if (!connected) return;
        // send logout if clientId known in listeners? Not tracked here; callers should send explicit logout
        disconnectInternal();
    }

    private synchronized void disconnectInternal() throws IOException {
        connected = false;
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

    public boolean isConnected() {
        return connected;
    }

    public void addListener(Consumer<SocketMessage> listener) {
        listeners.add(listener);
    }

    public void removeListener(Consumer<SocketMessage> listener) {
        listeners.remove(listener);
    }

    public synchronized void login(userWrapper user) throws IOException {
        ensureConnected();
        SocketMessage msg = new SocketMessage();
        msg.setType(SocketMessage.MessageType.LOGIN);
        msg.setUser(user);
        msg.setFromId(user.getId());
        sendMessage(msg);
    }

    public synchronized void logout(String reason) throws IOException {
        ensureConnected();
        SocketMessage msg = new SocketMessage();
        msg.setType(SocketMessage.MessageType.LOGOUT);
        msg.setPayload(reason);
        sendMessage(msg);
        // Do not forcibly close here; allow caller to call disconnect()
    }

    public synchronized void sendChat(String toId, String text) throws IOException {
        ensureConnected();
        SocketMessage msg = new SocketMessage();
        msg.setType(SocketMessage.MessageType.CHAT);
        msg.setToId(toId);
        msg.setPayload(text);
        sendMessage(msg);
    }

    public synchronized void sendMessage(SocketMessage msg) throws IOException {
        ensureConnected();
        out.writeObject(msg);
        out.flush();
    }

    private void ensureConnected() throws IOException {
        if (!connected || socket == null || socket.isClosed()) throw new IOException("Not connected to backend");
    }

    public void shutdown() {
        try {
            disconnectInternal();
        } catch (IOException ignored) {}
        readerExecutor.shutdownNow();
    }
}
