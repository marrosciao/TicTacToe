package tictactoe;

import java.util.*;
import javax.swing.*;

import java.awt.*;

import javax.swing.border.Border;
import java.awt.event.*;
/**
 *
 * @author Zakaria Braksa
 */
class Board extends JFrame {   
	static JFrame board;
	static JPanel panel, pan;
	static JButton NewGame;
	  
	public Board(){ 
            board = new JFrame("Tic Tac Toe / @qbraksa");
            board.setResizable(false);
            board.setLocationRelativeTo(null);
            board.setDefaultCloseOperation(EXIT_ON_CLOSE);
            board.setSize(600, 600);
            board.setVisible(true);
            board.setLayout(new BorderLayout());

            panel= new JPanel();
            panel.setLayout(new GridLayout(3,3));
            Border line = BorderFactory.createLineBorder(Color.black,1);
            for(int i = 0; i<9;i++){
                pan = new JPanel();
                pan.setSize(200, 200);
                pan.setBorder(line);
                pan.addMouseListener(new GameListener(pan));
                panel.add(pan);    
            }
            
            //create and add the "New Game" button
            NewGame = new JButton("New Game");
            NewGame.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Hi");
                    GameListener.replay();
                } 
            });
            NewGame.setSize(20, 20);
            board.add(NewGame, BorderLayout.SOUTH);
            //--

            
            //add the panel to the board and make the board visible
            board.add(panel, BorderLayout.CENTER);
            board.setVisible(true);
            //--
            
        }

}


public class Main 
{
	public static void main(String[] args)
	{
            new Board(); //create the board and boot the game 
	}
	
}