package com.pgrenaud.noterunner.server.runnable;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.pgrenaud.noterunner.server.repository.CommandRepository;
import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Inspired by ThreadCommandReader from CraftBukkit.
 */
public class TerminalCommandHandler implements Runnable {
    private static final Logger logger = LogManager.getLogger();

    private static final String PROMPT = "noterunner> ";

    private final ConsoleReader reader;
    private final CommandRepository commands;

    @Inject
    public TerminalCommandHandler(@Assisted ConsoleReader reader, CommandRepository commands) {
        this.reader = reader;
        this.commands = commands;

        // Initialize command repository by scanning for commands
        commands.initialize();

        // Add commands to the completer
        reader.addCompleter(new StringsCompleter(commands.keySet()));
    }

    public void run() {
        try {
            Thread.sleep(100); // Prevent display glitch at startup
        } catch (InterruptedException e) {
        }

        while (true) {
            try {
                String input = reader.readLine(PROMPT);

                if (input != null && !input.isEmpty()) {
                    String[] args = input.trim().toLowerCase().split("\\s+");

                    String command = args[0];
                    logger.debug("Command received: {}", input);

                    args = ArrayUtils.subarray(args, 1, args.length);
                    logger.debug("Command args: {}", args);

                    if (!commands.containsKey(command)) {
                        logger.error("Unknown command: {}", command);
                    } else {
                        if (commands.get(command).isArgsValid(args)) {
                            commands.get(command).execute(args);
                        } else {
                            logger.warn("Invalid number of arguments; expected {} arguments", commands.get(command).getNumberOfArgs());
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("Exception occurred while handling commands", e);
            } catch (Exception e) {
                logger.fatal("Unexpected exception occurred while handling commands", e);
                throw e;
            }
        }
    }
}
