/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kumpulan_game_arcade;

import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JFrame;
import Pacman.Board_pacman;
import Tetris.GameForm;

/**
 *
 * @author Daniel
 */
public class Main_menu extends javax.swing.JFrame {

    int index_game = 0;
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Main_menu() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        Btn_start = new javax.swing.JLabel();
        Btn_settings = new javax.swing.JLabel();
        Btn_quit = new javax.swing.JLabel();
        Btn_arrow_left = new javax.swing.JLabel();
        Btn_arrow_right = new javax.swing.JLabel();
        game_icon = new javax.swing.JLabel();
        Background = new javax.swing.JLabel();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Btn_start.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/start_button.png"))); // NOI18N
        Btn_start.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Btn_startMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Btn_startMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Btn_startMouseExited(evt);
            }
        });
        getContentPane().add(Btn_start, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 260, -1, 40));

        Btn_settings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/settings_button.png"))); // NOI18N
        getContentPane().add(Btn_settings, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 300, -1, 30));

        Btn_quit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/quit_button.png"))); // NOI18N
        Btn_quit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Btn_quitMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Btn_quitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Btn_quitMouseExited(evt);
            }
        });
        getContentPane().add(Btn_quit, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 335, -1, 20));

        Btn_arrow_left.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/arrow_left_btn.png"))); // NOI18N
        Btn_arrow_left.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Btn_arrow_leftMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Btn_arrow_leftMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Btn_arrow_leftMouseExited(evt);
            }
        });
        getContentPane().add(Btn_arrow_left, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, -1, -1));

        Btn_arrow_right.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/arrow_right_btn.png"))); // NOI18N
        Btn_arrow_right.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Btn_arrow_rightMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Btn_arrow_rightMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Btn_arrow_rightMouseExited(evt);
            }
        });
        getContentPane().add(Btn_arrow_right, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 190, -1, -1));

        game_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/snake_icon.png"))); // NOI18N
        getContentPane().add(game_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 120, 130, 130));

        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/main_menu_bg.jpeg"))); // NOI18N
        getContentPane().add(Background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 430));

        pack();
    }// </editor-fold>                        

    private void Btn_arrow_rightMouseClicked(java.awt.event.MouseEvent evt) {                                             
        index_game++;
        if(index_game > 2){
            index_game = 0;
        }
        if(index_game == 0){
            game_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/snake_icon.png")));
        }
        if(index_game == 1){
            game_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/tetris_icon.png")));
        }
        if(index_game == 2){
            game_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/pacman_icon.png")));
        }

    }                                            

    private void Btn_arrow_leftMouseClicked(java.awt.event.MouseEvent evt) {                                            
        index_game--;
        if(index_game < 0){
            index_game = 2;
        }
        if(index_game == 0){
            game_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/snake_icon.png")));
        }
        if(index_game == 1){
            game_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/tetris_icon.png")));
        }
        if(index_game == 2){
            game_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/pacman_icon.png")));
        }

    }                                           

    private void Btn_arrow_leftMouseEntered(java.awt.event.MouseEvent evt) {                                            
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }                                           

    private void Btn_arrow_leftMouseExited(java.awt.event.MouseEvent evt) {                                           
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }                                          

    private void Btn_arrow_rightMouseEntered(java.awt.event.MouseEvent evt) {                                             
       setCursor(new Cursor(Cursor.HAND_CURSOR));
    }                                            

    private void Btn_arrow_rightMouseExited(java.awt.event.MouseEvent evt) {                                            
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }                                           

    private void Btn_quitMouseClicked(java.awt.event.MouseEvent evt) {                                      
        System.exit(0);
    }                                     

    private void Btn_quitMouseEntered(java.awt.event.MouseEvent evt) {                                      
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }                                     

    private void Btn_quitMouseExited(java.awt.event.MouseEvent evt) {                                     
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }                                    

    private void Btn_startMouseClicked(java.awt.event.MouseEvent evt) {                                       
        if(index_game == 0){
            dispose();
            //instaniasi game snake
            JFrame obj = new JFrame();
            SnakeGame gp = new SnakeGame();

            obj.setBounds(10, 10, 905, 700);
            obj.setBackground(Color.BLACK);
            obj.setResizable(false);
            obj.setVisible(true);
            obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            obj.setLocationRelativeTo(null);
            obj.add(gp);
        }
        if(index_game == 1){
            dispose();
            GameForm gf = new GameForm();
            gf.setVisible(true);
            gf.startGame();
        }
        if(index_game == 2){
            dispose();
            //instaniasi game pacman
            Board_pacman board = new Board_pacman();
            board.show_board();
        } 
    }                                      

    private void Btn_startMouseEntered(java.awt.event.MouseEvent evt) {                                       
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }                                      

    private void Btn_startMouseExited(java.awt.event.MouseEvent evt) {                                      
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }                                     

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main_menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Main_menu().setVisible(true);
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JLabel Background;
    private javax.swing.JLabel Btn_arrow_left;
    private javax.swing.JLabel Btn_arrow_right;
    private javax.swing.JLabel Btn_quit;
    private javax.swing.JLabel Btn_settings;
    private javax.swing.JLabel Btn_start;
    private javax.swing.JLabel game_icon;
    private javax.swing.JComboBox<String> jComboBox1;
    // End of variables declaration                   
}
