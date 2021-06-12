package kumpulan_game_arcade;

import GameObject.AudioPlayer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


public class SnakeGame extends JPanel implements KeyListener, ActionListener{
    
    // possible snake lenth
    private final int[] snakexlength = new int[750];
    private final int[] snakeylength = new int[750];    
    
    // first snake movement's condition
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    
    // image and indicator for snake's part
    private ImageIcon rightmouth;
    private ImageIcon upmouth;
    private ImageIcon downmouth;
    private ImageIcon leftmouth;
    
    private int lengthofsnake = 3;
    
    private final Timer timer;
    private final int delay = 100;    // speed
    private ImageIcon snakeimage;
    
    // array for target position
    public int[] targetxpos = {25,50,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,
                                475,500,525,550,575,600,625,650,675,700,725,750,775,800,825,850};
    public int[] targetypos = {75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,
                                475,500,525,550,575,600,625};
    
    private ImageIcon targetimage;
    
    private final Random random = new Random();
    
    // randomize apple position with total possible position according to array
    private int xpos = random.nextInt(34);
    private int ypos = random.nextInt(23);
    
    // starting values
    private int score = 0;
    private int moves = 0;
    public static int highscore;//simpan highscore
    
    private ImageIcon titleImage;
    
    public static State game_state;//untuk menentukan state game
    public int menu_pick_num = 0;//untuk cek pilihan mana yang dipilih saat di Menu screen
    public AudioPlayer audioPlayer;//untuk play BGM
    
    public Date startTime;
    public long total_game_time;
    public long t_game_h;
    public long t_game_m;
    public long t_game_s;
    
    public SnakeGame(){
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnable(false);
        HighScore();//ambil highscore dari file
        game_state = State.Game;//state diawal di set langsung ke game
        audioPlayer = new AudioPlayer();
        audioPlayer.PlayBGM("Snake BGM.wav");//play BGMnya snake
        startTime = new Date();
        timer = new Timer(delay, this);
        timer.start();
    }
    //game state
    public enum State{
        Game,Game_Over,Menu,Help
    };
    
