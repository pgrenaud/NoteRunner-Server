package com.pgrenaud.noterunner.server.server;

import com.djdch.log4j.QueuedConsoleAppender;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.pgrenaud.noterunner.server.module.NoteRunnerModule;
import com.pgrenaud.noterunner.server.runnable.ClientHandler;
import com.pgrenaud.noterunner.server.exception.ServerException;
import com.pgrenaud.noterunner.server.repository.ClientRepository;
import com.pgrenaud.noterunner.server.runnable.ShutdownHandler;
import com.pgrenaud.noterunner.server.runnable.TerminalCommandHandler;
import com.pgrenaud.noterunner.server.runnable.TerminalOutputWriter;
import com.pgrenaud.noterunner.server.stoppable.Stoppable;
import jline.console.ConsoleReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class NoteRunnerServer implements Stoppable {
    private static final Logger logger = LogManager.getLogger();

    private static final int POOL_SIZE = 99;

    private final int listenPort;
    private final ClientRepository clients;
    private final Map<Integer, ClientHandler> handlers;
    private final Injector injector;

    private ConsoleReader console;
    private TerminalOutputWriter terminalOutputWriter;
    private TerminalCommandHandler terminalCommandHandler;
    private ExecutorService handlerPool;
    private ServerSocket server;
    private boolean running;

    public NoteRunnerServer(int listenPort) {
        this.listenPort = listenPort;

        injector = Guice.createInjector(new NoteRunnerModule());
        clients = injector.getInstance(ClientRepository.class);

        handlers = new ConcurrentHashMap<>();
    }

    public void start() {
        try {
            logger.debug("Starting NoteRunnerServer");
            running = true;

            logger.debug("Creating console reader");
            try {
                console = new ConsoleReader(System.in, System.out);
                console.setExpandEvents(false);
            } catch (IOException e) {
                logger.error("Exception occurred while creating console reader", e);
            }

            logger.debug("Adding shutdown hook");
            Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHandler(this, console)));

            logger.debug("Initializing output writer");
            terminalOutputWriter = new TerminalOutputWriter(console, QueuedConsoleAppender.getOutputQueue());

            logger.debug("Starting output writer thread");
            Thread terminalOutputWriterThread = new Thread(terminalOutputWriter);
            terminalOutputWriterThread.start();

            logger.debug("Initializing command handler");
            terminalCommandHandler = new TerminalCommandHandler(console, injector);

            logger.debug("Starting command handler thread");
            Thread terminalCommandHandlerThread = new Thread(terminalCommandHandler);
            terminalCommandHandlerThread.start();

            try {
                logger.debug("Creating handler thread pool with {} threads", POOL_SIZE);
                handlerPool = Executors.newFixedThreadPool(POOL_SIZE);
            } catch (IllegalArgumentException e) {
                throw new ServerException("Exception occurred while creating handler thread pool.", e);
            }

            try {
                logger.debug("Starting server socket with port {}", listenPort);
                server = new ServerSocket(listenPort);
            } catch (IOException | IllegalArgumentException e) {
                throw new ServerException("Exception occurred while starting server socket.", e);
            }

            logger.info("NoteRunnerServer started");

            while (running) {
                Socket socket = null;

                try {
                    socket = server.accept();
                } catch (IOException e) {
                    if (running && !server.isClosed()) {
                        throw new ServerException("Exception occurred while waiting for a new connection.", e);
                    }
                }

                if (!running) {
                    return; // Already stopped, nothing to do.
                }

                try {
                    logger.debug("New connection received, sending request to client handler");
                    ClientHandler handler = new ClientHandler(socket, clients);
                    handlerPool.submit(handler);
                } catch (RejectedExecutionException e) {
                    throw new ServerException("Exception occurred while submitting request to client handler.", e);
                }
            }
        } catch (ServerException e) {
            logger.fatal("Exception occurred while running application", e);
            System.exit(1); // Trigger shutdown sequence
        } catch (Exception e) {
            logger.fatal("Unexpected exception occurred while running application", e);
            System.exit(1); // Trigger shutdown sequence
            throw e;
        }
    }

    @Override
    public void stop() {
        logger.debug("Stopping NoteRunnerServer");
        running = false;

        // TODO: Send shutdown event to all clients

        logger.debug("Stopping all client handlers");
        for (ClientHandler handler : handlers.values()) {
            handler.stop();
        }
        handlers.clear();

        if (server != null) {
            logger.debug("Closing server socket");
            try {
                server.close();
            } catch (IOException e) {
                logger.fatal("Exception occurred while closing server socket", e);
            }
        }

        if (handlerPool != null) {
            logger.debug("Shutting down handler thread pool");
            handlerPool.shutdown();

            try {
                logger.debug("Waiting handler thread pool to terminate");
                boolean result = handlerPool.awaitTermination(30, TimeUnit.SECONDS);

                if (!result) {
                    logger.warn("Handler thread pool was not terminated before timeout elapsed");
                } else {
                    logger.debug("Handler thread pool successfully terminated");
                }
            } catch (InterruptedException e) {
                logger.fatal("Exception occurred while waiting handler thread pool to terminate", e);
            }
        }

        logger.info("NoteRunnerServer stopped");
    }
}
