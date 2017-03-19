package com.pgrenaud.noterunner.server.network;

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
