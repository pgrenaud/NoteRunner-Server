package com.pgrenaud.noterunner.server.factory;

import com.pgrenaud.noterunner.server.server.NoteRunnerServer;

public interface NoteRunnerServerFactory {
    NoteRunnerServer create(int listenPort);
}
