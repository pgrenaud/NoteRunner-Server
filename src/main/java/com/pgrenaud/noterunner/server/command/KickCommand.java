package com.pgrenaud.noterunner.server.command;

import com.google.inject.Inject;
import com.pgrenaud.noterunner.server.game.World;
import com.pgrenaud.noterunner.server.game.PlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KickCommand extends BaseCommand {
    private static final Logger logger = LogManager.getLogger();

    private final World world;

    @Inject
    public KickCommand(World world) {
        super("kick", 1);

        this.world = world;
    }

    @Override
    public void execute(String[] args) {
        String string = args[0];

        PlayerEntity player = null;

        if (string.equals("1") || string.toLowerCase().equals("player1")) {
            player = world.getPlayer1();
        } else if (string.equals("2") || string.toLowerCase().equals("player2")) {
            player = world.getPlayer2();
        }

        if (player != null) {
            player.kick("Kicked by operator");
            world.removePlayer(player);
        } else {
            logger.warn("Invalid player");
        }
    }
}
