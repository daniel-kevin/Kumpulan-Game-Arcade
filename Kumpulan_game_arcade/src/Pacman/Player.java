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
import GameObject.Tile;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 *
 * @author Daniel
 */

//class playernya pacman inherit method method yang ada di class entity
public class Player extends Entity{
    public int player_point = 0;//pointnya player bakal bertambah kalau makan point/berhasil makan ghost

    public Player(int x, int y, int lebar, int tinggi, Id id, Game_handler handler) {
        super(x, y, lebar, tinggi, id, handler);
        
    }
    //buat sementara player direpresentasikan sebagai kotak warna kuning
    @Override
    public void render(Graphics g) {
        g.setColor(Color.yellow);
        g.fillRect(x, y, lebar,tinggi);
    }
    
    @Override
    public void update() {
        this.x+=velX;
        this.y+=velY;
        //biar kalau keluar map bakal muncul lagi dari arah yang sebaliknya(khusus kanan-kiri)
        if(this.x < 0){
            this.x = 380;
        }
        if(this.x > 380){
            this.x = 0;
        }
        //biar ga keluar dari batas atas atau bawah
        if(this.y < 0){
            this.y = 0;
        }
        if(this.y > 440 - tinggi){
            this.y = 440 - tinggi;
        }
        //collision detection player dengan wall
        for(int i = 0;i < handler.tiles.size();i++){
            Tile tiles = handler.tiles.get(i);
            if(tiles.getID() == Id.wall){
                if(getBoundsBottom().intersects(tiles.getBounds())){
                    setVelY(0);
                    y = tiles.y - tinggi;
                }
                if(getBoundsTop().intersects(tiles.getBounds())){
                    setVelY(0);
                    y = tiles.y + tiles.tinggi;
                }
                if(getBoundsRight().intersects(tiles.getBounds())){
                    setVelX(0);
                    x = tiles.x - lebar;
                }
                if(getBoundsLeft().intersects(tiles.getBounds())){
                    setVelX(0);
                    x = tiles.x + tiles.lebar;
                }
            }
        }
        //collision detection player dengan point
        for(int i = 0;i < handler.points.size();i++){
            Point points = handler.points.get(i);
            if(getBounds().intersects(points.getBounds())&&points.getID()==Id.point){
                player_point += points.getPoint();
                points.remove();
            }
        }
        System.out.println("player point : " + player_point);//buat cek jumlah point(sementara pas fase testing)
    }
    //pergerakan player
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_UP){
            velY = -2;
        }
        if(key == KeyEvent.VK_DOWN){
            velY = 2;
        }
        if(key == KeyEvent.VK_RIGHT){
            velX = 2;
        }
        if(key == KeyEvent.VK_LEFT){
            velX = -2;
        }
    }
    //kalau player berhenti
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_UP){
            velY = 0;
        }
        if(key == KeyEvent.VK_DOWN){
            velY = 0;
        }
        if(key == KeyEvent.VK_RIGHT){
            velX = 0;
        }
        if(key == KeyEvent.VK_LEFT){
            velX = 0;
        }
    }
    
}
