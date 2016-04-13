import java.util.Random;
import java.util.Stack;

/*
 * Shawn Massoud
* Novemner 30, 2015
 * Home Work 5
 */

public class Maze {
	private static final int BORDER = 2;
	private static final int BORDER_CLOSE = 1;
	int myWidth;
	int myHeight;
	Node[][] maze; 
	Node startingPoint;
	Node endingPoint;
	
	boolean myDebug;
	Stack<Node> myStack; 
	Stack<Node> mySolution;
	
	private class Node {

		Node north, east, west, south;
		boolean isVisited;
		boolean isExit;
		boolean isStart;
		boolean northDeadEnd = false;
		boolean eastDeadEnd = false;
		boolean westDeadEnd = false;
		boolean southDeadEnd = false;
		
		int row; 
		int col; 
		
		String mazeWall = "X ";

		public Node() {
			
			north = null;
			east = null;
			west = null;
			south = null;
			
			isVisited = false;
			isStart = false;
			isExit = false;
		}
	}
		public Maze(int width, int depth, boolean debug) {
		myStack = new Stack<Node>();
		mySolution = new Stack<Node>();
		myWidth = width;
		myHeight = depth;
		myDebug = debug;

		//vertical borders
		int w = width * BORDER + BORDER_CLOSE;
		//horizontal borders
		int d = depth * BORDER + BORDER_CLOSE; 
		maze = new Node[w][d]; 
		setUpMaze(w, d, maze);
		if(myDebug) {
			display(w, d);
		}
		explore(w, d);
		if(myDebug) {
			display(w, d);
		}
		if(!myDebug) {
			display(w, d);
		}
	}

