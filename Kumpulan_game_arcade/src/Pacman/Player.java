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
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Daniel
 */

//class playernya pacman inherit method method yang ada di class entity
public class Player extends Entity{
    public int player_point = 0;//pointnya player bakal bertambah kalau makan point/berhasil makan ghost
    public Board_pacman game;
    public Image sprite = new ImageIcon("src/PacmanImage/PacLeft1.png").getImage();//sprite pacman diawal
    public int sprite_num = 0;//sprite yang dirender akan sesuai dengan sprite num dan arah gerak
    public Player(int x, int y, int lebar, int tinggi, Id id, Game_handler handler,Board_pacman game) {
        super(x, y, lebar, tinggi, id, handler);
        this.game = game;
        
    }
    //render sprite player 
    @Override
    public void render(Graphics g) {
        g.drawImage(sprite, x,y, null);
    }
    //update lokasi player
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
            if(tiles.getID() == Id.wall || tiles.getID() == Id.gate){
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
            if(getBounds().intersects(points.getBounds())){
                if(points.id == id.point){
                    player_point += points.getPoint();
                    points.remove();
                    Board_pacman.point_num--; 
                }
                else if(points.id == id.sp_point){
                    Special_Point sp = (Special_Point) points;
                    player_point += points.getPoint();
                    sp.WeakenGhost();//pakai method special point untuk mengubah state ghost jadi weak
                    points.remove();
                    Board_pacman.point_num--; //setiap ada point kemakan jumlah point di board dikurangi
                    }
                   
                }
                
            }
        //collision detection dengan ghost
        for(int i = 0;i < handler.entities.size();i++){
            Entity entities = handler.entities.get(i);
            if(getBounds().intersects(entities.getBounds())){
                if(entities.id == id.enemy){
                    Ghost ghost = (Ghost) entities;
                    //cek statenya ghost
                    if(ghost.IsWeak() == false){
                        mati();
                        Board_pacman.score = player_point;
                        game.game_over();
                    }
                    if(ghost.IsWeak() == true){
                        ghost.mati();
                        player_point += ghost.getPoint();
                        handler.addEntity(new Ghost(180,180,20,20,Id.enemy, handler,ghost.ghost_num));//ghost respawn
                    }
                }
                    
                }
                
            } 
        }
        
    
    //pergerakan player + ganti sprite
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_UP){
            velY = -2;
            if(sprite_num == 0){
                sprite = new ImageIcon("src/PacmanImage/PacUp1.png").getImage();
                sprite_num = 1;
            }
            else if(sprite_num == 1){
                sprite = new ImageIcon("src/PacmanImage/PacUp2.png").getImage();
                sprite_num = 2;
            }
            else if(sprite_num == 2){
                sprite = new ImageIcon("src/PacmanImage/PacUp3.png").getImage();
                sprite_num = 3;
            }
            else if(sprite_num == 3){
                sprite = new ImageIcon("src/PacmanImage/PacUp2.png").getImage();
                sprite_num = 0;
            }
        }
        if(key == KeyEvent.VK_DOWN){
            velY = 2;
            if(sprite_num == 0){
                sprite = new ImageIcon("src/PacmanImage/PacDown1.png").getImage();
                sprite_num = 1;
            }
            else if(sprite_num == 1){
                sprite = new ImageIcon("src/PacmanImage/PacDown2.png").getImage();
                sprite_num = 2;
            }
            else if(sprite_num == 2){
                sprite = new ImageIcon("src/PacmanImage/PacDown3.png").getImage();
                sprite_num = 3;
            }
            else if(sprite_num == 3){
                sprite = new ImageIcon("src/PacmanImage/PacDown2.png").getImage();
                sprite_num = 0;
            }
        }
        if(key == KeyEvent.VK_RIGHT){
            velX = 2;
            if(sprite_num == 0){
                sprite = new ImageIcon("src/PacmanImage/PacRight1.png").getImage();
                sprite_num = 1;
            }
            else if(sprite_num == 1){
                sprite = new ImageIcon("src/PacmanImage/PacRight2.png").getImage();
                sprite_num = 2;
            }
            else if(sprite_num == 2){
                sprite = new ImageIcon("src/PacmanImage/PacRight3.png").getImage();
                sprite_num = 3;
            }
            else if(sprite_num == 3){
                sprite = new ImageIcon("src/PacmanImage/PacRight2.png").getImage();
                sprite_num = 0;
            }
            
            
        }
        if(key == KeyEvent.VK_LEFT){
            velX = -2;
            if(sprite_num == 0){
                sprite = new ImageIcon("src/PacmanImage/PacLeft1.png").getImage();
                sprite_num = 1;
            }
            else if(sprite_num == 1){
                sprite = new ImageIcon("src/PacmanImage/PacLeft2.png").getImage();
                sprite_num = 2;
            }
            else if(sprite_num == 2){
                sprite = new ImageIcon("src/PacmanImage/PacLeft3.png").getImage();
                sprite_num = 3;
            }
            else if(sprite_num == 3){
                sprite = new ImageIcon("src/PacmanImage/PacLeft2.png").getImage();
                sprite_num = 0;
            }
        }
        if(key == KeyEvent.VK_ESCAPE){
            Board_pacman.game_state = Board_pacman.state.Menu;
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
