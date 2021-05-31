package Tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.JPanel;
import tetrisBlocks.*;      // import all from tetrisBlocks

public class GameArea extends JPanel{
    
    // ukuran grid
    private int gridRows;
    private int gridColumns;
    private int gridCellSize;
    private Color[][] background;
    
    private TetrisBlock block;
    
    private TetrisBlock[] blocks;
    
    // menggunakan jpanel dari swing GameForm
    public GameArea(JPanel placeholder, int columns){
        // placeholder.setVisible(false);
        this.setBounds(placeholder.getBounds());            // tepi batas dari jframe GameForm 
        this.setBackground(placeholder.getBackground());    // background dari jframe GameForm 
        this.setBorder(placeholder.getBorder());            // tepi border dari jframe GameForm 
        
        gridColumns = columns;                              // jumlah kolom
        gridCellSize = this.getBounds().width/gridColumns;  // ukuran cell
        gridRows = this.getBounds().height/gridCellSize;    // jumlah baris
        
        blocks = new TetrisBlock[]{ new IShape(),
                                    new JShape(),
                                    new LShape(),
                                    new OShape(),
                                    new SShape(),
                                    new TShape(),
                                    new ZShape()
        };  // memanggil semua method masing-masing shape 
    }
    
    public void initBackgroundArray(){
        background = new Color[gridRows][gridColumns];
    }
    
    // method memunculkan blok bentuk tetris 
    public void spawnBlock(){
        Random r = new Random();
        
        block = blocks[r.nextInt(blocks.length)];   // randomize tetris shape 
        block.spawn(gridColumns);
    }
    
    // mengecek posisi stack blok background 
    public boolean isBlockOutOfBounds(){
        if(block.getY()<0){
            block = null;
            return true;
        }
        return false;
    }
    
    // menggerakkan blok ke bawah 
    public boolean moveBlockDown(){
        // kondisi utk menghentikan pergerakan ke bawah 
        if(checkBottom() == false){
            return false;               // menghentikan gerakan block tetris 
        }
        
        block.moveDown();   // method menggerakkan blok ke bawah
        repaint();          // mengulang paint
        return true;        // meneruskan gerakan block tetris 
    }
    
    // menggerakkan blok ke kanan 
    public void moveBlockRight(){
        // menghentikan penerimaan input user 
        if(block == null) return;
        
        // kondisi utk menghentikan pergerakan ke kanan 
        if(!checkRight()) return;
        
        block.moveRight();  // x++ 
        repaint();
    }
    
    // menggerakkan blok ke kiri 
    public void moveBlockLeft(){
        // menghentikan penerimaan input user 
        if(block == null) return;
        
        // kondisi utk menghentikan pergerakan ke kiri 
        if(!checkLeft()) return;
        
        block.moveLeft();   // x-- 
        repaint();
    }
    
    public void dropBlock(){
        // menghentikan penerimaan input user 
        if(block == null) return;
        
        while(checkBottom()){
            block.moveDown();
        }
        repaint();
    }
    
    public void rotateBlock(){
        // menghentikan penerimaan input user 
        if(block == null) return;
        // if(checkRotate()) return;
        block.rotate();
        
        // memposisikan tetris agar tidak keluar dari tepi saat rotasi 
        if(block.getLeftEdge()<0) block.setX(0);
        if(block.getRightEdge()>=gridColumns) block.setX(gridColumns - block.getWidth());
        if(block.getBottomEdge()>=gridRows) block.setY(gridRows-block.getHeight());
        
        repaint();
    }
    
    private boolean checkBottom(){
        // mengecek posisi blok terhadap tepi bawah 
        if(block.getBottomEdge() == gridRows){
            return false;
        }
        
        // mengecek posisi blok terhadap blok lainnya 
        int[][]shape = block.getShape();
        int w = block.getWidth();
        int h = block.getHeight();
        
        for(int col=0; col<w; col++){
            for(int row=h-1; row>=0; row--){
                if(shape[row][col] != 0){
                    int x = col + block.getX();
                    int y = row + block.getY() + 1;
                    if(y<0) break;
                    if(background[y][x] != null) return false;
                    break;
                }
            }
        }
        
        return true;
    }
    
