package Tetris;

import GameObject.AudioPlayer;

public class GameThread extends Thread{
    private GameArea ga;
    private GameForm gf;
    private int score;
    private int level = 1;
    private int scorePerLevel = 3;
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
                Tetris.gameOver(score);
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
}
