package com.pgrenaud.noterunner.server.command;

import com.google.inject.Inject;
import com.pgrenaud.noterunner.server.game.World;

public class LobbyCommand extends BaseCommand {

    private final World world;

    @Inject
    public LobbyCommand(World world) {
        super("lobby");

        this.world = world;
    }

    @Override
    public void execute(String[] args) {
        world.resetState();
    }
}
