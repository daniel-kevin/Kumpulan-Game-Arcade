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
//Class untuk Tiles yang ada dalam game
//Contoh : wall,gate,dll
public abstract class Tile {
    public int x,y;
    public int lebar, tinggi;
    public Id id;
    public Game_handler handler;
    
    public Tile(int x,int y,int lebar,int tinggi,Id id,Game_handler handler){
        this.x = x;
        this.y = y;
        this.lebar = lebar;
        this.tinggi = tinggi;
        this.id = id;
        this.handler = handler;
    }
    
    public abstract void render(Graphics g);
    public abstract void update();
    
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
    public Id getID(){
        return id;
    }
    public void hapus_tile(){
        handler.removeTiles(this);
    }
    //boundary untuk collision detection
    public Rectangle getBounds(){
        return new Rectangle(x,y,lebar,tinggi);
    }
    public Rectangle getBoundsTop(){
        return new Rectangle(x+1,y,lebar-2,1);
    }
    public Rectangle getBoundsBottom(){
        return new Rectangle(x+1,y+tinggi-1,lebar-2,1);
    }
    public Rectangle getBoundsLeft(){
        return new Rectangle(x,y+1,1,tinggi-2);
    }
    public Rectangle getBoundsRight(){
        return new Rectangle(x+lebar-1,y+1,1,tinggi-2);
    }
}
