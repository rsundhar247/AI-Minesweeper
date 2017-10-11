/*
================================================================================================================
Project - Minesweeper

Class for Part 1 of Minesweeper Assignment; 

Legend:
? - uncovered cell
M - cell marked as mine
C - cell marked as clear
1 - searched cell, numeric indicates how many adjacent mines
* - searched cell, indicates hit mine, game over
================================================================================================================
*/

package app;
import java.util.Scanner;

public class Minesweeper{
	
	static int length, width;
	static int[][] minesBoard;
	static char[][] cluesBoard;
	
	public static void main(String[] args){
		System.out.println("Initializing Minesweeper...");
		Scanner in = new Scanner(System.in);
		
		System.out.println("Enter length of board: ");
		length = in.nextInt();
		
		System.out.println("Enter width of board: ");
		width = in.nextInt();
		
		minesBoard = createMines(length, width);
		cluesBoard = createBoard(length, width);
		
		System.out.println("Enter first move in the format of: x y");
		System.out.println("(0 0 is the move in the bottom left most corner, 1 0 is the move to the right of that):");
		
		int x = in.nextInt();
		int y = in.nextInt();
		if (checkValidMove(x, y)){
			minesBoard[length-y-1][x-1] = 0;
			executeMove(x, y);
			System.out.println("Entered move: (" + x +", " + y + ")");
			printBoard(minesBoard);
			printBoard(cluesBoard);
		}
		
		while(checkGameInPlay()){
			
			printBoard(cluesBoard);
			
			int command = in.nextInt();
			if (command == 1) break;

			
		}
		
		
		in.close();
		
	}
	
	public static boolean checkValidMove(int input1, int input2){
		if (input1>=0 && input1<width && input2>=0 && input2<length){
			return true;
		}
		return false;
	}
	
	public static void executeMove(int input1, int input2){
		int x = length-input2-1;
		int y = input1-1;
		
		if (minesBoard[x][y] == 1){
			cluesBoard[x][y] = '*';
		} else {
			int count = 0;
			if ((x-1)>=0 && (x-1)<length && (y-1)>=0 && (y-1)<width) count = count + minesBoard[x-1][y-1];
			if ((x)>=0 && (x)<length && (y-1)>=0 && (y-1)<width) count = count + minesBoard[x][y-1];
			if ((x+1)>=0 && (x+1)<length && (y-1)>=0 && (y-1)<width) count = count + minesBoard[x+1][y-1];
			if ((x-1)>=0 && (x-1)<length && (y)>=0 && (y)<width) count = count + minesBoard[x-1][y];
			if ((x+1)>=0 && (x+1)<length && (y)>=0 && (y)<width) count = count + minesBoard[x+1][y];
			if ((x-1)>=0 && (x-1)<length && (y+1)>=0 && (y+1)<width) count = count + minesBoard[x-1][y+1];
			if ((x)>=0 && (x)<length && (y+1)>=0 && (y+1)<width) count = count + minesBoard[x][y+1];
			if ((x+1)>=0 && (x+1)<length && (y+1)>=0 && (y+1)<width) count = count + minesBoard[x+1][y+1];
			
			char clue = (char)(count+48);
			System.out.println(clue);
			cluesBoard[x][y] = clue;
		}
	}
	
	public static int[][] createMines(int length, int width){
		int[][] newMines = new int[length][width];
		for (int i=0; i<length; i++){
			for (int j=0; j<width; j++){

				double z = Math.random();
				int mine = 0;
				if (z<=0.4){
					mine = 1;
				}
				
				newMines[i][j] = mine;
			}
		}
		
		return newMines;
	}
	
	
	public static char[][] createBoard(int length, int width){
		char[][] clues = new char[length][width];
		for (int i=0; i<length; i++){
			for (int j=0; j<width; j++){
				clues[i][j] = '?';
			}
		}
		
		return clues;
	}
	
	public static void printBoard(char[][] board){
		if (board == null){
			return;
		}
		for (int i = 0; i< board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				System.out.print(board[i][j]);
				if (j == board[0].length - 1) {
					System.out.println(" ");
				} else {
					System.out.print(" ");
				}
			}
		}
		System.out.println();
	}
	
	public static void printBoard(int[][] board){
		if (board == null){
			return;
		}
		for (int i = 0; i< board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				System.out.print(board[i][j]);
				if (j == board[0].length - 1) {
					System.out.println(" ");
				} else {
					System.out.print(" ");
				}
			}
		}
		System.out.println();
	}
	
	
	public static boolean checkGameInPlay(){
		for (int i=0; i<cluesBoard.length; i++){
			for (int j=0; j<cluesBoard[0].length; j++){
				if (cluesBoard[i][j]=='?') return true;
				if (cluesBoard[i][j]=='*') return false;
			}
		}
		return false;
	}
	
}

