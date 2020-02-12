package siddhantdubey;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Entity extends Box {

	private int gTick = 17;

	public String sprite;
	public int frame = 0;
	public int tillNextFrame;
	public Image image;

	public float velocity;
	public float maxVelocity = 0.25f;
	public float velocitySpeed = 0.04f;

	public float gravity = 0.4f;
	public float fallVelocity;
	public float mass = 0.0075f;
	public float jumpHeight = 8; // 6
	public float jumpCounter;

	public boolean moving;
	public boolean jumping;
	public boolean falling;
	public boolean facingLeft = true;
	
	public void render(GameContainer gc, Graphics g) {

		if (sprite != null && tillNextFrame <= 0) {

			if (frame < 4) {
				frame++;
			} else {
				frame = 1;
			}

			if (frame == 1) {
				image = Resources.getImage(sprite + "1");
			} else if (frame == 2) {
				image = Resources.getImage(sprite + "2");
			} else if (frame == 3) {
				image = Resources.getImage(sprite + "3");
			} else if (frame == 4) {
				image = Resources.getImage(sprite + "2");
			}
		}

		if (sprite != null) {

			if (velocity == 0) {
				image = Resources.getImage(sprite + "1");
			}
			if (fallVelocity != 0)
				image = Resources.getImage(sprite + "3");
		}

		if (tillNextFrame <= 0) {
			tillNextFrame = 3;
		} else {
			tillNextFrame--;
		}

		if (image == null) {

			g.setColor(Color.red);
			g.fillRect(x, y, width, height);
		} else {

			if (!facingLeft) {
				image.draw(getEndX(), y, -width, height);
			} else {
				image.draw(x, y, width, height);
			}
		}
	}

	public void update(WorldManager worldManager, GameContainer gc, int ticks) {
		testCollision(worldManager, ticks);
	}

	public void moveLeft() {

		if (velocity > -maxVelocity) {
			velocity -= velocitySpeed;
		} else {
			velocity = -maxVelocity;
		}

		facingLeft = true;
		moving = true;
	}

	public void moveRight() {

		if (velocity < maxVelocity) {
			velocity += velocitySpeed;
		} else {
			velocity = maxVelocity;
		}

		facingLeft = false;
		moving = true;
	}

	public boolean requestJump() {

		if (!falling && !jumping) {
			jumping = true;
			jumpCounter = jumpHeight;
			return true;
		}

		return false;
	}
	public boolean requestJump(int x) {

		jumping = true;
		jumpCounter = jumpHeight;
		return true;
	}

	public void updateMove(int ticks) {

		x += velocity * gTick;

		if (!moving && velocity != 0) {

			if (velocity < 0 && velocity > -velocitySpeed) { // Stop
				velocity = 0;
			}

			if (velocity > 0) {
				velocity -= velocitySpeed;
			} else {
				velocity += velocitySpeed;
			}
		}

		moving = false;
	}

	public void updateJump(int ticks) {

		if (jumping) {

			if (jumpCounter > 0) {
				jumpCounter--;
				fallVelocity -= (jumpCounter / jumpHeight) / gTick;
			} else {
				jumpCounter = 0;
				jumping = false;
				falling = true;
			}

			y += fallVelocity * gTick;
		}
	}

	public void updateFall(int ticks) {

		if (falling) {

			if (fallVelocity < gravity) {
				fallVelocity += mass;
			} else {
				fallVelocity = gravity;
			}
		}

		y += fallVelocity * gTick;
	}

	private void testCollision(WorldManager wm, int ticks) {

		/*
		 * Wall collision detection
		 */
		updateMove(ticks);
		Box b = wm.hitTest(this);
		if (b != null) {

			if (velocity > 0) {
				x = b.x - width - 0.1f;
			} else {
				x = b.getEndX() + 0.1f;
			}

			velocity = 0;
			moving = false;
		}
		/*
		 * Ceiling hitTest if jumping
		 */
		// updateJump(ticks);
		if (!jumping) {

			b = wm.hitTest(this);

			if (b != null) {
				// y = b.getEndY() + 0.1f;
				jumping = false;
				falling = true;
			}
		}

		updateJump(ticks);

		/*
		 * Ground collision detection
		 */
		updateFall(ticks);
		b = wm.hitTest(this);

		if (b != null) {
			y = b.y - height - 0.1f;
			fallVelocity = 0;
			falling = false;
		} else {
			falling = true;
		}

	}
}
