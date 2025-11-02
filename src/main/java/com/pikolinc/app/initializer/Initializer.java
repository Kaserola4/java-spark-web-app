package com.pikolinc.app.initializer;

/**
 * Functional interface for components that perform application initialization tasks.
 * Implementations should perform small, well-scoped initialization logic (DB setup,
 * route registration, event listeners, websockets, etc.).
 */
public interface Initializer {
    /**
     * Execute the initialization procedure.
     */
    void init();
    /**
     * Returns a human-readable name for logging purposes. Default returns the class simple name.
     */
    default String name(){
        return this.getClass().getSimpleName();
    }

}