		//exploring through the maze
		public void explore(int width, int depth) {
		Random random = new Random();
		int number = random.nextInt(4);
		boolean solutionsFlag = false;

		while(true) {
			switch(number) {
			//NORTH
			case 0: 
				//checks for the bounds
				if(myStack.peek().row - 2 >= 0 && myStack.peek().row - BORDER < width - BORDER_CLOSE) { 
					//if the node is visited
					if(!maze[myStack.peek().row - BORDER][myStack.peek().col].isVisited) { 
						maze[myStack.peek().row][myStack.peek().col].north = maze[myStack.peek().row - 2][myStack.peek().col];
						maze[myStack.peek().row - BORDER][myStack.peek().col].south = maze[myStack.peek().row][myStack.peek().col];
						maze[myStack.peek().row - BORDER][myStack.peek().col].isVisited = true; 
						maze[myStack.peek().row - BORDER][myStack.peek().col].mazeWall = "* "; 
						maze[myStack.peek().row - BORDER_CLOSE][myStack.peek().col].mazeWall = "  "; 
						myStack.push(maze[myStack.peek().row - BORDER][myStack.peek().col]); 
						if(myStack.peek().isExit) {
							mySolution = (Stack<Node>) myStack.clone();
							solutionsFlag = true;
						}
						number = random.nextInt(4);
						myStack.peek().northDeadEnd = false;
						if(myDebug) {
						display(width, depth);
						}
					} else {
						myStack.peek().northDeadEnd = true;
						number = random.nextInt(4);
					}
				} else {
					myStack.peek().northDeadEnd = true;
					number = random.nextInt(4);
				}
				break;
				//WEST
				case 1: 
				if(myStack.peek().col - BORDER >= 0 && myStack.peek().col - BORDER < depth - BORDER_CLOSE) { 
					if(!maze[myStack.peek().row][myStack.peek().col - BORDER].isVisited) { 
						maze[myStack.peek().row][myStack.peek().col].west = maze[myStack.peek().row][myStack.peek().col - BORDER];
						maze[myStack.peek().row][myStack.peek().col - BORDER].east = maze[myStack.peek().row][myStack.peek().col];
						maze[myStack.peek().row][myStack.peek().col - BORDER].isVisited = true; 
						maze[myStack.peek().row][myStack.peek().col - BORDER].mazeWall = "* "; 
						maze[myStack.peek().row][myStack.peek().col - BORDER_CLOSE].mazeWall = "  "; 
						myStack.push(maze[myStack.peek().row][myStack.peek().col - BORDER]);
						if(myStack.peek().isExit && !solutionsFlag) {
							mySolution = (Stack<Node>) myStack.clone();
							solutionsFlag = true;
						}
						number = random.nextInt(4);
						myStack.peek().westDeadEnd = false;
						if(myDebug) {
						display(width, depth);
						}
					} else {
						myStack.peek().westDeadEnd = true;
						number = random.nextInt(4);
					}
				} else {
					myStack.peek().westDeadEnd = true;
					number = random.nextInt(4);
				}
				break;
				//SOUTH
			case 2: 
				if(myStack.peek().row + BORDER >= 0 && myStack.peek().row + BORDER < width - BORDER_CLOSE) { 
					if(!maze[myStack.peek().row + BORDER][myStack.peek().col].isVisited) { 
						maze[myStack.peek().row][myStack.peek().col].south = maze[myStack.peek().row + 2][myStack.peek().col];
						maze[myStack.peek().row + BORDER][myStack.peek().col].north = maze[myStack.peek().row][myStack.peek().col];
						maze[myStack.peek().row + BORDER][myStack.peek().col].isVisited = true; 
						maze[myStack.peek().row + BORDER][myStack.peek().col].mazeWall = "* "; 
						maze[myStack.peek().row + 1][myStack.peek().col].mazeWall = "  "; 
						myStack.push(maze[myStack.peek().row + BORDER][myStack.peek().col]);
						if(myStack.peek().isExit && !solutionsFlag) {
							mySolution = (Stack<Node>) myStack.clone();
							solutionsFlag = true;
						}
						number = random.nextInt(4);
						myStack.peek().southDeadEnd = false;
						if(myDebug) {
						display(width, depth);
						}
					} else {
						myStack.peek().southDeadEnd = true;
						number = random.nextInt(4);
					}
				} else {
					myStack.peek().southDeadEnd = true;
					number = random.nextInt(4);
				}
				break;
				//EAST
			case 3: 
				if(myStack.peek().col + 2 >= 0 && myStack.peek().col + BORDER < depth - BORDER_CLOSE) { //checks if it is a valid node not out of bounds
					if(!maze[myStack.peek().row][myStack.peek().col + BORDER].isVisited) { //if it is not visited then it is linked
						maze[myStack.peek().row][myStack.peek().col].east = maze[myStack.peek().row][myStack.peek().col + 2];
						maze[myStack.peek().row][myStack.peek().col + BORDER].west = maze[myStack.peek().row][myStack.peek().col];
						maze[myStack.peek().row][myStack.peek().col + BORDER].isVisited = true; //makes it a visited node
						maze[myStack.peek().row][myStack.peek().col + BORDER].mazeWall = "* "; //traversed
						maze[myStack.peek().row][myStack.peek().col + BORDER_CLOSE].mazeWall = "  "; //removes border
						myStack.push(maze[myStack.peek().row][myStack.peek().col + BORDER]);
						if(myStack.peek().isExit && !solutionsFlag) {
							mySolution = (Stack<Node>) myStack.clone();
							solutionsFlag = true;
						}
						number = random.nextInt(4);
						myStack.peek().eastDeadEnd = false;
						if(myDebug) {
						display(width, depth);
						}
					} else {
						myStack.peek().eastDeadEnd = true;
						number = random.nextInt(4);
					}
				} else {
					myStack.peek().eastDeadEnd = true;
					number = random.nextInt(4);
				}
				break;
			default: 
				number = random.nextInt(4);
				break;
			}
			if(myStack.peek().northDeadEnd == true && myStack.peek().westDeadEnd == true && myStack.peek().southDeadEnd == true && myStack.peek().eastDeadEnd == true) {
				if(myStack.size() > 1) {
					myStack.pop();
				} 
				else if (myStack.size() == 1) {
					break;
				}
			}
		}
		//clears path
		for(int i = 1; i < width; i += 2) {
			for(int j = 1; j < depth; j += 2) {
				maze[i][j].mazeWall = "  ";
			}
		}
		//points out shortest path of maze
		for(Node e: mySolution) {
			maze[e.row][e.col].mazeWall	= "* ";
		}

	}
	
	private void setUpMaze(int width, int depth, Node[][] maze) {

		//fills every spot with a false node
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < depth; j++) {
				maze[i][j] = new Node();
				maze[i][j].row = i;
				maze[i][j].col = j;
				maze[i][j].isVisited = true;
			}
		}
		//setup the real nodes
		for(int i = 1; i < width; i += 2) {
			for(int j = 1; j < depth; j += 2) {
				maze[i][j].mazeWall = "  ";
				maze[i][j].isVisited = false;
			}
		}
		startingPoint = maze[1][1];
		endingPoint = maze[width - BORDER][depth - BORDER];
		maze[0][1].mazeWall = "  "; 
		maze[1][1].mazeWall = "* "; 
		maze[1][1].isStart = true; 
		maze[1][1].isVisited = true;

		maze[width - BORDER_CLOSE][depth - BORDER].mazeWall = "  ";
		maze[width - BORDER][depth - BORDER].isExit = true;
		maze[width - BORDER][depth - BORDER].mazeWall = "  ";
		myStack.push(maze[1][1]);


	}

	void display(int width, int depth) {

		for(int i = 0; i < width; i++) {
			for(int j = 0; j < depth; j++) {
				System.out.print(maze[i][j].mazeWall);
			}
			System.out.print('\n');
		}
		System.out.print('\n');
	}

}



