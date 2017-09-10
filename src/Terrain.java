import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class Terrain extends MeshView {

	private List<Integer> blockData;
	private float blockWidth;
	private float blockDepth;
	private int dataWidth;
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

	private Terrain(float blockWidth, float blockDepth, int dataWidth, int dataDepth, List<Integer> map) {
		this.dataWidth = dataWidth;
		this.dataDepth = dataDepth;
		this.blockWidth = blockWidth;
		this.blockDepth = blockDepth;
		this.blockData = map;
		TriangleMesh mesh = new TriangleMesh();
		setMesh(mesh);
		setMaterial(new PhongMaterial(Color.WHITE, new Image("/cube.png"), null, null, null));
		CompletableFuture.runAsync(() -> build(mesh));
	}

	private void build(TriangleMesh mesh) {
		mesh.getTexCoords().setAll(TEX_COORDS);
		float minX = -(blockWidth * dataWidth / 2);
		float minZ = -(blockDepth * dataDepth / 2);

		//To remember, depth INCREASES when further away, and decreases when coming nearer.
		System.out.println("Creating points [" + (dataWidth + 1) * (dataDepth + 1) + "]");
		for (int z = 0; z <= dataDepth; z++) {
			for (int x = 0; x <= dataWidth; x++) {
				mesh.getPoints().addAll(
						minX + x * blockWidth, 0, -(minZ + z * blockDepth)
				);
			}
		}

		System.out.println("Creating faces...");
		for (int blockZ = 0; blockZ < dataDepth; blockZ++) {
			for (int blockX = 0; blockX < dataWidth; blockX++) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int blockState = blockData.get(blockZ * dataWidth + blockX);
				if (blockState == 0) {
					System.out.println("  Gap");
				} else {
					// ltb = left, top, back (x=0, y=0, z=0)
					// rbf = right, bottom, front (x=1, y=1, z=1)
					int ltb = blockZ * dataWidth + blockX + blockZ;
					int rtb = blockZ * dataWidth + blockX + blockZ + 1;
					int ltf = (blockZ + 1) * dataWidth + blockX + (blockZ + 1);
					int rtf = (blockZ + 1) * dataWidth + blockX + (blockZ + 1) + 1;

					if (blockState == 1) {
						System.out.println("  Plane (dataNr=" + (blockZ * dataWidth + blockX) +
								", pointNrLtb=" + (blockZ * dataWidth + blockX + blockZ));

						mesh.getFaces().addAll(
								rtb, 11, ltb, 10, ltf, 0,
								rtb, 11, ltf, 0, rtf, 1
						);
					} else {
						System.out.println("  Unknown, make gap");
					}
				}
			}
		}
		System.out.println("Done creating faces");
	}

	public static Terrain generateRandom(float blockWidth, float blockDepth, int gridWidth, int gridDepth) {
		List<Integer> blockData = new ArrayList<>(gridWidth * gridDepth);
		for (int i = 0; i < gridWidth * gridDepth; i++)
			blockData.add((int) Math.round(Math.random()));
		Terrain terrain = new Terrain(blockWidth, blockDepth, gridWidth, gridDepth, blockData);
		return terrain;
	}
}
