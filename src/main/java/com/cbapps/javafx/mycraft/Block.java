package com.cbapps.javafx.mycraft;

import com.cbapps.javafx.gamo.objects.GameObjectBase;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

/**
 * @author Coen Boelhouwers
 * @version 1.0
 */
public class Block extends GameObjectBase {
	private MeshView meshView;
	private TriangleMesh triangleMesh;
	private float blockDepth;
	private float blockHeight;
	private float blockWidth;
	private int blockState;
	private float rize;

	public Block(double w, double h, double d, int state) {
		blockWidth = (float) w;
		blockHeight = (float) h;
		blockDepth = (float) d;
		blockState = state;
		rize = (float) (Math.random() * blockHeight);
	}

	public void buildEmbedded(TriangleMesh mesh) {
		buildFaces(mesh, true);
	}

	public void buildStandalone() {
		triangleMesh = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);
		buildFaces(triangleMesh,false);
		meshView = new MeshView(triangleMesh);
		meshView.setMaterial(new PhongMaterial(Color.WHITE,
				new Image("/cube.png", 100, 100, true, false),
				null, null, null));
		setNode(meshView);
	}

	public void buildFaces(TriangleMesh mesh, boolean applyOffset)
	{
		if (blockState == 0)
			return;

		float x = (applyOffset ? (float) getCurrentVector().getPosition().getX() : 0);
		float y = (applyOffset ? (float) -getCurrentVector().getPosition().getY() : 0);
		float z = (applyOffset ? (float) getCurrentVector().getPosition().getZ() : 0);
		float hw = blockWidth / 2;
		float hh = blockHeight / 2;
		float hd = blockDepth / 2;
		float right = x + hw;
		float left = x - hw;
		float bottom = y + hh;
		float top = y - hh;
		float back = z + hd;
		float front = z - hd;

		float points[] = {
				//Top
				right, top, back,
				left, top, back,
				left, top, front,
				right, top, front,
				//Left
				left, top, front,
				left, top, back,
				left, bottom, back,
				left, bottom, front,
				//Front
				right, top, front,
				left, top, front,
				left, bottom, front,
				right, bottom, front,
				//Right
				right, top, back,
				right, top, front,
				right, bottom, front,
				right, bottom, back,
				//Back
				left, top, back,
				right, top, back,
				right, bottom, back,
				left, bottom, back,
				//Bottom
				right, bottom, front,
				left, bottom, front,
				left, bottom, back,
				right, bottom, back
		};

		float normals[] = {
				0, -1,  0, //Top
				-1,  0,  0, //Left
				0,  0, -1, //Front
				1,  0,  0, //Right
				0,  0,  1, //Back
				0,  1,  0  //Bottom
		};

		float texCoords[] = {
				//Top
				0.50f, 0.00f,
				0.25f, 0.00f,
				0.25f, 0.25f,
				0.50f, 0.25f,
				//Left
				0.25f, 0.25f,
				0.00f, 0.25f,
				0.00f, 0.50f,
				0.25f, 0.50f,
				//Front
				0.50f, 0.25f,
				0.25f, 0.25f,
				0.25f, 0.50f,
				0.50f, 0.50f,
				//Right
				0.75f, 0.25f,
				0.50f, 0.25f,
				0.50f, 0.50f,
				0.75f, 0.50f,
				//Back
				1.00f, 0.25f,
				0.75f, 0.25f,
				0.75f, 0.50f,
				1.00f, 0.50f,
				//Front
				0.50f, 0.50f,
				0.25f, 0.50f,
				0.25f, 0.75f,
				0.50f, 0.75f,
		};

		int pointsOffset = mesh.getPoints().size() / 3;
		int normalsOffset = mesh.getNormals().size() / 3;
		int texOffset = mesh.getTexCoords().size() / 2;

		mesh.getPoints().addAll(points);
		mesh.getNormals().addAll(normals);
		mesh.getTexCoords().addAll(texCoords);

		for (int side = 0; side < 6 ; side++) {
			mesh.getFaces().addAll(pointsOffset + side * 4, normalsOffset + side, texOffset + side * 4);
			mesh.getFaces().addAll(pointsOffset + side * 4 + 1, normalsOffset + side, texOffset + side * 4 + 1);
			mesh.getFaces().addAll(pointsOffset + side * 4 + 2, normalsOffset + side, texOffset + side * 4 + 2);
			mesh.getFaces().addAll(pointsOffset + side * 4, normalsOffset + side, texOffset + side * 4);
			mesh.getFaces().addAll(pointsOffset + side * 4 + 2, normalsOffset + side, texOffset + side * 4 + 2);
			mesh.getFaces().addAll(pointsOffset + side * 4 + 3, normalsOffset + side, texOffset + side * 4 + 3);
		}
	}

	@Override
	public void onUpdate(double elapsedSeconds) {
		super.onUpdate(elapsedSeconds);
	}
}
