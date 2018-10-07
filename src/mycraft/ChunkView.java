package mycraft;

import javafx.scene.shape.MeshView;

public class ChunkView extends MeshView {
	private Terrain chunk;

	public ChunkView(Terrain chunk)
	{
		super();
		this.chunk = chunk;
	}

	public final Terrain getChunk() {
		return chunk;
	}
}
