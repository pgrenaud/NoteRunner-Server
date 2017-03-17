package com.pgrenaud.noterunner.server.packet.response;

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
