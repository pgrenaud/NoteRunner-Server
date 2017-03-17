package com.pgrenaud.noterunner.server.factory;

import com.pgrenaud.noterunner.server.packet.Response;

public class ResponseFactory {

    public static Response createRegisteredResponse(String name) {
        Response response = new Response(Response.Type.REGISTERED);

        response.getPayload().setName(name);

        return response;
    }

    public static Response createUnregisteredResponse() {
        Response response = new Response(Response.Type.UNREGISTERED);

        return response;
    }

    public static Response createKickResponse() {
        Response response = new Response(Response.Type.KICK);

        return response;
    }

    public static Response createShutdownResponse() {
        Response response = new Response(Response.Type.SHUTDOWN);

        return response;
    }
}
