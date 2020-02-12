package siddhantdubey;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Mixer {

	public static Music theme;
	private static Map<String, Sound> sounds;

	public Mixer() {

		sounds = new HashMap<String, Sound>();

		try {
			sounds.put("checkpoint", new Sound("resources/sounds/checkpoint.wav"));
			sounds.put("jump", new Sound("resources/sounds/jump.wav"));
			sounds.put("death", new Sound("resources/sounds/death.wav"));
		} catch (SlickException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			theme = new Music("resources/sounds/killingtime.ogg", true);
			theme.loop(1, 0.65f);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void playSound(String sound) {
		sounds.get(sound).play();
	}
}
