package hw4;

	import java.io.BufferedWriter;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.Collections;
	import java.util.List;
	import java.util.Random;
	import java.util.Scanner;

	public class tcss343 {
		/**
		 * A node to be used with the associated post
		 * @author sean hoyt
		 *
		 */
		public static class Node{
			int myPostNumber;
			List<Edge> adjacencyList;
			boolean visited;
			int cost;
			Node retrace;
			public Node(int thePostNumber){
				visited = false;
				cost = Integer.MAX_VALUE;
				myPostNumber = thePostNumber;
				adjacencyList = new ArrayList<Edge>();
				retrace = null;
			}
			/**
			 * Adds an edge to current node's edges
			 * @param theDestination
			 * @param theWeight
			 */
			public void addEdge(Node theDestination, int theWeight){
				if(!this.equals(theDestination)){
					Edge myEdge = new Edge(this, theDestination, theWeight);
					adjacencyList.add(myEdge);
				}

			}
		}
		/**
		 * An edge to connect two nodes, with a weight
		 * @author Sean Hoyt
		 *
		 */
		public static class Edge{
			int myWeight;
			Node mySource;
			Node myDestination;
			public Edge(Node theSource, Node theDestination, int theWeight){
				myWeight = theWeight;
				mySource = theSource;
				myDestination = theDestination;
			}
			@Override
			public String toString(){
				return "("+mySource.myPostNumber+")"+"->"+"("+myDestination.myPostNumber+")";
			}
		}
		/**
		 * A path is a list of edges,
		 *  with certain rules on the addition of an edge.
		 *  Either the front or end of path.
		 * @author Sean Hoyt
		 *
		 */
		public static class Path{
			List<Edge> myPath;
			int myCost;
			public Path(){
				myPath = new ArrayList<Edge>();
				myCost =0;
			}
			/**
			 * A check to see if a particular edge can be added to front or end of a path.
			 * @param theEdge
			 */
			public void addEdge(Edge theEdge){
				if(!theEdge.mySource.equals(theEdge.myDestination)){
					int size = myPath.size();
					//if empty
					if(myPath.size() < 1){
						myPath.add(theEdge);
						myCost+=theEdge.myWeight;

					}else if(myPath.get(size-1).myDestination.myPostNumber == theEdge.mySource.myPostNumber){
						
						if(!myPath.contains(theEdge)){
							//adds to end
							myPath.add(theEdge);
							myCost+=theEdge.myWeight;	
						}
						
					}else if(myPath.get(0).mySource.myPostNumber == theEdge.myDestination.myPostNumber){
							//adds to beginning
						myPath.add(0, theEdge);
						myCost+=theEdge.myWeight;
					}else {

						//System.out.println("Can't add that edge!");
					}
				}
			}
			
			@Override
			public String toString(){
				String path = "";
				if(this.myPath.size() > 0){
					for(Edge edges: myPath){
						path+=(edges.mySource.myPostNumber+"->");
						//path+= edges.toString();

					}
					path += myPath.get(myPath.size()-1).myDestination.myPostNumber;
				}
				return path;
			}
			@Override
			public boolean equals(Object someObject){
				Path somePath = (Path) someObject;
				boolean equals = false;
				if(this.myCost == somePath.myCost){
					if(this.myPath.equals(somePath.myPath)){
						equals = true;
					}
				}
				return equals;
			}
			/**
			 * A check to see if possible to add edge to path.
			 * @param theEdge
			 * @return
			 */
			public boolean canAdd(Edge theEdge){
				int size = myPath.size();
				boolean youCanAdd = false;
				if(myPath.size() < 1){
					youCanAdd = true;
				}else if(myPath.get(size-1).myDestination.myPostNumber == theEdge.mySource.myPostNumber){
					youCanAdd = true;
				}else {
					//System.out.println("Can't add that edge!");
				}
				return youCanAdd;
			}
		}
		/**
		 * A basic graph containing nodes with directed edges.
		 * Can be built from 2-d array of integers.
		 * @author sean hoyt, winfield
		 *
		 */
		public static class Graph{
			List<Node> myVertices;
			List<Edge> myEdges;
			public Graph(){
				myVertices = new ArrayList<Node>();
				myEdges = new ArrayList<Edge>();
			}
			public void addNode(Node theNode){
				myVertices.add(theNode);
			}
			/**
			 * Adds a cost array size n x n to graph
			 * @param theInput
			 */
			public void addInputArray(int[][] theInput){
				int numPosts = theInput.length;
				int i= 0;
				int j =0;
				for(i =0; i < numPosts; i++){
					Node someNode = new Node(i);
					myVertices.add(someNode);
				}
				for(Node nodes: myVertices){
					for(j=0; j < numPosts; j++){

						if(theInput[myVertices.indexOf(nodes)][j] != 0){
							Node temp = myVertices.get(j);
							nodes.addEdge(temp, theInput[myVertices.indexOf(nodes)][j]);
						}
					}
				}

			}
			/**
			 * Creates a "random" cost array sizexsize in a file specified by string.
			 * @param size the size of cost array
			 * @param theName file name.
			 * @throws IOException
			 */
			public static void createFileInput(int size, String theName) throws IOException{
				FileWriter fileOut = new FileWriter(theName);
				BufferedWriter bufOut = new BufferedWriter(fileOut);
				Random randy = new Random(System.currentTimeMillis());
				int i =0; 
				int j =0;
				for(i=0; i < size; i++){
					for(j=0; j < size; j++){
						int val = randy.nextInt(50) + 1;
						if(j > i){
							bufOut.write(""+val);
						}else if(j == i){
							bufOut.write("0");
						}else {
							bufOut.write("NA");
						}
						bufOut.write("\t");
					}
					bufOut.write("\n");
				}
				bufOut.close();
				fileOut.close();
			}
			/**
			 * Prints each node in graph along with vertices.
			 */
			public void printGraph(){
				for(Node nodes: myVertices){	
					System.out.println("Post#:"+nodes.myPostNumber + "Cost:"+nodes.cost);
					if(nodes.retrace != null){
						System.out.println("Retrace"+"("+nodes.retrace.myPostNumber+")");
					}
					System.out.println("Edges:");
					for(Edge edges: nodes.adjacencyList){
						//System.out.println("\tPost:" +edges.myDestination.myPostNumber + 
						//	" Weight:"+ edges.myWeight);
						System.out.println("("+edges.mySource.myPostNumber+")"
								+"->("+edges.myDestination.myPostNumber+")" + "Cost:"+edges.myWeight);
								
					}
				}
			}
			/**
			 * Reads in a cost array from file and return 2d array of ints.
			 * @param size
			 * @return
			 */
			public int[][] buildCostArrayFromFile(int size){			
				int[][] contents = new int[size][size];
				String[] inputs = new String[size*size];
				int i =0;
				int j =0;
				int k =0;
				Scanner scan = new Scanner(System.in);
				String line = null;
				for(i = 0; i < size*size; i++){
					if((line = scan.next())!= null){
						inputs[i] = line;					
					}
				}
				for(i=0; i <size; i++){
					for(j=0; j < size; j++){
						if(!inputs[k].equals("NA")){
							contents[i][j] = Integer.parseInt(inputs[k]);
						}
						k++;
					}
				}
				return contents;
			}
			/**
			 * Prints a given cost array;
			 * @param theCostArray
			 */
			public void printCostArray(int[][] theCostArray){
				int i=0;
				int j=0;
				for(i=0; i < theCostArray.length; i++){
					for(j=0; j< theCostArray.length; j++){
						System.out.print(theCostArray[i][j]);
					}
					System.out.println();
				}

			}
			/**
			 * Generates a list of all possible subpaths within the graph.
			 * This runs in 2^(n) time. So large sets will take some time...
			 * @return Returns all the possible complete subsets containing starting and ending nodes.
			 */
			public List<Path> getPathSetBruteForce(){
				List<Edge> allEdges = new ArrayList<Edge>();
				for(Node nodes: myVertices){
					for(Edge edges: nodes.adjacencyList){
						allEdges.add(edges);
					}
				}
				List<Path> thePaths = new ArrayList<Path>();
				Path somePath = new Path();
				somePath.addEdge(allEdges.get(0));
				thePaths.add(somePath);
				//generates all possible subsets that are able to be connected
				for(Edge edge: allEdges){	
					List<Path> pathsClone = new ArrayList<Path>(thePaths);			
					//take that java concurrency warning :^)
					for(Path path: pathsClone){
						Path anotherPath = new Path();
						anotherPath.myPath.addAll(path.myPath);
						anotherPath.myCost = path.myCost;
						if(anotherPath.canAdd(edge)){
							anotherPath.addEdge(edge);
						}else {
							anotherPath = new Path();
							anotherPath.addEdge(edge);
						}
						if(!thePaths.contains(anotherPath)){
							thePaths.add(anotherPath);
						}
					}
				}
				//removes any subsets that do not contain a path from start to end
				List<Path>tempPaths = new ArrayList<Path>(thePaths);
				for(Path path: tempPaths){
					if(path.myPath.get(0).mySource.myPostNumber != 0
							|| path.myPath.get(path.myPath.size() -1).myDestination.myPostNumber
							!= myVertices.size()-1){
						thePaths.remove(path);
					}
				}
				return thePaths;

			}
			/**
			 * Gets minimum path out of set of all possible paths.
			 * @return Returns the minimal path.
			 */
			public Path getMinimumPathBruteForce(){
				Path currentMin = null;
				List<Path> completePaths = this.getPathSetBruteForce();
				for(Path path: completePaths){
					if(currentMin != null){
						if(currentMin.myCost > path.myCost){
							currentMin = path;
						}
					}else {
						currentMin = path;
					}
				}
				return currentMin;
			}
			/**
			 * Looks for edge within graph containing two nodes.
			 * @param source soure node.
			 * @param dest destination node.
			 * @return Returns the edge if set contains the edge, else null.
			 */
			public Edge getEdge(Node source, Node dest){
				Edge temp = null;
				for(Node node: myVertices){
					for(Edge edges: node.adjacencyList){
						if(edges.mySource.myPostNumber == source.myPostNumber 
								&& edges.myDestination.myPostNumber == dest.myPostNumber){
							temp = edges;

						}
					}
				}
				return temp;
			}
			/**
			 * Recursively computes the minimal path by dividing set into sets 
			 * minimal overall choice for each node.
			 * @return Returns the minimum path.
			 */
			public Path getMinimumPathRecursive(){
				Node source = myVertices.get(0);
				Node dest = myVertices.get(myVertices.size()-1);
				source.cost =0;
				//recursively steps through and calculates the minimum cost of each sub-level
				calculateMinPaths(source, dest);
				return recoverPath();

			}
			/**
			 * Recovers minimal path from set of all nodes, using the retrace field of node.
			 * 
			 * @return Returns the recovered minimal path.
			 */
			public Path recoverPath(){
				Path shortestPath = new Path();
				List<Node> vertices = new ArrayList<Node>(myVertices);
				Collections.reverse(vertices);
				for(Node nodes: vertices){
					if(nodes.retrace != null){
						Edge temp = getEdge(nodes.retrace, nodes);
						if(temp != null){
							shortestPath.addEdge(temp);
						}
					}
				}
				return shortestPath;

			}
			/**
			 * This algorithm recursively goes till it finds bottom node, then updates costs back up the way.
			 * At each level it will pick the cheapest edge as it's cost and updates a retrace node to help recover solution.
			 * This algorithm runs in O(n^3) since this particular graph has (n^2-n)/2 edges. 
			 * @param src Source node
			 * @param dest Destination node
			 */
			public void calculateMinPaths(Node src, Node dest){
				for(Edge edges: src.adjacencyList){
					if(edges.myDestination.cost > (src.cost+edges.myWeight)){
						edges.myDestination.cost = src.cost+edges.myWeight;
						if(!src.adjacencyList.isEmpty()){
							calculateMinPaths(edges.myDestination, dest);
							edges.myDestination.retrace = src;
						}
					}
				}		

			}
			/**
			 * Resets various fields in all vertices, to be used for other algorithms.
			 */
			public void resetDistances(){
				for(Node nodes: myVertices){
					nodes.retrace = null;
					nodes.cost = Integer.MAX_VALUE;
					nodes.visited = false;
				}
			}
			/**
			 * This algorithm uses Dijkstra's approach to finding the minimum cost path.
			 * It explores each node's adjacency list for any unvisited nodes,
			 * Then updates the current node with the minimum cost.
			 * This algorithm runs in O(n^2) time. 
			 * @return
			 */
			public Path getMinPathDynamicProgramming(){
				myVertices.get(0).cost =0;
				for(Node node: myVertices){				
						for(Edge edges: node.adjacencyList){
							if(!edges.myDestination.visited){
								if(edges.myDestination.cost > (node.cost+ edges.myWeight)){
									edges.myDestination.cost = node.cost + edges.myWeight;
									edges.myDestination.retrace = node;
								}
							}

						}
						node.visited = true;
				}
				return recoverPath();
			}

		}
		public static void main(String[] theArgs) throws IOException{
			
			//needed to determine what size input to build, must set correct size with correct input file size
			int size = 16;
			
			Graph myGraph = new Graph();
			//Graph.createFileInput(size, size+"_input.txt");
			int [][] testInput = myGraph.buildCostArrayFromFile(size);
			myGraph.addInputArray(testInput);

			long bruteStart =  System.nanoTime();
			Path tempPath = myGraph.getMinimumPathBruteForce();
			long bruteEnd = System.nanoTime();
			long bruteRunTime = bruteEnd - bruteStart;

			long recStart =  System.nanoTime();
			Path recPath =myGraph.getMinimumPathRecursive();
			long recEnd = System.nanoTime();
			long recRunTime = recEnd - recStart;
			
			myGraph.resetDistances();
			long dyStart = System.nanoTime();
			Path dynamic = myGraph.getMinPathDynamicProgramming();
			long dyEnd = System.nanoTime();
			long dyRunTime = dyEnd - dyStart;
			System.out.println("Input size:"+size);
			System.out.println("Brute force took:"+ bruteRunTime+"ns");
			System.out.println("Minimum Path Bruteforce is:\n"+tempPath.toString()+ "\nCost:"+tempPath.myCost);
			System.out.println("Recursive time:"+"\t"+ recRunTime+"ns");

			System.out.println("Minimum Path recursive is:\n"+recPath.toString()+ "\nCost:"+recPath.myCost);
			System.out.println("Dynamic time:\t"+dyRunTime+"ns");
			System.out.println("Dynamic path:\n"+ dynamic.toString() + "\nCost:"+ dynamic.myCost);

			System.out.println("--------------------------------------");
			
		}
	}




