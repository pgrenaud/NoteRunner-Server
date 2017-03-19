package com.pgrenaud.noterunner.server.util;

import com.djdch.log4j.QueuedConsoleAppender;
import jline.console.ConsoleReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShutdownHandler implements Runnable {
    private static final Logger logger = LogManager.getLogger();

    private final Stoppable stoppable;
    private final ConsoleReader console;

    public ShutdownHandler(Stoppable stoppable, ConsoleReader console) {
        this.stoppable = stoppable;
        this.console = console;
    }

    @Override
    public void run() {
        try {
            logger.debug("ShutdownHandler invoked");

            logger.debug("Restoring terminal");
            try {
                console.getTerminal().restore();
            } catch (Exception e) {
                logger.error("Exception occurred while restoring terminal", e);
            }

            System.out.print("\r"); // Manually erase prompt leftover
            QueuedConsoleAppender.setRunning(false); // Disable queued message and send them directly to stdout

            logger.debug("Invoking stop method");
            stoppable.stop();
        } finally {
            logger.debug("Shutting down Log4j manually");
            LogManager.shutdown();
        }
    }
}
