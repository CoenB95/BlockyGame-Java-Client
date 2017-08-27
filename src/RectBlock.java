import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class RectBlock extends Group {

	public RectBlock(double w, double h, double d, Image image) {
		ImageView top = new ImageView(image);
		top.setFitWidth(w);
		top.setFitHeight(h);
		top.setViewport(new Rectangle2D(image.getWidth() * 0.25, image.getHeight() * 0,
				image.getWidth() * 0.25, image.getHeight() * 0.25));
		top.getTransforms().addAll(
				new Translate(-w/2, -h, d/2),
				new Rotate(-90, new Point3D(1, 0, 0))

		);
		getChildren().add(top);
	}
}
