package com.pgrenaud.noterunner.server.network.request;

import com.pgrenaud.noterunner.server.entity.ConfigEntity;

public class Payload {

    private ConfigEntity config;

    public ConfigEntity getConfig() {
        return config;
    }

    public void setConfig(ConfigEntity config) {
        this.config = config;
    }
}
