package com.pgrenaud.noterunner.server.server;

public interface NoteRunnerServerFactory {
    NoteRunnerServer create(int listenPort);
}
