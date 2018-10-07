package mycraft.gameobject;

public abstract class GameObjectComponent {
	private GameObject parentObject;

	protected GameObject getParentObject() {
		return parentObject;
	}

	protected void onAddedToObject(GameObject parent) {}
	public abstract void onUpdate(double elapsedSeconds);

	public final void setParentObject(GameObject parent) {
		parentObject = parent;
		onAddedToObject(parentObject);
	}
}
