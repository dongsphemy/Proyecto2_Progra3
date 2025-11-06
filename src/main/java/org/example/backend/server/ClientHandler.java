package org.example.backend.server;

import org.example.common.wrappers.SocketMessage;
import org.example.common.wrappers.userWrapper;
import org.example.backend.dao.*;
import org.example.backend.service.PrescripcionService;
import org.example.common.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final BackendServer server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private volatile boolean running = false;
    private String clientId;
    private String clientName;

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
            // RPC endpoints
            case LOAD_PACIENTES -> rpcLoadPacientes(msg);
            case LOAD_MEDICAMENTOS -> rpcLoadMedicamentos(msg);
            case LISTAR_RECETAS -> rpcListarRecetas(msg);
            case OBTENER_RECETA -> rpcObtenerReceta(msg);
            case INICIAR_GUARDAR_RECETA -> rpcIniciarGuardarReceta(msg);
            case AGREGAR_DETALLE -> rpcAgregarDetalle(msg);
            case ELIMINAR_DETALLE -> rpcEliminarDetalle(msg);
            case REGISTRAR_RECETA -> rpcRegistrarReceta(msg);
            default -> System.out.println("Unhandled message type: " + msg.getType());
        }
    }

    private void rpcLoadPacientes(SocketMessage req) {
        try {
            PacienteDao pacienteDao = new PacienteDao();
            var list = pacienteDao.getAllPacientes();
            replyOk(req, list);
        } catch (Exception e) {
            replyErr(req, e);
        }
    }

    private void rpcLoadMedicamentos(SocketMessage req) {
        try {
            MedicamentoDao medicamentoDao = new MedicamentoDao();
            var list = medicamentoDao.loadMedicamentos().getMedicamentos();
            replyOk(req, list);
        } catch (Exception e) {
            replyErr(req, e);
        }
    }

    private void rpcListarRecetas(SocketMessage req) {
        try {
            RecetaDao recetaDao = new RecetaDao();
            List<Receta> list = recetaDao.findAll();
            replyOk(req, list);
        } catch (Exception e) {
            replyErr(req, e);
        }
    }

    private void rpcObtenerReceta(SocketMessage req) {
        try {
            RecetaDao recetaDao = new RecetaDao();
            Receta r = recetaDao.findById(req.getPayload()).orElse(null);
            if (r == null) throw new IllegalArgumentException("Receta no encontrada");
            replyOk(req, r);
        } catch (Exception e) {
            replyErr(req, e);
        }
    }

    private void rpcIniciarGuardarReceta(SocketMessage req) {
        try {
            String[] parts = req.getPayload().split("\\|");
            String pacienteUsername = parts[0];
            String medicoUsername = parts.length > 1 ? parts[1] : null;

            PacienteDao pacienteDao = new PacienteDao();
            MedicoDao medicoDao = new MedicoDao();
            RecetaDao recetaDao = new RecetaDao();
            MedicamentoDao medicamentoDao = new MedicamentoDao();
            PrescripcionService service = new PrescripcionService(pacienteDao, medicoDao, recetaDao, medicamentoDao);

            Receta temp = service.iniciarReceta(pacienteUsername);
            Receta guardada = service.guardarReceta(temp, medicoUsername);
            replyOk(req, guardada);
        } catch (Exception e) {
            replyErr(req, e);
        }
    }

    private void rpcAgregarDetalle(SocketMessage req) {
        try {
            String idReceta = req.getPayload();
            DetalleMedicamento det = (DetalleMedicamento) req.getBody();

            RecetaDao recetaDao = new RecetaDao();
            // persist detail in DB
            new DetalleMedicamentoDao().addDetalleByLogicalId(idReceta, det);
            // reload receta with details
            Receta receta = recetaDao.findById(idReceta).orElseThrow(() -> new IllegalArgumentException("Receta no encontrada"));
            replyOk(req, receta);
        } catch (Exception e) {
            replyErr(req, e);
        }
    }

    private void rpcEliminarDetalle(SocketMessage req) {
        try {
            String[] parts = req.getPayload().split("\\|");
            String idReceta = parts[0];
            String code = parts[1];
            RecetaDao recetaDao = new RecetaDao();
            new DetalleMedicamentoDao().removeDetalleByLogicalIdAndCode(idReceta, code);
            Receta receta = recetaDao.findById(idReceta).orElseThrow(() -> new IllegalArgumentException("Receta no encontrada"));
            replyOk(req, receta);
        } catch (Exception e) {
            replyErr(req, e);
        }
    }

    private void rpcRegistrarReceta(SocketMessage req) {
        try {
            String idReceta = req.getPayload();
            LocalDate fechaRetiro = (LocalDate) req.getBody();
            RecetaDao recetaDao = new RecetaDao();
            Receta receta = recetaDao.findById(idReceta).orElseThrow(() -> new IllegalArgumentException("Receta no encontrada"));
            if (receta.getMedicamentos() == null || receta.getMedicamentos().isEmpty()) {
                throw new IllegalStateException("No se puede registrar una receta sin medicamentos");
            }
            receta.registrar(fechaRetiro);
            recetaDao.update(receta);
            replyOk(req, receta);
        } catch (Exception e) {
            replyErr(req, e);
        }
    }

    private void replyOk(SocketMessage req, Object body) {
        SocketMessage resp = new SocketMessage();
        resp.setType(SocketMessage.MessageType.RESPONSE);
        resp.setCorrelationId(req.getCorrelationId());
        resp.setSuccess(true);
        resp.setBody(body);
        try { sendMessage(resp); } catch (IOException e) { e.printStackTrace(); }
    }

    private void replyErr(SocketMessage req, Exception ex) {
        SocketMessage resp = new SocketMessage();
        resp.setType(SocketMessage.MessageType.RESPONSE);
        resp.setCorrelationId(req.getCorrelationId());
        resp.setSuccess(false);
        resp.setError(ex.getMessage());
        try { sendMessage(resp); } catch (IOException e) { e.printStackTrace(); }
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
