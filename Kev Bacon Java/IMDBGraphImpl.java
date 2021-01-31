import java.io.*;
import java.util.*;
import java.util.HashSet;

public class IMDBGraphImpl implements IMDBGraph {

	private class Nodes implements Node {
		private String name;
		private HashSet<Nodes> Neighbors;

		public Nodes (String n){
			name = n;
			Neighbors = new HashSet<Nodes>();
		}

		/**
		 * add a neighbor to the current Node
		 * @param n neighbor node to add
		 */
		public void addNeighbors(Nodes n){
			Neighbors.add(n);
		}

		/**
		 * @return the name of the node
		 */
		public String getName (){
			return name;
		}

		/**
		 * @return the neighbors of the node
		 */
		public Collection<? extends Node> getNeighbors (){
			return Neighbors;
		}
	}

	private HashSet<Nodes> actors = new HashSet<>();
	private HashSet<Nodes> movies = new HashSet<>();
	private HashSet<String> movieNames = new HashSet<>();
	private HashMap<String,Nodes> movieNodes = new HashMap<>();
	private HashMap<String,Nodes> actorNodes = new HashMap<>();

	/**
	 * instanciate the IMBGraphImpl class with two files consisting of the actors and actresses
	 * @param actorsFilename name of a file in IMB format with actors and movies they starred in
	 * @param actressesFilename name of a file in IMB format with actresses and movies they starred in
	 * @throws IOException
	 */
	public IMDBGraphImpl (String actorsFilename, String actressesFilename) throws IOException {
		processActors(actorsFilename);
		processActors(actressesFilename);
	}

	/**
	 * @return the collection of actors
	 */
	public Collection<? extends Node> getActors () {
		return actors;
	}
	/**
	 * @return the collection of movies
	 */
	public Collection<? extends Node> getMovies () {
		return movies;
	}
	/**
	 * @return a movie node with given name from the collection
	 */
	public Node getMovie (String name) {
		return movieNodes.get(name);
	}
	/**
	 * @return a actor node with given name from the collection
	 */
	public Node getActor (String name) {
		return actorNodes.get(name);
	}

	/**
	 * Parses the movie title from a line containing a movie
	 * @param str line containing a movie
	 * @return the movie title
	 */
	protected static String parseMovieName (String str) {
		int idx1 = str.indexOf("(");
		int idx2 = str.indexOf(")", idx1 + 1);
		return str.substring(0, idx2 + 1);
	}

	/**
	 * Scans an IMDB file for its actors/actresses and movies
	 * @param filename the movie file to parse
	 */
	protected void processActors (String filename) throws IOException {
		final Scanner s = new Scanner(new File(filename), "ISO-8859-1");
		while (s.hasNextLine()) {
			String line = s.nextLine();
			if (line.startsWith("Name") && line.indexOf("Titles") >= 0) {
				break;
			}
		}
		s.nextLine();  // read one more
		String actorName = null;
		Nodes actor = new Nodes(actorName);
		while (s.hasNextLine()) {
			final String line = s.nextLine();
			if (line.indexOf("\t") >= 0) {  // new movie, either for an existing or a new actor
				int idxOfTab = line.indexOf("\t");
				if (idxOfTab > 0) {  // not at beginning of line => new actor
					actorName = line.substring(0, idxOfTab);
					// We have found a new actor...
					actor = new Nodes(actorName);
				}
				if (line.indexOf("(TV)") < 0 && line.indexOf("\"") < 0) {  // Only include bona-fide movies
					int lastIdxOfTab = line.lastIndexOf("\t");
					final String movieName = parseMovieName(line.substring(lastIdxOfTab + 1));
					if(!actorNodes.containsKey(actorName)){ //if the actor has a movie and isn't already in the Collection
						actors.add(actor);
						actorNodes.put(actorName,actor);
					}
					Nodes movie = new Nodes(movieName);
					if(movieNames.contains(movieName))
						movie = movieNodes.get(movieName);
					movie.addNeighbors(actor);
					actor.addNeighbors(movie);
					movies.add(movie);
					movieNames.add(movieName);
					movieNodes.put(movieName,movie);
				}
			}
		}
	}
}


