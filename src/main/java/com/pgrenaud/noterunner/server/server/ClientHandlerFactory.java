package com.pgrenaud.noterunner.server.server;

import java.net.Socket;

public interface ClientHandlerFactory {
    ClientHandler create(Socket socket);
}
