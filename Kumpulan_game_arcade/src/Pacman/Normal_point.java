/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacman;

import GameObject.Game_handler;
import GameObject.Id;
import GameObject.Point;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Daniel
 */
//class untuk normal point di pacman
//(kalau dimakan hanya nambah point doang)
public class Normal_point extends Point{
    
    public Normal_point(int x, int y, int lebar, int tinggi, Id id, Game_handler handler) {
        super(x, y, lebar, tinggi, id, handler);
        this.setPoint(1);//jumlah point yang bakal dikasih kalau player makan : 1
    }
    //Normal point direpresentasikan sebagai lingkaran warna putih
    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.drawOval(x, y, lebar, tinggi);
    }

    @Override
    public void update() {
        
    }
    
}
