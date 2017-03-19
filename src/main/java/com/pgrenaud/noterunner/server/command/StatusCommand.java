package com.pgrenaud.noterunner.server.command;

import com.google.inject.Inject;
import com.pgrenaud.noterunner.server.game.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatusCommand extends BaseCommand {
    private static final Logger logger = LogManager.getLogger();

    private final World world;

    @Inject
    public StatusCommand(World world) {
        super("status");

        this.world = world;
    }

    @Override
    public void execute(String[] args) {
        logger.info("Players States    Addresses");
        logger.info("------- --------- ---------");

        if (world.getPlayer1() != null) {
            logger.info("Player1 Connected {}", world.getPlayer1().getHandler().getAddress());
        } else {
            logger.info("Player1 None");
        }

        if (world.getPlayer2() != null) {
            logger.info("Player2 Connected {}", world.getPlayer2().getHandler().getAddress());
        } else {
            logger.info("Player2 None");
        }
    }
}
