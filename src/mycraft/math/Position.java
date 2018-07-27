package mycraft.math;

public class Position {
	private double x;
	private double y;
	private double z;

	public static final Position ORIGIN = new Position(0, 0, 0);

	public Position(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Position add(Position position) {
		return new Position(x + position.x,
				y + position.y,
				z + position.z);
	}

	public Position addX(double value) {
		return new Position(x + value, y, z);
	}

	public Position addY(double value) {
		return new Position(x, y + value, z);
	}

	public Position addZ(double value) {
		return new Position(x, y, z + value);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public Position multiply(double factor) {
		return new Position(x * factor, y * factor, z * factor);
	}

	public Position withX(double value) {
		return new Position(value, y, z);
	}

	public Position withY(double value) {
		return new Position(x, value, z);
	}

	public Position withZ(double value) {
		return new Position(x, y, value);
	}
}
