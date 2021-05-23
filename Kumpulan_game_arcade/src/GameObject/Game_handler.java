/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObject;

import java.awt.Graphics;
import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
//game handler isinya list entitas,tile,dan point
//kalau mau menambahkan entitas,point atau tile bakal ditambahin ke list disini
public class Game_handler {
    public LinkedList<Entity> entities = new LinkedList<Entity>();
    public LinkedList<Tile> tiles = new LinkedList<Tile>();
    public LinkedList<Point> points = new LinkedList<Point>();
    //buat manggil method render dari semua entity,tile,dan point yang ada dalam list
    public void render(Graphics g){
        for(Entity entity:entities){
            entity.render(g);
        }
        for(Tile tile:tiles){
            tile.render(g);
        }
        for(Point point:points){
            point.render(g);
        }
    }
    //buat manggil method update semua entitas,tile,dan point yang ada dilist
    public void update(){
        for(Entity entity:entities){
            entity.update();
        }
        for(Tile tile:tiles){
            tile.update();
        }
        for(Point point:points){
            point.update();
        }
    }
    //method buat menambahkan atau menghapus dari list
    public void addEntity(Entity entity){
        entities.add(entity);
    }
    public void removeEntity(Entity entity){
        entities.remove(entity);
    }
    public void addTile(Tile tile){
        tiles.add(tile);
    }
    public void removeTiles(Tile tile){
        tiles.remove(tile);
    }
    public void addPoint(Point point){
        points.add(point);
    }
    public void removePoint(Point point){
        points.remove(point);
    }
}
