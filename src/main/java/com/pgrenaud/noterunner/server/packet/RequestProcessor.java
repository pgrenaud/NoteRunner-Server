package com.pgrenaud.noterunner.server.packet;

import com.google.inject.Singleton;
import com.pgrenaud.noterunner.server.factory.ResponseFactory;
import com.pgrenaud.noterunner.server.runnable.ClientHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
public class RequestProcessor {
    private static final Logger logger = LogManager.getLogger();

    private final Queue<RequestContainer> queue;

    public RequestProcessor() {
        queue = new ConcurrentLinkedQueue<>();
    }

    public void tick() {
        int max = 100; // TODO: Adjust value and move to constant <coding@pgrenaud.com>
        RequestContainer container;

        while ((container = queue.poll()) != null && --max >= 0) {
            process(container);
        }
    }

    private void process(RequestContainer container) {
        long startTime = System.currentTimeMillis();

        Request request = container.getRequest();
        ClientHandler handler = container.getHandler();

        logger.debug("Processing {} request", request.getType());
        switch (request.getType()) {
            case REGISTER:
                doRegister(request, handler);
                break;
            case UNREGISTER:
                doUnregister(request, handler);
                break;
            default:
                logger.warn("Request contains invalid payload, ignoring request");
        }

        long endTime = System.currentTimeMillis();

        logger.debug("Completed {} request in {}ms", request.getType(), endTime - startTime);
    }

    private void doRegister(Request request, ClientHandler handler) {
        logger.debug("Name is {}", request.getPayload().getName());

        handler.sendResponse(ResponseFactory.createRegisteredResponse("OK"));
    }

    private void doUnregister(Request request, ClientHandler handler) {
        handler.sendResponse(ResponseFactory.createUnregisteredResponse());
    }

    public void put(RequestContainer container) {
        queue.add(container);
    }
}
