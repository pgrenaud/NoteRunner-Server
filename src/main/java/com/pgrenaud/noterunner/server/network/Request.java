package com.pgrenaud.noterunner.server.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.pgrenaud.noterunner.server.network.request.Payload;

public class Request implements Packet {

    @SerializedName("request")
    private final Type type;

    private Payload payload;

    public Request(Type type) {
        this.type = type;
    }

    public Payload getPayload() {
        if (payload == null) {
            payload = new Payload();
        }

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

    public static String encode(Request command) {
        return command.encode();
    }

    public static Request decode(String json) throws JsonSyntaxException {
        Gson gson = new Gson();

        return gson.fromJson(json, Request.class);
    }

    public enum Type {
        @SerializedName("register")
        REGISTER,
        @SerializedName("unregister")
        UNREGISTER,
        @SerializedName("update_config")
        UPDATE_CONFIG,
        @SerializedName("set_ready")
        SET_READY,
        @SerializedName("damage")
        DAMAGE,
        @SerializedName("finish")
        FINISH,
        ;
    }
}
