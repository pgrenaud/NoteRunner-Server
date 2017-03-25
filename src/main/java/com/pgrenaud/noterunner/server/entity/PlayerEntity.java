package com.pgrenaud.noterunner.server.entity;

import com.pgrenaud.noterunner.server.network.Response;
import com.pgrenaud.noterunner.server.server.ClientHandler;

public class PlayerEntity {

    private final ClientHandler handler;

    private boolean ready;
    private boolean finish;
    private int health;

    public PlayerEntity(ClientHandler handler) {
        this.handler = handler;

        ready = false;
        finish = false;
        health = 0;
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void reset() {
        ready = false;
        finish = false;
    }
}
