package com.cbapps.javafx.mycraft.states;

import com.cbapps.gamo.javafx.TextObject;
import com.cbapps.gamo.objects.GameObject;
import com.cbapps.gamo.state.GameStateBase;
import com.cbapps.javafx.mycraft.data.BlockInfo;
import com.cbapps.javafx.mycraft.data.ChunkInfo;
import com.cbapps.javafx.mycraft.objects.World;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class DebugGameState extends GameStateBase {
	private final TextObject debugText;
	private final GameObject steve;
	private final World world;

	private String gameMode = "<unknown>";
	private boolean shown = false;

	public DebugGameState(World world, GameObject steve) {
		this.debugText = new TextObject();
		this.steve = steve;
		this.world = world;
	}

	@Override
	public void onStop() {
		super.onStop();

		showDebug(false);
	}

	@Override
	public void onKeyPressed(KeyEvent event) {
		super.onKeyPressed(event);

		if (event.getCode() == KeyCode.F3) {
			showDebug(!isDebugShown());
		}
	}

	public boolean isDebugShown() {
		return shown;
	}

	protected void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}

	public void showDebug(boolean shown) {
		this.shown = shown;

		if (shown) {
			getScene().get2D().addObject(debugText);
		} else {
			getScene().get2D().removeObject(debugText);
		}
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		super.onUpdate(elapsedSeconds);

		var currentChunk = world.getChunkInfo(getScene().getCamera().getPosition());
		var currentBlock = world.getBlockInfo(getScene().getCamera().getPosition());
		final var debugString = """
				MyCraft   v0.0.1-alpha1
				Mode:     %s
				Position: %s
				   Chunk: %s
				   Block: %s
				""".formatted(gameMode,
				steve.getPosition(),
				currentChunk.map(ChunkInfo::toPositionString).orElse(null),
				currentBlock.map(BlockInfo::toPositionString).orElse(null));
		debugText.textProperty().set(debugString);
	}
}
