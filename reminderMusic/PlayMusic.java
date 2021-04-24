package reminderMusic;

import java.io.File;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

public class PlayMusic {

	public static void playMusic(String musicLocation) {
		InputStream music;
		try{
			File musicPath=new File(musicLocation);
			
			if(musicPath.exists()) {
				AudioInputStream audioInput=AudioSystem.getAudioInputStream(musicPath);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInput);
				clip.start();
				clip.loop(clip.LOOP_CONTINUOUSLY);
				
			} else {
				System.out.println("Can't find file");
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
}