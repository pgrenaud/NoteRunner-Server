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

    private boolean player1Changed;
    private boolean player2Changed;
    private boolean ready1Changed;
    private boolean ready2Changed;
    private boolean configChanged;

    public World() {
        config = new ConfigEntity();

        config.initialize();
    }

    public void tick() {
        // TODO: Ensure that we are in lobby <coding@pgrenaud.com>
        if (player1Changed) {
            if (player1 != null) {
                logger.info("Player1 connected");
                broadcast(ResponseFactory.createPlayerConnectedResponse(1));
            } else {
                logger.info("Player1 disconnected");
                broadcast(ResponseFactory.createPlayerDisconnectedResponse(1));
            }

            player1Changed = false;
        }

        if (player2Changed) {
            if (player2 != null) {
                logger.info("Player2 connected");
                broadcast(ResponseFactory.createPlayerConnectedResponse(2));
            } else {
                logger.info("Player2 disconnected");
                broadcast(ResponseFactory.createPlayerDisconnectedResponse(2));
            }

            player2Changed = false;
        }

        if (ready1Changed) {
            if (player1.isReady()) {
                logger.info("Player1 is ready");
                broadcast(ResponseFactory.createPlayerReadyResponse(1));
            } else {
                logger.info("Player1 is not ready");
                broadcast(ResponseFactory.createPlayerNotReadyResponse(1));
            }

            ready1Changed = false;
        }

        if (ready2Changed) {
            if (player2.isReady()) {
                logger.info("Player2 is ready");
                broadcast(ResponseFactory.createPlayerReadyResponse(2));
            } else {
                logger.info("Player2 is not ready");
                broadcast(ResponseFactory.createPlayerNotReadyResponse(2));
            }

            ready2Changed = false;
        }

        if (configChanged) {
            logger.info("Config updated");
            broadcast(ResponseFactory.createConfigResponse(config));

            configChanged = false;
        }
    }

    public void addPlayer(PlayerEntity player) {
        if (player1 == null) {
            player1 = player;
            player1.getHandler().setListener(this);
            player1.send(ResponseFactory.createRegisteredResponse(1));
            player1.send(ResponseFactory.createConfigResponse(config));

            // Very hacky ... but we need to synchronize states
            if (player2 != null) {
                player1.send(ResponseFactory.createPlayerConnectedResponse(2));

                if (player2.isReady()) {
                    player1.send(ResponseFactory.createPlayerReadyResponse(2));
                }
            }

            player1Changed = true;
        } else if (player2 == null) {
            player2 = player;
            player2.getHandler().setListener(this);
            player2.send(ResponseFactory.createRegisteredResponse(2));
            player2.send(ResponseFactory.createConfigResponse(config));

            // Very hacky ... but we need to synchronize states
            if (player1 != null) {
                player2.send(ResponseFactory.createPlayerConnectedResponse(1));

                if (player1.isReady()) {
                    player2.send(ResponseFactory.createPlayerReadyResponse(1));
                }
            }

            player2Changed = true;
        } else {
            player.kick("Server is full");
        }
    }

    public void removePlayer(PlayerEntity player) {
        if (player1 == player) {
            player1.send(ResponseFactory.createUnregisteredResponse());
            player1.getHandler().setListener(null);
            player1 = null;

            player1Changed = true;
        } else if (player2 == player) {
            player2.send(ResponseFactory.createUnregisteredResponse());
            player2.getHandler().setListener(null);
            player2 = null;

            player2Changed = true;
        }
    }

    public void readyPlayer(PlayerEntity player, Boolean ready) {
        if (ready == null) {
            player.send(ResponseFactory.createInvlidConfigResponse("Ready is missing")); // Too lazy to make a dedicated response for this
            return;
        }

        if (player1 == player) {
            player1.setReady(ready);

            ready1Changed = true;
        } else if (player2 == player) {
            player2.setReady(ready);

            ready2Changed = true;
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

        configChanged = true;
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
