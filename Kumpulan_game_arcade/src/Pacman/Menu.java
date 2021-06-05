/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacman;

import static Pacman.Board_pacman.SCALE;
import static Pacman.Board_pacman.high_score;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import kumpulan_game_arcade.Main_menu;

/**
 *
 * @author Daniel
 */
//Class untuk screen menu
public class Menu extends MouseAdapter{
    //method yang dipanggil kalau mouse click sesuatu
    public void mousePressed(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();
        //balik ke game jika pilih continue
        if(mouseOver(mouseX, mouseY,95,110,200,70)){
            Board_pacman.game_state = Board_pacman.state.Game;
        }
        //balik ke main menu jika pilih quit
        if(mouseOver(mouseX, mouseY,115,260, 160,60)){
            Board_pacman.game_state = Board_pacman.state.Game;
            Board_pacman.isRunning = false;
            Board_pacman.frame.dispose();
            Main_menu main_menu = new Main_menu();
            main_menu.setVisible(true);
        }
        //masuk ke help screen jika pilih help
        if(mouseOver(mouseX, mouseY,105,190,180,60) && Board_pacman.game_state == Board_pacman.state.Menu){
            Board_pacman.game_state = Board_pacman.state.Help;
        }
    }
    public void mouseReleased(MouseEvent e){
        
    }
    //cek lokasi mouse(cursor)
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
    public void update(){
    
    }
    //render screen menu
    public void render(Graphics g){
        Font font1 = new Font("Courier",1,70);
        Font font2 = new Font("Courier",1,40);
        g.setFont(font1);
        g.setColor(Color.WHITE);
        g.drawString("Menu",105,80);
        g.setFont(font2);
        g.setColor(Color.WHITE);
        g.drawString("Continue",110,160);
        g.drawString("Help",155,230);
        g.drawString("Quit",155,300);
        g.drawString("High Score : " + high_score,10, 22*SCALE);
      
        g.setColor(Color.white);
        g.drawRect(95,110, 200,70);
        g.setColor(Color.white);
        g.drawRect(105,190,180,60);
        g.setColor(Color.white);
        g.drawRect(115,260, 160,60);
    }
}
