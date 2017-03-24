package com.pgrenaud.noterunner.server.network.response;

import com.pgrenaud.noterunner.server.entity.ConfigEntity;

public class Payload {

    private ConfigEntity config;
    private String message;
    private Integer player;
    private Boolean ready;

    public ConfigEntity getConfig() {
        return config;
    }

    public Payload setConfig(ConfigEntity config) {
        this.config = config;

        return this;
    }

    public String getMessage() {
        return message;
    }

    public Payload setMessage(String message) {
        this.message = message;

        return this;
    }

    public Integer getPlayer() {
        return player;
    }

    public Payload setPlayer(Integer player) {
        this.player = player;

        return this;
    }

    public Boolean isReady() {
        return ready;
    }

    public Payload setReady(Boolean ready) {
        this.ready = ready;

        return this;
    }
}
