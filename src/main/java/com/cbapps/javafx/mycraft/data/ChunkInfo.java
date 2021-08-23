package com.cbapps.javafx.mycraft.data;

public record ChunkInfo(
        int worldX,
        int worldY,
        int worldZ) {

    public String toPositionString() {
        return String.format("x=%d, y=%d, z= %d", worldX, worldY, worldZ);
    }
}
