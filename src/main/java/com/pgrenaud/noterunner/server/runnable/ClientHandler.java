package com.pgrenaud.noterunner.server.runnable;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.pgrenaud.noterunner.server.exception.InvalidPacketException;
import com.pgrenaud.noterunner.server.packet.Request;
import com.pgrenaud.noterunner.server.packet.Packet;
import com.pgrenaud.noterunner.server.packet.RequestContainer;
import com.pgrenaud.noterunner.server.packet.RequestProcessor;
import com.pgrenaud.noterunner.server.stoppable.Stoppable;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable, Stoppable {
    private static final Logger logger = LogManager.getLogger();

    private final Socket socket;
    private final RequestProcessor requests;

    private BufferedReader reader;
    private BufferedWriter writer;
    private PrintWriter printer;
    private volatile boolean running;

    @Inject
    public ClientHandler(@Assisted Socket socket, RequestProcessor requests) {
        this.socket = socket;
        this.requests = requests;
    }

    @Override
    public void run() {
        logger.debug("ClientHandler begin");
        running = true;

        try {
            logger.info("Incoming connection {}", socket.getRemoteSocketAddress());

            logger.debug("Initializing streams, buffers, readers and writers");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            printer = new PrintWriter(writer, true);

            // TODO: Use socket.setSoTimeout() to prevent readLine from blocking infinitely <coding@pgrenaud.com>

            String json;

            logger.debug("Waiting for requests");

            while ((json = reader.readLine()) != null) {
                try {
                    logger.debug("Received request {}", json); // TODO: Remove payload from message

                    logger.debug("Parsing request");
                    Request request = Request.decode(json);

                    if (request.getType() == null) {
                        throw new InvalidPacketException(String.format("Unknown request type %s.", request.getType()));
                    }

                    logger.debug("Queueing request");
                    requests.put(new RequestContainer(request, this));
                } catch (JsonSyntaxException e) {
                    logger.warn("Request is not valid json, ignoring request", e);
                } catch (InvalidPacketException e) {
                    logger.warn("Request contains invalid payload, ignoring request", e);
                }
            }
        } catch (IOException e) {
            if (running) {
                logger.error("Exception occurred while handling a client", e);
            }
        // TODO: Catch SocketException
        } catch (Exception e) {
            logger.fatal("Unexpected exception occurred while handling a client", e);
            throw e;
        } finally {
            stop();
        }
    }

    @Override
    public void stop() {
        if (!running) {
            return; // Already stopped, nothing to do.
        }

        logger.debug("Stopping ClientHandler");
        running = false;

        // For whatever reason, PrintWriter must be closed first, or exceptions will be raised
        if (printer != null) {
            printer.close();

            if (printer.checkError()) {
                logger.warn("PrintWriter has encountered an internal error and will be closed in a dirty state");
            }
        }

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                logger.error("Exception occurred while closing a buffered reader", e);
            }
        }

        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                logger.error("Exception occurred while closing an buffered writer", e);
            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("Exception occurred while closing a socket", e);
            }
        }

        logger.debug("ClientHandler ended");
    }

    public synchronized void sendResponse(Packet response) {
        if (running && printer != null) {
            logger.debug("Sending response {}", response.encode()); // TODO: Remove payload from message
            printer.println(response.encode());
        }
    }
}
