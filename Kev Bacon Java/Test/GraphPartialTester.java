import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import java.util.*;
import java.io.*;

/**
 * Code to test Project 3; you should definitely add more tests!
 */
public class GraphPartialTester {
	IMDBGraph imdbGraph;
	GraphSearchEngine searchEngine;

	/**
	 * Verifies that there is no shortest path between a specific and actor and actress.
	 * And that actors can path to actresses
	 * And that an actor paths to himself within multiple steps
	 */
	@Test(timeout=5000)
	public void findShortestPath () throws IOException {
		imdbGraph = new IMDBGraphImpl("actors_test.list", "actresses_test.list");
		final Node actor3 = imdbGraph.getActor("Actor3");
		final Node actor1 = imdbGraph.getActor("Actor1");
		final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor3);
		assertNull(shortestPath);  // there is no path between these people
		final Node actress3 = imdbGraph.getActor("Actress3");
		final List<Node> shortestPath2 = searchEngine.findShortestPath(actor3, actress3);
		assertNotNull(shortestPath2);
		final List<Node> shortestPath3 = searchEngine.findShortestPath(actor1, actor1);
		assertNotNull(shortestPath3);  // there is a path between these people
		assertEquals(shortestPath3.size(),1); //the start and end are equal (score = 0)
	}

	@Before
	/**
	 * Instantiates the graph
	 */
	public void setUp () throws IOException {
		imdbGraph = new IMDBGraphImpl("actors_test.list", "actresses_test.list");
		searchEngine = new GraphSearchEngineImpl();
	}


	/**
	 * Verifies it finds the shortest path on the large list
	 * @throws IOException
	 */

	@Test
	public void findLongPath() throws IOException {
		imdbGraph = new IMDBGraphImpl("actors.list", "actresses.list");
		final Node actor3 = imdbGraph.getActor("Bacon, Kevin (I)");
		final Node actress4 = imdbGraph.getActor("Pratt, Chris (I)");
		final List<Node> shortestPath = searchEngine.findShortestPath(actor3, actress4);
		assertNotNull(shortestPath);  // there is a path between these people
		assertEquals(shortestPath.size(),5); // the path has a score of 3
	}


	@Test
	public void findShortPath () throws IOException {
		imdbGraph = new IMDBGraphImpl("actors_test.list", "actresses_test.list");
		final Node actor2 = imdbGraph.getActor("Actor2");
		final Node actress2 = imdbGraph.getActor("Actress2");
		final List<Node> shortestPath = searchEngine.findShortestPath(actor2, actress2);
		assertNotNull(shortestPath);  // there is a path between these people
		assertEquals(shortestPath.size(),3); // the path has a score of 1
	}

	@Test
	/**
	 * Just verifies that the graphs could be instantiated without crashing.
	 */
	public void finishedLoading () {
		assertTrue(true);
		// Yay! We didn't crash
	}

	@Test
	/**
	 * Verifies that a specific movie has been parsed.
	 */
	public void testSpecificMovie () throws IOException {
		imdbGraph = new IMDBGraphImpl("actors_test.list", "actresses_test.list");
		testFindNode(imdbGraph.getMovies(), "Movie1 (2001)");
		imdbGraph = new IMDBGraphImpl("actors.list", "actresses.list");
		testFindNode(imdbGraph.getMovies(), "Vilamor (2012)");
	}

	@Test
	/**
	 * Verifies that a specific actress has been parsed.
	 */
	public void testSpecificActress () throws IOException {
		imdbGraph = new IMDBGraphImpl("actors_test.list", "actresses_test.list");
		testFindNode(imdbGraph.getActors(), "Actress2");
		imdbGraph = new IMDBGraphImpl("actors.list", "actresses.list");
		testFindNode(imdbGraph.getActors(), "Abad, Encarnita");
		testCantFindNode(imdbGraph.getActors(), "Prentice, Travis"); //does not keep an actor with only tv/tv movies
	}

	/**
	 * Verifies that the specific graph contains a node with the specified name
	 * @param nodes a Collection of Nodes to be searched
	 * @param name the name of the Node
	 */
	private static void testFindNode (Collection<? extends Node> nodes, String name) {
		boolean found = false;
		for (Node node : nodes) {
			if (node.getName().trim().equals(name)) {
				found = true;
			}

		}
		assertTrue(found);
	}

	/**
	 * Verifies that the specific graph does not contain a node with the specified name
	 * @param nodes a Collection of Nodes to be searched
	 * @param name the name of the Node
	 */
	private static void testCantFindNode (Collection<? extends Node> nodes, String name) {
		boolean found = false;
		for (Node node : nodes) {
			if (node.getName().trim().equals(name)) {
				found = true;
			}

		}
		assertFalse(found);
	}
}
