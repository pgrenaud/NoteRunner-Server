package com.pgrenaud.noterunner.server.util;

import jline.console.ConsoleReader;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Inspired by TerminalConsoleWriterThread from CraftBukkit.
 */
public class TerminalOutputWriter implements Runnable {

    private final ConsoleReader console;
    private final BlockingQueue<String> queue;

    public TerminalOutputWriter(ConsoleReader console, BlockingQueue<String> queue) {
        this.console = console;
        this.queue = queue;
    }

    public void run() {
        String message = null;

        while (true) {
            try {
                message = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (message == null) {
                continue;
            }

            try {
                console.print(ConsoleReader.RESET_LINE + "");
                console.flush();

                System.out.print(message);
                System.out.flush();

                try {
                    console.drawLine();
                } catch (Throwable ex) {
                    console.getCursorBuffer().clear();
                }
                console.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
