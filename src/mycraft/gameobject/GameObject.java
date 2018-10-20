package mycraft.gameobject;

import mycraft.math.Position;
import mycraft.math.Rotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GameObject {
	private GameScene parentScene;
	private List<GameObjectComponent> components = new ArrayList<>();
	private Position position;
	private Rotation rotation;
	private Position targetPosition;
	private Rotation targetRotation;

	public GameObject() {
		targetPosition = position = Position.ORIGIN;
		targetRotation = rotation = Rotation.ORIGIN;
	}

	public void addComponent(GameObjectComponent component) {
		component.setParentObject(this);
		components.add(component);
	}

	public List<GameObjectComponent> getComponents() {
		return Collections.unmodifiableList(components);
	}

	public GameScene getParentScene() {
		return parentScene;
	}

	public Position getPosition() {
		return position;
	}

	public Rotation getRotation() {
		return rotation;
	}

	public Position getTargetPosition() {
		return targetPosition;
	}

	public Rotation getTargetRotation() {
		return targetRotation;
	}

	protected void onAddedToScene(GameScene parent, boolean as3D) {}

	public void onUpdate(double elapsedSeconds) {
		components.forEach(c -> c.onUpdate(elapsedSeconds));
	}

	public final void setParentScene(GameScene scene, boolean as3D){
		parentScene = scene;
		onAddedToScene(parentScene, as3D);
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}

	public void setTargetPosition(Position targetPosition) {
		this.targetPosition = targetPosition;
	}

	public void setTargetRotation(Rotation targetRotation) {
		this.targetRotation = targetRotation;
	}
}
