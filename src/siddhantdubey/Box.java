package siddhantdubey;

import org.newdawn.slick.Image;

public class Box {

	public float x;
	public float y;
	public float width;
	public float height;

	public Box() {
	}

	public Box(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public float getEndX() {
		return x + width;
	}

	public float getEndY() {
		return y + height;
	}

	public boolean hitTest(Box b) {
		return (b.getEndX() > x && b.x < getEndX() && b.getEndY() > y && b.y < getEndY());
	}
}
