package com.pgrenaud.noterunner.server.entity;

import com.pgrenaud.noterunner.server.network.Response;
import com.pgrenaud.noterunner.server.server.ClientHandler;

public class PlayerEntity {

    private final ClientHandler handler;

    public PlayerEntity(ClientHandler handler) {
        this.handler = handler;
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
}
