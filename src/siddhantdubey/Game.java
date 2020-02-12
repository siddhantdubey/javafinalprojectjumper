package siddhantdubey;

import java.io.File;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {

	public static boolean applet = true;

	public Entity hero;
	private Color bgColor;
	public WorldManager worldManager;

	private static int winWidth = 640;
	private static int winHeight = 380;
//	private static int winWidth = 1920;
	//private static int winHeight = 1152;

	public static void main(String[] args) {

		applet = false;

		File f = new File("natives.jar");
		if (f.exists())
			System.setProperty("org.lwjgl.librarypath", f.getAbsolutePath());

		try {
			AppGameContainer game = new AppGameContainer(new Game());
			game.setDisplayMode(winWidth, winHeight, false);
			game.setIcon("resources/ico.png");
			game.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public Game() {
		super("Jumper");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {

		gc.setMaximumLogicUpdateInterval(60);
		gc.setTargetFrameRate(60);
		gc.setAlwaysRender(true);
		gc.setShowFPS(false);
		gc.setVSync(true);

		new Resources();
//		new Mixer();

		hero = new Entity();
		hero.sprite = "potato";
		hero.x = 100;
		hero.y = 64;
		hero.width = 32;
		hero.height = 32;
		bgColor = new Color(Color.decode("0x555555"));

		worldManager = new WorldManager(hero);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.scale(gc.getWidth()/winWidth, gc.getHeight()/winHeight);
		if (GLOBAL.MENU || GLOBAL.YOU_WIN) {

			for (int x = 0; x < 20; x++) {
				for (int y = 0; y < 12; y++) {

					Resources.getImage("tile").draw(x * 32, y * 32, 32, 32, bgColor);

					if (Math.random() * 2 >= 1) { // Useless but nice effect.
						g.setColor(new Color(0, 0, 0, (float) Math.random() * 0.1f));
						g.fillRect(x * 32, y * 32, 32, 32);
					}
				}
			}
		}

		if (GLOBAL.MENU) {
			Resources.getImage("menu").draw(0, 0, winWidth, winHeight);
			g.setColor(Color.yellow);
			return;
		} else if (GLOBAL.YOU_WIN) {
			Resources.getImage("win").draw(0, 0, winWidth, winHeight);
			return;
		}

		worldManager.render(gc, g);
		hero.render(gc, g);

		if ((worldManager.jumps - GLOBAL.JUMP_COUNT) <= 0) {

			if (GLOBAL.RESPAWN_COUNTDOWN_TIMER <= 0) {
				Resources.getImage("respawn").draw((winWidth / 2) - (Resources.getImage("respawn").getWidth() / 2),
						(winHeight / 2) - (Resources.getImage("respawn").getHeight() / 2));
			} else {
				GLOBAL.RESPAWN_COUNTDOWN_TIMER--;
			}
		} else {
			GLOBAL.RESPAWN_COUNTDOWN_TIMER = 150;
		}

		int jumpsLength = g.getFont().getWidth("Jumps Left: " + (worldManager.jumps - GLOBAL.JUMP_COUNT));
		int deathLength = g.getFont().getWidth("Death Count: " + GLOBAL.DEATHS);

		g.setColor(Color.black);
		g.drawString("Jumps Left: " + (worldManager.jumps - GLOBAL.JUMP_COUNT), (winWidth / 2) - (jumpsLength / 2) + 6,
				6);
		g.setColor(Color.yellow);
		g.drawString("Jumps Left: " + (worldManager.jumps - GLOBAL.JUMP_COUNT), (winWidth / 2) - (jumpsLength / 2) + 7,
				5);

		g.setColor(Color.black);
		g.drawString("Death Count: " + GLOBAL.DEATHS, winWidth - deathLength - 7, 6);
		g.setColor(Color.yellow);
		g.drawString("Death Count: " + GLOBAL.DEATHS, winWidth - deathLength - 7, 5);
		g.setColor(Color.black);

		g.drawString("Level: " + worldManager.level, 8, 6);
		g.setColor(Color.yellow);
		g.drawString("Level: " + worldManager.level, 7, 5);
	}

	@Override
	public void update(GameContainer gc, int ticks) throws SlickException {

		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE) && !applet) {
			gc.exit();
		}

		if (GLOBAL.CREDITS) {

			if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
				GLOBAL.CREDITS = false;
			}

			return;
		}

		if (GLOBAL.MENU) {

			if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
				GLOBAL.MENU = false;
				worldManager.level = 1; // Cheat here >:3
				worldManager.loadMap(Resources.getImage("test"));
				worldManager.respawn(false);
				GLOBAL.DEATHS = 0;
			} 

			return;
		} else if (GLOBAL.YOU_WIN) {

			if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
				GLOBAL.MENU = true;
				GLOBAL.YOU_WIN = false;
			}

			return;
		}

		events(gc);

		worldManager.update(gc, ticks);
		hero.update(worldManager, gc, ticks);

		if (hero.y > winHeight) {
			worldManager.respawn(true);
		}
	}

	private void events(GameContainer gc) {

		Input input = gc.getInput();

		if (input.isKeyPressed(Input.KEY_R)) {
			worldManager.respawn(true);
		}

		if (input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT)) {
			hero.moveLeft();
		} else if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
			hero.moveRight();
		}

		if (input.isKeyDown(Input.KEY_SPACE) || input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP)) {

			if ((worldManager.jumps - GLOBAL.JUMP_COUNT) > 0) {
				if (hero.requestJump()) {
//					Mixer.playSound("jump");
					GLOBAL.JUMP_COUNT++;
				}
			}
		}
	}
}
