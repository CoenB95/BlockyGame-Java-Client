package mycraft.gameobject;

import mycraft.math.Position;
import mycraft.math.Rotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GameObject {
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
		component.setParent(this);
		components.add(component);
	}

	public List<GameObjectComponent> getComponents() {
		return Collections.unmodifiableList(components);
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

	public void onUpdate(double elapsedSeconds) {
		components.forEach(c -> c.onUpdate(elapsedSeconds));
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
