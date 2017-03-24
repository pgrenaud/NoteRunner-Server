package com.pgrenaud.noterunner.server.game;

import com.google.inject.Singleton;
import com.pgrenaud.noterunner.server.entity.ConfigEntity;
import com.pgrenaud.noterunner.server.entity.NoteEntity;
import com.pgrenaud.noterunner.server.entity.PlayerEntity;
import com.pgrenaud.noterunner.server.network.Response;
import com.pgrenaud.noterunner.server.network.ResponseFactory;
import com.pgrenaud.noterunner.server.server.ClientHandlerListener;
import com.pgrenaud.noterunner.server.server.ClientHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

@Singleton
public class World implements ClientHandlerListener {
    private static final Logger logger = LogManager.getLogger();

    private PlayerEntity player1;
    private PlayerEntity player2;

    private ConfigEntity config;

    public World() {
        config = new ConfigEntity();

        config.initialize();
    }

    public void tick() {
        // TODO: Broadcast when a player connect <coding@pgrenaud.com>
        // TODO: Broadcast when a player disconnect <coding@pgrenaud.com>
        // TODO: Broadcast when a player is ready <coding@pgrenaud.com>
        // TODO: Broadcast when a player is not ready <coding@pgrenaud.com>
        // TODO: Broadcast when config is updated <coding@pgrenaud.com>
    }

    public void addPlayer(PlayerEntity player) {
        if (player1 == null) {
            player1 = player;
            player1.getHandler().setListener(this);
            player1.send(ResponseFactory.createRegisteredResponse());
            player1.send(ResponseFactory.createConfigResponse(config));

            logger.info("Player1 connected");
        } else if (player2 == null) {
            player2 = player;
            player2.getHandler().setListener(this);
            player2.send(ResponseFactory.createRegisteredResponse());
            player2.send(ResponseFactory.createConfigResponse(config));

            logger.info("Player2 connected");
        } else {
            player.kick("Server is full");
        }
    }

    public void removePlayer(PlayerEntity player) {
        if (player1 == player) {
            player1.send(ResponseFactory.createUnregisteredResponse());
            player1.getHandler().setListener(null);
            player1 = null;

            logger.info("Player1 disconnected");
        } else if (player2 == player) {
            player2.send(ResponseFactory.createUnregisteredResponse());
            player2.getHandler().setListener(null);
            player2 = null;

            logger.info("Player2 disconnected");
        }
    }

    public void updateConfig(PlayerEntity player, ConfigEntity newConfig) {
        // TODO: Ensure that we are in lobby <coding@pgrenaud.com>

        // TODO: Move all this logic inside ConfigEntity <coding@pgrenaud.com>
        if (newConfig == null) {
            player.send(ResponseFactory.createInvlidConfigResponse("Config is missing"));
            return;
        }

        int sequenceLength = newConfig.getSequenceLength();
        int playerHealth = newConfig.getPlayerHealth();
        Set<NoteEntity> notesEnabled = newConfig.getNotesEnabled();

        if (sequenceLength < 3 || sequenceLength > 15) {
            player.send(ResponseFactory.createInvlidConfigResponse("Invalid sequence length (must be between 3 and 15)"));
            return;
        }

        if (playerHealth < 1 || playerHealth > 20) {
            player.send(ResponseFactory.createInvlidConfigResponse("Invalid player health (must be between 1 and 20)"));
            return;
        }

        if (notesEnabled.remove(null)) {
            player.send(ResponseFactory.createInvlidConfigResponse("One or more notes are invalid"));
            return;
        }

        if (notesEnabled.size() < 3) {
            player.send(ResponseFactory.createInvlidConfigResponse("At least 3 notes need to be enabled"));
            return;
        }

        config.setPlayerHealth(sequenceLength);
        config.setSequenceLength(playerHealth);
        config.getNotesEnabled().clear();
        config.getNotesEnabled().addAll(notesEnabled);

        broadcast(ResponseFactory.createConfigResponse(newConfig)); // FIXME: Should be in tick() <coding@pgrenaud.com>
    }

    public boolean isFull() {
        return player1 != null && player2 != null;
    }

    public PlayerEntity getPlayer1() {
        return player1;
    }

    public PlayerEntity getPlayer2() {
        return player2;
    }

    public PlayerEntity getPlayer(ClientHandler handler) {
        if (player1 != null && player1.getHandler() == handler) {
            return player1;
        } else if (player2 != null && player2.getHandler() == handler) {
            return player2;
        } else {
            throw new UnauthorizedException();
        }
    }

    public void broadcast(Response response) {
        if (player1 != null) {
            player1.send(response);
        }
        if (player2 != null) {
            player2.send(response);
        }
    }

    @Override
    public void onClientHandlerStop(ClientHandler handler) {
        removePlayer(getPlayer(handler));
    }
}
