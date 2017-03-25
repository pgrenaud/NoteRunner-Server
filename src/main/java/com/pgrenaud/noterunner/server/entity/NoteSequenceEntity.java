package com.pgrenaud.noterunner.server.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteSequenceEntity {

    private final List<NoteEntity> notes;

    public NoteSequenceEntity(Random random, ConfigEntity config) {

        notes = new ArrayList<>();

        generate(random, config);
    }

    private void generate(Random random, ConfigEntity config) {
        List<NoteEntity> catalogue = new ArrayList<>(config.getNotesEnabled());

        for (int i = 0; i < config.getSequenceLength(); i++) {
            int index = random.nextInt(catalogue.size());

            notes.add(catalogue.get(index));
        }

    }

    public List<NoteEntity> getNotes() {
        return notes;
    }
}
