package com.pikolinc.app;

import com.pikolinc.app.initializer.*;
import com.pikolinc.app.initializer.config.DatabaseInitializer;
import com.pikolinc.config.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.util.List;

/**
 * Bootstraps and initializes the server.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Read environment variables (PORT) via {@link com.pikolinc.config.Env}.</li>
 *   <li>Configure Spark (port, static files).</li>
 *   <li>Run all application initializers (database, routes, websockets, etc.).</li>
 *   <li>Start Spark and register a shutdown hook for clean termination.</li>
 * </ul>
 *
 * This class logs initializer progress and stops the server if an initializer fails.
 *
 * @see com.pikolinc.config.Env
 * @see com.pikolinc.app.initializer.Initializer
 */
public class ServerInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ServerInitializer.class);

    public static void run() {
        int port = Integer.parseInt(Env.get("PORT", "8080"));
        Spark.port(port);
        Spark.staticFiles.location("/public");

        List<Initializer> initializers = List.of(
                new DatabaseInitializer(),
                new EventListenersInitializer(),
                new WebsocketInitializer(),
                new ExceptionHandlerInitializer(),
                new MiddlewaresInitializer(),
                new RoutesInitializer()
        );

        try {
            for (Initializer initializer : initializers) {
                logger.info("🚀 Running initializer: {}", initializer.name());
                initializer.init();
            }

            Spark.init();
            Spark.awaitInitialization();
        } catch (Exception e) {
            logger.error("❌ Server failed to start due to initializer error", e);
            Spark.stop();
            return;
        }

        logger.info("🚀 Application started on port {}", port);
        addShutdownHook();
    }

    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Spark.stop();
            logger.info("🧹 Server stopped cleanly");
        }));
    }
}
