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
    private GameState state;

    private boolean player1Changed;
    private boolean player2Changed;
    private boolean ready1Changed;
    private boolean ready2Changed;
    private boolean configChanged;
    private boolean roundFinish;

    public World() {
        config = new ConfigEntity();

        config.initialize();

        state = GameState.LOBBY;

        player1Changed = false;
        player2Changed = false;
        ready1Changed = false;
        ready2Changed = false;
        configChanged = false;
        roundFinish = false;
    }

    public void tick() {
        if (player1Changed) {
            if (player1 != null) {
                logger.info("Player1 connected");
                broadcast(ResponseFactory.createPlayerConnectedResponse(1));
            } else {
                logger.info("Player1 disconnected");
                broadcast(ResponseFactory.createPlayerDisconnectedResponse(1));
            }
        }

        if (player2Changed) {
            if (player2 != null) {
                logger.info("Player2 connected");
                broadcast(ResponseFactory.createPlayerConnectedResponse(2));
            } else {
                logger.info("Player2 disconnected");
                broadcast(ResponseFactory.createPlayerDisconnectedResponse(2));
            }
        }

        if (ready1Changed) {
            if (player1.isReady()) {
                logger.info("Player1 is ready");
                broadcast(ResponseFactory.createPlayerReadyResponse(1));
            } else {
                logger.info("Player1 is not ready");
                broadcast(ResponseFactory.createPlayerNotReadyResponse(1));
            }
        }

        if (ready2Changed) {
            if (player2.isReady()) {
                logger.info("Player2 is ready");
                broadcast(ResponseFactory.createPlayerReadyResponse(2));
            } else {
                logger.info("Player2 is not ready");
                broadcast(ResponseFactory.createPlayerNotReadyResponse(2));
            }
        }

        switch (state) {
            case LOBBY:
                if (configChanged) {
                    logger.info("Config updated");
                    broadcast(ResponseFactory.createConfigResponse(config));
                }

                if (ready1Changed || ready2Changed) {
                    // FIXME: Should apply a 3 second cooldown before checking <coding@pgrenaud.com>
                    if (player1.isReady() && player2.isReady()) {
                        // TODO: Generate notes sequence <coding@pgrenaud.com>
                        broadcast(ResponseFactory.createRoundPreparedResponse());
                        // TODO: Send config <coding@pgrenaud.com>
                        // TODO: Send notes_sequence <coding@pgrenaud.com>

                        player1.reset();
                        player2.reset();

                        logger.info("Round prepared");
                        state = GameState.ROUND_LOADING;
                    }
                }
                break;
            case ROUND_LOADING:
                if (player1Changed || player2Changed) {
                    resetState(); // One player disconnected, abort
                    break;
                }

                if (ready1Changed || ready2Changed) {
                    if (player1.isReady() && player2.isReady()) {
                        broadcast(ResponseFactory.createRoundBeganResponse());

                        player1.reset();
                        player2.reset();

                        logger.info("Round began");
                        state = GameState.ROUND_RUNNING;
                    }
                }
                break;
            case ROUND_RUNNING:
                if (player1Changed || player2Changed) {
                    resetState(); // One player disconnected, abort
                    break;
                }

                if (roundFinish) {
                    // TODO: Check for winner/looser/gameover <coding@pgrenaud.com>

                    // TODO: Generate notes sequence <coding@pgrenaud.com>
                    broadcast(ResponseFactory.createRoundPreparedResponse());
                    // TODO: Send config <coding@pgrenaud.com>
                    // TODO: Send notes_sequence <coding@pgrenaud.com>

                    player1.reset();
                    player2.reset();

                    logger.info("Round ended");
                    state = GameState.ROUND_LOADING;
                }
                break;
            default:
                logger.error("That should not happened");
                state = GameState.LOBBY;
        }

        player1Changed = false;
        player2Changed = false;
        ready1Changed = false;
        ready2Changed = false;
        configChanged = false;
        roundFinish = false;
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
        if (state != GameState.LOBBY && state != GameState.ROUND_LOADING) {
            throw new BadStateException();
        }

        if (ready == null) {
            player.send(ResponseFactory.createInvalidRequestResponse("Ready is missing")); // Too lazy to make a dedicated response for this
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
        if (state != GameState.LOBBY) {
            throw new BadStateException();
        }

        // TODO: Move all this logic inside ConfigEntity <coding@pgrenaud.com>
        if (newConfig == null) {
            player.send(ResponseFactory.createInvalidRequestResponse("Config is missing"));
            return;
        }

        int sequenceLength = newConfig.getSequenceLength();
        int playerHealth = newConfig.getPlayerHealth();
        Set<NoteEntity> notesEnabled = newConfig.getNotesEnabled();

        if (sequenceLength < 3 || sequenceLength > 15) {
            player.send(ResponseFactory.createInvalidRequestResponse("Invalid sequence length (must be between 3 and 15)"));
            return;
        }

        if (playerHealth < 1 || playerHealth > 20) {
            player.send(ResponseFactory.createInvalidRequestResponse("Invalid player health (must be between 1 and 20)"));
            return;
        }

        if (notesEnabled.remove(null)) {
            player.send(ResponseFactory.createInvalidRequestResponse("One or more notes are invalid"));
            return;
        }

        if (notesEnabled.size() < 3) {
            player.send(ResponseFactory.createInvalidRequestResponse("At least 3 notes need to be enabled"));
            return;
        }

        config.setPlayerHealth(sequenceLength);
        config.setSequenceLength(playerHealth);
        config.getNotesEnabled().clear();
        config.getNotesEnabled().addAll(notesEnabled);

        configChanged = true;
    }

    public void finishRound(PlayerEntity player) {
        if (state != GameState.ROUND_RUNNING) {
            throw new BadStateException();
        }

        if (player1 == player) {
            player1.setFinish(true);

            roundFinish = true;
        } else if (player2 == player) {
            player2.setFinish(true);

            roundFinish = true;
        }
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

    public GameState getState() {
        return state;
    }

    public void resetState() {
        logger.warn("Round abort, loading lobby");
        broadcast(ResponseFactory.createLobbyLoadedResponse());

        player1.reset();
        player2.reset();
        state = GameState.LOBBY;
    }

    @Override
    public void onClientHandlerStop(ClientHandler handler) {
        removePlayer(getPlayer(handler));
    }
}
