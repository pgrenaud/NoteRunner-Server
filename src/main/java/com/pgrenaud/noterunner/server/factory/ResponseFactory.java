package com.pgrenaud.noterunner.server.factory;

import com.pgrenaud.noterunner.server.packet.Response;

public class ResponseFactory {

    public static Response createRegisteredResponse() {
        Response response = new Response(Response.Type.REGISTERED);

        return response;
    }

    public static Response createUnregisteredResponse() {
        Response response = new Response(Response.Type.UNREGISTERED);

        return response;
    }

    public static Response createKickResponse(String message) {
        Response response = new Response(Response.Type.KICK);

        response.getPayload().setMessage(message);

        return response;
    }
}
