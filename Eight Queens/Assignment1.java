// Nazmul Rabbi
// Programming Project #1 : ITCS 3153
// 2/11/2018

import java.util.*;

public class Assignment1{

  public static void main(String[] args){
    final int sizeLimit = 10;
    int reset = 0, edits = 0;
    
    Board presentState = new Board(sizeLimit);

    while(!presentState.match()) {
      int heuristic = presentState.getHeuristic();

      System.out.println("-------------------\nCurrent H: " + heuristic + "\nCurrent State...\n-------------------\n\n" + presentState.toString());

      HashMap neighbor = presentState.getNeighbors();
      Set set = neighbor.entrySet();
      Iterator index = set.iterator();

      int lowerNeighbor = 0;               
      Board lowestNeighbor = presentState;  

      while(index.hasNext()){
        Map.Entry me = (Map.Entry)index.next();
        if((int)(me.getValue()) < heuristic) lowerNeighbor++;
        if(((Board)me.getKey()).getHeuristic() < lowestNeighbor.getHeuristic())
          lowestNeighbor = (Board)me.getKey();
      }

      System.out.println("\n---------------------------------\nNeighbors found with lower H: " + lowerNeighbor);

      if(lowerNeighbor == 0){
        System.out.println("Reset! Successfully!\n---------------------------------\n");
        presentState = new Board(sizeLimit);
        reset++;
      }
      else {
        System.out.println("Setting new current state...\n---------------------------------\n");
        presentState = lowestNeighbor;
        edits++;
      }

    } 

    System.out.println(presentState.toString() + "\n\nSuccess! Solution Found...\n\n-------------------\nState changes: " + edits + "\nRestarts: " + reset + "\n-------------------");

  }
}