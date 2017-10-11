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
		
		int x = width/2 + 1;
		int y = length/2 + 1;	
		
		System.out.println("First move: " + x + ", " + y);
		System.out.println("(x by y coordinates)");
		
		System.out.println("Enter numeric state of specified cell: ");
		System.out.println("(If mine, enter 9)");
		printBoard(cluesBoard);
		int state = in.nextInt();
		

		if (checkValidMove(x, y)){
			minesBoard[length-y][x-1] = 0;
			putClue(x, y, state);
			//printBoard(minesBoard);
			//printBoard(cluesBoard);
		}
		
		while(checkGameInPlay()){
			
			printBoard(cluesBoard);
			
			int command = in.nextInt();
			if (command == 1) break;

			
		}
		
		
		in.close();
		
	}
	
	public static boolean checkValidMove(int input1, int input2){
		if (input1>=1 && input1<=width && input2>=1 && input2<=length){
			return true;
		}
		return false;
	}
	
	public static void putClue(int input1, int input2, int clue){
		int x = length-input2;
		int y = input1-1;
		
		if (clue == 9){
			minesBoard[x][y] = 1;
			cluesBoard[x][y] = '*';
		} else {
//			int count = 0;
//			if ((x-1)>=0 && (x-1)<length && (y-1)>=0 && (y-1)<width) count = count + minesBoard[x-1][y-1];
//			if ((x)>=0 && (x)<length && (y-1)>=0 && (y-1)<width) count = count + minesBoard[x][y-1];
//			if ((x+1)>=0 && (x+1)<length && (y-1)>=0 && (y-1)<width) count = count + minesBoard[x+1][y-1];
//			if ((x-1)>=0 && (x-1)<length && (y)>=0 && (y)<width) count = count + minesBoard[x-1][y];
//			if ((x+1)>=0 && (x+1)<length && (y)>=0 && (y)<width) count = count + minesBoard[x+1][y];
//			if ((x-1)>=0 && (x-1)<length && (y+1)>=0 && (y+1)<width) count = count + minesBoard[x-1][y+1];
//			if ((x)>=0 && (x)<length && (y+1)>=0 && (y+1)<width) count = count + minesBoard[x][y+1];
//			if ((x+1)>=0 && (x+1)<length && (y+1)>=0 && (y+1)<width) count = count + minesBoard[x+1][y+1];
//			
//			char clue = (char)(count+48);
//			System.out.println(clue);
			cluesBoard[x][y] = (char)(clue+48);
		}
	}
	
	public static int[][] createMines(int length, int width){
		int[][] newMines = new int[length][width];
		for (int i=0; i<length; i++){
			for (int j=0; j<width; j++){
				newMines[i][j] = -1; //unknown
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
		System.out.print("    ");
		for (int j = 0; j<board[0].length; j++){
			System.out.print((j+1) + " ");
		}
		System.out.println();
		System.out.println();
		for (int i = 0; i< board.length; i++){
			System.out.print(length-i + "   ");
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
		System.out.print("    ");
		for (int j = 0; j<board[0].length; j++){
			System.out.print((j+1) + " ");
		}
		System.out.println();
		System.out.println();
		for (int i = 0; i< board.length; i++){
			System.out.print(length-i + "  ");
			for (int j = 0; j < board[0].length; j++){
				if (board[i][j]<0){
					System.out.print(board[i][j]);
				} else {
					System.out.print(" " + board[i][j]);
				}
				if (j == board[0].length - 1) {
					System.out.println();
				}
			}
		}
		System.out.println();
	}
	
	
	public static boolean checkGameInPlay(){
		boolean unsolved = false;
		for (int i=0; i<cluesBoard.length; i++){
			for (int j=0; j<cluesBoard[0].length; j++){
				if (cluesBoard[i][j]=='?') unsolved = true;
				if (cluesBoard[i][j]=='*') return false;
			}
		}
		if (unsolved == true) return true;
		return false;
	}
	
}

