package mycraft.data;

import javafx.scene.shape.ObservableFaceArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to bridge the gap between faces and the actual blocks.
 * @author Coen Boelhouwers
 */
public class FaceUtils {


	private List<Integer> mapping;

	public FaceUtils() {
		mapping = new ArrayList<>();
	}

	public void addFace(int blockId, Side side) {
		mapping.add(setBlockId(setSide(0, side.ordinal()), blockId));
	}

	public static void adjustFace(ObservableFaceArray array, int face,
								  int c00, int c01, int c02, int c10, int c11, int c12) {
		if (face < 0) return;
		array.set(face * 12 + 1, c00);
		array.set(face * 12 + 3, c01);
		array.set(face * 12 + 5, c02);
		array.set(face * 12 + 7, c10);
		array.set(face * 12 + 9, c11);
		array.set(face * 12 + 11, c12);
	}

	public int getFace(int blockId, Side side) {
		for (int i = 0; i < mapping.size(); i++) {
			int face = mapping.get(i);
			if (getBlockId(face) == blockId && getSide(face) == side.ordinal())
				return i;
		}
		return -1;
	}

	public int getBlockByFace(int face) {
		return getBlockId(mapping.get(face));
	}

	private int getBlockId(int data) {
		return data & 0x00FFFFFF;
	}

	private int getSide(int data) {
		return (data & 0xFF000000) >> 24;
	}

	private int setBlockId(int data, int value) {
		return data | (value & 0x00FFFFFF);
	}

	private int setSide(int data, int value) {
		return data | (value << 24);
	}

	public enum Side {
		TOP, LEFT, FRONT, RIGHT, BACK, BOTTOM
	}
}
