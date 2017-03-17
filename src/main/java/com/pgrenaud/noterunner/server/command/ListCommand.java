package com.pgrenaud.noterunner.server.command;

import com.google.inject.Inject;
import com.pgrenaud.noterunner.server.entity.ClientEntity;
import com.pgrenaud.noterunner.server.repository.ClientRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

public class ListCommand extends BaseCommand {
    private static final Logger logger = LogManager.getLogger();

    private final ClientRepository repository;

    @Inject
    public ListCommand(ClientRepository repository) {
        super("list");

        this.repository = repository;
    }

    @Override
    public void execute(String[] args) {
        Collection<ClientEntity> clients = repository.getAll();

        logger.info("Currently {} registered clients", clients.size());

        for (ClientEntity client : clients) {
            logger.info("{}", client);
        }
    }
}
