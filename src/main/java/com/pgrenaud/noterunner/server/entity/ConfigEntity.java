package com.pgrenaud.noterunner.server.entity;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashSet;
import java.util.Set;

public class ConfigEntity {

    @SerializedName("sequence_length")
    private int sequenceLength;
    @SerializedName("player_health")
    private int playerHealth;
    @SerializedName("notes_enabled")
    private final Set<NoteEntity> notesEnabled;

    public ConfigEntity() {
        notesEnabled = new LinkedHashSet<>();
    }

    public void initialize() {
        sequenceLength = 3;
        playerHealth = 10;
        notesEnabled.add(NoteEntity.A);
        notesEnabled.add(NoteEntity.B);
        notesEnabled.add(NoteEntity.C);
        notesEnabled.add(NoteEntity.D);
        notesEnabled.add(NoteEntity.E);
        notesEnabled.add(NoteEntity.F);
        notesEnabled.add(NoteEntity.G);
    }

    public int getSequenceLength() {
        return sequenceLength;
    }

    public void setSequenceLength(int sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public Set<NoteEntity> getNotesEnabled() {
        return notesEnabled;
    }
}
