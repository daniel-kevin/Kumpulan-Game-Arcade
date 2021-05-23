/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pacman;

import GameObject.Entity;
import GameObject.Game_handler;
import GameObject.Id;
import GameObject.Tile;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author Daniel
 */
//class buat ghost dalam game pacman
public class Ghost extends Entity{
    public boolean weak;//state dari ghostnya (kalau weaknya true bisa dimakan sama pacman)
    public boolean moving = false;//(cek sedang gerak atau tidak
    
    public Ghost(int x, int y, int lebar, int tinggi, Id id, Game_handler handler) {
        super(x, y, lebar, tinggi, id, handler);
    }
    //untuk sementara ghost direpresentasikan sebagai kotak warna pink
    @Override
    public void render(Graphics g) {
        g.setColor(Color.pink);
        g.fillRect(x, y, lebar,tinggi);
    }
    //pergerakan dari ghost
    @Override
    public void update() {
        if(moving == false){
            start_move();
        }
        
        this.x+=velX;
        this.y+=velY;
        //collision detection antara ghost dengan wall
        for(int i = 0;i < handler.tiles.size();i++){
            Tile tiles = handler.tiles.get(i);
            if(tiles.getID() == Id.wall){
                //kalau tabrakan akan manggil method change_direction buat ganti arah
                if(getBoundsBottom().intersects(tiles.getBounds())){
                    setVelY(0);
                    y = tiles.y - tinggi;
                    change_direction(2);
                }
                if(getBoundsTop().intersects(tiles.getBounds())){
                    setVelY(0);
                    y = tiles.y + tiles.tinggi;
                    change_direction(0);
                }
                if(getBoundsRight().intersects(tiles.getBounds())){
                    setVelX(0);
                    x = tiles.x - lebar;
                    change_direction(0);
                }
                if(getBoundsLeft().intersects(tiles.getBounds())){
                    setVelX(0);
                    x = tiles.x + tiles.lebar;
                    change_direction(3);
                }
            }
        }
    }
    //pergerakan ghost(arah)
    public void moveUp(){
        velY = -1;
    }
    public void moveDown(){
        velY = 1;
    }
    public void moveLeft(){
        velX = -1;
    }
    public void moveRight(){
        velX = 1;
    }
    public boolean IsWeak(){
        return weak;
    }
    //untuk memulai gerak
    //arah pergerakan awal bakal random
    public void start_move(){
        int random_direction;
        Random random = new Random(System.currentTimeMillis());
        
        random_direction = random.nextInt(3);
        if(random_direction == 0){
            moveUp();
        }
        if(random_direction == 1){
            moveRight();
        }
        if(random_direction == 2){
            moveLeft();
        }
        moving = true;
    }
    public void move(){
        
    }
    //untuk ganti arah kalau tabrakan sama wall
    public void change_direction(int collision_index){
        int direction_index;
        int random_index;
        Random random = new Random(System.currentTimeMillis());
        
        if(collision_index == 0){
            int[] arr_dir_index = {1,2,3};
            random_index = random.nextInt(3);
            direction_index = arr_dir_index[random_index];
            move_direction(direction_index);
        }
        if(collision_index == 1){
            int[] arr_dir_index = {0,2,3};
            random_index = random.nextInt(3);
            direction_index = arr_dir_index[random_index];
            move_direction(direction_index);
        }
        if(collision_index == 2){
            int[] arr_dir_index = {0,1,3};
            random_index = random.nextInt(3);
            direction_index = arr_dir_index[random_index];
            move_direction(direction_index);
        }
        if(collision_index == 3){
            direction_index = random.nextInt(3);
            move_direction(direction_index);
        }
    }
    //arah gerak sesuai indeks arah
    public void move_direction(int direction_index){
        if(direction_index == 0){
            moveUp();
        }
        if(direction_index == 1){
            moveRight();
        }
        if(direction_index == 2){
            moveDown();
        }
        if(direction_index == 3){
            moveLeft();
        }
    }
}
