/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObject;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Daniel
 */

//class buat entitas - entitas yang ada dalam game
//class ini akan diinherit oleh entitas - entitas game seperti : player , enemy,dll
public abstract class Entity {
    public int x,y;//posisi
    public int lebar, tinggi;
    public int velX,velY;//kecepetan (untuk movement)
    public Id id;//tags buat nandain jenis suatu entitas
    
    public Game_handler handler;
    public Entity(int x,int y,int lebar,int tinggi,Id id,Game_handler handler){
        this.x = x;
        this.y = y;
        this.lebar = lebar;
        this.tinggi = tinggi;
        this.id = id;
        this.handler = handler;
    }
    
    public abstract void render(Graphics g);//fungsi untuk render entitasnya
    public abstract void update();
    //getter and setter
    public int getX(){
        return x;
    }
    public void setX(int x){
        this.x = x;
    }
    public int getY(){
        return y;
    }
    public void setY(int y){
        this.y = y;
    }
    public int getLebar(){
        return lebar;
    }
    public void setLebar(int lebar){
        this.lebar = lebar;
    }
    public int getTinggi(){
        return tinggi;
    }
    public void setTinggi(int tinggi){
        this.tinggi = tinggi;
    }
    public int getVelX(){
        return velX;
    }
    public void setVelX(int velX){
        this.velX = velX;
    }
    public int getVelY(){
        return velY;
    }
    public void setVelY(int velY){
        this.velY = velY;
    }
    public Id getID(){
        return id;
    }
    //fungsi ini dipanggil jika entitas mati
    //Contoh : player tabrakan dengan enemy
    public void mati(){
        handler.removeEntity(this);
    }
    //boundary yang bakal dipakai buat cek collision antar objek
    public Rectangle getBounds(){
        return new Rectangle(x,y,lebar,tinggi);
    }
    public Rectangle getBoundsTop(){
        return new Rectangle(x+2,y,lebar-4,2);
    }
    public Rectangle getBoundsBottom(){
        return new Rectangle(x+2,y+tinggi-2,lebar-4,2);
    }
    public Rectangle getBoundsLeft(){
        return new Rectangle(x,y+2,2,tinggi-4);
    }
    public Rectangle getBoundsRight(){
        return new Rectangle(x+lebar-2,y+2,2,tinggi-4);
    }
}
