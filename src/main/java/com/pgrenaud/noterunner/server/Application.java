package com.pgrenaud.noterunner.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.pgrenaud.noterunner.server.module.NoteRunnerModule;
import com.pgrenaud.noterunner.server.server.NoteRunnerServer;
import com.pgrenaud.noterunner.server.server.NoteRunnerServerFactory;
import com.pgrenaud.noterunner.server.util.ArgumentParser;

public class Application {

    public static void main(String[] args) {
        ArgumentParser parser = new ArgumentParser();
        parser.parse(args);

        Injector injector = Guice.createInjector(new NoteRunnerModule());
        NoteRunnerServerFactory serverFactory = injector.getInstance(NoteRunnerServerFactory.class);

        NoteRunnerServer server = serverFactory.create(parser.getListenPort());
        server.start();
    }
}
