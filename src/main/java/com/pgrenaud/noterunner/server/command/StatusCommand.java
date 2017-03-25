package com.pgrenaud.noterunner.server.command;

import com.google.inject.Inject;
import com.pgrenaud.noterunner.server.entity.PlayerEntity;
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
        logger.info("Player  Netlink   Ready Health Address");
        logger.info("------- --------- ----- ------ -------");

        if (world.getPlayer1() != null) {
            logger.info(getStatusLine("Player1", world.getPlayer1()));
        } else {
            logger.info("Player1 None");
        }

        if (world.getPlayer2() != null) {
            logger.info(getStatusLine("Player2", world.getPlayer2()));
        } else {
            logger.info("Player2 None");
        }

        logger.info("--------------------------------------");
        logger.info("GameState: {}", world.getState());
    }

    private String getStatusLine(String name, PlayerEntity player) {
        return String.format("%s Connected %-5b %6d %s",
                name,
                player.isReady(),
                player.getHealth(),
                player.getHandler().getAddress()
        );
    }
}
