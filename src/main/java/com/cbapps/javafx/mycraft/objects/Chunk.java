package com.cbapps.javafx.mycraft.objects;

import com.cbapps.gamo.javafx.CubeObject;
import com.cbapps.gamo.javafx.GameObjectBase;
import com.cbapps.gamo.javafx.TextObject;
import com.cbapps.gamo.math.FastNoiseLite;
import com.cbapps.gamo.math.Vector3;
import com.cbapps.javafx.mycraft.Blocks;
import com.cbapps.javafx.mycraft.data.BlockInfo;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

import java.util.Optional;
import java.util.Random;

public class Chunk extends GameObjectBase {
    private final int[] blocks;
    private final float blockDepth;
    private final float blockHeight;
    private final float blockWidth;
    private final int depth;
    private final int height;
    private final int width;

    private final CubeObject debugOutline;
    private final TextObject debugText;
    private final MeshView meshView;

    public Chunk() {
        this(new MeshView());
    }

    public Chunk(int width, int height, int depth, float blockWidth, float blockHeight, float blockDepth) {
        this(width, height, depth, blockWidth, blockHeight, blockDepth, new MeshView());
    }

    private Chunk(MeshView meshView) {
        this(16, 16, 16, 1.0f, 1.0f, 1.0f, meshView);
    }

    private Chunk(int width, int height, int depth, float blockWidth, float blockHeight, float blockDepth, MeshView meshView) {
        super(meshView);

        this.blocks = new int[width * height * depth];
        this.blockDepth = blockDepth;
        this.blockHeight = blockHeight;
        this.blockWidth = blockWidth;
        this.depth = depth;
        this.height = height;
        this.meshView = meshView;
        this.width = width;

        this.meshView.setMaterial(new PhongMaterial(Color.WHITE,
                new Image("/cube.png", 500, 500, true, false),
                null, null, null));

        this.debugOutline = CubeObject.of(width * blockWidth, height * blockHeight, depth * blockDepth, Color.ORANGE);
        this.debugOutline.setPosition(new Vector3(width * blockWidth / 2, height * blockHeight / 2, depth * blockDepth / 2));
        this.debugOutline.wireframe(true);

        this.debugText = new TextObject();
        this.debugText.setScale(0.01);

        addObject(debugOutline);
        addObject(debugText);
    }

    public void build() {
        var mesh = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);

