package siddhantdubey;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class WorldManager {

	private Tile[][] world;
	private Color bgColor;

	private Entity hero;
	private Entity enemy;
	private Entity enemy1;
	private Entity checkpoint;

	public float spawnX;
	public float spawnY;
	private int worldWidth = 20;
	private int worldHeight = 12;
	private int tileSize = 32;

	public int jumps = 0;

	public int level = 0;
	private List<String> levels;

	public WorldManager(Entity hero) {

		world = new Tile[worldWidth][worldHeight];
		bgColor = new Color(Color.decode("0x555555"));
		levels = new ArrayList<String>();
		this.hero = hero;

		checkpoint = new Entity();
		checkpoint.image = Resources.getImage("checkpoint");
		checkpoint.width = tileSize;
		checkpoint.height = tileSize;

		enemy = new Entity();
		enemy.image = Resources.getImage("enemy");
		enemy.width = tileSize;
		enemy.height = tileSize;
		
		enemy1 = new Entity();
		enemy1.image = Resources.getImage("enemy1");
		enemy1.width = tileSize;
		enemy1.height = tileSize;

		for (int x = 0; x < worldWidth; x++) {
			for (int y = 0; y < worldHeight; y++) {
				world[x][y] = new Tile();
			}
		}
		levels.add("test");
	}

	public void render(GameContainer gc, Graphics g) {

		for (int x = 0; x < worldWidth; x++) {
			for (int y = 0; y < worldHeight; y++) {

				if (world[x][y].image != null) {
					world[x][y].image.draw(x * tileSize, y * tileSize, tileSize, tileSize);
				} else {
					Resources.getImage("tile").draw(x * tileSize, y * tileSize, tileSize, tileSize, bgColor);

					/*
					 * if (Math.random()*2 >= 1) { g.setColor(new Color(0, 0, (float) Math.random(),
					 * (float) Math.random()*0.2f)); g.fillRect(x * tileSize, y * tileSize,
					 * tileSize, tileSize); }
					 */
				}

				if (Math.random() * 2 >= 1) { // Useless but nice effect.
					g.setColor(new Color(0, 0, 0, (float) Math.random() * 0.1f));
					g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
				}
			}
		}

		checkpoint.render(gc, g);
		enemy.render(gc, g);
		enemy1.render(gc, g);
	}

	public void update(GameContainer gc, int ticks) {

		if (hero.hitTest(checkpoint)) {

			if (levels.size() == level) {
				GLOBAL.YOU_WIN = true;
			} else {
				level ++;
				loadMap(Resources.getImage(levels.get(level-1)));
//				Mixer.playSound("checkpoint");
			}
		}
		if (hero.hitTest(enemy)) {
			hero.requestJump(4);
			hero.updateJump(ticks);
		}
		if (hero.hitTest(enemy1)) {
			hero.requestJump(4);
			hero.updateJump(ticks);
		}
	}

	public void loadMap(Image map) {

		Color c;
		jumps = 0;

		for (int x = 0; x < worldWidth; x++) {
			for (int y = 0; y < worldHeight; y++) {

				world[x][y].image = null;

				c = map.getColor(x, y);

				if (c.equals(Color.decode("0x000000"))) {
					world[x][y].image = Resources.getImage("tile");
				} else if (c.equals(Color.decode("0xFF0000"))) {
					spawnX = (x * tileSize) - (hero.width / 2) + (tileSize / 2);
					spawnY = (y * tileSize) - (hero.height / 2) + (tileSize / 2);
					respawn(false);
					GLOBAL.DEATHS--;
				} else if (c.equals(Color.decode("0x808080"))) {
					jumps = 1000;
				} else if (c.equals(Color.decode("0x4CFF00"))) {
					checkpoint.x = x * tileSize;
					checkpoint.y = y * tileSize;
				} else if (c.equals(Color.decode("#3F51B5"))) {
					enemy.x = x * tileSize;
					enemy.y = y * tileSize;
				} else if (c.equals(Color.decode("#6a0dad"))) {
					enemy1.x = x * tileSize;
					enemy1.y = y * tileSize;
				}

			}
		}
	}

	public Box hitTest(Box a) {

		Box b;

		int xStart = (int) (a.x / tileSize) - 2;
		int yStart = (int) (a.y / tileSize) - 2;
		int xEnd = (int) ((((a.width) / tileSize) + xStart) + 4);
		int yEnd = (int) ((((a.height) / tileSize) + yStart) + 4);

		for (int x = xStart; x < xEnd; x++) {
			for (int y = yStart; y < yEnd; y++) {

				if (inBounds(x, y) && world[x][y].image != null) {

					b = new Box(x * tileSize, y * tileSize, tileSize, tileSize);

					if (b.hitTest(a)) {
						return b;
					}
				}
			}
		}

		return null;
	}

	public boolean inBounds(int x, int y) {
		return (x >= 0 && x < worldWidth && y >= 0 && y < worldHeight);
	}

	public void respawn(boolean sounds) {
		hero.x = spawnX;
		hero.y = spawnY;
		hero.velocity = 0;
		hero.fallVelocity = 0;
		GLOBAL.JUMP_COUNT = 0;
		if (sounds)
//			Mixer.playSound("death");
		GLOBAL.RESPAWN_COUNTDOWN_TIMER = 150;
		GLOBAL.DEATHS++;
	}
}
