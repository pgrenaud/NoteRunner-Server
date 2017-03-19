package com.pgrenaud.noterunner.server.packet;

import com.pgrenaud.noterunner.server.runnable.ClientHandler;

public class RequestContainer {

    private final Request request;
    private final ClientHandler handler;

    public RequestContainer(Request request, ClientHandler handler) {
        this.request = request;
        this.handler = handler;
    }

    public Request getRequest() {
        return request;
    }

    public ClientHandler getHandler() {
        return handler;
    }
}
