package com.pikolinc.app.initializer;

public interface Initializer {
    void init();
    default String name(){
        return this.getClass().getSimpleName();
    }

}
