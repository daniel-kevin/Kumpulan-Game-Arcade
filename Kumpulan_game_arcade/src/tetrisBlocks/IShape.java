package tetrisBlocks;

import Tetris.TetrisBlock;

public class IShape extends TetrisBlock{
    
    public IShape() {
        super(new int[][]{{1,1,1,1}});
    }
    
    @Override
    public void rotate(){
        super.rotate();
        
        // kondisi untuk rotasi 
        if(this.getWidth()==1){         // ketika vertikal 
            this.setX(this.getX() + 1);
            this.setY(this.getY() - 1);
        }else{                          // ketika horizontal 
            this.setX(this.getX() - 1);
            this.setY(this.getY() + 1);
        }
    }
}
