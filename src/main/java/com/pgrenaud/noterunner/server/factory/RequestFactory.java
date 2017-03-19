package com.pgrenaud.noterunner.server.factory;

import com.pgrenaud.noterunner.server.packet.Request;

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
