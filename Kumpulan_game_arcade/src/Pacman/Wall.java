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

/**
 *
 * @author Daniel
 */
//class buat wall di pacman
public class Wall extends Tile{

    public Wall(int x, int y, int lebar, int tinggi, Id id, Game_handler handler) {
        super(x, y, lebar, tinggi, id, handler);
    }
    //untuk sementara wall direpresentasikan sebagai
    //kotak warna biru
    @Override
    public void render(Graphics g) {
        g.setColor(Color.blue);
        g.fillRect(x,y , lebar, tinggi);
    }

    @Override
    public void update() {
        
    }
    
}
