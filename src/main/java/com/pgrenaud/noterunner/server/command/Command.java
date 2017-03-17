package com.pgrenaud.noterunner.server.command;

public interface Command {
    public String getName();
    public int getNumberOfArgs();

    public boolean isArgsValid(String[] args);
    public void execute(String[] args);
}
