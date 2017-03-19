package com.pgrenaud.noterunner.server.module;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.pgrenaud.noterunner.server.server.ClientHandlerFactory;
import com.pgrenaud.noterunner.server.game.GameLoopFactory;
import com.pgrenaud.noterunner.server.server.NoteRunnerServerFactory;
import com.pgrenaud.noterunner.server.util.TerminalCommandHandlerFactory;

public class NoteRunnerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(ClientHandlerFactory.class));
        install(new FactoryModuleBuilder().build(GameLoopFactory.class));
        install(new FactoryModuleBuilder().build(NoteRunnerServerFactory.class));
        install(new FactoryModuleBuilder().build(TerminalCommandHandlerFactory.class));
    }
}
