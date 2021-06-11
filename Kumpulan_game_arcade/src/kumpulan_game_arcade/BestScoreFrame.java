/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kumpulan_game_arcade;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Daniel
 */
public class BestScoreFrame extends Canvas implements MouseListener{
    public static final int TINGGI = 22;
    public static final int LEBAR = 19;
    public static final int SCALE = 20;
    public static JFrame frame;
    public int page = 1;
    public int highscore;
    public int time_s,time_m,time_h;
    
    public BestScoreFrame(){
        Dimension dimensi = new Dimension(LEBAR * SCALE, TINGGI * SCALE);
        setPreferredSize(dimensi);
        setMaximumSize(dimensi);
        setMinimumSize(dimensi);  
        addMouseListener(this);
        updateScreen();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        
        if(mouseOver(mouseX, mouseY,295,370,75,40)){
            page++;//kalau next page, pagenya numbernya diincrement
            //kalau udah sampai page terakhir balik ke page awal
            if(page > 3){
                page = 1;
            }
            updateScreen();
            repaint();
        }
        if(mouseOver(mouseX, mouseY,5,370,75,40)){
            page--;//kalau click previous bakal mundur 1 page
            //kalau udah sampai page pertama balik ke page terakhir
            if(page < 1){
                page = 3;
            }
            updateScreen();
            repaint();
        }
        //kalau pilih back to menu
        if(mouseOver(mouseX, mouseY,135,405,105,19)){
            Main_menu.isBestScoreFrameOpen = false;
            frame.dispose();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    public boolean mouseOver(int mouseX,int mouseY,int x,int y,int lebar, int tinggi){
        if(mouseX > x && mouseX < x + lebar){
            if(mouseY > y && mouseY < y + tinggi){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
    @Override
    public void paint(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0,getWidth(),getHeight());
        Font font1 = new Font("Courier",1,60);
        Font font2 = new Font("Courier",1,30);
        Font font3 = new Font("Courier",1,15);
        g.setFont(font1);
        g.setColor(Color.WHITE);
        if(page == 1){
            g.drawString("Snake",7,80);

        }
        if(page == 2){
            g.drawString("Tetris",7,80); 
        }
        if(page == 3){
            g.drawString("Pacman",7,80); 
        }
        g.setFont(font2);
        g.drawString("Score : " + highscore, 7, 125);
        g.drawString("Total time : "+ time_h +" : " + time_m +" : " + time_s, 7, 175);
        g.fillRect(7, 85, 350, 5);
        g.fillRect(7, 135, 350, 5);
        g.fillRect(7, 185, 350, 5);
        g.setFont(font2);
        g.drawString("Next",300,400);
        g.drawString("Prev",10,400);
        g.drawRect(295,370,75,40);
        g.drawRect(5,370,75,40);
        g.setFont(font3);
        g.drawString("Back to Menu",140, 420);
        g.drawRect(135,405,105,19);
    }
    public void showFrame(){
        BestScoreFrame bestScoreFrame = new BestScoreFrame();
        frame = new JFrame();
        frame.add(bestScoreFrame);
        frame.setUndecorated(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void updateScreen(){
        File file = null;
        if(page == 1){
            file = new File("snake_score.txt");
        }
        else if(page == 2){
            file = new File("tetris_score.txt");
        }
        else if(page == 3){
            file = new File("pacman_score.txt");
        }
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(BestScoreFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            highscore = 0;
        }
        else{
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                for(int i = 0;i < 4;i++){
                    if(i == 0){
                        highscore = Integer.parseInt(br.readLine());
                    }
                    if(i == 1){
                        time_h = Integer.parseInt(br.readLine());
                    }
                    if(i == 2){
                        time_m = Integer.parseInt(br.readLine());
                    }
                    if(i == 3){
                        time_s = Integer.parseInt(br.readLine());
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BestScoreFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BestScoreFrame.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(BestScoreFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
