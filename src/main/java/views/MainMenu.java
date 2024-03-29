package views;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import main.AudioClips;
import models.User;
import services.UserService;
import utils.ScannerUtil;

public class MainMenu implements View {
	private Clip clip;

	public MainMenu() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		File file = new File(AudioClips.LOGIN.toString());
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());
		clip = AudioSystem.getClip();

		// Open clip in audioInputStream and loop
		clip.open(audioInputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public static void printMenu() {
		System.out.println("Welcome to MapleStoryge UwU!");
		System.out.println("----------------------------\n");
		System.out.println("Choose from one of the options below:");
		System.out.println("1) Signup\n" + "2) Login\n" + "0) Exit");
	}

	@Override
	public View process() {
		printMenu();
		int selection = ScannerUtil.getInput(2);

		switch (selection) {
		case 0:
			System.out.println("Exiting MapleStoryge");
			clip.close();
			return null;
		case 1:
			new UserService().signup();
			clip.close();
			return this;
		case 2:
			User user = new UserService().login();
			try {
				clip.close();
				return new LoggedInMenu(user);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			} 
		default:
			clip.close();
			return this;
		}
	}
}
