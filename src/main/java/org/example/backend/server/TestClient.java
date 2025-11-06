package org.example.backend.server;

import org.example.common.wrappers.SocketMessage;
import org.example.common.wrappers.userWrapper;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TestClient {
    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 55555;

        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // send login
            userWrapper user = new userWrapper("testuser", "Test User", "Paciente");
            SocketMessage login = new SocketMessage();
            login.setType(SocketMessage.MessageType.LOGIN);
            login.setUser(user);
            login.setFromId(user.getId());
            out.writeObject(login);
            out.flush();

            // listen for messages for a short while
            long end = System.currentTimeMillis() + 2000;
            while (System.currentTimeMillis() < end) {
                if (in.available() > 0) {
                    Object obj = in.readObject();
                    System.out.println("Received: " + obj);
                } else {
                    try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                }
            }

            // send logout
            SocketMessage logout = new SocketMessage();
            logout.setType(SocketMessage.MessageType.LOGOUT);
            logout.setFromId(user.getId());
            out.writeObject(logout);
            out.flush();

            // give a bit time to receive logout broadcast
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }

        System.out.println("TestClient finished");
    }
}

