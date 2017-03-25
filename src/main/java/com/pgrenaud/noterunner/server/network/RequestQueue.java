package com.pgrenaud.noterunner.server.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pgrenaud.noterunner.server.entity.PlayerEntity;
import com.pgrenaud.noterunner.server.game.BadStateException;
import com.pgrenaud.noterunner.server.game.UnauthorizedException;
import com.pgrenaud.noterunner.server.game.World;
import com.pgrenaud.noterunner.server.server.ClientHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
public class RequestQueue {
    private static final Logger logger = LogManager.getLogger();

    private final World world;
    private final Queue<RequestContainer> queue;

    @Inject
    public RequestQueue(World world) {
        this.world = world;

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

        try {
            logger.debug("Processing {} request", request.getType());
            switch (request.getType()) {
                case REGISTER:
                    doRegister(request, handler);
                    break;
                case UNREGISTER:
                    doUnregister(request, handler);
                    break;
                case SET_READY:
                    doReady(request, handler);
                    break;
                case UPDATE_CONFIG:
                    doConfig(request, handler);
                    break;
                case DAMAGE:
                    doDamage(request, handler);
                    break;
                case FINISH:
                    doFinish(request, handler);
                    break;
                default:
                    logger.warn("Request contains invalid payload, ignoring request");
            }
        } catch (UnauthorizedException e) {
            logger.debug("Unauthorized exception occurred while handling a client");

            if (handler != null) {
                handler.kick("Who are you?");
            }
        } catch (BadStateException e) {
            logger.debug("Bad state exception occurred while handling a client");

            if (handler != null) {
                handler.sendResponse(ResponseFactory.createInvalidRequestResponse("What are you trying to do?"));
            }
        } catch (Exception e) {
            logger.fatal("Unexpected exception occurred while handling a client", e);
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();

            logger.debug("Completed {} request in {}ms", request.getType(), endTime - startTime);
        }
    }

    private void doRegister(Request request, ClientHandler handler) {
        world.addPlayer(new PlayerEntity(handler));
    }

    private void doUnregister(Request request, ClientHandler handler) {
        world.removePlayer(world.getPlayer(handler));
    }

    private void doReady(Request request, ClientHandler handler) {
        world.readyPlayer(world.getPlayer(handler), request.getPayload().isReady());
    }

    private void doConfig(Request request, ClientHandler handler) {
        world.updateConfig(world.getPlayer(handler), request.getPayload().getConfig());
    }

    private void doDamage(Request request, ClientHandler handler) {
        world.damagePlayer(world.getPlayer(handler));
    }

    private void doFinish(Request request, ClientHandler handler) {
        world.finishRound(world.getPlayer(handler));
    }

    public void put(RequestContainer container) {
        queue.add(container);
    }
}
