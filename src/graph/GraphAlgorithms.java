package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

		dist[source - 1] = 0;

		for (int i = 0; i < vertices; i++) {
			queue.push(dist[i], i + 1);
		}

		int u;
		while (!queue.isEmpty()) {
			u = queue.topElement();
			queue.pop();
			List<Integer> neighbors = graph.getNeighbors(u);
			for (int i = 0; i < neighbors.size(); i++) {
				int alt = dist[u - 1] + 1;
				int v = neighbors.get(i);
				if (alt < dist[v - 1]) {
					dist[v - 1] = alt;
					prev[v - 1] = u;
					queue.changePriority(alt, v);
				}
			}
		}

		return prev;
	}
	
	public static int[][] floydWarshall(Graph<Integer> graph) {
		int v = graph.numVertices();
		Set<Integer> vertices = graph.getVertices();
		Integer[] verticesArray = vertices.toArray(new Integer[v]);
		int[][] dist = new int[v][v];
		int i, j, k;
		for (i = 0; i < v; i++) {
			for (j = 0; j < v; j++) {
				if (i == j) {
					dist[i][j] = 0;
				} else if (graph.edgeExists(verticesArray[i], verticesArray[j])) {
					dist[i][j] = 1;
				} else {
					dist[i][j] = Short.MAX_VALUE;
				}
			}
		}
		for (k = 0; k < v; k++) {
			for (i = 0; i < v; i++) {
				for (j = 0; j < v; j++) {
					dist[i][j] = Math.min(dist[i][k] + dist[k][j], dist[i][j]);
				}
			}
		}
		return dist;
	}
	public static void main (String args[]) {
		Graph<Integer> graph = new Graph<Integer>();
		graph.addVertex(1);
		graph.addVertex(2);
		graph.addVertex(3);
		graph.addVertex(4);
		graph.addVertex(5);
		graph.addEdge(5,2);
        graph.addEdge(2,3);
        graph.addEdge(3,4);
        
        System.out.println(Arrays.toString(dijkstrasAlgorithm(graph, 5)));
	}
}
