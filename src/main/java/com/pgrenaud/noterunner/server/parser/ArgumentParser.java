package com.pgrenaud.noterunner.server.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class ArgumentParser {
    private static final int DEFAULT_LISTEN_PORT = 7745;

    private final Options options;

    private int listenPort;

    public ArgumentParser() {
        options = new Options();
        options.addOption(Option.builder()
                .longOpt("port")
                .hasArg()
                .argName("PORT")
                .valueSeparator(' ')
                .desc("use specified listen port (default to 7745)")
                .build());
        options.addOption(new Option("v", "verbose", false, "enable debugging messages"));

        listenPort = DEFAULT_LISTEN_PORT;
    }

    public void parse(String[] args) {
        if (args.length == 0) {
            displayHelpAndExit();
        } else {
            CommandLineParser parser = new DefaultParser();

            try {
                CommandLine cmd = parser.parse(options, args);

                if (cmd.hasOption("port")) {
                    try {
                        listenPort = Integer.parseInt(cmd.getOptionValue("port"));

                        if (listenPort < 0) {
                            throw new ParseException("Port number cannot be negative");
                        }
                    } catch (NumberFormatException e) {
                        throw new ParseException("Invalid port number");
                    }
                }

                if (cmd.hasOption("verbose")) {
                    LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
                    Configuration configuration = loggerContext.getConfiguration();
                    LoggerConfig loggerConfig = configuration.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
                    loggerConfig.setLevel(Level.DEBUG);
                    loggerContext.updateLoggers();
                }
            } catch(ParseException e) {
                System.err.println(e.getMessage());

                System.exit(1);
            }
        }
    }

    private void displayHelpAndExit() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(120, "noterunner [--port PORT] [--verbose]", null, options, null);

        System.exit(0);
    }

    public int getListenPort() {
        return listenPort;
    }
}
