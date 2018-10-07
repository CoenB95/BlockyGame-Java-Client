package mycraft.gameobject;

import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public abstract class GameObjectBase extends GameObject {
	private Node node;
	private Translate translateTransform;
	private Rotate horizontalRotateTransform;
	private Rotate verticalRotateTransform;

	public GameObjectBase() {

	}

	protected void setNode(Node node) {
		this.node = node;
		translateTransform = new Translate(0, 0, 0);
		horizontalRotateTransform = new Rotate(0, 0, 0, 0, new Point3D(0, 1, 0));
		verticalRotateTransform = new Rotate(0, 0, 0, 0, new Point3D(1, 0, 0));
		node.getTransforms().addAll(translateTransform, horizontalRotateTransform, verticalRotateTransform);
	}

	@Override
	protected void onAddedToScene(GameScene parent) {
		if (node == null)
			return;

		if (node instanceof Camera)
			parent.setCamera((Camera) node);
		else
			parent.addNode(node);
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		super.onUpdate(elapsedSeconds);
		translateTransform.setX(getPosition().getX());
		translateTransform.setY(-getPosition().getY());
		translateTransform.setZ(getPosition().getZ());
		horizontalRotateTransform.setAngle(getRotation().getHorizontal());
		verticalRotateTransform.setAngle(getRotation().getVertical());
	}
}
