package com.pgrenaud.noterunner.server.factory;

import com.pgrenaud.noterunner.server.runnable.TerminalCommandHandler;
import jline.console.ConsoleReader;

public interface TerminalCommandHandlerFactory {
    TerminalCommandHandler create(ConsoleReader reader);
}
