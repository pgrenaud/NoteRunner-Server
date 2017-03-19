package com.pgrenaud.noterunner.server.network;

public class InvalidPacketException extends Exception {
    private static final long serialVersionUID = 4622842024813332361L;

    public InvalidPacketException(String message) {
        super(message);
    }
}
