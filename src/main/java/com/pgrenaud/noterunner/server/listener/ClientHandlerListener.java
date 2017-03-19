package com.pgrenaud.noterunner.server.listener;

import com.pgrenaud.noterunner.server.runnable.ClientHandler;

public interface ClientHandlerListener {
    void onClientHandlerStop(ClientHandler handler);
}
