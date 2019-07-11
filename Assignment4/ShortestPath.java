/* ShortestPath.java
   CSC 226 - Fall 2016
   Assignment 4 - Template for Dijkstra's Algorithm
   
   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
	java ShortestPath
	
   To conveniently test the algorithm with a large input, create a text file
   containing one or more test graphs (in the format described below) and run
   the program with
	java ShortestPath file.txt
   where file.txt is replaced by the name of the text file.
   
   The input consists of a series of graphs in the following format:
   
    <number of vertices>
	<adjacency matrix row 1>
	...
	<adjacency matrix row n>
	
   Entry A[i][j] of the adjacency matrix gives the weight of the edge from 
   vertex i to vertex j (if A[i][j] is 0, then the edge does not exist).
   Note that since the graph is undirected, it is assumed that A[i][j]
   is always equal to A[j][i].
	
   An input file can contain an unlimited number of graphs; each will be 
   processed separately.


   B. Bird - 08/02/2014
*/

import java.util.*;
import java.io.*;

class Vertex implements Comparable<Vertex>{
		
		private final int name; 
		private int minDistance;
		private int[] adjVertices;
		
		public Vertex(int name, int minDistance, int numV){
			this.name = name;
			this.minDistance = minDistance;
			this.adjVertices = new int[numV];
		}
		
		public int compareTo(Vertex other){
			if(minDistance == other.minDistance){
				return 0;
			} else if(minDistance > other.minDistance){
				return 1;
			} else {
				return -1;
			}
		}
		
		public int getName(){
			return name;
		}
		
		public int getminDistance(){
			return minDistance;
		}
		
		public void setminDistance(int d){
			this.minDistance = d;	
		}
		
		public void setadjVertex(int index, int weight){
			adjVertices[index] = weight;
		}
		
		//get the weight of the edge incident with the adj vertex 
		public int getadjVertex(int index){
			return adjVertices[index];
		}
		
	}
	
//Do not change the name of the ShortestPath class
public class ShortestPath{

	/* ShortestPath(G)
		Given an adjacency matrix for graph G, return the total weight
		of a minimum weight path from vertex 0 to vertex 1.
		
		If G[i][j] == 0, there is no edge between vertex i and vertex j
		If G[i][j] > 0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/
	static int ShortestPath(int[][] G){
		int numV = G.length;
		int[] distance = new int[numV];
		distance[0] = 0;
		for (int i = 1; i < numV; i++){
			distance[i] = Integer.MAX_VALUE;
		}
		PriorityQueue<Vertex> pq = new PriorityQueue<>();
		for (int i = 0; i < numV; i++){
			Vertex V = new Vertex(i, distance[i], numV);
			for (int j = 0; j < numV; j++){
				if (G[i][j] != 0){
					V.setadjVertex(j, G[i][j]);
				}			
			}
			pq.add(V);
		}
		Stack<Vertex> st = new Stack<>();
		
		while(pq.size() != 0){
			Vertex u = pq.poll();
			for (int i = 0; i < numV; i++){
				if (u.getadjVertex(i) != 0){
					if ((distance[u.getName()] + u.getadjVertex(i)) < distance[i]){
						distance[i] = distance[u.getName()] + u.getadjVertex(i);
						//update z's key in pq
						while(pq.size() != 0){
							Vertex x = pq.poll();
							if (x.getName() != i){
								st.push(x);
							} else if (x.getName() == i){
								x.setminDistance(distance[i]);
								st.push(x);
							}
						}
						while(!st.empty()){
							Vertex y = st.pop();
							pq.add(y);
						}	
					}
				}	
			}
		}
		
		int totalWeight = distance[1];
		return totalWeight;
		
	}
		
	/* main()
	   Contains code to test the ShortestPath function. You may modify the
	   testing code if needed, but nothing in this function will be considered
	   during marking, and the testing process used for marking will not
	   execute any of the code below.
	*/
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		//Read graphs until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			long startTime = System.currentTimeMillis();
			
			int totalWeight = ShortestPath(G);
			
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			System.out.printf("Graph %d: Minimum weight of a 0-1 path is %d\n",graphNum,totalWeight);
		}
		graphNum--;
		System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
	}
}