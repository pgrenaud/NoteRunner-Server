package com.pgrenaud.noterunner.server.repository;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.pgrenaud.noterunner.server.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class CommandRepository {
    private static final Logger logger = LogManager.getLogger();

    private static final String SEARCH_PACKAGE = "com.pgrenaud.noterunner";

    private final Injector injector;
    private final Map<String, Command> commands;

    private volatile boolean initialized;

    @Inject
    public CommandRepository(Injector injector) {
        this.injector = injector;

        commands = new HashMap<>();
    }

    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("CommandRepository already initialized");
        }

        loadCommands();

        initialized = true;
    }

    private void loadCommands() {
        logger.debug("Scanning package {} looking for commands", SEARCH_PACKAGE);

        // Searching for classes implementing the Command interface
        Reflections reflections = new Reflections(SEARCH_PACKAGE, new SubTypesScanner(false));
        Set<Class<? extends Command>> commandClasses = reflections.getSubTypesOf(Command.class);

        // For each Command found, create an instance and add it to the command map
        for (Class<? extends Command> commandClass : commandClasses) {
            if (Modifier.isAbstract(commandClass.getModifiers())) {
                continue; // Skip abstract class
            }

            // Create instance while injecting dependencies
            Command command = injector.getInstance(commandClass);

            commands.put(command.getName(), command);
        }

        logger.debug("Loaded {} commands: {}", commands.size(), commands.keySet());
    }

    public Command get(String command) {
        return commands.get(command);
    }

    public boolean containsKey(String command) {
        return commands.containsKey(command);
    }

    public Set<String> keySet() {
        return commands.keySet();
    }

    @Override
    public String toString() {
        return String.valueOf(keySet());
    }
}
