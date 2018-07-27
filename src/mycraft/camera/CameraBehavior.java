package mycraft.camera;

public interface CameraBehavior {
	void onUpdate(double elapsedSeconds,
				  double targetHorizontalRotation,
				  double targetVerticalRotation);
}
