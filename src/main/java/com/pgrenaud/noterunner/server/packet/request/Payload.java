package com.pgrenaud.noterunner.server.packet.request;

public class Payload {

    private String name;

    public String getName() {
        return name;
    }

    public Payload setName(String name) {
        this.name = name;

        return this;
    }
}
