package com.pgrenaud.noterunner.server.factory;

import com.pgrenaud.noterunner.server.runnable.GameLoop;

public interface GameLoopFactory {
    GameLoop create();
}
