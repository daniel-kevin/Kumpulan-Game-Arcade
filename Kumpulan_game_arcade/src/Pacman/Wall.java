/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacman;

import GameObject.Game_handler;
import GameObject.Id;
import GameObject.Tile;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Daniel
 */
//class buat wall di pacman
public class Wall extends Tile{
    public int sprite_num;//menentukan sprite yang akan dirender
    public Image sprite;//sprite yang akan dirender

    public Wall(int x, int y, int lebar, int tinggi, Id id, Game_handler handler,int sprite_num) {
        super(x, y, lebar, tinggi, id, handler);
        this.sprite_num = sprite_num;
    }
    //render sprite wall sesuai sprite numbernya
    @Override
    public void render(Graphics g) {
        if(sprite_num == 1){
            sprite = new ImageIcon("src/PacmanImage/wall1.png").getImage();
        }
        if(sprite_num == 2){
            sprite = new ImageIcon("src/PacmanImage/wall2.png").getImage();
        }
        if(sprite_num == 3){
            sprite = new ImageIcon("src/PacmanImage/wall3.png").getImage();
        }
        if(sprite_num == 4){
            sprite = new ImageIcon("src/PacmanImage/wall4.png").getImage();
        }
        if(sprite_num == 5){
            sprite = new ImageIcon("src/PacmanImage/wall5.png").getImage();
        }
        if(sprite_num == 6){
            sprite = new ImageIcon("src/PacmanImage/wall6.png").getImage();
        }
        if(sprite_num == 7){
            sprite = new ImageIcon("src/PacmanImage/wall7.png").getImage();
        }
        if(sprite_num == 8){
            sprite = new ImageIcon("src/PacmanImage/wall8.png").getImage();
        }
        if(sprite_num == 9){
            sprite = new ImageIcon("src/PacmanImage/wall9.png").getImage();
        }
        if(sprite_num == 10){
            sprite = new ImageIcon("src/PacmanImage/wall10.png").getImage();
        }
        if(sprite_num == 11){
            sprite = new ImageIcon("src/PacmanImage/wall11.png").getImage();
        }
        if(sprite_num == 12){
            sprite = new ImageIcon("src/PacmanImage/wall12.png").getImage();
        }
        if(sprite_num == 13){
            sprite = new ImageIcon("src/PacmanImage/wall13.png").getImage();
        }
        if(sprite_num == 14){
            sprite = new ImageIcon("src/PacmanImage/wall14.png").getImage();
        }
        if(sprite_num == 15){
            sprite = new ImageIcon("src/PacmanImage/gate.png").getImage();
        }
        g.drawImage(sprite, x, y,null);
    }

    @Override
    public void update() {
        
    }
    
}
