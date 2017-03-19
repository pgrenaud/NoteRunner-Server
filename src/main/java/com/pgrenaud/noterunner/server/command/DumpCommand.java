package com.pgrenaud.noterunner.server.command;

import com.pgrenaud.noterunner.server.network.RequestFactory;
import com.pgrenaud.noterunner.server.network.Packet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DumpCommand extends BaseCommand {
    private static final Logger logger = LogManager.getLogger();

    public DumpCommand() {
        super("dump");
    }

    @Override
    public void execute(String[] args) {
        Packet packet1 = RequestFactory.createRegisterRequest();
        Packet packet2 = RequestFactory.createUnregisterRequest();

        logger.info("{}", packet1.encode());
        logger.info("{}", packet2.encode());
    }
}
