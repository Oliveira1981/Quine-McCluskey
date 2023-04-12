package mazerouter;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Rodrigo da Rosa
 */
public class Sound {
    
    
    
    public Clip play(String sound) throws
            LineUnavailableException,
            UnsupportedAudioFileException,
            IOException {
        File soundFile = new File("sound\\"+sound);
        Clip clip = AudioSystem.getClip();
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile);
        clip.open(inputStream);
        //clip.setFramePosition(5000);
        clip.start();
        return clip;
    }
    
    public Clip loop(String sound) throws
            LineUnavailableException,
            UnsupportedAudioFileException,
            IOException {
        File soundFile = new File("sound\\"+sound);
        Clip clip = AudioSystem.getClip();
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile);
        clip.open(inputStream);
        //clip.setFramePosition(5000);
        //clip.start();
        clip.loop(-1);
        return clip;
    }
    
    public void stop(Clip clip) {
        clip.stop();
    }
}
