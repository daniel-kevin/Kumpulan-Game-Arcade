/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacman;

import GameObject.AudioPlayer;
import GameObject.Entity;
import GameObject.Game_handler;
import GameObject.Id;
import GameObject.Point;
import java.awt.Dimension;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Period;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Daniel
 */
//Class yang dipakai untuk Board yang dipakai oleh game pacman
public class Board_pacman extends Canvas implements Runnable, ActionListener{
    public static final int TINGGI = 22;
    public static final int LEBAR = 19;
    public static final int SCALE = 20;
    //ukuran board (tinggi dan lebar akan dikalikan dengan scale untuk mendapatkan ukuran aslinya.)
    private Thread thread;
    public static boolean isRunning = false;
    public static int score;
    public static int high_score;
    public static int point_num = 185;//jumlah point yang ada
    public int standby_counter = 300;//untuk countdown saat statenya standby (sekitar 5 detik)
    public Game_handler handler;
    public Player player;
    public static JFrame frame;
    public static state game_state = state.Game;
    public Menu menu;//untuk screen menu
    public Help help;//untuk screen help
    public GameOver gameOver;//untuk game over screen
    public AudioPlayer audioPlayer;
    public Date startTime;
    
    
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Board_pacman() {
        Dimension dimensi = new Dimension(LEBAR * SCALE, TINGGI * SCALE);
        setPreferredSize(dimensi);
        setMaximumSize(dimensi);
        setMinimumSize(dimensi);
    }
    //game state menentukan apa yang dirender dan diupdate
    public enum state{
        Menu,Game,Help,Game_over,Standby
    };

    
    private void init(){
        handler = new Game_handler();
        player = new Player(180,220,15,15,Id.player,handler,this);
        addKeyListener(new Player_action());
        audioPlayer = new AudioPlayer();
        menu = new Menu(audioPlayer);
        help = new Help();
        gameOver = new GameOver(audioPlayer);
        
        addMouseListener(menu);
        addMouseListener(help);
        addMouseListener(gameOver);
        handler.addEntity(player);
        game_wall();
        game_points();
        point_num = 185;
        game_ghost();
        HighScore();
        audioPlayer.PlayBGM("Pacman BGM.wav");
    }
    
    private synchronized void start(){
        if(isRunning == true){
            return;//kalau sedang running langsung keluar dari method ini
        }
        else {
            isRunning = true;
            thread = new Thread(this);
            thread.start();//memulai thread
        }
    }
    
