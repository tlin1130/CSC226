/* MWST.java
   CSC 226 - Fall 2016
   Assignment 3 - Minimum Weight Spanning Tree Template
   
   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
	java MWST
	
   To conveniently test the algorithm with a large input, create a text file
   containing one or more test graphs (in the format described below) and run
   the program with
	java MWST file.txt
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

import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.io.File;

class WeightedQuickUnionUF{
	
	private int[] id;
	private int[] sz;
	private int count;
	
	public WeightedQuickUnionUF(int N){
		count = N;
		id = new int[N];
		for (int i = 0; i < N; i++){
			id[i] = i;
		}
		sz = new int[N];
		for (int i = 0; i < N; i++){
			sz[i] = 1;
		}
	}
	
	public int count(){
		return count;
	}

	public boolean connected(int p, int q){
		return find(p) == find(q);
	}
	
	public int find(int p){
		while (p != id[p]){
			id[p] = id[id[p]];
			p = id[p];
		}
		return p;
	}
	
	public void union(int p, int q){
		int i = find(p);
		int j = find(q);
		if (i == j){
			return;
		}
		if (sz[i] < sz[j]){
			id[i] = j;
			sz[j] += sz[i];
		} else {
			id[j] =i;
			sz[i] += sz[j];
		}
		count--;
	}

}

class SimpleMinPQ<Edge extends Comparable<Edge>>{
	   
	private Edge[] pq;                    // store items at indices 1 to n
    private int n = 0;                    // number of items on priority queue
    
    public SimpleMinPQ(int maxN) {
    	pq = (Edge[]) new Comparable[maxN+1];
    }
    
    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    
    public void insert(Edge x) {
        pq[++n] = x;
        swim(n);     
    }

    public Edge delMin() {
        exch(1, n);
        Edge min = pq[n--];
        sink(1);
        pq[n+1] = null;         
        return min;
    }


    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

   
    private boolean greater(int i, int j) {
    	return pq[i].compareTo(pq[j]) > 0;
    }

    private void exch(int i, int j) {
        Edge swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }
	
}

class Edge implements Comparable<Edge>{
	
	private int w;
	private int V;
	private int U;
	
	public Edge(int w, int V, int U){
		this.w = w;
		this.V = V;
		this.U = U;
	}
	
	public int compareTo(Edge E){
		if(w == E.w){
			return 0;
		} else if(w > E.w){
			return 1;
		} else {
			return -1;
		}
	}
	
	public int getWeight(){
		return w;
	}
	
	public int getV(){
		return V;
	}

	public int getU(){
		return U;
	}
	
}


//Do not change the name of the MWST class
public class MWST{

	/* mwst(G)
		Given an adjacency matrix for graph G, return the total weight
		of all edges in a minimum weight spanning tree.
		
		If G[i][j] == 0, there is no edge between vertex i and vertex j
		If G[i][j] > 0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/
	static int MWST(int[][] G){
		
		int totalWeight = 0;
		int numV = G.length;
		SimpleMinPQ pq = new SimpleMinPQ(numV*numV); 
		//initialize a priority queue to store edge weights 
		
		WeightedQuickUnionUF UF = new WeightedQuickUnionUF(numV);
		//initialize the union-find data structure
		
		int y = 0;
		
		for(int i = 0; i < numV; i++){
			for(int j = numV-1; j >= y; j--){
				if (G[i][j] != 0){
					Edge E = new Edge(G[i][j], i, j);
					pq.insert(E);
				}
				
			}
		}
		//insert edge weights in to pq
		
		Edge minEdgeWeight = new Edge(0, 0, 0);
		int MWSTEdgeNum = 0;
		int vertexV = 0;
		int vertexU = 0;
		
		while (!(MWSTEdgeNum == numV-1)){
			
			minEdgeWeight = (Edge) pq.delMin();
			
			vertexV = minEdgeWeight.getV();
			vertexU = minEdgeWeight.getU();
			
			boolean connection = UF.connected(vertexV, vertexU);
			if (!connection){
				totalWeight += minEdgeWeight.getWeight();
				UF.union(vertexV, vertexU);
				MWSTEdgeNum += 1;
			}
		}

		return totalWeight;
		
	}
	
		
	/* main()
	   Contains code to test the MWST function. You may modify the
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
			
			int totalWeight = MWST(G);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			System.out.printf("Graph %d: Total weight is %d\n",graphNum,totalWeight);
		}
		graphNum--;
		System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
	}
}