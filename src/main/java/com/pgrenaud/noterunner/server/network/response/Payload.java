package com.pgrenaud.noterunner.server.network.response;

import com.pgrenaud.noterunner.server.entity.ConfigEntity;

public class Payload {

    private ConfigEntity config;
    private String message;

    public ConfigEntity getConfig() {
        return config;
    }

    public void setConfig(ConfigEntity config) {
        this.config = config;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
