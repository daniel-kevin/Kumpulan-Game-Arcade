package Tetris;

import GameObject.AudioPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameThread extends Thread{
    private GameArea ga;
    private GameForm gf;
    private int score;
    private int level = 1;
    private int scorePerLevel = 3;
    public static int highscore;
    public AudioPlayer audioPlayer;
    
    private int pause = 1000;
    private int speedupPerLevel = 100;
    
    public GameThread(GameArea ga, GameForm gf){
        this.ga = ga;
        this.gf = gf;
        gf.audioPlayer.PlayBGM("Tetris BGM.wav");
        
        gf.updateScore(score);
        gf.updateLevel(level);
    }
    
    @Override
    public void run(){
        Date startTime = new Date();
        while(true){ 
            ga.spawnBlock();                // method memunculkan blok
            
            // menggerakkan blok tetris ke bawah
            while(ga.moveBlockDown()){
                try {
                    Thread.sleep(pause);    // jeda gerakan turun tetris 
                }
                catch (InterruptedException ex) {
                    // to interupt the latest thread
                    return;
                }
            }
            
            if(ga.isBlockOutOfBounds()){
                // show game over with JOptionPane
                gf.audioPlayer.stop();
                gf.dispose();
                Tetris.gameOver(score);
                
                Date endTime = new Date();
                long total_game_time = endTime.getTime() - startTime.getTime();
                long t_game_h = total_game_time/(60 * 60 * 1000) % 24;
                long t_game_m = total_game_time/(60 * 1000) % 60;
                long t_game_s = total_game_time/1000 % 60;
                
                //update highscore kalau current score lebih tinggi dari highscore saat sudah game over
                if(score > highscore){
                FileWriter fileWriter = null;
                    try {
                        File high_score_file = new File("tetris_score.txt");
                        fileWriter = new FileWriter(high_score_file,false);
                        fileWriter.write(Integer.toString(score)+"\n"+t_game_h+"\n"+t_game_m+"\n"+t_game_s);
                        fileWriter.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Tetris.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            fileWriter.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Tetris.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                HighScore();    //update variabel highscore
                break;
            }
            
            // game continue 
            ga.moveBlockToBackground();             // mengubah block jadi background 
            score += ga.clearLines();               // menambahkan nilai score saat membersihkan line tetris 
            gf.updateScore(score);                  // memanggil method updateScore untuk merubah score 
            
            int lvl = score / scorePerLevel + 1;    
            // update level setiap 3 score 
            if(lvl > level){
                level = lvl;
                gf.updateLevel(level);
                pause -= speedupPerLevel;           // menambah kecepatan game / mengurangi sleep time game
            }
        }
    }
    
    //buat set isi dari variable highscore
    public void HighScore(){
        File high_score_file = new File("tetris_score.txt");
        String score;
        if(!high_score_file.exists()){
            try {
                high_score_file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Tetris.class.getName()).log(Level.SEVERE, null, ex);
            }
            highscore = 0;
        }
        else{
            try {
                Scanner scanner = new Scanner(high_score_file);
                    while(scanner.hasNextLine()){
                        score = scanner.nextLine();
                        highscore = Integer.parseInt(score);
                    }
                    scanner.close();
                } catch (FileNotFoundException ex) {
                Logger.getLogger(Tetris.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }
}
