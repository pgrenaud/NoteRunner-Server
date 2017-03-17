package com.pgrenaud.noterunner.server.repository;

import com.google.inject.Singleton;
import com.pgrenaud.noterunner.server.entity.ClientEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class ClientRepository {
    private static final Logger logger = LogManager.getLogger();

    private final Map<Integer, ClientEntity> clients;
    private final AtomicInteger nextId;

    public ClientRepository() {
        clients = new ConcurrentHashMap<>();
        nextId = new AtomicInteger();

        nextId.set(1);
    }

    public ClientEntity create() {
        ClientEntity client = new ClientEntity(nextId.getAndIncrement());

        add(client);

        return client;
    }

    public void add(ClientEntity client) {
        // FIXME: Throw exception if key already exists

        clients.put(client.getId(), client);
        logger.info("Added {}", client.getName());
    }

    public void remove(ClientEntity client) {
        clients.remove(client.getId());
        logger.info("Removed {}", client.getName());
    }

    public ClientEntity get(int id) {
        return clients.get(id);
    }

    public Collection<ClientEntity> getAll() {
        return clients.values();
    }
}
