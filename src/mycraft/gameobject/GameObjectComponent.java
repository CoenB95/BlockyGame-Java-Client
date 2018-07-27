package mycraft.gameobject;

public abstract class GameObjectComponent {
	private GameObject parent;

	protected GameObject getParent() {
		return parent;
	}

	public abstract void onUpdate(double elapsedSeconds);

	public void setParent(GameObject parent) {
		this.parent = parent;
	}
}
