package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.Pair;
import util.PriorityQueue;

public class GraphAlgorithms {
	
	public static int[] dijkstrasAlgorithm(Graph<Integer> graph, int source) {
		int vertices = graph.numVertices();
		PriorityQueue queue = new PriorityQueue();
		int[] dist = new int[vertices];
		Arrays.fill(dist, Short.MAX_VALUE);
		
		int[] prev = new int[vertices];
		Arrays.fill(prev, -1);
		
		dist[source-1] = 0;
		
		for (int i = 0; i < vertices; i++) {
			queue.push(dist[i], i+1);
		}
		//System.out.println(queue.heap.toString());
		
		Pair<Integer, Integer> u = null;
		while (!queue.isEmpty()) {
			u = queue.pop();
			//System.out.println(u.element);
			List<Integer> neighbors = graph.getNeighbors(u.element);
			for (int i = 0; i < neighbors.size(); i++) {
				//System.out.println(dist[u.element-1]);
				int alt = dist[u.element-1] + 1;
				int v = neighbors.get(i);
//				System.out.println(neighbors);
//				System.out.println(v);
//				System.out.println(alt+ " < " + dist[v-1]);
				if (alt < dist[v-1]) {
					dist[v-1] = alt;
					prev[v-1] = u.element;
					System.out.println(alt);
					queue.changePriority(alt, v);
				}
			}
		}
		
		return prev;
	}
	
	public static int[][] floydWarshall(Graph<Integer> graph){
		int v = graph.numVertices();
		int[][][] dist = new int[v+1][v][v];
		int i, j, k;
		for (i = 0; i < v; i++) { 
			Arrays.fill(dist[0][i], Short.MAX_VALUE);
			List<Integer> list = graph.getNeighbors(i+1);
			dist[0][i][i] = 0;
	        for (j = 0; j < list.size(); j++) { 
	        	dist[0][i][list.get(j)-1] = 1;
	        }
		}
		for (k = 1; k < v; k++)  
	    {  
	        for (i = 0; i < v; i++)  
	        {  
	            for (j = 0; j < v; j++)  
	            {  
	            	dist[k][i][j] = Math.min(dist[k-1][i][k] + dist[k-1][k][j], dist[k-1][i][j]);
	            }  
	        }  
	    }  
		return dist[v-1];
	}
	public static void main (String args[]) {
		Graph<Integer> graph = new Graph<Integer>();
		graph.addVertex(1);
		graph.addVertex(2);
		graph.addVertex(3);
		graph.addVertex(4);
		graph.addVertex(5);
		graph.addEdge(1,2);
        graph.addEdge(2,3);
        graph.addEdge(3,1);
        graph.addEdge(4,1);
        graph.addEdge(5,2);
        graph.addEdge(5,4);
        
        
        System.out.println(Arrays.toString(dijkstrasAlgorithm(graph, 1)));
	}
}
