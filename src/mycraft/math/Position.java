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

	public Position add(Rotation direction, double delta)
	{
		// Positive horizontal rotation == look to right.
		// Positive vertical rotation == look up.
		double invY = 1 - Math.abs(Math.sin(Math.toRadians(direction.getVertical())));
		return new Position(
				x + invY * Math.cos(Math.toRadians(90 - direction.getHorizontal())) * delta,
				y + Math.sin(Math.toRadians(direction.getVertical())) * delta,
				z + invY * Math.sin(Math.toRadians(90 - direction.getHorizontal())) * delta);
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

	public Position limitX(double minValue, double maxValue)
	{
		return new Position(x < minValue ? minValue : (x > maxValue ? maxValue : x), y, z);
	}

	public Position limitY(double minValue, double maxValue)
	{
		return new Position(x, y < minValue ? minValue : (y > maxValue ? maxValue : y), z);
	}

	public Position limitZ(double minValue, double maxValue)
	{
		return new Position(x, y, z < minValue ? minValue : (z > maxValue ? maxValue : z));
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
