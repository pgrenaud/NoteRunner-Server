package com.pgrenaud.noterunner.server.network;

import com.pgrenaud.noterunner.server.entity.ConfigEntity;
import com.pgrenaud.noterunner.server.entity.NoteEntity;

import java.util.List;

public class ResponseFactory {

    public static Response createRegisteredResponse(int playerId) {
        Response response = new Response(Response.Type.REGISTERED);

        response.getPayload().setPlayer(playerId);

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

    public static Response createInvalidRequestResponse(String message) {
        Response response = new Response(Response.Type.REQUEST_REJECTED);

        response.getPayload().setMessage(message);

        return response;
    }

    public static Response createPlayerConnectedResponse(int playerId) {
        Response response = new Response(Response.Type.PLAYER_CONNECTED);

        response.getPayload().setPlayer(playerId);

        return response;
    }

    public static Response createPlayerDisconnectedResponse(int playerId) {
        Response response = new Response(Response.Type.PLAYER_DISCONNECTED);

        response.getPayload().setPlayer(playerId);

        return response;
    }

    public static Response createPlayerReadyResponse(int playerId) {
        Response response = new Response(Response.Type.PLAYER_READY);

        response.getPayload().setPlayer(playerId).setReady(true);

        return response;
    }

    public static Response createPlayerNotReadyResponse(int playerId) {
        Response response = new Response(Response.Type.PLAYER_READY);

        response.getPayload().setPlayer(playerId).setReady(false);

        return response;
    }

    public static Response createPlayerHealthResponse(int playerId, int health) {
        Response response = new Response(Response.Type.PLAYER_HEALTH);

        response.getPayload().setPlayer(playerId).setHealth(health);

        return response;
    }

    public static Response createRoundPreparedResponse(ConfigEntity config, List<NoteEntity> notes) {
        Response response = new Response(Response.Type.ROUND_PREPARED);

        response.getPayload().setConfig(config).setNotes(notes);

        return response;
    }

    public static Response createRoundBeganResponse() {
        Response response = new Response(Response.Type.ROUND_BEGAN);

        return response;
    }

    public static Response createRoundEndedResponse() {
        Response response = new Response(Response.Type.ROUND_ENDED);

        return response;
    }

    public static Response createGameoverResponse(int playerId) {
        Response response = new Response(Response.Type.GAMEOVER);

        response.getPayload().setPlayer(playerId);

        return response;
    }

    public static Response createLobbyLoadedResponse() {
        Response response = new Response(Response.Type.LOBBY_LOADED);

        return response;
    }
}
