import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

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
		this.blockData = map;
		TriangleMesh mesh = new TriangleMesh();
		setMesh(mesh);
		CompletableFuture.runAsync(() -> build(mesh));
	}

	private void build(TriangleMesh mesh) {
		mesh.getTexCoords().setAll(TEX_COORDS);
		float minX = -(blockWidth * dataWidth / 2);
		float minZ = -(blockDepth * dataDepth / 2);

		System.out.println("Creating points [" + (dataWidth + 1) * (dataDepth + 1) + "]");
		for (int z = 0; z <= dataDepth; z++) {
			for (int x = 0; x <= dataWidth; x++) {
				mesh.getPoints().addAll(
						minX + x * blockWidth, 0, minZ + z * blockDepth
				);
			}
		}

		System.out.println("Creating faces...");
		for (int blockZ = 0; blockZ < dataDepth; blockZ++) {
			for (int blockX = 0; blockX < dataWidth; blockX++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int blockState = blockData.get(blockZ * dataWidth + blockX);
				if (blockState == 1) {
					System.out.println("  Plane");
					int tl = blockZ * dataWidth + blockX + blockZ;
					int tr = blockZ * dataWidth + blockX + blockZ + 1;
					int bl = (blockZ + 1) * dataWidth + blockX + (blockZ + 1);
					int br = (blockZ + 1) * dataWidth + blockX + (blockZ + 1) + 1;
					mesh.getFaces().addAll(
							tr, tl, bl,
							tr, bl, br
					);
				} else {
					System.out.println("  Gap");
				}
			}
		}
		System.out.println("Done creating faces");
	}

	public static Terrain generateRandom(float blockWidth, float blockDepth) {
		Terrain terrain = new Terrain(blockWidth, blockDepth, 3, 3,
				Arrays.asList(1,0,0,0,1,0,0,0,1)
				//Arrays.asList(2,1,1,1,2,1,1,1,2)
		);
		return terrain;
	}
}
