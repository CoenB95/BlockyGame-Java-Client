import data.FaceUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static data.FaceUtils.*;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class Terrain extends MeshView {

	private List<Integer> blockData;
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

	public Terrain(float blockWidth, float blockDepth, float blockHeight,
				   int dataWidth, int dataDepth, int dataHeight, List<Integer> map) {
		this.dataWidth = dataWidth;
		this.dataHeight = dataHeight;
		this.dataDepth = dataDepth;
		this.blockWidth = blockWidth;
		this.blockHeight = blockHeight;
		this.blockDepth = blockDepth;
		this.faceMap = new FaceUtils();//FXCollections.observableList(new ArrayList<>());
		setMaterial(new PhongMaterial(Color.WHITE, new Image("/cube.png", 100, 100, true, false), null, null, null));
		setBlockData(map);
	}

	private void build(TriangleMesh mesh) {
		try {
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
			int blocksPerLevel = (dataWidth + 1) * (dataDepth + 1);
			for (int blockY = 0; blockY < dataHeight; blockY++) {
				for (int blockZ = 0; blockZ < dataDepth; blockZ++) {
					for (int blockX = 0; blockX < dataWidth; blockX++) {
//						try {
//							Thread.sleep(10);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						int blockId = blockZ * dataWidth + blockX;
						int blockState = blockData.get(blockId);
						if (blockState == 0) {
							System.out.println("  Gap");
						} else {
							// ltb = left, top, back (x=0, y=0, z=0)
							// rbf = right, bottom, front (x=1, y=1, z=1)
							int ltb = (blockY + 1) * blocksPerLevel +(blockZ * dataWidth + blockX + blockZ);
							int rtb = (blockY + 1) * blocksPerLevel + (blockZ * dataWidth + blockX + blockZ + 1);
							int ltf = (blockY + 1) * blocksPerLevel + ((blockZ + 1) * dataWidth + blockX + blockZ + 1);
							int rtf = (blockY + 1) * blocksPerLevel + ((blockZ + 1) * dataWidth + blockX + blockZ + 2);
							int lbb = blockY * blocksPerLevel + (blockZ * dataWidth + blockX + blockZ);
							int lbf = blockY * blocksPerLevel + ((blockZ + 1) * dataWidth + blockX + blockZ + 1);

							if (blockState == 1) {
								System.out.println("  Plane (dataNr=" + (blockZ * dataWidth + blockX) +
										", pointNrLtb=" + (blockZ * dataWidth + blockX + blockZ));

								mesh.getFaces().addAll(
										rtb, 11, ltb, 10, ltf, 0,
										rtb, 11, ltf, 0, rtf, 1
								);
								mesh.getFaceSmoothingGroups().addAll(0, 0);
								faceMap.addFace(blockId, Side.TOP);
								int leftBlockIndex = blockZ * dataWidth + blockX - 1;
								if (leftBlockIndex < 0 || blockData.get(leftBlockIndex) == 1) {
									System.out.println("    Left side skipped!");
								} else {
									mesh.getFaces().addAll(
											ltf, 0, ltb, 4, lbb, 5,
											ltf, 0, lbb, 5, lbf, 3
									);
									mesh.getFaceSmoothingGroups().addAll(0, 0);
									faceMap.addFace(blockId, Side.LEFT);
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
		}
		System.out.println("Done creating faces");
	}

	public int findBlockByFace(int face) {
		return faceMap.getBlockByFace((int) Math.floor(face / 2));
	}

	private int lastMarked = -1;

	public void markBlock(int blockId) {
		if (lastMarked >= 0) {
			FaceUtils.adjustFace(((TriangleMesh) getMesh()).getFaces(), faceMap.getFace(lastMarked, Side.TOP),
					11, 10, 0,
					11, 0, 11);
			FaceUtils.adjustFace(((TriangleMesh) getMesh()).getFaces(), faceMap.getFace(lastMarked, Side.LEFT),
					0, 4,5,
					0, 5,3);
		}
		FaceUtils.adjustFace(((TriangleMesh) getMesh()).getFaces(), faceMap.getFace(blockId, Side.TOP),
				6, 1, 2,
				6, 2, 7);
		FaceUtils.adjustFace(((TriangleMesh) getMesh()).getFaces(), faceMap.getFace(blockId, Side.LEFT),
				6, 1, 2,
				6, 2, 7);
		lastMarked = blockId;
	}

	public static Terrain generateRandom(float blockSize, int gridWidth, int gridDepth, int gridHeight) {
		List<Integer> blockData = new ArrayList<>(gridWidth * gridDepth);
		for (int i = 0; i < gridWidth * gridDepth; i++)
			blockData.add((int) Math.round(Math.random()));
		return new Terrain(blockSize, blockSize, blockSize, gridWidth, gridDepth, gridHeight, blockData);
	}

//	public static Terrain generateFull(float blockWidth, float blockDepth, int gridWidth, int gridDepth) {
//		List<Integer> blockData = new ArrayList<>(gridWidth * gridDepth);
//		for (int i = 0; i < gridWidth * gridDepth; i++)
//			blockData.add(1);
//		return new Terrain(blockWidth, blockDepth, gridWidth, gridDepth, blockData);
//	}

	public List<Integer> getBlockData() {
		return blockData;
	}

	public void setBlockData(List<Integer> blockData) {
		this.blockData = blockData;
		TriangleMesh mesh = new TriangleMesh();
		setMesh(mesh);
		CompletableFuture.runAsync(() -> build(mesh));
	}
}
