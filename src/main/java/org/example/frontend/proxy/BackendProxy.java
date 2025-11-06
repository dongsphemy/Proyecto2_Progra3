package org.example.frontend.proxy;

import org.example.common.wrappers.SocketMessage;
import org.example.common.wrappers.userWrapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.CopyOnWriteArrayList;
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

    // Pending RPC requests
    private final Map<String, CompletableFuture<SocketMessage>> pending = new ConcurrentHashMap<>();

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
                        // First try to match pending RPC
                        String corr = msg.getCorrelationId();
                        if (msg.getType() == SocketMessage.MessageType.RESPONSE && corr != null) {
                            CompletableFuture<SocketMessage> fut = pending.remove(corr);
                            if (fut != null) {
                                fut.complete(msg);
                                continue;
                            }
                        }
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
        disconnectInternal();
    }

    private synchronized void disconnectInternal() throws IOException {
        connected = false;
        try { if (in != null) in.close(); } catch (IOException ignored) {}
        try { if (out != null) out.close(); } catch (IOException ignored) {}
        try { if (socket != null && !socket.isClosed()) socket.close(); } catch (IOException ignored) {}
        // Fail any pending requests
        for (var entry : pending.entrySet()) {
            entry.getValue().completeExceptionally(new IOException("Disconnected"));
        }
        pending.clear();
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

    public SocketMessage request(SocketMessage msg, long timeoutMillis) throws IOException, TimeoutException, ExecutionException, InterruptedException {
        ensureConnected();
        String corr = msg.getCorrelationId();
        if (corr == null || corr.isEmpty()) {
            corr = UUID.randomUUID().toString();
            msg.setCorrelationId(corr);
        }
        CompletableFuture<SocketMessage> fut = new CompletableFuture<>();
        pending.put(corr, fut);
        sendMessage(msg);
        try {
            return fut.get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (TimeoutException te) {
            pending.remove(corr);
            throw te;
        }
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
