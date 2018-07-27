package mycraft.math;

public class Rotation {
	private double horizontal;
	private double vertical;
	private double roll;

	public static final Rotation ORIGIN = new Rotation(0, 0, 0);

	public Rotation(double horizontal, double vertical, double roll) {
		this.horizontal = horizontal;
		this.vertical = vertical;
		this.roll = roll;
	}

	public Rotation add(Rotation rotation) {
		return new Rotation(horizontal + rotation.horizontal,
				vertical + rotation.vertical,
				roll + rotation.roll);
	}

	public Rotation addHorizontal(double value) {
		return new Rotation(horizontal + value, vertical, roll);
	}

	public Rotation addVertical(double value) {
		return new Rotation(horizontal, vertical + value, roll);
	}

	public Rotation addRoll(double value) {
		return new Rotation(horizontal, vertical, roll + value);
	}

	public double getHorizontal() {
		return horizontal;
	}

	public double getVertical() {
		return vertical;
	}

	public double getRoll() {
		return roll;
	}

	public Rotation multiply(double factor) {
		return new Rotation(horizontal * factor, vertical * factor, roll * factor);
	}

	public Rotation withHorizontal(double value) {
		return new Rotation(value, vertical, roll);
	}

	public Rotation withVertical(double value) {
		return new Rotation(horizontal, value, roll);
	}

	public Rotation withRoll(double value) {
		return new Rotation(horizontal, vertical, value);
	}
}
