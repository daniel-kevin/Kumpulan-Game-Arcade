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
//Class untuk collectable point dalam game
public abstract class Point {
    public int x,y;//posisi
    public int lebar,tinggi;
    public int point;//point yang bakal diberikan jika di collect
    
    public Id id;//tags
    public Game_handler handler;
    
    public Point(int x,int y, int lebar, int tinggi,Id id,Game_handler handler){
        this.x = x;
        this.y = y;
        this.lebar = lebar;
        this.tinggi = tinggi;
        this.id = id;
        this.handler = handler;
    }
    public abstract void render(Graphics g);
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
    public Id getID(){
        return id;
    }
    public int getPoint(){
        return point;
    }
    public void setPoint(int point){
        this.point = point;
    }
    public Rectangle getBounds(){
        return new Rectangle(x,y,lebar,tinggi);
    }
    public void remove(){
        handler.removePoint(this);
    }
}
