package com.pgrenaud.noterunner.server.network;

public class RequestFactory {

    public static Request createRegisterRequest() {
        Request request = new Request(Request.Type.REGISTER);

        return request;
    }

    public static Request createUnregisterRequest() {
        Request request = new Request(Request.Type.UNREGISTER);

        return request;
    }
}
