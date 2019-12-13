package analyzer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import data.Movie;
import graph.Graph;
import util.DataLoader;
import util.PriorityQueue;
import graph.GraphAlgorithms;

/**
 * Please include in this comment you and your partner's name and describe any
 * extra credit that you implement
 * 
 * Jordan and Thalia 
 * 
 */
public class MovieLensAnalyzer {

	public static void main(String[] args) {
		// Your program should take two command-line arguments:
		// 1. A ratings file
		// 2. A movies file with information on each movie e.g. the title and genres
		if (args.length != 2) {
			System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
			System.exit(-1);
		}

		String ratingsFileName = args[0];
		String moviesFileName = args[1];
		
		boolean quit = false;

		Scanner input = new Scanner(System.in);

		System.out.println("======== Welcome to MovieLens Analyzer ========");
		System.out.println("The files being analyzed are:");
		System.out.println(ratingsFileName);
		System.out.println(moviesFileName);

		DataLoader loader = new DataLoader();
		loader.loadData(moviesFileName, ratingsFileName);

		Graph<Integer> graph = new Graph<Integer>();
		Map<Integer, Movie> movies = loader.getMovies();

		for (int i = 1; i <= loader.getMovies().size(); i++) {
			graph.addVertex(i);
		}

		int v = graph.numVertices();

		System.out.println("\nThere are 3 choices for defining adjacency:");
		System.out.println("[Option 1] u and v are adjacent if the same 12 users gave the same rating to both movies");
		System.out.println(
				"[Option 2] u and v are adjacent if the same 12 users watched the same movie (regardless of rating)");
		System.out.println(
				"[Option 3] u and v are adjacent if at least 33.0% of the users that rated u gave the same rating to v");

		System.out.println("\nChoose an option to build the graph (1-3):");
		int graphChoice = input.nextInt();

		if (graphChoice == 1) {
			buildGraphOne(v, graph, movies);
		} else if (graphChoice == 2) {
			buildGraphTwo(v, graph, movies);
		}
		else if (graphChoice == 3) {
			buildGraphThree(v, graph, movies);
		} else {
			System.out.println("Choice does not exist, please enter a valid choice.");
			System.exit(0);
		}
		while (!quit) {
			System.out.println("[Option 1] Print out statistics about the graph");
			System.out.println("[Option 2] Print node information");
			System.out.println("[Option 3] Display shortest path between two nodes");
			System.out.println("[Option 4] Quit");
			System.out.println("Choose an option (1-4)");
			int analyzeChoice = input.nextInt();
			if (analyzeChoice == 1) {
				analysisChoiceOne(graph);
			} else if (analyzeChoice == 2) {
				System.out.println("Enter movie ID (1-1000): ");
				input = new Scanner(System.in);
				analyzeChoice = input.nextInt();
				analysisChoiceTwo(graph, analyzeChoice, movies);
			} else if (analyzeChoice == 3) {
				System.out.println("Enter the starting node: ");
				input = new Scanner(System.in);
				int start = input.nextInt();
				System.out.println("Enter the ending node: ");
				input = new Scanner(System.in);
				int end = input.nextInt();
				int[] path = dijkstrasAlgorithm(graph, start);
				int current = end;
				while (current != start) {
					System.out.println(movies.get(current).title + " ===> " + movies.get(path[current - 1]).title);
					current = path[current - 1];
				}
			} else if (analyzeChoice == 4) {
				System.out.println("Exiting... bye");
				quit = true;
			} else {
				System.out.println("Choice does not exist");
			}
		}
	}
	
	private static void buildGraphOne(int v, Graph<Integer> graph, Map<Integer, Movie> movies) {
		for (int i = 1; i <= v; i++) {
			for (int j = i + 1; j <= v; j++) {
				int counter = 0;
				for (Integer user : movies.get(i).getRatings().keySet()) {
					if (movies.get(j).rated(user)) {
						if (movies.get(i).getRating(user) == movies.get(j).getRating(user)) {
							counter++;
							if (counter == 12) {
								graph.addEdge(i, j);
								graph.addEdge(j, i);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private static void buildGraphTwo(int v, Graph<Integer> graph, Map<Integer, Movie> movies) {
		for (int i = 1; i <= v; i++) {
			for (int j = i + 1; j <= v; j++) {
				int counter = 0;
				for (Integer user : movies.get(i).getRatings().keySet()) {
					if (movies.get(j).rated(user)) {
						counter++;
						if (counter == 12) {
							graph.addEdge(i, j);
							graph.addEdge(j, i);
							break;
						}
					}
				}
			}
		}
	}
	
	private static void buildGraphThree(int v, Graph<Integer> graph, Map<Integer, Movie> movies) {
		for (int i = 1; i <= v; i++) {
			for (int j = i + 1; j <= v; j++) {
				int sameRatingCounter = 0;
				int userCounter = 0;
				for (Integer user : movies.get(i).getRatings().keySet()) {
					if (movies.get(j).rated(user)) {
						userCounter++;
						if (movies.get(i).getRating(user) == movies.get(j).getRating(user)) {
							sameRatingCounter++;
							if (sameRatingCounter >= userCounter * 0.333333) {
								graph.addEdge(i, j);
								graph.addEdge(j, i);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private static void analysisChoiceOne(Graph<Integer> graph) {
		System.out.println("\nGraph Statistics:");
		int numVert = graph.numVertices();
		int numEdges = graph.numEdges();
		double dbVert = (double) numVert;
		double dbEdge = (double) numEdges;
		System.out.println("	Number of nodes: " + numVert);
		System.out.println("	Number of edges: " + numEdges);
		System.out.println("	Density of graph: " + (dbEdge / (dbVert * (dbVert - 1))));
		int maxDegree = 0;
		int degreeNode = -1;
		for (int i = 1; i <= numVert; i++) {
			if (graph.degree(i) > maxDegree) {
				maxDegree = graph.degree(i);
				degreeNode = i;
			}
		}
		System.out.println("	Maximum degree: " + maxDegree + " (node " + degreeNode + ")");
		int[][] floyd = floydWarshall(graph);
		int maxShortPath = 0;
		int startNode = -1;
		int endNode = -1;
		for (int i = 0; i < numVert; i++) {
			for (int j = 0; j < numVert; j++) {
				if (floyd[i][j] > maxShortPath && floyd[i][j] != Short.MAX_VALUE) {
					maxShortPath = floyd[i][j];
					startNode = i + 1;
					endNode = j + 1;
				}
			}
		}
		System.out.println("	Diameter: " + maxShortPath + " (from " + startNode + " to " + endNode + ")");
		double averagePath = 0.0;
		int edgeCounter = 0;
		for (int i = 0; i < numVert; i++) {
			for (int j = 0; j < numVert; j++) {
				if (floyd[i][j] != Short.MAX_VALUE) {
					edgeCounter++;
					averagePath += floyd[i][j];
				}
			}
		}
		averagePath = averagePath / edgeCounter;
		System.out.println("	Average Path Length: " + averagePath);
	}
	
	private static void analysisChoiceTwo(Graph<Integer> graph, int analyzeChoice, Map<Integer, Movie> movies) {
		Movie movie = movies.get(analyzeChoice);
		List<Integer> neighbors = graph.getNeighbors(analyzeChoice);
		System.out.println(movie.toString());
		System.out.println("Neighbors:");
		for (int i = 0; i < graph.getNeighbors(analyzeChoice).size(); i++) {
			System.out.println("	" + movies.get(neighbors.get(i)).title);
		}
	}

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
}
