package mycraft;

import javafx.scene.Group;
import javafx.scene.shape.VertexFormat;
import mycraft.data.FaceUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import mycraft.gameobject.GameObject;
import mycraft.gameobject.GameObjectBase;
import mycraft.gameobject.GameScene;
import mycraft.math.Position;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static mycraft.data.FaceUtils.*;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class Terrain extends GameObjectBase {
	private ChunkView meshView;
	private List<Integer> blockData;
	private List<Block> blocks;
	private FaceUtils faceMap;
	private float blockWidth;
	private float blockHeight;
	private float blockDepth;
	private int dataWidth;
	private int dataHeight;
	private int dataDepth;

	private static float TEX_COORDS[] = {
			//Base-Front
			0.25f,	0.25f,	// 0
			0.5f,	0.25f,	// 1
			0.5f,	0.5f,	// 2
			0.25f,	0.5f,	// 3
			//Left
			0.0f,	0.25f,	// 4
			0.0f,	0.5f,	// 5
			//Right
			0.75f,	0.25f,	// 6
			0.75f,	0.5f,	// 7
			//Back
			1.0f,	0.25f,	// 8
			1.0f,	0.5f,	// 9
			//Top
			0.25f,	0.0f,	// 10
			0.5f,	0.0f	// 11
	};

	public Terrain(GameScene scene, float blockWidth, float blockDepth, float blockHeight,
				   int dataWidth, int dataDepth, int dataHeight, List<Integer> map) {
		this.dataWidth = dataWidth;
		this.dataHeight = dataHeight;
		this.dataDepth = dataDepth;
		this.blockWidth = blockWidth;
		this.blockHeight = blockHeight;
		this.blockDepth = blockDepth;
		this.faceMap = new FaceUtils();

		meshView = new ChunkView(this);
		meshView.setMaterial(new PhongMaterial(Color.WHITE,
				new Image("/cube.png", 100, 100, true, false),
				null, null, null));
		//setNode(meshView);

		//setBlockData(map);
		blocks = new ArrayList<>();
		for (int blockY = 0; blockY < dataHeight; blockY++) {
			for (int blockZ = 0; blockZ < dataDepth; blockZ++) {
				for (int blockX = 0; blockX < dataWidth; blockX++) {
					int blockId = blockY * dataWidth * dataDepth + blockZ * dataWidth + blockX;
					int blockState = map.get(blockId);
					Block block = new Block(blockWidth, blockHeight, blockDepth, blockState);
					//block.setPosition(new Position(blockX, blockY, blockZ));
					block.setPosition(new Position(blockX * blockWidth, blockY * blockHeight, blockZ * blockDepth));
					blocks.add(block);
					block.buildStandalone();
				}
			}
		}
		scene.add3DObjects(blocks);
		//build(new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD));
	}

	private void build(TriangleMesh mesh) {
		/*try {
			mesh.getTexCoords().setAll(TEX_COORDS);
			float minX = -(blockWidth * dataWidth / 2);
			float minY = blockWidth / 2;
			float minZ = -(blockDepth * dataDepth / 2);

			//To remember, depth INCREASES when further away, and decreases when coming nearer.
			System.out.println("Creating points [" + (dataWidth + 1) * (dataDepth + 1) * (dataHeight + 1) + "]");
			for (int y = 0; y <= dataHeight; y++) {
				for (int z = 0; z <= dataDepth; z++) {
					for (int x = 0; x <= dataWidth; x++) {
						mesh.getPoints().addAll(
								minX + x * blockWidth, minY + -(y * blockWidth), -(minZ + z * blockDepth)
						);
					}
				}
			}

			System.out.println("Creating faces...");
			int pointsPerLevel = (dataWidth + 1) * (dataDepth + 1);
			int pointsWidth = (dataWidth + 1);
			for (int blockY = 0; blockY < dataHeight; blockY++) {
				for (int blockZ = 0; blockZ < dataDepth; blockZ++) {
					for (int blockX = 0; blockX < dataWidth; blockX++) {
//						try {
//							Thread.sleep(20);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						int blockId = blockY * dataWidth * dataDepth + blockZ * dataWidth + blockX;
						int blockState = blockData.get(blockId);
						if (blockState == 0) {
							//System.out.println("  Gap");
						} else {
							// ltb = left, top, back (x=0, y=0, z=0)
							// rbf = right, bottom, front (x=1, y=1, z=1)
							int ltb = (blockY + 1) * pointsPerLevel + blockZ * pointsWidth + blockX;
							int rtb = (blockY + 1) * pointsPerLevel + blockZ * pointsWidth + blockX + 1;
							int ltf = (blockY + 1) * pointsPerLevel + (blockZ + 1) * pointsWidth + blockX;
							int rtf = (blockY + 1) * pointsPerLevel + (blockZ + 1) * pointsWidth + blockX + 1;
							int lbb = blockY * pointsPerLevel + blockZ * pointsWidth + blockX;
							int lbf = blockY * pointsPerLevel + (blockZ + 1) * pointsWidth + blockX;
							int rbb = blockY * pointsPerLevel + blockZ * pointsWidth + blockX + 1;
							int rbf = blockY * pointsPerLevel + (blockZ + 1) * pointsWidth + blockX + 1;

							if (blockState == 1) {
								int topBlockIndex = (blockY + 1) * dataWidth * dataDepth + blockZ * dataWidth + blockX;
								if (topBlockIndex >= 0 && (topBlockIndex >= blockData.size() ||
										blockData.get(topBlockIndex) == 0)) {
									mesh.getFaces().addAll(
											rtb, 11, ltb, 10, ltf, 0,
											rtb, 11, ltf, 0, rtf, 1
									);
									mesh.getFaceSmoothingGroups().addAll(0, 0);
									faceMap.addFace(blockId, Side.TOP);
								}
								int leftBlockIndex = blockX == 0 ? -1 :
										blockY * dataWidth * dataDepth + blockZ * dataWidth + blockX - 1;
								if (leftBlockIndex >= 0 && leftBlockIndex < blockData.size() &&
										blockData.get(leftBlockIndex) == 0) {
									mesh.getFaces().addAll(
											ltf, 0, ltb, 4, lbb, 5,
											ltf, 0, lbb, 5, lbf, 3
									);
									mesh.getFaceSmoothingGroups().addAll(0, 0);
									faceMap.addFace(blockId, Side.LEFT);
								}
								int frontBlockIndex = blockZ == dataDepth - 1 ? -1 :
										blockY * dataWidth * dataDepth + (blockZ + 1) * dataWidth + blockX;
								if (frontBlockIndex >= 0 && frontBlockIndex < blockData.size() &&
										blockData.get(frontBlockIndex) == 0) {
									mesh.getFaces().addAll(
											rtf, 1, ltf, 0, lbf, 3,
											rtf, 1, lbf, 3, rbf, 2
									);
									mesh.getFaceSmoothingGroups().addAll(0, 0);
									faceMap.addFace(blockId, Side.FRONT);
								}
								int rightBlockIndex = blockX == dataWidth - 1 ? -1 :
										blockY * dataWidth * dataDepth + blockZ * dataWidth + blockX + 1;
								if (rightBlockIndex >= 0 && rightBlockIndex < blockData.size() &&
										blockData.get(rightBlockIndex) == 0) {
									mesh.getFaces().addAll(
											rtb, 6, rtf, 1, rbf, 2,
											rtb, 6, rbf, 2, rbb, 7
									);
									mesh.getFaceSmoothingGroups().addAll(0, 0);
									faceMap.addFace(blockId, Side.RIGHT);
								}
							} else {
								System.out.println("  Unknown, make gap");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		/*for (int blockY = 0; blockY < dataHeight; blockY++) {
			for (int blockZ = 0; blockZ < dataDepth; blockZ++) {
				for (int blockX = 0; blockX < dataWidth; blockX++) {
					int blockId = blockY * dataWidth * dataDepth + blockZ * dataWidth + blockX;
					int blockState = blockData.get(blockId);
					Block block = new Block(blockWidth, blockHeight, blockDepth);
					block.setPosition(new Position(blockX, blockY, blockZ));
					if (blockState != 0)
						block.buildEmbedded(mesh);
				}
			}
		}*/

		blocks.forEach(block -> block.buildEmbedded(mesh));

		//System.out.println("Done creating faces");
		//meshView.setMesh(null);
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		meshView.setMesh(mesh);
	}

	public int findBlockByFace(int face) {
		return faceMap.getBlockByFace((int) Math.floor(face / 2.0));
	}

	private int lastMarked = -1;

	public void markBlock(int blockId) {
		if (lastMarked >= 0) {
			FaceUtils.adjustFace(((TriangleMesh) meshView.getMesh()).getFaces(), faceMap.getFace(lastMarked, Side.TOP),
					11, 10, 0,
					11, 0, 1);
			FaceUtils.adjustFace(((TriangleMesh) meshView.getMesh()).getFaces(), faceMap.getFace(lastMarked, Side.LEFT),
					0, 4,5,
					0, 5,3);
		}
		FaceUtils.adjustFace(((TriangleMesh) meshView.getMesh()).getFaces(), faceMap.getFace(blockId, Side.TOP),
				6, 1, 2,
				6, 2, 7);
		FaceUtils.adjustFace(((TriangleMesh) meshView.getMesh()).getFaces(), faceMap.getFace(blockId, Side.LEFT),
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

	public List<Integer> getBlockData() {
		return blockData;
	}

	public void setBlockData(List<Integer> blockData) {
		this.blockData = blockData;
		TriangleMesh mesh = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);
		//setMesh(mesh);
		CompletableFuture.runAsync(() -> build(mesh));
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		super.onUpdate(elapsedSeconds);
		//blocks.forEach(b -> b.onUpdate(elapsedSeconds));
		//TriangleMesh mesh = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);
		//CompletableFuture.runAsync(() -> blocks.forEach(block -> block.buildEmbedded(mesh)))
		//		.thenAccept((e) -> meshView.setMesh(mesh));
	}
}
