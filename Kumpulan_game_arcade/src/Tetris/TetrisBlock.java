package Tetris;

import java.awt.Color;
import java.util.Random;

public class TetrisBlock {
    private int[][] shape;
    private Color color;
    private int x,y;
    private int[][][] shapes;
    private int currentRotation;
    
    private final Color[] availableColors = {Color.green, Color.red, Color.blue};
    
    public TetrisBlock(int [][] shape){
        this.shape = shape; // memberi bentuk tetris 
        
        initShapes();
    }
    
    // method untuk menyimpan 4 posisi rotasi dari sebuah blok 
    private void initShapes(){
        shapes = new int[4][][];
        
        for(int i=0; i<4; i++){
            int r = shape[0].length;
            int c = shape.length;
            
            shapes[i] = new int[r][c];
            
            // loop menukar posisi rows dan columns dari array blok 
            for(int y=0; y<r; y++){
                for(int x=0; x<c; x++){
                    shapes[i][y][x] = shape[c-x-1][y];
                }
            }
            
            shape = shapes[i];
        }
    }
    
    // method memunculkan blok dari luar panel
    public void spawn(int gridWidth){
        Random r = new Random();
        
        currentRotation = r.nextInt(shapes.length);                 // set rotasi awal blok 
        shape = shapes[currentRotation];
        
        y = -getHeight();                                           // posisi y blok di-spawn 
        x = r.nextInt(gridWidth - getWidth());                      // posisi x blok di-spawn 
        
        color = availableColors[r.nextInt(availableColors.length)]; // memberi warna random pada tetris 
    }
    
    public int[][] getShape (){return shape;}
    
    public Color getColor(){return color;}
    
    public int getHeight(){return shape.length;}
    
    public int getWidth(){return shape[0].length;}
    
    public int getX(){return x;}
    
    public void setX(int newX){x = newX;}
    
    public int getY(){return y;}
    
    public void setY(int newY){y = newY;}
    
    public void moveDown(){y++;}
    
    public void moveLeft(){x--;}
    
    public void moveRight(){x++;}
    
    public void rotate(){
        currentRotation++;
        if(currentRotation > 3) currentRotation = 0;
        shape = shapes[currentRotation];
    }
    
    // mengembalikan nilai dari tepi dasar panel 
    public int getBottomEdge(){return y + getHeight();}
    
    // mengembalikan nilai tepi kiri panel 
    public int getLeftEdge(){return x;}
    
    // mengembalikan nilai tepi kanan panel 
    public int getRightEdge(){return x + getWidth();}
}
