package com.pikolinc;

import com.pikolinc.app.ServerInitializer;

/**
 * Entry point for the Java Spark Web Application.
 *
 * <p>Starts the application by delegating to {@link com.pikolinc.app.ServerInitializer}.
 * Usage:
 * <pre>
 *   public static void main(String[] args) {
 *       ServerInitializer.run();
 *   }
 * </pre>
 */
public class JavaSparkWebApp {
    public static void main(String[] args) {
        ServerInitializer.run();
    }
}