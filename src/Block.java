import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class Block extends MeshView {

	public Block(double w, double h, double d) {
		super(createMesh((float) w, (float) h, (float) d));
	}

	private static TriangleMesh createMesh(float w, float h, float d) {
		float hw = w / 2f;
		float hh = h / 2f;
		float hd = d / 2f;

		float points[] = {
				//Base-Front
				-hw, -hh, -hd,
				hw, -hh, -hd,
				hw, hh, -hd,
				-hw, hh, -hd,
				//Left
				-hw, -hh, hd,
				-hw, hh, hd,
				//Right
				hw, -hh, hd,
				hw, hh, hd
		};

		float texCoords[] = {
				//Base-Front
				0.25f, 0.25f,
				0.5f, 0.25f,
				0.5f, 0.5f,
				0.25f, 0.5f,
				//Left
				0, 0.25f,
				0, 0.5f,
				//Right
				0.75f, 0.25f,
				0.75f, 0.5f,
				//Back
				1, 0.25f,
				1, 0.5f
		};

		int faces[] = {
				//Front triangles
				0,0, 3,3, 1,1,
				1,1, 3,3, 2,2,
				//Left
				4,4, 5,5, 0,0,
				0,0, 5,5, 3,3,
				//Right
				1,1, 2,2, 6,6,
				6,6, 2,2, 7,7,
				//Back
				6,6, 7,7, 4,8,
				4,8, 7,7, 5,9
		};

		TriangleMesh mesh = new TriangleMesh();
		mesh.getPoints().setAll(points);
		mesh.getTexCoords().setAll(texCoords);
		mesh.getFaces().setAll(faces);
		return mesh;
	}
}
