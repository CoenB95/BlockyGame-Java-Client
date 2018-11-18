package com.cbapps.javafx.mycraft;

import com.cbapps.javafx.gamo.components.FollowComponent;
import com.cbapps.javafx.gamo.math.Position;
import com.cbapps.javafx.gamo.objects.GameObjectBase;
import com.cbapps.javafx.gamo.scenes.GameScene;
import com.cbapps.javafx.mycraft.components.FloatingComponent;
import com.cbapps.javafx.mycraft.data.FaceUtils;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class Terrain extends GameObjectBase {
	private ChunkView meshView;
	private List<Integer> blockData;
	private List<Block> blocks;
	private FaceUtils faceMap;
	private boolean embedded = false;

	public Terrain(GameScene scene, float blockWidth, float blockDepth, float blockHeight,
				   int dataWidth, int dataDepth, int dataHeight, List<Integer> map) {
		this.faceMap = new FaceUtils();

		meshView = new ChunkView(this);
		meshView.setMaterial(new PhongMaterial(Color.WHITE,
				new Image("/cube.png", 100, 100, true, false),
				null, null, null));

		blocks = new ArrayList<>();
		blockData = map;

		for (int blockY = 0; blockY < dataHeight; blockY++) {
			for (int blockZ = 0; blockZ < dataDepth; blockZ++) {
				for (int blockX = 0; blockX < dataWidth; blockX++) {
					int blockId = blockY * dataWidth * dataDepth + blockZ * dataWidth + blockX;
					int blockState = blockData.get(blockId);
					Block block = new Block(blockWidth, blockHeight, blockDepth, blockState);
					block.setTargetPosition(new Position(blockX * blockWidth, blockY * blockHeight, blockZ * blockDepth));
					blocks.add(block);
				}
			}
		}

		if (embedded)
			buildEmbeddedBlocks();
		else
			buildStandaloneBlocks(scene, blockHeight);
	}

	private void buildStandaloneBlocks(GameScene scene, float blockHeight) {
		blocks.forEach(b -> {
			b.buildStandalone();
			b.addComponent(FollowComponent.translating(this, b.getTargetPosition()));
			b.addComponent(new FloatingComponent(Math.random() * blockHeight));
		});
		scene.add3DObjects(blocks);
	}

	private void buildEmbeddedBlocks() {
		TriangleMesh mesh  = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);
		blocks.forEach(b -> b.buildEmbedded(mesh));
		meshView.setMesh(mesh);
		setNode(meshView);
	}

	public int findBlockByFace(int face) {
		return faceMap.getBlockByFace((int) Math.floor(face / 2.0));
	}

	private int lastMarked = -1;

	public void markBlock(int blockId) {
		if (lastMarked >= 0) {
			FaceUtils.adjustFace(((TriangleMesh) meshView.getMesh()).getFaces(), faceMap.getFace(lastMarked, FaceUtils.Side.TOP),
					11, 10, 0,
					11, 0, 1);
			FaceUtils.adjustFace(((TriangleMesh) meshView.getMesh()).getFaces(), faceMap.getFace(lastMarked, FaceUtils.Side.LEFT),
					0, 4,5,
					0, 5,3);
		}
		FaceUtils.adjustFace(((TriangleMesh) meshView.getMesh()).getFaces(), faceMap.getFace(blockId, FaceUtils.Side.TOP),
				6, 1, 2,
				6, 2, 7);
		FaceUtils.adjustFace(((TriangleMesh) meshView.getMesh()).getFaces(), faceMap.getFace(blockId, FaceUtils.Side.LEFT),
				6, 1, 2,
				6, 2, 7);
		lastMarked = blockId;
	}

	public static Terrain generateRandom(GameScene scene,float blockSize, int gridWidth, int gridDepth, int gridHeight) {
		List<Integer> blockData = new ArrayList<>(gridWidth * gridDepth);
		for (int i = 0; i < gridWidth * gridDepth * gridHeight; i++)
			blockData.add(i < gridWidth * gridDepth ? 1 : Math.random() > 0.8 ? 1 : 0);
		return new Terrain(scene, blockSize, blockSize, blockSize, gridWidth, gridDepth, gridHeight, blockData);
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		super.onUpdate(elapsedSeconds);
		if (embedded) {
			blocks.forEach(b -> b.onUpdate(elapsedSeconds));
			TriangleMesh mesh = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);
			CompletableFuture.runAsync(() -> blocks.forEach(block -> block.buildEmbedded(mesh)))
					.thenAccept((e) -> Platform.runLater(() -> meshView.setMesh(mesh)));
		}
	}
}
