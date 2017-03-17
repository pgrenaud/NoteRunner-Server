package com.pgrenaud.noterunner.server.command;

import com.google.inject.Inject;
import com.pgrenaud.noterunner.server.repository.CommandRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelpCommand extends BaseCommand {
    private static final Logger logger = LogManager.getLogger();

    private final CommandRepository commands;

    @Inject
    public HelpCommand(CommandRepository commands) {
        super("help");

        this.commands = commands;
    }

    @Override
    public void execute(String[] args) {
        logger.info("Available commands: {}", commands);
    }
}
