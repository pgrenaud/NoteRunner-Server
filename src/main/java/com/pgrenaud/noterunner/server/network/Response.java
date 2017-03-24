package com.pgrenaud.noterunner.server.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.pgrenaud.noterunner.server.network.response.Payload;

public class Response implements Packet {

    @SerializedName("response")
    private final Type type;
    private final Payload payload;

    public Response(Type type) {
        this.type = type;

        payload = new Payload();
    }

    public Payload getPayload() {
        return payload;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(this);
    }

    @Override
    public String encode() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }

    public static String encode(Response command) {
        return command.encode();
    }

    public static Response decode(String json) throws JsonSyntaxException {
        Gson gson = new Gson();

        return gson.fromJson(json, Response.class);
    }

    public enum Type {
        @SerializedName("registered")
        REGISTERED,
        @SerializedName("unregistered")
        UNREGISTERED,
        @SerializedName("kick")
        KICK,
        @SerializedName("config_updated")
        CONFIG_UPDATED,
        @SerializedName("config_rejected")
        CONFIG_REJECTED,
        ;
    }
}
