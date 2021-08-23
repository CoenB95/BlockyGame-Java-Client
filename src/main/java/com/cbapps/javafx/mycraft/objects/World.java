package com.cbapps.javafx.mycraft.objects;

import com.cbapps.gamo.javafx.GameObjectBase;
import com.cbapps.gamo.math.FastNoiseLite;
import com.cbapps.gamo.math.Vector3;
import com.cbapps.javafx.mycraft.data.BlockInfo;
import com.cbapps.javafx.mycraft.data.ChunkInfo;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class World extends GameObjectBase {
    private final IntegerProperty depth;
    private final IntegerProperty height;
    private final IntegerProperty width;
    private final int chunkDepth;
    private final int chunkHeight;
    private final int chunkWidth;
    private final float blockDepth;
    private final float blockHeight;
    private final float blockWidth;

    private final List<Chunk> chunks;

    public World() {
        this(3, 3, 3);
    }

    public World(int width, int height, int depth) {
        this(width, height, depth, 16, 16, 16);
    }

    public World(int width, int height, int depth, int chunkWidth, int chunkHeight, int chunkDepth) {
        this(width, height, depth, chunkWidth, chunkHeight, chunkDepth, 1.0f, 1.0f, 1.0f);
    }

    public World(int width, int height, int depth, int chunkWidth, int chunkHeight, int chunkDepth,
                 float blockWidth, float blockHeight, float blockDepth) {
        super(null);

        this.depth = new SimpleIntegerProperty(depth);
        this.height = new SimpleIntegerProperty(height);
        this.width = new SimpleIntegerProperty(width);
        this.chunkDepth = chunkDepth;
        this.chunkHeight = chunkHeight;
        this.chunkWidth = chunkWidth;
        this.blockDepth = blockDepth;
        this.blockHeight = blockHeight;
        this.blockWidth = blockWidth;
        this.chunks = new ArrayList<>();

        fillArray();
    }

    private void fillArray() {
        final var worldDepth = depth.get();
        final var worldHeight = height.get();
        final var worldWidth = width.get();
        final var size = worldDepth * worldWidth * worldHeight;

        chunks.clear();

        for (int y = 0; y < worldHeight; y++) {
            for (int z = 0; z < worldDepth; z++) {
                for (int x = 0; x < worldWidth; x++) {
                    var chunk = new Chunk(chunkWidth, chunkHeight, chunkDepth, blockWidth, blockHeight, blockDepth);
                    chunk.setPosition(new Vector3(
                            x * blockWidth * chunkWidth,
                            y * blockHeight * chunkHeight,
                            z * blockDepth * chunkDepth));
                    chunks.add(chunk);
                    addObject(chunk);
                }
            }
        }
    }

    public CompletableFuture<Void> generateAsync(int seed) {
        return CompletableFuture.runAsync(() -> {
            final var worldDepth = depth.get();
            final var worldHeight = height.get();
            final var worldWidth = width.get();

            for (int y = 0; y < worldHeight; y++) {
                for (int z = 0; z < worldDepth; z++) {
                    for (int x = 0; x < worldWidth; x++) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.format("Generating chunk %d %d %d%n", x, y, z);
                        final var index = x + z * worldDepth + y * worldWidth * worldDepth;
                        var chunk = chunks.get(index);
                        chunk.generate(seed);
                        Platform.runLater(chunk::build);
                    }
                }
            }
        });
    }

    public Optional<BlockInfo> getBlockInfo(Vector3 positionInWorld) {
        return getChunk(positionInWorld).flatMap(chunk -> chunk.getBlockInfo(positionInWorld.subtract(chunk.getPosition())));
    }

    public Optional<ChunkInfo> getChunkInfo(Vector3 positionInWorld) {
        final var worldDepth = depth.get();
        final var worldHeight = height.get();
        final var worldWidth = width.get();

        int x = (int) Math.floor(positionInWorld.x() / blockWidth / chunkWidth);
        int y = (int) Math.floor(positionInWorld.y() / blockHeight / chunkHeight);
        int z = (int) Math.floor(positionInWorld.z() / blockDepth / chunkDepth);

        if (x < 0 || x >= worldWidth ||
                y < 0 || y >= worldHeight ||
                z < 0 || z >= worldDepth) {
            return Optional.empty();
        }

        return Optional.of(new ChunkInfo(x, y, z));
    }

    private Optional<Chunk> getChunk(Vector3 positionInWorld) {
        return getChunkInfo(positionInWorld).map(chunkInfo -> {
            final var worldDepth = depth.get();
            final var worldWidth = width.get();

            return chunks.get(chunkInfo.worldX() + chunkInfo.worldZ() * worldDepth + chunkInfo.worldY() * worldWidth * worldDepth);
        });
    }
}
