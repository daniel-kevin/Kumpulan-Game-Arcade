package Tetris;

import javax.swing.JOptionPane;

public class Tetris {
    private static GameForm gf;
    private static StartupForm sf;
    private static LeaderboardForm lf;
    
    // start the game 
    public static void start(){
        gf = new GameForm();
        gf.setVisible(true);
        gf.startGame();
    }
    
    // tampilan leaderboard
    public static void showLeaderboard(){
        lf = new LeaderboardForm();
        lf.setVisible(true);
    }
    
    // tampilan game over 
    public static void gameOver(int score, String waktu){
        lf = new LeaderboardForm();
        gf = new GameForm();
        // menerima input nama player 
        String playerName = JOptionPane.showInputDialog("Game Over\nEnter Your Name : ");
        
        lf.addPlayer(playerName, score, waktu);
        gf.dispose();
    }
    
    // tampilan start game 
    public static void showStartup(){
        sf = new StartupForm();
        sf.setVisible(true);
    }
    
    public static void main(String[] args){
        java.awt.EventQueue.invokeLater(() -> {
        
        sf.setVisible(true);
        });
        
        
    }
}
