package com.pgrenaud.noterunner.server.network.response;

import com.google.gson.annotations.SerializedName;
import com.pgrenaud.noterunner.server.entity.ConfigEntity;
import com.pgrenaud.noterunner.server.entity.NoteEntity;

import java.util.List;

public class Payload {

    private ConfigEntity config;
    private Integer health;
    private String message;
    @SerializedName("note_sequence")
    private List<NoteEntity> notes;
    private Integer player;
    private Boolean ready;

    public ConfigEntity getConfig() {
        return config;
    }

    public Payload setConfig(ConfigEntity config) {
        this.config = config;

        return this;
    }

    public Integer getHealth() {
        return health;
    }

    public Payload setHealth(Integer health) {
        this.health = health;

        return this;
    }

    public String getMessage() {
        return message;
    }

    public Payload setMessage(String message) {
        this.message = message;

        return this;
    }

    public List<NoteEntity> getNotes() {
        return notes;
    }

    public Payload setNotes(List<NoteEntity> notes) {
        this.notes = notes;

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
