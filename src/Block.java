import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class Block extends MeshView {

	public Block(double w, double h, double d) {
		this(w, h, d, new PhongMaterial());
	}

	public Block(double w, double h, double d, Color color) {
		this(w, h, d, new PhongMaterial(color));
	}

	public Block(double w, double h, double d, String image) {
		this(w, h, d, new PhongMaterial(Color.WHITE, new Image(image), null,
				null, null));
	}

	public Block(double w, double h, double d, Material material) {
		super(createMesh((float) w, (float) h, (float) d));
		setMaterial(material);
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
				1, 0.5f,
				//Top
				0.25f, 0,
				0.5f, 0
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
				4,8, 7,7, 5,9,
				//Top
				4,10, 0,0, 6,11,
				6,11, 0,0, 1,1
		};

		int faceSmoothingGroups[] = {
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0
		};

		TriangleMesh mesh = new TriangleMesh();
		mesh.getPoints().setAll(points);
		mesh.getTexCoords().setAll(texCoords);
		mesh.getFaces().setAll(faces);
		mesh.getFaceSmoothingGroups().setAll(faceSmoothingGroups);
		return mesh;
	}
}
