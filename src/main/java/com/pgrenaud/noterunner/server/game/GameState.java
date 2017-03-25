package com.pgrenaud.noterunner.server.game;

public enum GameState {
    LOBBY,         // Waiting for ready
    ROUND_LOADING, // Waiting for ready
    ROUND_RUNNING, // Waiting for roundFinish
    ;
}
