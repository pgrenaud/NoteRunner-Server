package com.pgrenaud.noterunner.server.command;

public class StopCommand extends BaseCommand {
    public StopCommand() {
        super("stop");
    }

    @Override
    public void execute(String[] args) {
        System.exit(0); // Trigger shutdown sequence
    }
}
