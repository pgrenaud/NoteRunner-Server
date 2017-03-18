package com.pgrenaud.noterunner.server.command;

public interface Command {
    String getName();
    int getNumberOfArgs();

    boolean isArgsValid(String[] args);
    void execute(String[] args);
}
