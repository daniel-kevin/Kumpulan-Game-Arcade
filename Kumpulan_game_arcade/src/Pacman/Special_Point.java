/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacman;

import GameObject.Entity;
import GameObject.Game_handler;
import GameObject.Id;
import GameObject.Point;
import java.awt.Color;

/**
 *
 * @author Daniel
 */
import java.awt.Graphics;/**
 *
 * @author Daniel
 */
//Class untuk Special Point di Pacman
//Special Point kalau dimakan bakal buat ghost jadi weak (bisa dimakan)
public class Special_Point extends Point{

    public Special_Point(int x, int y, int lebar, int tinggi, Id id, Game_handler handler) {
        super(x, y, lebar, tinggi, id, handler);
        this.setPoint(5);//bakal ngasih 5 point kalau dimakan
    }
    //special point direpresentasikan sebagai lingkaran warna kuning
    @Override
    public void render(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y,lebar,tinggi);
        
    }

    @Override
    public void update() {
    }
    //method untuk ubah state ghost di map jadi weak semua
    public void WeakenGhost(){
        for(int i = 0;i < handler.entities.size();i++){
            Entity entity = handler.entities.get(i);
            if(entity.id == Id.enemy){
                Ghost ghost = (Ghost) entity;
                ghost.weak = true;
                ghost.recov_counter = 300;
            }
        }
    }
}