    private synchronized void stop(){
        if(isRunning == false){
            return;//kalau memang tidak running langsung keluar dari method ini
        }
        else {
            isRunning = false;
            try {
                thread.join();//stop thread
            } catch (InterruptedException ex) {
                Logger.getLogger(Board_pacman.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //method untuk melakukan rendering
    public void render(){
        BufferStrategy bufferstrategy = getBufferStrategy();
        if(bufferstrategy == null){
            createBufferStrategy(3);
            return;
        }
        Graphics g = bufferstrategy.getDrawGraphics();
        g.setColor(Color.BLACK);//warna background
        g.fillRect(0, 0, getWidth(), getHeight());
        if(game_state == state.Game || game_state == state.Standby ){
            handler.render(g);
            Font font =  new Font("Courier",1,20);
            g.setFont(font);
            g.setColor(Color.white);
            g.drawString("Score : " + player.player_point,10,22*SCALE);//scorenya player
            g.drawString("Sisa : " + point_num,280, 22*SCALE);//sisa point yang ada di map
            if(game_state == state.Standby){
                g.drawString(Integer.toString(standby_counter/60), 9*SCALE +5,11*SCALE-3);//memunculkan countdown standby phase
            }
        }
        else if(game_state == state.Menu){
            menu.render(g);
        }
        else if(game_state == state.Help){
            help.render(g);
        }
        else if(game_state == state.Game_over){
            gameOver.render(g);
        }
        
        g.dispose();
        bufferstrategy.show();
    }
    
    public void update(){
        if(game_state == state.Game){
            handler.update();
           
        }
        else if(game_state == state.Menu){
            menu.update();
        }
        else if(game_state == state.Help){
            help.update();
        }
        else if(game_state == state.Game_over){
            gameOver.update();
        }
        else if(game_state == state.Standby){
            standby_counter--;
            if(standby_counter == 0){
                game_state = state.Game;
            }
        }
        //kalau point di board sudah habis game direstart (point player tidak ikut direstart)
        if(point_num == 0){
            restartBoard();
        }

    }
    public void show_board() {
        Board_pacman board = new Board_pacman();
        frame = new JFrame();
        frame.add(board);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board.start();
    }
    //ini game loopnya
    @Override
    public void run() {
       init();
       requestFocus();
       long lastTime = System.nanoTime();
       startTime = new Date();
       double delta = 0;
       double ns = 1000000000/60;
       while(isRunning == true){
           long now = System.nanoTime();
           delta += (double)(now-lastTime)/ns;
           lastTime = now;
           while(delta >= 1){
               update();
               delta--;
           }
           render();
           
       }
        stop();
    }
    public class Player_action extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            player.keyPressed(e);
        }
        @Override
        public void keyReleased(KeyEvent e){
            player.keyReleased(e);
        }
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
       
    }
    //Menambahkan wall pada map game
    public void game_wall(){
        handler.addTile(new Wall(0,0,1*SCALE,1*SCALE,Id.wall,handler,1));
        handler.addTile(new Wall(20,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(40,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(60,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(80,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(100,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(120,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(140,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(160,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(180,0,1*SCALE,1*SCALE,Id.wall,handler,3));
        handler.addTile(new Wall(200,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(220,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(240,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(260,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(280,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(300,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(320,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(340,0,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(360,0,1*SCALE,1*SCALE,Id.wall,handler,4));
        handler.addTile(new Wall(0,20,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(180,20,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(360,20,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(0,40,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(40,40,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(60,40,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(100,40,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(120,40,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(140,40,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(180,40,1*SCALE,1*SCALE,Id.wall,handler,8));
        handler.addTile(new Wall(220,40,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(240,40,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(260,40,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(300,40,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(320,40,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(360,40,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(0,60,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(360,60,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(0,80,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(40,80,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(60,80,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(100,80,1*SCALE,1*SCALE,Id.wall,handler,9));
        handler.addTile(new Wall(140,80,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(160,80,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(180,80,1*SCALE,1*SCALE,Id.wall,handler,3));
        handler.addTile(new Wall(200,80,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(220,80,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(260,80,1*SCALE,1*SCALE,Id.wall,handler,9));
        handler.addTile(new Wall(300,80,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(320,80,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(360,80,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(0,100,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(100,100,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(180,100,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(260,100,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(360,100,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(0,120,1*SCALE,1*SCALE,Id.wall,handler,10));
        handler.addTile(new Wall(100,120,1*SCALE,1*SCALE,Id.wall,handler,11));
        handler.addTile(new Wall(180,120,1*SCALE,1*SCALE,Id.wall,handler,8));
        handler.addTile(new Wall(260,120,1*SCALE,1*SCALE,Id.wall,handler,12));
        handler.addTile(new Wall(360,120,1*SCALE,1*SCALE,Id.wall,handler,13));
        handler.addTile(new Wall(0,120,1*SCALE,1*SCALE,Id.wall,handler,10));
        handler.addTile(new Wall(20,120,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(40,120,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(60,120,1*SCALE,1*SCALE,Id.wall,handler,4));
        handler.addTile(new Wall(100,120,1*SCALE,1*SCALE,Id.wall,handler,11));
        handler.addTile(new Wall(120,120,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(140,120,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(180,120,1*SCALE,1*SCALE,Id.wall,handler,8));
        handler.addTile(new Wall(220,120,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(240,120,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(260,120,1*SCALE,1*SCALE,Id.wall,handler,12));
        handler.addTile(new Wall(300,120,1*SCALE,1*SCALE,Id.wall,handler,1));
        handler.addTile(new Wall(320,120,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(340,120,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(360,120,1*SCALE,1*SCALE,Id.wall,handler,13));
        handler.addTile(new Wall(-20,160,1*SCALE,1*SCALE,Id.wall,handler,1));
        handler.addTile(new Wall(0,160,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(20,160,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(40,160,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(60,140,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(100,140,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(260,140,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(300,140,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(60,160,1*SCALE,1*SCALE,Id.wall,handler,13));
        handler.addTile(new Wall(100,160,1*SCALE,1*SCALE,Id.wall,handler,8));
        handler.addTile(new Wall(140,160,1*SCALE,1*SCALE,Id.wall,handler,1));
        handler.addTile(new Wall(160,160,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(200,160,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(220,160,1*SCALE,1*SCALE,Id.wall,handler,4));
        handler.addTile(new Wall(260,160,1*SCALE,1*SCALE,Id.wall,handler,8));
        handler.addTile(new Wall(300,160,1*SCALE,1*SCALE,Id.wall,handler,10));
        handler.addTile(new Wall(320,160,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(340,160,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(360,160,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(380,160,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(140,180,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(220,180,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(-20,200,1*SCALE,1*SCALE,Id.wall,handler,1));
        handler.addTile(new Wall(0,200,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(20,200,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(40,200,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(60,200,1*SCALE,1*SCALE,Id.wall,handler,4));
        handler.addTile(new Wall(100,200,1*SCALE,1*SCALE,Id.wall,handler,9));
        handler.addTile(new Wall(140,200,1*SCALE,1*SCALE,Id.wall,handler,10));
        handler.addTile(new Wall(160,200,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(180,200,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(200,200,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(220,200,1*SCALE,1*SCALE,Id.wall,handler,13));
        handler.addTile(new Wall(260,200,1*SCALE,1*SCALE,Id.wall,handler,9));
        handler.addTile(new Wall(300,200,1*SCALE,1*SCALE,Id.wall,handler,1));
        handler.addTile(new Wall(320,200,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(340,200,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(360,200,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(380,200,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(60,220,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(100,220,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(260,220,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(300,220,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(0,240,1*SCALE,1*SCALE,Id.wall,handler,1));
        handler.addTile(new Wall(20,240,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(40,240,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(60,240,1*SCALE,1*SCALE,Id.wall,handler,13));
        handler.addTile(new Wall(100,240,1*SCALE,1*SCALE,Id.wall,handler,8));
        handler.addTile(new Wall(140,240,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(160,240,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(180,240,1*SCALE,1*SCALE,Id.wall,handler,3));
        handler.addTile(new Wall(200,240,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(220,240,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(260,240,1*SCALE,1*SCALE,Id.wall,handler,8));
        handler.addTile(new Wall(300,240,1*SCALE,1*SCALE,Id.wall,handler,10));
        handler.addTile(new Wall(320,240,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(340,240,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(360,240,1*SCALE,1*SCALE,Id.wall,handler,4));
        handler.addTile(new Wall(0,260,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(180,260,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(360,260,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(0,280,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(40,280,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(60,280,1*SCALE,1*SCALE,Id.wall,handler,4));
        handler.addTile(new Wall(100,280,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(120,280,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(140,280,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(180,280,1*SCALE,1*SCALE,Id.wall,handler,8));
        handler.addTile(new Wall(220,280,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(240,280,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(260,280,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(300,280,1*SCALE,1*SCALE,Id.wall,handler,1));
        handler.addTile(new Wall(320,280,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(360,280,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(0,300,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(60,300,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(300,300,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(360,300,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(0,320,1*SCALE,1*SCALE,Id.wall,handler,11));
        handler.addTile(new Wall(20,320,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(60,320,1*SCALE,1*SCALE,Id.wall,handler,8));
        handler.addTile(new Wall(100,320,1*SCALE,1*SCALE,Id.wall,handler,9));
        handler.addTile(new Wall(140,320,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(160,320,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(180,320,1*SCALE,1*SCALE,Id.wall,handler,3));
        handler.addTile(new Wall(200,320,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(220,320,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(260,320,1*SCALE,1*SCALE,Id.wall,handler,9));
        handler.addTile(new Wall(300,320,1*SCALE,1*SCALE,Id.wall,handler,8));
        handler.addTile(new Wall(340,320,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(360,320,1*SCALE,1*SCALE,Id.wall,handler,12));
        handler.addTile(new Wall(0,340,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(100,340,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(180,340,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(260,340,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(360,340,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(0,360,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(40,360,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(60,360,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(80,360,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(100,360,1*SCALE,1*SCALE,Id.wall,handler,14));
        handler.addTile(new Wall(120,360,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(140,360,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(180,360,1*SCALE,1*SCALE,Id.wall,handler,8));
        handler.addTile(new Wall(220,360,1*SCALE,1*SCALE,Id.wall,handler,6));
        handler.addTile(new Wall(240,360,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(260,360,1*SCALE,1*SCALE,Id.wall,handler,14));
        handler.addTile(new Wall(280,360,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(300,360,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(320,360,1*SCALE,1*SCALE,Id.wall,handler,7));
        handler.addTile(new Wall(360,360,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(0,380,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(360,380,1*SCALE,1*SCALE,Id.wall,handler,5));
        handler.addTile(new Wall(0,400,1*SCALE,1*SCALE,Id.wall,handler,10));
        handler.addTile(new Wall(20,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(40,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(60,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(80,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(100,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(120,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(140,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(160,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(180,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(200,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(220,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(240,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(260,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(280,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(300,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(320,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(340,400,1*SCALE,1*SCALE,Id.wall,handler,2));
        handler.addTile(new Wall(360,400,1*SCALE,1*SCALE,Id.wall,handler,13));
        handler.addTile(new Wall(9*SCALE,8*SCALE + 6,1*SCALE,1*SCALE,Id.gate,handler,15));
    }
    //menambahkan points pada map game
    public void game_points(){
        handler.addPoint(new Normal_point(1*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(2*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(3*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(5*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(7*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(8*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(10*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(11*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(13*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(15*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(16*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(17*SCALE+7,1*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Special_Point(1*SCALE+5,2*SCALE+5,9,9,Id.sp_point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,2*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(8*SCALE+7,2*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(10*SCALE+7,2*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,2*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Special_Point(17*SCALE+5,2*SCALE+5,9,9,Id.sp_point, handler));
        handler.addPoint(new Normal_point(1*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(2*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(3*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(5*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(7*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(8*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(9*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(10*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(11*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(13*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(15*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(16*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(17*SCALE+7,3*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(1*SCALE+7,4*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,4*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,4*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,4*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,4*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(17*SCALE+7,4*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(1*SCALE+7,5*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(2*SCALE+7,5*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(3*SCALE+7,5*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,5*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,5*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(7*SCALE+7,5*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(8*SCALE+7,5*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(10*SCALE+7,5*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(11*SCALE+7,5*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,5*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,5*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(15*SCALE+7,5*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(16*SCALE+7,5*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(17*SCALE+7,5*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,6*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(8*SCALE+7,6*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(10*SCALE+7,6*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,6*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,7*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,7*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(7*SCALE+7,7*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(8*SCALE+7,7*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(9*SCALE+7,7*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(10*SCALE+7,7*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(11*SCALE+7,7*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,7*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,7*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,8*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,8*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,8*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,8*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(0*SCALE+7,9*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(1*SCALE+7,9*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(2*SCALE+7,9*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(3*SCALE+7,9*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,9*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(5*SCALE+7,9*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,9*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,9*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(13*SCALE+7,9*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,9*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(15*SCALE+7,9*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(16*SCALE+7,9*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(17*SCALE+7,9*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(18*SCALE+7,9*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,10*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,10*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,10*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,10*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,11*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,11*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(7*SCALE+7,11*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(8*SCALE+7,11*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(9*SCALE+7,11*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(10*SCALE+7,11*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(11*SCALE+7,11*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,11*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,11*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,12*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,12*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,12*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,12*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(1*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(2*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(3*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(5*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(7*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(8*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(10*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(11*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(13*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(15*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(16*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(17*SCALE+7,13*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Special_Point(1*SCALE+5,14*SCALE+5,9,9,Id.sp_point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,14*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(8*SCALE+7,14*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(10*SCALE+7,14*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,14*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Special_Point(17*SCALE+5,14*SCALE+5,9,9,Id.sp_point, handler));
        handler.addPoint(new Normal_point(1*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(2*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(5*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(7*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(8*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(9*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(10*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(11*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(13*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(16*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(17*SCALE+7,15*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(2*SCALE+7,16*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,16*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,16*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,16*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,16*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(16*SCALE+7,16*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(1*SCALE+7,17*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(2*SCALE+7,17*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(3*SCALE+7,17*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,17*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,17*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(7*SCALE+7,17*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(8*SCALE+7,17*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(10*SCALE+7,17*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(11*SCALE+7,17*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,17*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,17*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(15*SCALE+7,17*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(16*SCALE+7,17*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(17*SCALE+7,17*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(1*SCALE+7,18*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(8*SCALE+7,18*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(10*SCALE+7,18*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(17*SCALE+7,18*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(1*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(2*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(3*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(4*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(5*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(6*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(7*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(8*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(9*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(10*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(11*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(12*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(13*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(14*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(15*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(16*SCALE+7,19*SCALE+7,5,5,Id.point, handler));
        handler.addPoint(new Normal_point(17*SCALE+7,19*SCALE+7,5,5,Id.point, handler));   
    }
    //menambahkan ghost ke map game
    public void game_ghost(){
        handler.addEntity(new Ghost(9*SCALE,9*SCALE,1*SCALE,1*SCALE,Id.enemy, handler,1));
        handler.addEntity(new Ghost(9*SCALE,9*SCALE,1*SCALE,1*SCALE,Id.enemy, handler,2));
        handler.addEntity(new Ghost(9*SCALE,9*SCALE,1*SCALE,1*SCALE,Id.enemy, handler,3));
        handler.addEntity(new Ghost(9*SCALE,9*SCALE,1*SCALE,1*SCALE,Id.enemy, handler,4));
        handler.addEntity(new Ghost(9*SCALE,9*SCALE,1*SCALE,1*SCALE,Id.enemy, handler,5));
        handler.addEntity(new Ghost(9*SCALE,9*SCALE,1*SCALE,1*SCALE,Id.enemy, handler,6));
    }
    //method yang akan dipanggil jika player collide dengan ghost
    //saat ghost tidak dalam weak state
    public void game_over(){
        game_state = state.Game_over;
        Date endTime = new Date();
        long total_game_time = endTime.getTime() - startTime.getTime();
        long t_game_h = total_game_time/(60 * 60 * 1000) % 24;
        long t_game_m = total_game_time/(60 * 1000) % 60;
        long t_game_s = total_game_time/1000 % 60;
        for(int i = 0;i < handler.entities.size();i++){
                Entity entities = handler.entities.get(i);
                    handler.entities.clear();
            }
            for(int i = 0;i < handler.points.size();i++){
                Point points = handler.points.get(i);
                handler.points.clear();
            }
            handler.addEntity(player);
            player.player_point = 0;
            player.setX(180);
            player.setY(220);
            game_ghost();
            game_points();
            point_num = 185;
            //update highscore seandainya score player melebihi highscore
            if(score > high_score){
            try {
                File high_score_file = new File("pacman_score.txt");
                FileWriter fileWriter = new FileWriter(high_score_file,false);
                fileWriter.write(Integer.toString(score)+"\n"+t_game_h+"\n"+t_game_m+"\n"+t_game_s);
                fileWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(Board_pacman.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            HighScore();//ubah highscore yang ditampilkan
    }
    //method untuk restart board
    //akan dipanggil jika semua point di board sudah dimakan
    public void restartBoard(){
        for(int i = 0;i<handler.entities.size();i++){
                Entity entities = handler.entities.get(i);
                if(entities.getID() == Id.enemy){
                    Ghost ghost = (Ghost) entities;
                    ghost.setX(9*SCALE);
                    ghost.setY(9*SCALE);
                    ghost.weak = false;
                }
            }
        game_points();
        point_num = 185;
        player.setX(180);
        player.setY(220);
        game_state = state.Standby;
        standby_counter = 300;   
    }
    //method untuk mendapatkan highscore yang disimpan dalam file
    public void HighScore(){
        File high_score_file = new File("pacman_score.txt");
        String score;
        if(!high_score_file.exists()){
            try {
                high_score_file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Board_pacman.class.getName()).log(Level.SEVERE, null, ex);
            }
            high_score = 0;
            }
        else{
            try {
                Scanner scanner = new Scanner(high_score_file);
                    score = scanner.nextLine();
                    high_score = Integer.parseInt(score); 
                scanner.close();
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Board_pacman.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
