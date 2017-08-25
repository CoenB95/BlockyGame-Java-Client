import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class Block extends MeshView {

	public Block() {

	}

	private void createMesh(float w, float h, float d) {
		float hw = w / 2f;
		float hh = h / 2f;
		float hd = d / 2f;

		float points[] = {
				-hw, -hh, -hd,
				hw, -hh, -hd,
				hw,  hh, -hd,
				-hw,  hh, -hd,
				-hw, -hh,  hd,
				hw, -hh,  hd,
				hw,  hh,  hd,
				-hw,  hh,  hd};

		float texCoords[] = {
				0, 0.25f,
				0.25f, 0.25f,
				0.5f, 0.25f,
				0.75f, 0.25f,
				0.25f, 0,
				0.25f, 0.5f
		};

		int faces[] = {
				0, 0, 2, 2, 1, 1,
				2, 2, 0, 0, 3, 3,
				1, 0, 6, 2, 5, 1,
				6, 2, 1, 0, 2, 3,
				5, 0, 7, 2, 4, 1,
				7, 2, 5, 0, 6, 3,
				4, 0, 3, 2, 0, 1,
				3, 2, 4, 0, 7, 3,
				3, 0, 6, 2, 2, 1,
				6, 2, 3, 0, 7, 3,
				4, 0, 1, 2, 5, 1,
				1, 2, 4, 0, 0, 3,
		};

		TriangleMesh mesh = new TriangleMesh();
		mesh.getPoints().setAll(points);
		mesh.getTexCoords().setAll(texCoords);
		mesh.getFaces().setAll(faces);
	}
}
