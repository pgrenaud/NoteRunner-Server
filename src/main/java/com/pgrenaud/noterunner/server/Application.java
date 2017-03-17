package com.pgrenaud.noterunner.server;

import com.pgrenaud.noterunner.server.parser.ArgumentParser;
import com.pgrenaud.noterunner.server.server.NoteRunnerServer;

public class Application {

    public static void main(String[] args) {
        ArgumentParser parser = new ArgumentParser();
        parser.parse(args);

        NoteRunnerServer server = new NoteRunnerServer(parser.getListenPort());
        server.start();
    }
}
