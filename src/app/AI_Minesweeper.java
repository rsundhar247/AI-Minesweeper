package app;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class AI_Minesweeper {
	
	static int length, width;
	static char[][] cluesBoard;
	static boolean[][] cluesFound;
	static boolean boardChanged = false;
	static Queue<int[]> queue;
	static int[][][][] probClue;
	
	public static void main(String[] args){
		System.out.println("Initializing Minesweeper...");
		Scanner in = new Scanner(System.in);
		queue = new LinkedList<int[]>();
				
		System.out.println("Enter length of board: ");
		length = in.nextInt();
		System.out.println("Enter width of board: ");
		width = in.nextInt();
		
		probClue = new int[length][width][3][3]; // -1 - Unknown, 0 - Found/Safe, 1 - Mine
		cluesFound = new boolean[length][width];
		
		populateProbClue(length,width);
		cluesBoard = createBoard(length, width);
		expandProbClues();
		
		int x = width/2 + 1;
		int y = length/2 + 1;
		int[] XY = getXY(x, y);
		
		System.out.println("First move: " + x + ", " + y);
		System.out.println("(Row,Col Coordinates)");
		
		System.out.println("Enter numeric state of specified cell: ");
		System.out.println("(If mine, enter 9)");
		printBoard(cluesBoard);
		int state = in.nextInt();
		
		putProbClue(XY[0], XY[1], state);
		//printBoard(cluesBoard);
		
		while(checkGameInPlay()){
			while(boardChanged){
				boardChanged = false;
				expandProbClues();
			}
			System.out.println("Clues: ");
			printBoard(cluesBoard);
			
			int[] nextXY;
			if (!queue.isEmpty()){
				nextXY = queue.remove();
			} else {
				nextXY = nextRequestedXY();
			}
			int[]next = getCoordinate(nextXY[0], nextXY[1]);
			System.out.println("Enter state of next move: " + next[0] + ", " + next[1]);
			int command = in.nextInt();
			if(command == 9) {
				break;
				
			}
			putProbClue(nextXY[0], nextXY[1], command);
			
		}
		
		System.out.println("Game Over. Final board: ");
		printBoard(cluesBoard);
		in.close();
		
	}
	
	public static void populateProbClue(int length, int width) {
		for(int[][][] temp: probClue) {
			for(int[][] temp1:temp) {
				for(int i=0;i<3;i++) {
					for(int j=0;j<3;j++) {
							temp1[i][j]=-1;
					}
				}
			}
		}
		
		for(int i=0; i<width; i++) {
			for(int j=0; j<3; j++) {
				probClue[0][i][0][j]=0;
				probClue[length-1][i][2][j]=0;
			}
		}
		
		for(int i=0; i<length; i++) {
			for(int j=0; j<3; j++) {
				probClue[i][0][j][0]=0;
				probClue[i][width-1][j][2]=0;
			}
		}
	}
	
	public static void putProbClue(int x, int y, int clue) {
		
		if (clue == 9){
			cluesBoard[x][y] = '*';
		} else {
			cluesBoard[x][y] = (char)(clue+48);
		}
		
		probClue[x][y][1][1]=clue;
		cluesFound[x][y]=true;
		boardChanged = true;
		
		if (x-1>=0 && y-1>=0)	probClue[x-1][y-1][2][2]=0;
		if (x-1>=0 && y>=0)	probClue[x-1][y][2][1]=0;
		if (x-1>=0 && y+1<width)	probClue[x-1][y+1][2][0]=0;
		if (x>=0 && y-1>=0)	probClue[x][y-1][1][2]=0;
		if (x>=0 && y+1<width)	probClue[x][y+1][1][0]=0;
		if (x+1<length && y-1>=0)	probClue[x+1][y-1][0][2]=0;
		if (x+1<length && y>=0)	probClue[x+1][y][0][1]=0;
		if (x+1<length && y+1<width)	probClue[x+1][y+1][0][0]=0;
		
		
		if(probClue[x][y][1][1] == 0) {
			if (x-1>=0 && y-1>=0 && cluesBoard[x-1][y-1] == '?' && cluesFound[x-1][y-1]==false)	pushToQueue(x-1,y-1);
			if (x-1>=0 && y>=0 && cluesBoard[x-1][y] == '?' && cluesFound[x-1][y]==false)	pushToQueue(x-1,y);
			if (x-1>=0 && y+1<width && cluesBoard[x-1][y+1] == '?' && cluesFound[x-1][y+1]==false)	pushToQueue(x-1,y+1);
			if (x>=0 && y-1>=0 && cluesBoard[x][y-1] == '?' && cluesFound[x][y-1]==false)	pushToQueue(x,y-1);
			if (x>=0 && y+1<width && cluesBoard[x][y+1] == '?' && cluesFound[x][y+1]==false)	pushToQueue(x,y+1);
			if (x+1<length && y-1>=0 && cluesBoard[x+1][y-1] == '?' && cluesFound[x+1][y-1]==false)	pushToQueue(x+1,y-1);
			if (x+1<length && y>=0 && cluesBoard[x+1][y] == '?' && cluesFound[x+1][y]==false)	pushToQueue(x+1,y);
			if (x+1<length && y+1<width && cluesBoard[x+1][y+1] == '?' && cluesFound[x+1][y+1]==false)	pushToQueue(x+1,y+1);
		}
		expandProbClues();
	}
	
	public static void pushToQueue(int x, int y) {
		queue.add(new int[]{x, y});
		cluesFound[x][y]=true;
	}
	
	public static void expandProbClues() {
		for(int[][][] temp:probClue) {
			for(int[][] temp1: temp) {
				if(temp1[1][1]!=0) {
					int tempVal=0;
					for(int i=0; i<3; i++) {
						for(int j=0; j<3; j++) {
							if(!(i==1 && j==1))
								tempVal+=temp1[i][j];
						}
					}
					temp1[1][1]=tempVal;
				}
			}
		}
		neighbourClues();
	}
	
	public static void neighbourClues() {
		for(int i=0; i<length; i++) {
			for(int j=0; j<width; j++) {
				if(cluesBoard[i][j] != '*' && cluesBoard[i][j] != '?'  && Math.abs(Character.getNumericValue(cluesBoard[i][j])) == (neighbourUnknowns(i, j)+neighbourMines(i,j))) {
					if (i-1>=0 && j-1>=0 && cluesBoard[i-1][j-1] == '?')	flagMines(i-1,j-1);
					if (i-1>=0 && j>=0 && cluesBoard[i-1][j] == '?')	flagMines(i-1,j);
					if (i-1>=0 && j+1<width && cluesBoard[i-1][j+1] == '?')	flagMines(i-1,j+1);
					if (i>=0 && j-1>=0 && cluesBoard[i][j-1] == '?')	flagMines(i,j-1);
					if (i>=0 && j+1<width && cluesBoard[i][j+1] == '?')	flagMines(i,j+1);
					if (i+1<length && j-1>=0 && cluesBoard[i+1][j-1] == '?')	flagMines(i+1,j-1);
					if (i+1<length && j>=0 && cluesBoard[i+1][j] == '?')	flagMines(i+1,j);
					if (i+1<length && j+1<width && cluesBoard[i+1][j+1] == '?')	flagMines(i+1,j+1);
				}
				
				if(cluesBoard[i][j] != '*' && cluesBoard[i][j] != '?'  && Math.abs(Character.getNumericValue(cluesBoard[i][j])) == neighbourMines(i,j)) {
					if (i-1>=0 && j-1>=0 && cluesBoard[i-1][j-1] == '?' && cluesFound[i-1][j-1]==false)	pushToQueue(i-1,j-1);
					if (i-1>=0 && j>=0 && cluesBoard[i-1][j] == '?' && cluesFound[i-1][j]==false)	pushToQueue(i-1,j);
					if (i-1>=0 && j+1<width && cluesBoard[i-1][j+1] == '?' && cluesFound[i-1][j+1]==false)	pushToQueue(i-1,j+1);
					if (i>=0 && j-1>=0 && cluesBoard[i][j-1] == '?' && cluesFound[i][j-1]==false)	pushToQueue(i,j-1);
					if (i>=0 && j+1<width && cluesBoard[i][j+1] == '?' && cluesFound[i][j+1]==false)	pushToQueue(i,j+1);
					if (i+1<length && j-1>=0 && cluesBoard[i+1][j-1] == '?' && cluesFound[i+1][j-1]==false)	pushToQueue(i+1,j-1);
					if (i+1<length && j>=0 && cluesBoard[i+1][j] == '?' && cluesFound[i+1][j]==false)	pushToQueue(i+1,j);
					if (i+1<length && j+1<width && cluesBoard[i+1][j+1] == '?' && cluesFound[i+1][j+1]==false)	pushToQueue(i+1,j+1);
				}
			}
		}
	}
	
	public static int neighbourUnknowns(int x, int y) {
		int neighourUnknowns=0;
		if (x-1>=0 && y-1>=0 && cluesBoard[x-1][y-1] == '?')	++neighourUnknowns;
		if (x-1>=0 && y>=0 && cluesBoard[x-1][y] == '?')	++neighourUnknowns;
		if (x-1>=0 && y+1<width && cluesBoard[x-1][y+1] == '?')	++neighourUnknowns;
		if (x>=0 && y-1>=0 && cluesBoard[x][y-1] == '?')	++neighourUnknowns;
		if (x>=0 && y+1<width && cluesBoard[x][y+1] == '?')	++neighourUnknowns;
		if (x+1<length && y-1>=0 && cluesBoard[x+1][y-1] == '?')	++neighourUnknowns;
		if (x+1<length && y>=0 && cluesBoard[x+1][y] == '?')	++neighourUnknowns;
		if (x+1<length && y+1<width && cluesBoard[x+1][y+1] == '?')	++neighourUnknowns;
		
		return neighourUnknowns;
	}
	
	public static int neighbourMines(int x, int y) {
		int neighbourMines=0;
		if (x-1>=0 && y-1>=0 && cluesBoard[x-1][y-1] == '*')	++neighbourMines;
		if (x-1>=0 && y>=0 && cluesBoard[x-1][y] == '*')	++neighbourMines;
		if (x-1>=0 && y+1<width && cluesBoard[x-1][y+1] == '*')	++neighbourMines;
		if (x>=0 && y-1>=0 && cluesBoard[x][y-1] == '*')	++neighbourMines;
		if (x>=0 && y+1<width && cluesBoard[x][y+1] == '*')	++neighbourMines;
		if (x+1<length && y-1>=0 && cluesBoard[x+1][y-1] == '*')	++neighbourMines;
		if (x+1<length && y>=0 && cluesBoard[x+1][y] == '*')	++neighbourMines;
		if (x+1<length && y+1<width && cluesBoard[x+1][y+1] == '*')	++neighbourMines;

		return neighbourMines;
	}
	
	public static void flagMines(int i, int j) {
		cluesBoard[i][j] = '*';
		System.out.println("Mine Found ::: Row-"+i+" Column-"+j);
	}
	
	public static int[] nextRequestedXY(){
		int[] XY = new int[2];
		int x, y;
		do{
			x = (int)(Math.random() * length);
			y = (int)(Math.random() * width);
		} while (cluesBoard[x][y]!='?');
		XY[0] = x;
		XY[1] = y;
		return XY;
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
	
	public static int[] getXY(int input1, int input2){
		int x = input1-1;
		int y = input2-1;
		return new int[]{x, y};
	}
	
	public static int[] getCoordinate(int x, int y){
		int input1 = x+1;
		int input2 = y+1;
		return new int[]{input1, input2};
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
			System.out.print(i+1 + "   ");
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
			}
		}
		return false;
	}
	
	public static boolean isNumClue(char c){
		if (c=='0' || c=='1' || c=='2' || c=='3' || c=='4' || c=='5' || c=='6' || c=='7' || c=='8' || c=='9') return true;
		return false;
	}


}
