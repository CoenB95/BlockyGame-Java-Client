package com.cbapps.javafx.mycraft.main;

import com.cbapps.javafx.gamo.apps.GameApp;
import com.cbapps.javafx.gamo.groups.GameObjectGroup;
import com.cbapps.javafx.mycraft.gamescene.MainGameScene;

public class BoxApp extends GameApp {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void onStart(GameObjectGroup gameObjectGroup) {
		//gameObjectGroup.addObject(new MainGameScene());
	}
}
