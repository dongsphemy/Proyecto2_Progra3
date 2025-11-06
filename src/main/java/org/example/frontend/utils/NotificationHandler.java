package org.example.frontend.utils;

import org.example.common.wrappers.SocketMessage;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class NotificationHandler {

    // map userId -> displayName (keeps insertion order)
    private final Map<String, String> activeUsers = Collections.synchronizedMap(new LinkedHashMap<>());

    // external listeners that want to be notified when a SocketMessage is processed
    private final List<Consumer<SocketMessage>> listeners = new CopyOnWriteArrayList<>();

    public NotificationHandler() {}

    /**
     * Process an incoming SocketMessage from the BackendProxy.
     * Updates internal active-users state and notifies registered listeners.
     */
    public void process(SocketMessage msg) {
        if (msg == null) return;

        switch (msg.getType()) {
            case LOGIN -> handleLogin(msg);
            case LOGOUT -> handleLogout(msg);
            case ACTIVE_USERS -> handleActiveUsers(msg);
            case CHAT -> handleChat(msg);
            case PING, SYSTEM -> {
                // no-op for state, but listeners may still care
            }
            default -> {
                // unknown types ignored
            }
        }

        // Notify listeners after updating internal state
        for (Consumer<SocketMessage> l : listeners) {
            try {
                l.accept(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleLogin(SocketMessage msg) {
        if (msg.getUser() != null && msg.getUser().getId() != null) {
            activeUsers.put(msg.getUser().getId(), msg.getUser().getName() == null ? "" : msg.getUser().getName());
        } else if (msg.getFromId() != null) {
            activeUsers.put(msg.getFromId(), msg.getPayload() == null ? "" : msg.getPayload());
        }
    }

    private void handleLogout(SocketMessage msg) {
        if (msg.getFromId() != null) activeUsers.remove(msg.getFromId());
    }

    private void handleActiveUsers(SocketMessage msg) {
        parseActiveUsersPayload(msg.getPayload());
    }

    private void handleChat(SocketMessage msg) {
        // by default do not change active users on chat, but optionally show popup
        String fromId = msg.getFromId();
        String name = null;
        if (fromId != null) {
            synchronized (activeUsers) {
                name = activeUsers.get(fromId);
            }
        }
        // show a lightweight popup for incoming chat messages (non-blocking)
        if (msg.getToId() != null && msg.getPayload() != null) {
            // it's either a private message or broadcast; show popup only when it is directed to this client
            // The caller/listener should decide whether to call showChatPopup; we don't assume the current client id here.
        }
    }

    private void parseActiveUsersPayload(String payload) {
        activeUsers.clear();
        if (payload == null || payload.isEmpty()) return;
        String[] pairs = payload.split(",");
        for (String p : pairs) {
            String[] kv = p.split(":", 2);
            if (kv.length >= 1) {
                String id = kv[0];
                String name = kv.length == 2 ? kv[1] : "";
                activeUsers.put(id, name);
            }
        }
    }

    // Public API for UI code
    public Map<String, String> getActiveUsers() {
        synchronized (activeUsers) {
            return new LinkedHashMap<>(activeUsers);
        }
    }

    public List<String> getActiveUserIds() {
        synchronized (activeUsers) {
            return new ArrayList<>(activeUsers.keySet());
        }
    }

    public void addListener(Consumer<SocketMessage> listener) { listeners.add(listener); }
    public void removeListener(Consumer<SocketMessage> listener) { listeners.remove(listener); }

    /**
     * Small helper to show a Swing popup for an incoming chat message. Runs on the EDT.
     */
    public void showChatPopup(String fromName, String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, "Mensaje de " + (fromName == null ? "desconocido" : fromName), JOptionPane.INFORMATION_MESSAGE));
    }
}
