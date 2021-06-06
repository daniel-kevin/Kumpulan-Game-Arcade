/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObject;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Daniel
 */
public class AudioPlayer {
    private Clip clip;
    
    public void PlayBGM(String fileName){
       clip = getClip(fileName);
       clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public Clip getClip(String fileName){
        URL BGM_file= AudioPlayer.class.getResource("/Music/"+ fileName);
        try(AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(BGM_file)){
            Clip clip1 = AudioSystem.getClip();
            clip1.open(audioInputStream);
            clip1.setMicrosecondPosition(0);
            
            return clip1;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public void stop(){
        clip.close();
    }
}
