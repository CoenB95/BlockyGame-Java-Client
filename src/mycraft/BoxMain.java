package mycraft;

import gamo.scenes.GameScene;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
		Pane group = new StackPane();
		Scene scene = new Scene(group, 600, 400);
		scene.setFill(Color.ANTIQUEWHITE);
		mainScene = new MainGameScene(scene, group);

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
