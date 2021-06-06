/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacman;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Image;
import javax.swing.ImageIcon;
/**
 *
 * @author Daniel
 */
//class untuk screen help game pacman
public class Help extends MouseAdapter{
    int page = 1;//page screen help (screen help ada 4 page).Start page dari 1;
    @Override
    //cek kalau mouse ngeclick sesuatu
    public void mousePressed(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();
        
        if(mouseOver(mouseX, mouseY,295,370,75,40)){
            page++;//kalau next page, pagenya numbernya diincrement
            //kalau udah sampai page terakhir balik ke page awal
            if(page > 4){
                page = 1;
            }
        }
        if(mouseOver(mouseX, mouseY,5,370,75,40)){
            page--;//kalau click previous bakal mundur 1 page
            //kalau udah sampai page pertama balik ke page terakhir
            if(page < 1){
                page = 4;
            }
        }
        //kalau pilih back to menu
        if(mouseOver(mouseX, mouseY,135,405,105,19)){
            Board_pacman.game_state = Board_pacman.state.Menu;
        }
    }
    @Override
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
    //render screen help
    public void render(Graphics g){
        Font font1 = new Font("Courier",1,40);
        Font font2 = new Font("Courier",1,30);
        Font font3 = new Font("Courier",1,15);
        Image ghost = null;
         
        g.setFont(font1);
        g.setColor(Color.WHITE);
        g.drawString("Help",150,60);
        g.setFont(font2);
        g.setColor(Color.WHITE);
        //render sesuai page numbernya
        if(page == 1){
            g.drawString("Goal",160,110);
            g.setFont(font3);
            g.setColor(Color.white);
            g.drawString("Goal dari game ini adalah untuk memakan", 10, 150);
            g.drawString("semua point yang ada dalam maze.", 10, 170);
            g.drawString("Hindari ghost jika mereka tidak dalam", 10, 190);
            g.drawString("weakened state.", 10, 210);
            g.drawString("Jika bertabrakan dengan ghost", 10, 230);
            g.drawString("yang tidak dalam weakened state", 10, 250);
            g.drawString("maka akan game over", 10, 270);
        }
        if(page == 2){
           g.drawString("Point",155,110);
           g.setFont(font3);
           g.setColor(Color.white);
           g.fillOval(50, 142,20,20);
           g.drawString(": Point +1", 75, 157);
           g.setColor(Color.yellow);
           g.fillOval(50, 170, 20, 20);
           g.setColor(Color.white);
           g.drawString(": Point +5,ghost state : weakened", 75, 185);
        }
        if(page == 3){
           g.drawString("Ghost",150,110);
           g.setFont(font3);
           ghost = new ImageIcon("src/PacmanImage/ghost1.png").getImage();
           g.drawImage(ghost,80,160,null);
           g.drawString(": Ghost tidak dalam weak state.",120,175);
           g.drawString("  Jika bertabrakan akan game over.",120,195);
           ghost = new ImageIcon("src/PacmanImage/ghost2weak1.png").getImage();
           g.drawImage(ghost, 80, 220,null);
           g.drawString(": Ghost dalam weak state.",120,235);
           g.drawString("  +20 Point jika dimakan.",120,255);  
           g.drawString("  *State ini hanya berlangsung",120,275);
           g.drawString("   selama 10 detik",120,295);
        }
        if(page == 4){
           g.drawString("Control",140,110);
           g.setFont(font3);
           g.setColor(Color.white);
           g.drawString("Move : Arrow Key", 10, 150);
           g.drawString("Menu : Escape Key", 10, 170);
        }
        g.setFont(font2);
        g.drawString("Next",300,400);
        g.drawString("Prev",10,400);
        g.drawRect(295,370,75,40);
        g.drawRect(5,370,75,40);
        g.setFont(font3);
        g.drawString("Back to Menu",140, 420);
        g.drawRect(135,405,105,19);
    }    
}
