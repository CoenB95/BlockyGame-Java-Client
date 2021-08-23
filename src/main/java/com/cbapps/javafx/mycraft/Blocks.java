package com.cbapps.javafx.mycraft;

public enum Blocks {
    AIR(0),
    DIRT(1);

    private final int id;

    Blocks(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
