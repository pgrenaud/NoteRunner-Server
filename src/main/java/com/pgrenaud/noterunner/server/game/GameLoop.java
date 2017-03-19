package com.pgrenaud.noterunner.server.game;

import com.google.inject.Inject;
import com.pgrenaud.noterunner.server.network.RequestQueue;
import com.pgrenaud.noterunner.server.util.Stoppable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameLoop implements Runnable, Stoppable {
    private static final Logger logger = LogManager.getLogger();

    private static final double FRAMERATE      = 60.0;
    private static final double MILLISECONDS   = 1000.0;
    private static final double NANOSECONDS    = 1000000000.0;
    private static final double NANO_PER_MILLI = NANOSECONDS / MILLISECONDS;
    private static final double MILLI_PER_TICK = MILLISECONDS / FRAMERATE;
    private static final double NANO_PER_TICK  = NANOSECONDS / FRAMERATE;

    private final RequestQueue requests;
    private final World world;

    private volatile boolean running;

    @Inject
    public GameLoop(RequestQueue requests, World world) {
        this.requests = requests;
        this.world = world;
    }

    @Override
    public void run() {
        running = true;

        // TODO: Initialization goes here <coding@pgrenaud.com>

        long lastTime = System.nanoTime();
        long now;
        long diff;
        double delta;
        long sleep;

//        long lastFpsTime = 0L;
//        long fps = 0L;

        while (running) {
            // Update timing
            now = System.nanoTime();
            diff = now - lastTime;
            lastTime = now;
            delta = diff / NANO_PER_TICK; // TODO: Send delta to tick if needed <coding@pgrenaud.com>

//            // Update FPS
//            lastFpsTime += diff;
//            fps++;
//
//            // Display FPS if one second has passed
//            if (lastFpsTime >= NANOSECONDS) {
//                logger.debug("Game FPS: {}", fps);
//                lastFpsTime = 0L;
//                fps = 0L;
//            }

//            long before = System.nanoTime();
            tick();
//            long after = System.nanoTime();
//            logger.debug("Game tick time took {} / {}", (after - before) / NANO_PER_MILLI, MILLI_PER_TICK);

            // Calculate sleep time
            sleep = (long)((NANO_PER_TICK - (System.nanoTime() - now))/NANO_PER_MILLI);

            // Prevent negative sleep
            if (sleep <= 0L) {
                sleep = 1L;
            }

            // Sleep until next tick
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void tick() {
        requests.tick();
        world.tick();
    }

    @Override
    public void stop() {
        if (!running) {
            return; // Already stopped, nothing to do.
        }

        logger.debug("Stopping GameLoop");
        running = false;

        logger.debug("GameLoop ended");
    }
}
