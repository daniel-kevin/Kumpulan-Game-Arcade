/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacman;

import GameObject.Game_handler;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import kumpulan_game_arcade.Main_menu;

/**
 *
 * @author Daniel
 */
//Class untuk game over screen di game Pacman
public class GameOver extends MouseAdapter{
    //method yang akan dipanggil untuk menentukan
    //hal yang akan dilakukan jika mouse ditekan pada lokasi tertentu
       public void mousePressed(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();
        
        if(mouseOver(mouseX, mouseY,95,110, 200,70)){
            Board_pacman.game_state = Board_pacman.state.Game;
        }
        if(mouseOver(mouseX, mouseY,105,190,180,60) && Board_pacman.game_state == Board_pacman.state.Game_over){
            Board_pacman.game_state = Board_pacman.state.Game;
            Board_pacman.isRunning = false;
            Board_pacman.frame.dispose();
            Main_menu main_menu = new Main_menu();
            main_menu.setVisible(true);
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
    //render game over screen
    public void render(Graphics g){
        Font font1 = new Font("Courier",1,60);
        Font font2 = new Font("Courier",1,40);
        g.setFont(font1);
        g.setColor(Color.WHITE);
        g.drawString("GAME OVER",7,80);
        g.setFont(font2);
        g.setColor(Color.WHITE);
        g.drawString("Restart",125,160);
        g.drawString("Quit",155,230);
        g.drawString("Your score : " + Board_pacman.score, 20, 290);
      
        g.setColor(Color.white);
        g.drawRect(95,110, 200,70);
        g.setColor(Color.white);
        g.drawRect(105,190,180,60);
    } 
}
