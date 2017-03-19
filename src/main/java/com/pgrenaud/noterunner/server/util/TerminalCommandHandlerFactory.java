package com.pgrenaud.noterunner.server.util;

import jline.console.ConsoleReader;

public interface TerminalCommandHandlerFactory {
    TerminalCommandHandler create(ConsoleReader reader);
}
