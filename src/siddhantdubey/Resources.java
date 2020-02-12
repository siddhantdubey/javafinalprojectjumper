package siddhantdubey;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Resources {

	private static Map<String, Image> images;

	public Resources() {

		images = new HashMap<String, Image>();

		images.put("menu", loadImage("resources/menu.png"));
		images.put("win", loadImage("resources/win.png"));
		images.put("respawn", loadImage("resources/respawn.png"));

		images.put("tile", loadImage("resources/tile.png"));
		images.put("checkpoint", loadImage("resources/checkpoint.png"));
		images.put("enemy", loadImage("resources/enemy.png"));
		images.put("enemy1", loadImage("resources/enemy1.png"));

		images.put("potato1", loadImage("resources/morrison1.png"));
		images.put("potato2", loadImage("resources/morrison2.png"));
		images.put("potato3", loadImage("resources/morrison3.png"));

		images.put("test", loadImage("resources/levels/x.png"));
	}

	public static Image getImage(String getter) {
		return images.get(getter);
	}

	private Image loadImage(String path) {

		Image img = null;

		try {
			img = new Image(path);
			img.setFilter(Image.FILTER_NEAREST);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return img;
	}
}
