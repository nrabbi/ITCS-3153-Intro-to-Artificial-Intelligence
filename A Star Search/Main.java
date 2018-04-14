// Nazmul Rabbi
// ITCS 3153 : A Star Search
// Main.java -> Driver 
// 3/18/2018

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

        Map map = new Map();
        System.out.println("Default Environment");
        System.out.println("-------------------\n");
        System.out.println(map.toString() + "\n");
        char goAgain = 'N';
        
        do {
            System.out.println("Choose Starting Position");
            System.out.println("------------------------");
            System.out.print("Row: ");
            int startRow = scan.nextInt();
            System.out.print("Column: ");
            int startColumn = scan.nextInt();

            while ((map.getType(startRow, startColumn)).equals(Map.UNPATHABLE)) {
                System.out.println("\nError! selected starting position is blocked\n");
                System.out.print("Row: ");
                startRow = scan.nextInt();
                System.out.print("Column: ");
                startColumn = scan.nextInt();
                map.setElement(startRow, startColumn, "s");
            }
            
            map.setElement(startRow, startColumn, "s");
            System.out.println("\nChoose Destination Position");
            System.out.println("---------------------------");
            System.out.print("Row: ");
            int goalRow = scan.nextInt();
            System.out.print("Column: ");
            int goalColumn = scan.nextInt();

            while ((map.getType(goalRow, goalColumn)).equals(Map.UNPATHABLE)) {
                System.out.println("\nError! selected destination position is blocked\n");
                System.out.print("Row: ");
                goalRow = scan.nextInt();
                System.out.print("Column: ");
                goalColumn = scan.nextInt();
                map.setElement(goalRow, goalColumn, "g");
            }

            map.setElement(goalRow, goalColumn, "g");
            System.out.println("\n\n" + map.toString());
            map.generatePath(startRow, startColumn, goalRow, goalColumn);
            System.out.println("Path to goal " + map.displayPath());
            System.out.println("\nSolution Path");
            System.out.println("-------------");
            map.updateMap();
            System.out.println("\n" + map.pathToString());
            System.out.print("\nWould you like to try again?\n");
            System.out.print("Enter Y or N: ");
            goAgain = scan.next().charAt(0);
            map.resetNodes();
            map.resetPath();
            System.out.print("\n");
            map.setElement(startRow, startColumn, "-");
            map.setElement(goalRow, goalColumn, "-");

        }while(goAgain == 'Y' || goAgain == 'y');
        System.out.println("Program terminated...");
    }
}
