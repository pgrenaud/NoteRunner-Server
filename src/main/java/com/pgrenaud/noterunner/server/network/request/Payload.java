package com.pgrenaud.noterunner.server.network.request;

import com.pgrenaud.noterunner.server.entity.ConfigEntity;

public class Payload {

    private ConfigEntity config;
    private Boolean ready;

    public ConfigEntity getConfig() {
        return config;
    }

    public Payload setConfig(ConfigEntity config) {
        this.config = config;

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
