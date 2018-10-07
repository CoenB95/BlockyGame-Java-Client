package mycraft.gameobject;

import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameScene {
	private Group scene;
	private List<GameObject> objects = new ArrayList<>();

	public GameScene(Group scene)
	{
		this.scene = scene;
	}

	protected final void addNode(Node node) {
		this.scene.getChildren().add(node);
	}

	protected final void addNodes(Collection<Node> nodes) {
		this.scene.getChildren().addAll(nodes);
	}

	public final void addObject(GameObject object) {
		object.setParentScene(this);
		objects.add(object);
	}

	public final void addObjects(Collection<? extends GameObject> objects) {
		objects.forEach(this::addObject);
	}

	public void onUpdate(double elapsedSeconds) {
		objects.forEach(o -> o.onUpdate(elapsedSeconds));
	}

	protected final void setCamera(Camera camera) {
		scene.getScene().setCamera(camera);
	}
}
