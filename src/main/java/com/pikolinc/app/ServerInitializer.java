package com.pikolinc.app;

import com.pikolinc.app.initializer.ExceptionHandlerInitializer;
import com.pikolinc.app.initializer.Initializer;
import com.pikolinc.app.initializer.MiddlewaresInitializer;
import com.pikolinc.app.initializer.RoutesInitializer;
import com.pikolinc.app.initializer.config.DatabaseInitializer;
import com.pikolinc.config.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.util.List;

public class ServerInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ServerInitializer.class);

    public static void run() {
        int port = Integer.parseInt(Env.get("PORT", "8080"));
        Spark.port(port);

        List<Initializer> initializers = List.of(
                new DatabaseInitializer(),
                new ExceptionHandlerInitializer(),
                new MiddlewaresInitializer(),
                new RoutesInitializer()
        );

        try {
            for (Initializer initializer : initializers) {
                logger.info("🚀 Running initializer: {}", initializer.name());
                initializer.init();
            }
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
