package mycraft;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.stage.Stage;
import mycraft.gameobject.GameScene;
import mycraft.gamescene.MainGameScene;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class BoxMain extends Application {

	public static DoubleProperty cameraAngle = new SimpleDoubleProperty();
	private GameScene mainScene;

	@Override
	public void start(Stage primaryStage) {
		Scene scene = new Scene(new Group(), 600, 400, true, SceneAntialiasing.BALANCED);
		mainScene = new MainGameScene(scene);

		//Walking
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				mainScene.onUpdate(0.013);
			}
		}.start();

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
