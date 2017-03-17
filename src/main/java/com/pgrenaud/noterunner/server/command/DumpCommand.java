package com.pgrenaud.noterunner.server.command;

import com.pgrenaud.noterunner.server.factory.RequestFactory;
import com.pgrenaud.noterunner.server.packet.Packet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DumpCommand extends BaseCommand {
    private static final Logger logger = LogManager.getLogger();

    public DumpCommand() {
        super("dump");
    }

    @Override
    public void execute(String[] args) {
        Packet packet1 = RequestFactory.createRegisterRequest("Test");
        Packet packet2 = RequestFactory.createUnregisterRequest();

        logger.info("Packet: {}", packet1.encode());
        logger.info("Packet: {}", packet2.encode());
    }
}