    // main things
    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g){
        
        if(moves == 0){
            snakexlength[2] = 50;
            snakexlength[1] = 75;
            snakexlength[0] = 100;
            
            snakeylength[2] = 100;
            snakeylength[1] = 100;
            snakeylength[0] = 100;
        }
        
        
        // draw title image border
        g.setColor(Color.white);
        g.drawRect(24, 10, 852, 55);
        
        // draw title image
        titleImage = new ImageIcon("src/gambar/snaketitle.jpg");
        titleImage.paintIcon(this, g, 25, 11);
        
        // draw border for gameplay
        g.setColor(Color.WHITE);
        g.drawRect(24, 74, 851, 577);
        
        // draw background for the gameplay
        g.setColor(Color.BLACK);
        g.fillRect(25,75,850,575);
        
        // draw scores
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.PLAIN, 14));
        g.drawString("Scores : "+score,780,30);
        g.drawString("High score :" + highscore, 30, 30);
        
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.PLAIN, 14));
        g.drawString("Length : "+lengthofsnake,780,50);
        
        rightmouth = new ImageIcon("src/gambar/rightmouth.png");
        rightmouth.paintIcon(this, g, snakexlength[0], snakeylength[0]);
        
        for(int a = 0; a<lengthofsnake; a++){
            if(a==0 && right){
                rightmouth = new ImageIcon("src/gambar/rightmouth.png");
                rightmouth.paintIcon(this, g, snakexlength[a], snakeylength[a]);
            }
            if(a==0 && left){
                leftmouth = new ImageIcon("src/gambar/leftmouth.png");
                leftmouth.paintIcon(this, g, snakexlength[a], snakeylength[a]);
            }
            if(a==0 && down){
                downmouth = new ImageIcon("src/gambar/downmouth.png");
                downmouth.paintIcon(this, g, snakexlength[a], snakeylength[a]);
            }
            if(a==0 && up){
                upmouth = new ImageIcon("src/gambar/upmouth.png");
                upmouth.paintIcon(this, g, snakexlength[a], snakeylength[a]);
            }
            if(a!=0){
                snakeimage = new ImageIcon("src/gambar/snakeimage.png");
                snakeimage.paintIcon(this, g, snakexlength[a], snakeylength[a]);
            }
        }
        
        targetimage = new ImageIcon("src/gambar/target.png");
        
        if(targetxpos[xpos] == snakexlength[0] && targetypos[ypos] == snakeylength[0]){
            score++;
            lengthofsnake++;
            xpos = random.nextInt(34);
            ypos = random.nextInt(23);
        }
        
        targetimage.paintIcon(this, g, targetxpos[xpos], targetypos[ypos]);
        //gameover
        for(int b = 1; b<lengthofsnake; b++){
            if(snakexlength[b] == snakexlength[0] && snakeylength[b] == snakeylength[0]){
                Date endTime = new Date();
                total_game_time = endTime.getTime() - startTime.getTime();
                t_game_h = total_game_time/(60 * 60 * 1000) % 24;
                t_game_m = total_game_time/(60 * 1000) % 60;
                t_game_s = total_game_time/1000 % 60;
                
                right = false;
                left = false;
                up = false;
                down = false;
                game_state = State.Game_Over;
                
                g.setColor(Color.white);
                g.setFont(new Font("arial", Font.BOLD, 50));
                g.drawString("Game Over", 300, 300);
                
                g.setColor(Color.white);
                g.setFont(new Font("arial", Font.BOLD, 20));
                g.drawString("Space to RESTART", 350, 350);
            }
        }
        //kalau state menu screen menu akan ditampilkan
        if(game_state == State.Menu){
            menu(g);
        }
        //kalau state help screen help akan ditampilkan
        if(game_state == State.Help){
            help(g);
        }
        //kalau state gaem over
        if(game_state == State.Game_Over){
            //update highscore kalau current score lebih tinggi dari highscore saat sudah game over
            if(score > highscore){
                FileWriter fileWriter = null;
                try {
                    File high_score_file = new File("snake_score.txt");
                    fileWriter = new FileWriter(high_score_file,false);
                    fileWriter.write(Integer.toString(score)+"\n"+t_game_h+"\n"+t_game_m+"\n"+t_game_s);
                    fileWriter.close();
                } catch (IOException ex) {
                    Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        fileWriter.close();
                    } catch (IOException ex) {
                        Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            HighScore();//update variabel highscore
        }
        
        g.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(right == false && left == false && up == false && down == false && game_state == State.Game_Over)
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                moves = 0;
                score = 0;
                lengthofsnake = 3;
                game_state = State.Game;//start jadi statenya game
                repaint(); // keyword to repeat paint
        }
        
        // code for snake movement using arrow
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            //snake move kalau statenya game
            if(game_state == State.Game){
                moves++;
                right = true;
                if(!left){
                 right = true;
                }else{
                    right = false;
                    left = true;
                }
            
                up = false;
                down = false;
            }
            //kalau di menu arrow key untuk pilih dari beberapa pilihan yang ada
            else if(game_state == State.Menu){
                menu_pick_num++;
                if(menu_pick_num > 2){
                    menu_pick_num = 0;
                    
                }
                repaint();
            }
            
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(game_state == State.Game){
                moves++;
                left = true;
                if(!right){
                    left = true;
                }else{
                    left = false;
                    right = true;
                }
            
                up = false;
                down = false;
            }
            //untuk navigate di Menu screen
            else if(game_state == State.Menu){
                menu_pick_num--;
                if(menu_pick_num < 0){
                    menu_pick_num = 2;
                    
                }
                repaint();
            }
            
        }
        if(e.getKeyCode() == KeyEvent.VK_UP){
            if(game_state == State.Game){
                moves++;
                up = true;
                if(!down){
                    up = true;
                }else{
                    up = false;
                    down = true;
                }
            
                left = false;
                right = false;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            if(game_state == State.Game){
                moves++;
                down = true;
                if(!up){
                    down = true;
                }else{
                    down = false;
                    up = true;
                }
            
                left = false;
                right = false;
            }
            
        }
        //escape key untuk buka Menu
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            game_state = State.Menu;
            repaint();
        }
        //pilih dari options yang ada di screen Menu
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(game_state == State.Menu){
                //continue
                if(menu_pick_num == 0){
                    game_state = State.Game;
                }
                //masuk ke help screen
                if(menu_pick_num == 1){
                    game_state = State.Help;
                }
                //quit (balik ke main menu)
                if(menu_pick_num == 2){
                    audioPlayer.stop();
                    Main_menu.obj.dispose();
                    Main_menu main_menu = new Main_menu();
                    main_menu.setVisible(true);
                }
            }
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(right){
           for(int r = lengthofsnake-1; r>=0; r--) {
               snakeylength[r+1] = snakeylength[r];
           }
           for(int r = lengthofsnake; r>=0; r--){
               if(r == 0){
                   snakexlength[r] = snakexlength[r] + 25;
               }
               else{
                   snakexlength[r] = snakexlength[r-1];
               }
               if(snakexlength[r] > 850){
                   snakexlength[r] = 25;
               }
           }
           
           repaint();
        }
        if(left){
            for(int r = lengthofsnake-1; r>=0; r--) {
               snakeylength[r+1] = snakeylength[r];
           }
           for(int r = lengthofsnake; r>=0; r--){
               if(r == 0){
                   snakexlength[r] = snakexlength[r] - 25;
               }
               else{
                   snakexlength[r] = snakexlength[r-1];
               }
               if(snakexlength[r] < 25){
                   snakexlength[r] = 850;
               }
           }
           
           repaint();
        }
        if(up){
            for(int r = lengthofsnake-1; r>=0; r--) {
               snakexlength[r+1] = snakexlength[r];
           }
           for(int r = lengthofsnake; r>=0; r--){
               if(r == 0){
                   snakeylength[r] = snakeylength[r] - 25;
               }
               else{
                   snakeylength[r] = snakeylength[r-1];
               }
               if(snakeylength[r] < 75){
                   snakeylength[r] = 625;
               }
           }
           
           repaint();
        }
        if(down){
            for(int r = lengthofsnake-1; r>=0; r--) {
               snakexlength[r+1] = snakexlength[r];
           }
           for(int r = lengthofsnake; r>=0; r--){
               if(r == 0){
                   snakeylength[r] = snakeylength[r] + 25;
               }
               else{
                   snakeylength[r] = snakeylength[r-1];
               }
               if(snakeylength[r] > 625){
                   snakeylength[r] = 75;
               }
           }
           
           repaint();
        }
    }

    private void setFocusTraversalKeysEnable(boolean b) {
        
    }
    //Screen menu
    public void menu(Graphics g){
        //semua gerakan di stop
        up = false;
        down = false;
        right = false;
        left = false;
        //menampilkan Options di menu
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 50));
        g.drawString("Menu", 380, 300);
        //Highlight sesuai menu yg dipilih
        //(menu yang dipilih warna putih yang tidak dipilih warna abu gelap
        if(menu_pick_num == 0){
            g.drawRect(200, 350,100, 40);
            g.setFont(new Font("arial",Font.BOLD,20));
            g.drawString("Continue",210, 375);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(390,350,100,40);
            g.drawString("Help",420, 375);
            g.drawRect(580, 350, 100, 40);
            g.drawString("Quit",610, 375);
        }
        if(menu_pick_num == 1){
            g.setColor(Color.DARK_GRAY);
            g.drawRect(200, 350,100, 40);
            g.setFont(new Font("arial",Font.BOLD,20));
            g.drawString("Continue",210, 375);
            g.setColor(Color.WHITE);
            g.drawRect(390,350,100,40);
            g.drawString("Help",420, 375);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(580, 350, 100, 40);
            g.drawString("Quit",610, 375);
        }
        if(menu_pick_num == 2){
            g.setColor(Color.DARK_GRAY);
            g.drawRect(200, 350,100, 40);
            g.setFont(new Font("arial",Font.BOLD,20));
            g.drawString("Continue",210, 375);
            g.drawRect(390,350,100,40);
            g.drawString("Help",420, 375);
            g.setColor(Color.WHITE);
            g.drawRect(580, 350, 100, 40);
            g.drawString("Quit",610, 375);
        }
    }
    //Screen help isinya sedikit penjelasan tentang gamenya
    public void help(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 50));
        g.drawString("Help", 380, 300);
        g.setFont(new Font("arial", Font.BOLD, 20));
        g.drawString("Goalnya adalah memakan apel sebanyak - banyak ",200,330);
        g.drawString("dan menghindari tabrakan dengan bagian tubuh ular",185,350);
    }
    //buat set isi dari variable highscore
    public void HighScore(){
        File high_score_file = new File("snake_score.txt");
        String score;
        if(!high_score_file.exists()){
            try {
                high_score_file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            highscore = 0;
        }
        else{
            try {
                Scanner scanner = new Scanner(high_score_file);
                        score = scanner.nextLine();
                        highscore = Integer.parseInt(score);
                    
                    scanner.close();
                } catch (FileNotFoundException ex) {
                Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }
}