        for (int blockY = 0; blockY < height; blockY++) {
            for (int blockZ = 0; blockZ < depth; blockZ++) {
                for (int blockX = 0; blockX < width; blockX++) {
                    var blockPos = new Vector3(blockX, blockY, blockZ);
                    int blockId = getBlockId(blockPos);

                    if (blockId == Blocks.AIR.getId()) {
                        continue;
                    }

                    int rightId = getBlockId(blockPos.add(Vector3.unitX()));
                    int leftId = getBlockId(blockPos.subtract(Vector3.unitX()));
                    int topId = getBlockId(blockPos.add(Vector3.unitY()));
                    int bottomId = getBlockId(blockPos.subtract(Vector3.unitY()));
                    int frontId = getBlockId(blockPos.add(Vector3.unitZ()));
                    int backId = getBlockId(blockPos.subtract(Vector3.unitZ()));
                    var sideIds = new int[]{topId, leftId, frontId, rightId, backId, bottomId};

                    float right = blockX * blockWidth + blockWidth;
                    float left = blockX * blockWidth;
                    float top = blockY * -blockHeight - blockHeight;
                    float bottom = blockY * -blockHeight;
                    float back = blockZ * -blockDepth;
                    float front = blockZ * -blockDepth - blockDepth;

                    int pointsOffset = mesh.getPoints().size() / 3;
                    int normalsOffset = mesh.getNormals().size() / 3;
                    int texOffset = mesh.getTexCoords().size() / 2;

                    mesh.getPoints().addAll(
                            //Top
                            right, top, back,
                            left, top, back,
                            left, top, front,
                            right, top, front,
                            //Left
                            left, top, front,
                            left, top, back,
                            left, bottom, back,
                            left, bottom, front,
                            //Front
                            right, top, front,
                            left, top, front,
                            left, bottom, front,
                            right, bottom, front,
                            //Right
                            right, top, back,
                            right, top, front,
                            right, bottom, front,
                            right, bottom, back,
                            //Back
                            left, top, back,
                            right, top, back,
                            right, bottom, back,
                            left, bottom, back,
                            //Bottom
                            right, bottom, front,
                            left, bottom, front,
                            left, bottom, back,
                            right, bottom, back
                    );

                    mesh.getNormals().addAll(
                            0, -1, 0, //Top
                            -1, 0, 0, //Left
                            0, 0, -1, //Front
                            1, 0, 0, //Right
                            0, 0, 1, //Back
                            0, 1, 0  //Bottom
                    );

                    mesh.getTexCoords().addAll(
                            //Top
                            0.50f, 0.00f,
                            0.25f, 0.00f,
                            0.25f, 0.25f,
                            0.50f, 0.25f,
                            //Left
                            0.25f, 0.25f,
                            0.00f, 0.25f,
                            0.00f, 0.50f,
                            0.25f, 0.50f,
                            //Front
                            0.50f, 0.25f,
                            0.25f, 0.25f,
                            0.25f, 0.50f,
                            0.50f, 0.50f,
                            //Right
                            0.75f, 0.25f,
                            0.50f, 0.25f,
                            0.50f, 0.50f,
                            0.75f, 0.50f,
                            //Back
                            1.00f, 0.25f,
                            0.75f, 0.25f,
                            0.75f, 0.50f,
                            1.00f, 0.50f,
                            //Front
                            0.50f, 0.50f,
                            0.25f, 0.50f,
                            0.25f, 0.75f,
                            0.50f, 0.75f
                    );

                    for (int side = 0; side < 6; side++) {
                        if (sideIds[side] > 0) {
                            continue;
                        }

                        mesh.getFaces().addAll(pointsOffset + side * 4, normalsOffset + side, texOffset + side * 4);
                        mesh.getFaces().addAll(pointsOffset + side * 4 + 1, normalsOffset + side, texOffset + side * 4 + 1);
                        mesh.getFaces().addAll(pointsOffset + side * 4 + 2, normalsOffset + side, texOffset + side * 4 + 2);
                        mesh.getFaces().addAll(pointsOffset + side * 4, normalsOffset + side, texOffset + side * 4);
                        mesh.getFaces().addAll(pointsOffset + side * 4 + 2, normalsOffset + side, texOffset + side * 4 + 2);
                        mesh.getFaces().addAll(pointsOffset + side * 4 + 3, normalsOffset + side, texOffset + side * 4 + 3);
                    }
                }
            }
        }

        meshView.setMesh(mesh);
    }

    public void generate(int seed) {
        var noise = new FastNoiseLite(seed);

        for (int blockY = 0; blockY < height; blockY++) {
            for (int blockZ = 0; blockZ < depth; blockZ++) {
                for (int blockX = 0; blockX < width; blockX++) {
                    var pos = getPosition();
                    var n = noise.GetNoise((float) (pos.x() + blockX), (float) (pos.y() + blockY), (float) (pos.z() + blockZ));
                    var blockId = Blocks.AIR.getId();

                    if (n > 0.5f) {
                        blockId = Blocks.DIRT.getId();
                    }

                    blocks[blockX + blockZ * depth + blockY * width * depth] = blockId;
                }
            }
        }

        build();
    }

    private int getBlockId(Vector3 positionInChunk) {
        return getBlockInfo(positionInChunk).map(BlockInfo::id).orElse(-1);
    }

    public Optional<BlockInfo> getBlockInfo(Vector3 positionInChunk) {
        int x = (int) Math.floor(positionInChunk.x() / blockWidth);
        int y = (int) Math.floor(positionInChunk.y() / blockHeight);
        int z = (int) Math.floor(positionInChunk.z() / blockDepth);

        if (x < 0 || x >= width ||
                y < 0 || y >= height ||
                z < 0 || z >= depth) {
            return Optional.empty();
        }

        final var blockId = blocks[x + z * depth + y * width * depth];
        return Optional.of(new BlockInfo(x, y, z, blockId));
    }

    @Override
    public void onUpdate(double elapsedSeconds) {
        super.onUpdate(elapsedSeconds);

        var pos = getPosition();
        debugText.textProperty().set(String.format("Corner of Chunk: %.0f, %.0f, %.0f", pos.x(), pos.y(), pos.z()));
    }
}
