package com.pgrenaud.noterunner.server.entity;

import com.pgrenaud.noterunner.server.network.Response;
import com.pgrenaud.noterunner.server.server.ClientHandler;

public class PlayerEntity {

    private final ClientHandler handler;

    private boolean ready;
    private boolean finish;

    public PlayerEntity(ClientHandler handler) {
        this.handler = handler;

        ready = false;
        finish = false;
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

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public void reset() {
        ready = false;
        finish = false;
    }
}
