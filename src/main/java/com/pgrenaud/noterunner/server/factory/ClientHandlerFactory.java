package com.pgrenaud.noterunner.server.factory;

import com.pgrenaud.noterunner.server.runnable.ClientHandler;

import java.net.Socket;

public interface ClientHandlerFactory {
    ClientHandler create(Socket socket);
}
