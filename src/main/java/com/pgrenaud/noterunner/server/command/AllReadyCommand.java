package com.pgrenaud.noterunner.server.command;

import com.google.inject.Inject;
import com.pgrenaud.noterunner.server.game.GameState;
import com.pgrenaud.noterunner.server.game.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AllReadyCommand extends BaseCommand {
    private static final Logger logger = LogManager.getLogger();

    private final World world;

    @Inject
    public AllReadyCommand(World world) {
        super("allready");

        this.world = world;
    }

    @Override
    public void execute(String[] args) {
        if (world.getState() != GameState.LOBBY && world.getState() != GameState.ROUND_LOADING) {
            logger.error("Cannot mark all players ready in this state");
            return;
        }

        if (world.getPlayer1() != null) {
            world.readyPlayer(world.getPlayer1(), true);
        }

        if (world.getPlayer2() != null) {
            world.readyPlayer(world.getPlayer2(), true);
        }
    }
}
