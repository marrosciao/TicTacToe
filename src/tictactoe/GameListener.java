package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
/**
 *
 * @author Zakaria Braksa
 */

public class GameListener extends MouseAdapter {

    private JPanel pan;
    private static ArrayList<JPanel> listOfPanels = new ArrayList<JPanel>();
    private static ArrayList<String> GameBoard;
    private static int count = 0;
    private int rank;
    private static BufferedImage img;
    private static boolean rendered = false;
    private int[][] winCases = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
        {0, 4, 8}, {2, 4, 6}
    };
    
    
    public static void replay(){
        for (int i = 0; i < 9; i++) {
            GameBoard.set(i,"_");
            listOfPanels.get(i).removeAll();
            listOfPanels.get(i).updateUI();

        }  
    }
    
    

    //* Constructor
    public GameListener(JPanel p) {
           
        //we assign the parameter "p" to the instance varible "pan"
        this.pan = p; 
        
        //we assign count to rank where 
        // count is a class variable to keep track of the panels inserted until now
        // rank is an instance variable representing the position of the panel on the 3x3 grid 
        // the 3x3 grid is encoded as an ArrayList<String> of 9 elements. 
        rank = count; 
        
        
        // we increment the number of panels
        count++;

        // we add the current panel to the listOfPans which is a static array
        listOfPanels.add(pan);
        
        
        
        // We create a model that's going to represent our game (so our agent can make a decision)
        if(!rendered){
            
            GameBoard = new ArrayList<String>();
            for (int i = 0; i < 9; i++) {
                GameBoard.add("_");
            }
            
            //load an image from the internet if ever we wanna use it instead of drawing an empty circle
            /*try {
                img = ImageIO.read(new URL("http://www.ffiles.com/flash_files/3343/images/thumb/walle.jpg\n"));
                rendered = true;

            } catch (IOException ex) {
                System.out.println("error link");
            }*/
        }
    }
    

     
    public void mouseClicked(MouseEvent e) {
        //we draw an "X" where the user had clicked
        this.drawX();
        
        // we draw an "x" in that position. 
        
        // we update the model that represents the board
        GameBoard.set(rank, "x");
        
        // we compute the next move and we play it
        this.playNextMove();
        
        
    }
    
    
    
    public void playNextMove() {
        ArrayList<String> game = new ArrayList<String>(); //create a new model (again??!!).

        for (String s : GameBoard) {
            String tmp = s;
            game.add(tmp);
        }
        //making a copy of the inital model we had.

        int i = -1000;
        ArrayList<Integer> moves = Max(game);
        if(moves.get(0) > -1 && moves.get(0) < 9){
            game.set(moves.get(0), "o");
            GameBoard.set(moves.get(0), "o");
            System.out.println("score:" + moves.get(1) + " / best move was: " + moves.get(0));
            drawCircle(moves.get(0));
        }else{
            System.out.println("Done");
            GameListener.replay();
        }
        
        //show(game);

    }

    
    public ArrayList<Integer> Max(ArrayList<String> game) {

        if (GameEnded(game)) {
            ArrayList<Integer> a = new ArrayList<Integer>(); 
            a.add(new Integer(-1)); a.add(h(game));
            return a;
        } else {
            int max_val = Integer.MIN_VALUE;
            
            Integer best_move = -9;
            
            ArrayList<Integer> moves = possibleMoves(game);
            
            for (Integer aMove : moves) {
                
                game.set(aMove, "o"); //simulate our own move in the model we created for simulation
                
                ArrayList<Integer> result = Min(game);  
                
                int val = result.get(1);
                
                if( val < 0 && val >= -max_val){
                    max_val = Math.abs(val);
                    best_move = result.get(0); //the best move is our adversary move
                }
                else if ( val >= 0 && val > max_val ) {
                    max_val = val;
                    best_move = aMove;
                }
                
                game.set(aMove, "_"); //cancel move.

            }

            ArrayList<Integer> a = new ArrayList<Integer>(); 
            a.add(best_move); a.add(new Integer(max_val));
            return a;
        }


    }

    
    public ArrayList<Integer> Min(ArrayList<String> game) {
         if (GameEnded(game)) {
        ArrayList<Integer> a = new ArrayList<Integer>(); 
             
            a.add(new Integer(-1)); a.add(h(game));
            return a;
        } else {
            int max_val = Integer.MAX_VALUE;
            Integer best_move = 0;
            ArrayList<Integer> moves = possibleMoves(game);
            for (Integer current_move : moves) {
                game.set(current_move, "x"); //simulate move of the human player.  
                int val = Max(game).get(1);
                if (val < max_val) {
                    max_val = val;
                    best_move = current_move;
                }
                game.set(current_move, "_"); //cancel move.
            }
        ArrayList<Integer> a = new ArrayList<Integer>(); 

            a.add(best_move); a.add(new Integer(max_val));
            return a;
        }

    }

    public int h(ArrayList<String> game) {
        for (int i = 0; i < 8; i++) {
            if (game.get(winCases[i][0]).equals(game.get(winCases[i][1])) && game.get(winCases[i][1]).equals(game.get(winCases[i][2])) && game.get(winCases[i][0]).equals("o")) {
                Integer h = 9 + possibleMoves(game).size();
                
                
                return h;
            }
        }
        for (int i = 0; i < 8; i++) {
            if (game.get(winCases[i][0]).equals(game.get(winCases[i][1])) && game.get(winCases[i][1]).equals(game.get(winCases[i][2])) && game.get(winCases[i][0]).equals("x")) {
                Integer h = - 9 - possibleMoves(game).size();
                //Integer h = 9 + possibleMoves(game).size(); // to make the game stupid
                return h ;
            }
        }
        
        return 0;
    }
    
    
    //return true if the Game ended 
    public boolean GameEnded(ArrayList<String> game) {
        for (int i = 0; i < 8; i++) {
            if (game.get(winCases[i][0]).equals(game.get(winCases[i][1])) && game.get(winCases[i][1]).equals(game.get(winCases[i][2])) && !game.get(winCases[i][0]).equals("_")) {
                return true;
            }
        }
        
        if (possibleMoves(game).size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    // return an ArrayList of the available moves 
    public ArrayList<Integer> possibleMoves(ArrayList<String> game) {
        
        ArrayList<Integer> possibleMovesList = new ArrayList<Integer>();

        for (int i = 0; i < 9; i++) {
            if (game.get(i).equals("_")) {
                possibleMovesList.add(i);
            }
        }
        return possibleMovesList;
    }
    
    // draw an X on the selected panel
    public void drawX(){
        Graphics g = pan.getGraphics();
        g.drawLine(10, 10, 180, 180);
        g.drawLine(180, 10, 10, 180);
        g.dispose();
    }
    
    
    //draw an "O" on the selected
    public void drawCircle(int i){
        Graphics g = listOfPanels.get(i).getGraphics();
        g.drawOval(10, 10, 180, 160);
        //g.drawImage(img, 50, 40, null); 
        g.dispose();
    }

    
      //show a 2D grid of the current board in the console (for debugging)
    
      /*public void show(ArrayList<String> game) {
        System.out.println("----------------\n----------------"); 
        for (int i = 0; i < 9; i++) { 
            System.out.print(" " + game.get(i) + " "); 
            if ((i + 1) % 3 == 0) { 
                System.out.println("\n\n"); 
            }
        }
      }*/
      
}