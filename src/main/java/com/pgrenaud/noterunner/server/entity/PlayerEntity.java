package com.pgrenaud.noterunner.server.entity;

import com.pgrenaud.noterunner.server.network.Response;
import com.pgrenaud.noterunner.server.server.ClientHandler;

public class PlayerEntity {

    private final ClientHandler handler;

    private boolean ready;

    public PlayerEntity(ClientHandler handler) {
        this.handler = handler;

        ready = false;
    }

    public void send(Response response) {
        handler.sendResponse(response);
    }

    public void kick(String message) {
        handler.kick(message);
    }

    public ClientHandler getHandler() {
        return handler;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
