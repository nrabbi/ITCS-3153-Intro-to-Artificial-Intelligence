import java.util.*;

public class Board{

  private int val, grid[][], heuristic;

  public Board(){
    this.val = 8;
    grid = new int[val][val];
    generateRandomGrid();
    this.heuristic = checkHeuristic();
  }

  public Board(int n){
    this.val = n;
    grid = new int[val][val];
    generateRandomGrid();
    this.heuristic = checkHeuristic();
  }

  public Board(int[][] g){
    if(g.length == g[0].length){
      this.grid = g;
      val = g.length;
      this.heuristic = checkHeuristic();
    }
    else{
      System.out.println("Error!\n");
      generateRandomGrid();
      this.heuristic = checkHeuristic();
    }
  }

  public int[][] getGrid(){
    return grid;
  }

  public int getHeuristic(){
    return heuristic;
  }

  private void generateRandomGrid(){
        for(int i = 0; i < val; i++){
          for(int j = 0; j < val; j++){
            grid[i][j] = 0;
          }
        }

        for(int i = 0; i < val; i++){
          int rand = (int)(Math.random()*val);
          grid[rand][i] = 1;
        }
  }

  public boolean match(){
    if(heuristic == 0) return true;
    else return false;
  }

  public void moveQueen(int startRow, int startCol, int endRow, int endCol){
    if(grid[startRow][startCol] == 1){
      grid[startRow][startCol] = 0;
      grid[endRow][endCol] = 1;
    }
    heuristic = checkHeuristic();
  }

  private int checkHeuristic(){
    int conflicts = 0;
    for(int i = 0; i < val; i++){
      for(int j = 0; j < val; j++){
        if(grid[i][j] == 1){
          conflicts += checkHorizontal(i,j);
          conflicts += checkVertical(i,j);
          conflicts += checkDiagonal(i,j);
        }
      }
    }

    return conflicts/2;
  }

  private int checkVertical(int row, int column){
    int conflicts = 0;

    for(int i = 0; i < val; i++){
      if(i == row) continue;
      if(grid[i][column] == 1) conflicts++;
    }

    return conflicts;
  }

  private int checkHorizontal(int row, int column){
    int conflicts = 0;

    for(int i = 0; i < val; i++){
      if(i == column) continue;
      if(grid[row][i] == 1) conflicts++;
    }

    return conflicts;
  }

  private int checkDiagonal(int row, int column){
    int conflicts = 0;

    for(int i = row-1, j = column-1; i >= 0 && j >= 0; i--, j--){
      if(grid[i][j] == 1) conflicts++;
    }

    for(int i = row+1, j = column+1; i < val && j < val; i++, j++){
      if(grid[i][j] == 1) conflicts++;
    }

    for(int i = row+1, j = column-1; i < val && j >= 0; i++, j--){
      if(grid[i][j] == 1) conflicts++;
    }

    for(int i = row-1, j = column+1; i >= 0 && j < val; i--, j++){
      if(grid[i][j] == 1) conflicts++;
    }

    return conflicts;
  }

  public String toString(){
    String strGrid = "";

    for(int i = 0; i < val; i++){
      for(int j = 0; j < val; j++){
        strGrid += grid[i][j] + " ";
      }
      strGrid += "\n";
    }

    return strGrid.substring(0, strGrid.length() - 2);
  }

  public HashMap getNeighbors (){
    HashMap neighbor = new HashMap();

    for(int row = 0; row < val; row++){
      for(int col = 0; col < val; col++){

        if(grid[row][col] == 1){
          for(int i = 0; i < val; i++){
            if(i == row) continue;
            Board s = new Board(copy(grid, val));
            s.moveQueen(row, col, i, col); 
            neighbor.put(s, s.getHeuristic());
          }
        }
      }
    }
    return neighbor;
  }

  private int[][] copy(int[][] g, int size){

    int[][] a = new int[size][];
    for (int i = 0; i < size; i++) {
      a[i] = Arrays.copyOf(g[i], g[i].length);
    }

    return a;

  }

} 