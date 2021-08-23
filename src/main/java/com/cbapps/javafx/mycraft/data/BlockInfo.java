package com.cbapps.javafx.mycraft.data;

public record BlockInfo(
        int chunkX,
        int chunkY,
        int chunkZ,
        int id) {

    public String toPositionString() {
        return String.format("x=%d, y=%d, z= %d", chunkX, chunkY, chunkZ);
    }
}
