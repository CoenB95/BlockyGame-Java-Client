package mycraft;

import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class RectBlock extends Group {

	private Image image;
	private double width;
	private double height;
	private double depth;

	public RectBlock(double w, double h, double d, Image image) {
		this.image = image;
		this.width = w;
		this.height = h;
		this.depth = d;
		getChildren().add(createSide(Side.TOP));
	}

	public void setRaised(int levels) {
		getChildren().addAll(createSide(Side.FRONT), createSide(Side.BACK),
				createSide(Side.LEFT), createSide(Side.RIGHT));
		setTranslateY(-height * levels);
	}

	private ImageView createSide(Side side) {
		switch (side) {
			case TOP:
				return createSide(0.25, 0, 270, -1);
			case FRONT:
				return createSide(0.25, 0.25, -1, 0);
			case BACK:
				return createSide(0.75, 0.25, -1, 180);
			case LEFT:
				return createSide(0, 0.25, -1, 90);
			case RIGHT:
				return createSide(0.5, 0.25, -1, 270);
		}
		return null;
	}

	private ImageView createSide(double percentX, double percentY, double xAngle, double yAngle) {
		ImageView side = new ImageView(image);
		side.setFitWidth(width);
		side.setFitHeight(height);
		side.setViewport(new Rectangle2D(image.getWidth() * percentX, image.getHeight() * percentY,
				image.getWidth() * 0.25, image.getHeight() * 0.25));
		if (xAngle >= 0) side.getTransforms().add(new Rotate(xAngle, new Point3D(1, 0, 0)));
		if (yAngle >= 0) side.getTransforms().add(new Rotate(yAngle, new Point3D(0, 1, 0)));
		side.getTransforms().add(new Translate(-width/2, -height/2, -depth/2));

		if (yAngle >= 0)
			side.visibleProperty().bind(AngleBindings.isBetween(BoxMain.cameraAngle, yAngle - 110, yAngle + 110));
		return side;

	}

	private enum Side {
		LEFT,
		FRONT,
		RIGHT,
		BACK,
		TOP,
		BOTTOM
	}
}
