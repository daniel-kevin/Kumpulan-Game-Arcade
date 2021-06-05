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
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Daniel
 */
//class buat ghost dalam game pacman
public class Ghost extends Entity{
    public int ghost_num;
    public boolean weak =  false;//state dari ghostnya (kalau weaknya true bisa dimakan sama pacman)
    public boolean moving = false;//(cek sedang gerak atau tidak
    
    public int sprite_num = 0;//menentukan sprite yang akan dirender
    public int turning = 0;//turning counter biar coba ganti arah kalau sudah gerak minimal 1 kotak
    public int recov_counter = 0;//recovery counter agar balik ke state non-weak
    public int point;//pointnya ghost
    public Image sprite = null;//sprite yang akan dirender
    
    public Ghost(int x, int y, int lebar, int tinggi, Id id, Game_handler handler,int ghost_num) {
        super(x, y, lebar, tinggi, id, handler);
        this.ghost_num = ghost_num;
        this.setPoint(20);//ghost kalau dimakan bakal ngasih 20 point
    }
    //render ghost sprite
    @Override
    public void render(Graphics g) {
        if(weak == false){
            if(sprite_num == 0){
                sprite = new ImageIcon("src/PacmanImage/ghost1.png").getImage();
            }
            else if(sprite_num == 1){
                sprite = new ImageIcon("src/PacmanImage/ghost2.png").getImage();
            }
        }
        else if(weak == true){
            sprite = new ImageIcon("src/PacmanImage/ghost2weak1.png").getImage();
        }
        g.drawImage(sprite, x,y, null);
    }
    //pergerakan + collision detection dari ghost
    //update letak ghost dan sprite yang harus dirender
    @Override
    public void update() {
        //untuk pergerakan awal
        if(moving == false){
            start_move();
        }
        move();
        if(sprite_num == 0){
            sprite_num = 1;
        }
        else if(sprite_num == 1){
            sprite_num = 0;
        }
        //untuk lorong di tengah map biar dari satu ujung muncul di ujung yang lainnya lagi
        teleport();

        //collision detection antara ghost dengan wall
        wall_collision();
        turning++;
        //mencoba ganti arah kalau nilai turning sudah mencapai 10
        if(turning == 10){
        turn();
        turning = 0;
        }
        //Recovery dari state weak
        if(this.IsWeak() == true){
            recov_counter--;
            if(recov_counter == 0){
                this.weak = false;
            }
        }
        
        
    }
    //pergerakan ghost(arah)
    public void moveUp(){
        velY = -2;
    }
    public void moveDown(){
        velY = 2;
    }
    public void moveLeft(){
        velX = -2;
    }
    public void moveRight(){
        velX = 2;
    }
    public boolean IsWeak(){
        return weak;
    }
    public int getPoint(){
        return point;
    }
    public void setPoint(int point){
        this.point = point;
    }
    //untuk memulai gerak
    //arah pergerakan awal bakal random
    public void start_move(){
        int random_direction;
        Random random = new Random(System.currentTimeMillis() + ghost_num);
        
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
    //pergerakan ghost
    public void move(){
        this.x+=velX;
        this.y+=velY;
    }
    //untuk muncul di ujung yang lain (di lorong di tengah map)
    public void teleport(){
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
    }
    //Collision detection dengan wall
    public void wall_collision(){
            for(int i = 0;i < handler.tiles.size();i++){
            Tile tiles = handler.tiles.get(i);
            if(tiles.getID() == Id.wall){
                //kalau tabrakan akan manggil method change_direction buat ganti arah
                if(getBoundsBottom().intersects(tiles.getBounds())){
                    setVelY(0);
                    y = tiles.y - tinggi;
                    change_direction(2);
                    turning = 0;
                }
                if(getBoundsTop().intersects(tiles.getBounds())){
                    setVelY(0);
                    y = tiles.y + tiles.tinggi;
                    change_direction(0);
                    turning = 0;
                }
                if(getBoundsRight().intersects(tiles.getBounds())){
                    setVelX(0);
                    x = tiles.x - lebar;
                    change_direction(1);
                    turning = 0;
                }
                if(getBoundsLeft().intersects(tiles.getBounds())){
                    setVelX(0);
                    x = tiles.x + tiles.lebar;
                    change_direction(3);
                    turning = 0;
                }
            }
        }
    }
    //untuk ganti arah
    public void turn(){
            for(int i = 0;i < handler.tiles.size();i++){
            Tile tiles = handler.tiles.get(i);
            if(tiles.getID() == Id.wall){
                    if((cekMoveUp().intersects(tiles.getBounds()) == false)&&(cekMoveRight().intersects(tiles.getBounds()) == false)&&(cekMoveDown().intersects(tiles.getBounds()) == false)&&(cekMoveLeft().intersects(tiles.getBounds()) == false)){
                    change_direction(4);
                }
            }
            }        

    }
    //untuk ganti arah kalau tabrakan sama wall atau ada jalan buat belok
    //atau untuk ganti arah kalau memungkinkan
    public void change_direction(int collision_index){
        int direction_index;
        int random_index;
        Random random = new Random(System.currentTimeMillis() + ghost_num);
        
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
        if(collision_index == 4){
            direction_index = random.nextInt(4);
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
    //untuk cek ada tile dekat arah gerak
    public Rectangle cekMoveUp(){
        return new Rectangle(getX(),getY()-getTinggi(),getLebar(),getTinggi());
    }
    public Rectangle cekMoveRight(){
        return new Rectangle(getX()+getLebar(),getY(),getLebar(),getTinggi());
    }
    public Rectangle cekMoveDown(){
        return new Rectangle(getX(),getY()+getTinggi(),getLebar(),getTinggi());
    }
    public Rectangle cekMoveLeft(){
        return new Rectangle(getX()-getLebar(),getY(),getLebar(),getTinggi());
    }
}
