package com.pgrenaud.noterunner.server.network;

import com.pgrenaud.noterunner.server.entity.ConfigEntity;

public class RequestFactory {

    public static Request createRegisterRequest() {
        Request request = new Request(Request.Type.REGISTER);

        return request;
    }

    public static Request createUnregisterRequest() {
        Request request = new Request(Request.Type.UNREGISTER);

        return request;
    }

    public static Request createUpdateConfigRequest() {
        Request request = new Request(Request.Type.UPDATE_CONFIG);

        request.getPayload().setConfig(new ConfigEntity());

        return request;
    }

    public static Request createSetReadyRequest() {
        Request request = new Request(Request.Type.SET_READY);

        request.getPayload().setReady(true);

        return request;
    }

    public static Request createDamageRequest() {
        Request request = new Request(Request.Type.DAMAGE);

        return request;
    }

    public static Request createFinishRequest() {
        Request request = new Request(Request.Type.FINISH);

        return request;
    }
}
