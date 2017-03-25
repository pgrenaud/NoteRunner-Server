package com.pgrenaud.noterunner.server.command;

import com.pgrenaud.noterunner.server.network.Packet;
import com.pgrenaud.noterunner.server.network.RequestFactory;
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
        Packet packet3 = RequestFactory.createUpdateConfigRequest();
        Packet packet4 = RequestFactory.createSetReadyRequest();
        Packet packet5 = RequestFactory.createDamageRequest();
        Packet packet6 = RequestFactory.createFinishRequest();

        logger.info("{}", packet1.encode());
        logger.info("{}", packet2.encode());
        logger.info("{}", packet3.encode());
        logger.info("{}", packet4.encode());
        logger.info("{}", packet5.encode());
        logger.info("{}", packet6.encode());
    }
}
