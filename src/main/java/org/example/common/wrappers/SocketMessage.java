package org.example.common.wrappers;

import java.io.Serializable;
import java.time.Instant;

public class SocketMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum MessageType {
        LOGIN, LOGOUT, CHAT, SYSTEM, PING, ACTIVE_USERS,
        // RPC support
        RESPONSE,
        LOAD_PACIENTES, LOAD_MEDICAMENTOS, LISTAR_RECETAS, OBTENER_RECETA,
        INICIAR_GUARDAR_RECETA, AGREGAR_DETALLE, ELIMINAR_DETALLE, REGISTRAR_RECETA
    }

    private MessageType type;
    private String fromId;
    private String toId; // optional for private messages
    private String payload; // text message or other simple payload
    private userWrapper user; // used for LOGIN to send user info
    private Instant timestamp;

    // RPC correlation
    private String correlationId; // request/response correlation
    private boolean success = true; // default true; set false on error
    private String error; // error message when success=false
    private Object body; // generic payload for RPC (must be Serializable)

    public SocketMessage() {
        this.timestamp = Instant.now();
    }

    public SocketMessage(MessageType type, String fromId, String toId, String payload) {
        this();
        this.type = type;
        this.fromId = fromId;
        this.toId = toId;
        this.payload = payload;
    }

    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }

    public String getFromId() { return fromId; }
    public void setFromId(String fromId) { this.fromId = fromId; }

    public String getToId() { return toId; }
    public void setToId(String toId) { this.toId = toId; }

    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

    public userWrapper getUser() { return user; }
    public void setUser(userWrapper user) { this.user = user; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public Object getBody() { return body; }
    public void setBody(Object body) { this.body = body; }

    @Override
    public String toString() {
        return "SocketMessage{" +
                "type=" + type +
                ", fromId='" + fromId + '\'' +
                ", toId='" + toId + '\'' +
                ", payload='" + payload + '\'' +
                ", user=" + user +
                ", timestamp=" + timestamp +
                ", correlationId='" + correlationId + '\'' +
                ", success=" + success +
                ", error='" + error + '\'' +
                ", body=" + (body != null ? body.getClass().getSimpleName() : "null") +
                '}';
    }
}
