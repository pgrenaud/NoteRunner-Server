package com.pgrenaud.noterunner.server.entity;

import com.google.gson.Gson;

public class ClientEntity {

    private final int id;
    private final String name;

    public ClientEntity(int id) {
        this.id = id;

        this.name = String.format("Client%s", id);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }
}
