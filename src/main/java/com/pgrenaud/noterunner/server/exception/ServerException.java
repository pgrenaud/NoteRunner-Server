package com.pgrenaud.noterunner.server.exception;

public class ServerException extends Exception {
    private static final long serialVersionUID = -3619456564785336029L;

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