    private boolean checkLeft(){
        // mengecek posisi blok terhadap tepi kiri 
        if(block.getLeftEdge() == 0)return false;
        
        // mengecek posisi blok terhadap blok lainnya 
        int[][]shape = block.getShape();
        int w = block.getWidth();
        int h = block.getHeight();
        
        for(int row=0; row<h; row++){
            for(int col=0; col<w; col++){
                if(shape[row][col] != 0){
                    int x = col + block.getX() - 1;
                    int y = row + block.getY();
                    if(y<0) break;
                    if(background[y][x] != null) return false;
                    break;
                }
            }
        }
        
        return true;
    }
    
    private boolean checkRight(){
        // mengecek posisi blok terhadap tepi kanan 
        if(block.getRightEdge() == gridColumns) return false;
        
        // mengecek posisi blok terhadap blok lainnya 
        int[][]shape = block.getShape();
        int w = block.getWidth();
        int h = block.getHeight();
        
        for(int row=0; row<h; row++){
            for(int col=w-1; col>=0; col--){
                if(shape[row][col] != 0){
                    int x = col + block.getX() + 1;
                    int y = row + block.getY();
                    if(y<0) break;
                    if(background[y][x] != null) return false;
                    break;
                }
            }
        }
        
        return true;
    }
    
    // membersihkan baris blok yang penuh 
    public int clearLines(){
        boolean lineFilled;
        int linesCleared = 0;
        
        for(int r=gridRows-1; r>=0; r--){
            lineFilled = true;
            
            for(int c=0; c<gridColumns; c++){
                if(background[r][c] == null){
                    lineFilled = false;
                    break;
                }
            }
            
            if(lineFilled){
                linesCleared++;
                clearLine(r);
                shiftDown(r);
                clearLine(0);
                
                r++;    // agar clear line dapat dilakukan pada 2 atau lebih line sekaligus
                
                repaint();
            }
        }
        return linesCleared;
    }
    
    // membersihkan line yang penuh 
    private void clearLine(int r){
        for(int i=0; i<gridColumns; i++){
            background[r][i] = null;
        }
    }
    
    // memindahkan array blok dari atas ke bawah 
    private void shiftDown(int r){
        for(int row=r; row>0; row--){
            for(int col=0; col<gridColumns; col++){
                background[row][col] = background[row-1][col];
            }
        }
    }
    
    // membuat block yg menyentuh tepi bawah menjadi background 
    public void moveBlockToBackground(){
        int[][] shape = block.getShape();
        int h = block.getHeight();
        int w = block.getWidth();
        
        int xPos = block.getX();
        int yPos = block.getY();
        
        Color color = block.getColor();
        
        for(int r=0;r<h;r++){
            for(int c=0;c<w;c++){
                if(shape[r][c]==1){
                    // set warna dari block menjadi background 
                    background[r+yPos][c+xPos] = color; 
                }
            }
            
        }
    }
    
    private void drawBlock(Graphics g){
        int h = block.getHeight();          // tinggi petak 
        int w = block.getWidth();           // lebar petak 
        Color c = block.getColor();         // warna 
        int[][] shape = block.getShape();   // bentuk tetris 
        
        for(int row=0;row<h; row++){
            for(int col = 0; col<w;col++){
                if(shape[row][col] == 1){
                    int x = (block.getX() + col) * gridCellSize;    // posisi x tetris selama loop moveDown 
                    int y = (block.getY() + row) * gridCellSize;    // posisi y tetris selama loop moveDown 
                    
                    drawGridSquare(g,c,x,y);
                }
            }
        }
    }
    
    private void drawBackground(Graphics g){
        Color color;
        for (int r = 0; r < gridRows; r++){
            for(int c = 0; c < gridColumns; c++){
                color = background[r][c];
                
                if(color != null){
                    int x = c*gridCellSize;
                    int y = r*gridCellSize;
                    
                    drawGridSquare(g, color, x, y);
                }
            }
        }
    }
    
    private void drawGridSquare(Graphics g, Color color, int x, int y){
        g.setColor(color);                              // set warna tetris / background 
        g.fillRect(x, y, gridCellSize, gridCellSize);   // gambar kotak tetris / background 
        g.setColor(Color.black);                        // set warna tepi kotak tetris / background 
        g.drawRect(x, y, gridCellSize, gridCellSize);   // gambar tepi kotak tetris / background  
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);    // dapat memanggil method dari superclass dalam subclass 
        
        drawBackground(g);          // menggambar background tetris 
        drawBlock(g);               // menggambar blok tetris 
    }
}
