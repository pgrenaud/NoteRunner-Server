package com.pgrenaud.noterunner.server.command;

public abstract class BaseCommand implements Command {

    private final String name;
    private final int numberOfArgs;

    public BaseCommand(String name) {
        this(name, 0);
    }

    public BaseCommand(String name, int numberOfArgs) {
        this.name = name;
        this.numberOfArgs = numberOfArgs;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumberOfArgs() {
        return numberOfArgs;
    }

    @Override
    public boolean isArgsValid(String[] args) {
        return args.length == numberOfArgs;
    }
}
