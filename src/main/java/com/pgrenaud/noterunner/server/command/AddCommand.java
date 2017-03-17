package com.pgrenaud.noterunner.server.command;

import com.google.inject.Inject;
import com.pgrenaud.noterunner.server.repository.ClientRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddCommand extends BaseCommand {
    private static final Logger logger = LogManager.getLogger();

    private final ClientRepository repository;

    @Inject
    public AddCommand(ClientRepository repository) {
        super("add");

        this.repository = repository;
    }

    @Override
    public void execute(String[] args) {
        repository.create();
        logger.info("Added client to repository");
    }
}
