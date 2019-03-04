import java.util.*;
import java.io.*;
public class Maze{

    private char[][]maze;
    private int startx;
    private int starty;
    private int[][] moves = new int[][] {{-1,0},{1,0},{0,1},{0,-1}}; //MOVES ARRAY LIKE KNIGHT
    private int width;
    private int height;
    private boolean animate;//false by default

    /*Constructor loads a maze text file, and sets animate to false by default.

      1. The file contains a rectangular ascii maze, made with the following 4 characters:
      '#' - Walls - locations that cannot be moved onto
      ' ' - Empty Space - locations that can be moved onto
      'E' - the location of the goal (exactly 1 per file)
      'S' - the location of the start(exactly 1 per file)

      2. The maze has a border of '#' around the edges. So you don't have to check for out of bounds!

      3. When the file is not found OR the file is invalid (not exactly 1 E and 1 S) then:
         throw a FileNotFoundException or IllegalStateException
    */
    public Maze(String filename) throws FileNotFoundException{
      animate = false;
        //COMPLETE CONSTRUCTOR
      File text = new File(filename);
      //inf stands for the input file
      Scanner inf = new Scanner(text);
      while(inf.hasNextLine()){
          String line = inf.nextLine();
          height++; //find the length and height of array
          width = line.length();
        }
      maze = new char[height][width]; //initialize array
      inf = new Scanner(text); //scan again
      for (int x = 0; inf.hasNextLine(); x++){
        String line = inf.nextLine();
        for (int y = 0; y < line.length(); y++){
          maze[x][y] = line.charAt(y);
          if (line.charAt(y) == 'S') {
            startx = x;
            starty = y;
          }
        }
      }

    }

    public String toString(){
      String output = "";
        for(int i = 0; i < maze.length; i++){
            for (int j = 0; j< maze[0].length; j++){
                output += maze[i][j] + " ";
            }
            output += '\n';
        }
        return output;
    }

    private void wait(int millis){
         try {
             Thread.sleep(millis);
         }
         catch (InterruptedException e) {
         }
     }

    public void setAnimate(boolean b){
        animate = b;
    }

    public void clearTerminal(){
        //erase terminal, go to top left of screen.
        System.out.println("\033[2J\033[1;1H");
    }


    /*Wrapper Solve Function returns the helper function
      Note the helper function has the same name, but different parameters.
      Since the constructor exits when the file is not found or is missing an E or S, we can assume it exists.
    */
    public int solve(){
      //find the location of the S.
      for(int r = 0; r < height; r++) {
        for(int c = 0; c < width; c++) {
          if(maze[r][c] == 'S') {
            maze[r][c] = ' ';//erase the S
            return solve(r, c, 0); //return solve at the point
          }
        }
      }
      //and start solving at the location of the s.
      return -1; //return -1 if no solution
    }

    /*
      Recursive Solve function:

      A solved maze has a path marked with '@' from S to E.

      Returns the number of @ symbols from S to E when the maze is solved,
      Returns -1 when the maze has no solution.

      Postcondition:
        The S is replaced with '@' but the 'E' is not.
        All visited spots that were not part of the solution are changed to '.'
        All visited spots that are part of the solution are changed to '@'
    */
    private int solve(int row, int col, int steps){ //you can add more parameters since this is private; I ADDED INT STEPS
      if(animate){
                clearTerminal();
                System.out.println(this);
                wait(20);
            }
            if(maze[row][col] == 'E'){
              return steps;
            } //return the number of steps when u reach e

            if(maze[row][col] != ' '){
              return -1;
            } //check if the move is valid (eg not . or @ or #)
            for(int i = 0; i < moves.length; i++){
              maze[row][col] = '@';//mark wya with @ sign
              int nextmove = solve(row + moves[i][0], col + moves[i][1], steps + 1);
              if(nextmove != -1){ //if it is valid move
                return nextmove; //return the solve and continue
              }else{
                //if its not a valid move put a "." and backtrack
                maze[row][col] = '.';
              }
            }

            return -1; //so it compiles
    }

}
