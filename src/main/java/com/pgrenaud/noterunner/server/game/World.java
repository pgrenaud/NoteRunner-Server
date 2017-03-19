package com.pgrenaud.noterunner.server.game;

import com.google.inject.Singleton;
import com.pgrenaud.noterunner.server.entity.PlayerEntity;
import com.pgrenaud.noterunner.server.network.ResponseFactory;
import com.pgrenaud.noterunner.server.server.ClientHandlerListener;
import com.pgrenaud.noterunner.server.server.ClientHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Singleton
public class World implements ClientHandlerListener {
    private static final Logger logger = LogManager.getLogger();

    private PlayerEntity player1;
    private PlayerEntity player2;

    public World() {
    }

    public void tick() {

    }

    public void addPlayer(PlayerEntity player) {
        if (player1 == null) {
            player1 = player;
            player1.getHandler().setListener(this);
            player1.send(ResponseFactory.createRegisteredResponse());

            logger.info("Player1 connected");
        } else if (player2 == null) {
            player2 = player;
            player2.getHandler().setListener(this);
            player2.send(ResponseFactory.createRegisteredResponse());

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
        } else {
            player.kick("Who are you?");
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
            return null;
        }
    }

    @Override
    public void onClientHandlerStop(ClientHandler handler) {
        removePlayer(getPlayer(handler));
    }
}
