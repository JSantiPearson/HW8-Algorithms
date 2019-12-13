package analyzer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import data.Movie;
import graph.Graph;
import util.DataLoader;


/**
 * Please include in this comment you and your partner's name and describe any extra credit that you implement 
 */
public class MovieLensAnalyzer {
	
	public static void main(String[] args){
		// Your program should take two command-line arguments: 
		// 1. A ratings file
		// 2. A movies file with information on each movie e.g. the title and genres		
		if(args.length != 2){
			System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
			System.exit(-1);
		}		
		
		String ratingsFileName = args[0];
		String moviesFileName = args[1];
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("======== Welcome to MovieLens Analyzer ========");
		System.out.println("The files being analyzed are:");
		System.out.println(ratingsFileName);
		System.out.println(moviesFileName);
		
		DataLoader loader = new DataLoader();
		loader.loadData(moviesFileName, ratingsFileName);
		loader.printMovieList();
		
		Graph<Movie> graph = new Graph<Movie>();
		Map<Integer, Movie> movies = loader.getMovies();
		
		for (int i = 1; i <= loader.getMovies().size(); i++) {
			graph.addVertex(movies.get(i));
		}

		System.out.println("\nThere are 3 choices for defining adjacency:");
		System.out.println("[Option 1] u and v are adjacent if the same 12 users gave the same rating to both movies");
		System.out.println("[Option 2] u and v are adjacent if the same 12 users watched the same movie (regardless of rating)");
		System.out.println("[Option 3] u and v are adjacent if at least 33.0% of the users that rated u gave the same rating to v");
		
		System.out.println("\nChoose an option to build the graph (1-3):");
		int choice = input.nextInt();
		input.close();
		
		switch(choice) {
			case 1: {
				
			}
			case 2: {
				
			}
			case 3: {
				
			}
		}
	}
}
