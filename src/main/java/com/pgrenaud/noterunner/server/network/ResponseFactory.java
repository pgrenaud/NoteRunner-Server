package com.pgrenaud.noterunner.server.network;

import com.pgrenaud.noterunner.server.entity.ConfigEntity;

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

    public static Response createConfigResponse(ConfigEntity config) {
        Response response = new Response(Response.Type.CONFIG_UPDATED);

        response.getPayload().setConfig(config);

        return response;
    }

    public static Response createInvlidConfigResponse(String message) {
        Response response = new Response(Response.Type.CONFIG_REJECTED);

        response.getPayload().setMessage(message);

        return response;
    }
}
