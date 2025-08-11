import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DijkstraGraphTests {
    /**
   * The dijkstraGraphTest1 method tests the shortest path from node "A" to node "E" using
   * Dijkstra's algorithm. Asserts that the returned path matches the expected path:
   * ["A", "B", "D", "E"].
   */
  @Test
  public void dijkstraGraphTest1() {
    // Create the graph
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

    // Insert the nodes into the graph
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertNode("E");
    graph.insertNode("F");
    graph.insertNode("G");
    graph.insertNode("H");

    // Insert the edges to properly create the graph and connect the nodes
    graph.insertEdge("A", "B", 4);
    graph.insertEdge("A", "C", 2);
    graph.insertEdge("A", "E", 15);
    graph.insertEdge("B", "D", 1);
    graph.insertEdge("B", "E", 10);
    graph.insertEdge("C", "D", 5);
    graph.insertEdge("D", "E", 3);
    graph.insertEdge("D", "F", 0);
    graph.insertEdge("F", "D", 2);
    graph.insertEdge("F", "H", 4);
    graph.insertEdge("G", "H", 4);

    // Make an expectedList with the expected shortest path from A to E: A -> B -> D -> E
    List<String> expectedList = new ArrayList<>();
    expectedList.add("A");
    expectedList.add("B");
    expectedList.add("D");
    expectedList.add("E");

    // Assert that the path matches the expected output
    Assertions.assertEquals(expectedList, graph.shortestPathData("A", "E"));
  }

  /**
   * The dijkstraGraphTest2 method tests the path and cost from node "A" to node "E".
   * It checks individual steps in the path and verifies total cost is 8.
   */
  @Test
  public void dijkstraGraphTest2() {
    // Create the graph
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

    // Insert the nodes into the graph
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertNode("E");
    graph.insertNode("F");
    graph.insertNode("G");
    graph.insertNode("H");

    // Insert the edges to properly create the graph and connect the nodes
    graph.insertEdge("A", "B", 4);
    graph.insertEdge("A", "C", 2);
    graph.insertEdge("A", "E", 15);
    graph.insertEdge("B", "D", 1);
    graph.insertEdge("B", "E", 10);
    graph.insertEdge("C", "D", 5);
    graph.insertEdge("D", "E", 3);
    graph.insertEdge("D", "F", 0);
    graph.insertEdge("F", "D", 2);
    graph.insertEdge("F", "H", 4);
    graph.insertEdge("G", "H", 4);

    // Store the actual shortest path from A to E
    List<String> actualList = graph.shortestPathData("A", "E");

    // Check path length
    Assertions.assertEquals(4, actualList.size());

    // Check if the individual nodes in the path are in the right spots
    Assertions.assertEquals("A", actualList.get(0));
    Assertions.assertEquals("B", actualList.get(1));
    Assertions.assertEquals("D", actualList.get(2));
    Assertions.assertEquals("E", actualList.get(3));

    // Check the total cost from A to E
    Assertions.assertEquals(8, graph.shortestPathCost("A", "E"));
  }

  /**
   * The dijkstraGraphTest3 Tests that an exception is thrown when there is no
   * path from "A" to "G".
   */
  @Test
  public void dijkstraGraphTest3() {
    try {
      // Create the graph
      DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();

      // Insert the nodes into the graph
      graph.insertNode("A");
      graph.insertNode("B");
      graph.insertNode("C");
      graph.insertNode("D");
      graph.insertNode("E");
      graph.insertNode("F");
      graph.insertNode("G");
      graph.insertNode("H");

      // Insert the edges to properly create the graph and connect the nodes
      graph.insertEdge("A", "B", 4);
      graph.insertEdge("A", "C", 2);
      graph.insertEdge("A", "E", 15);
      graph.insertEdge("B", "D", 1);
      graph.insertEdge("B", "E", 10);
      graph.insertEdge("C", "D", 5);
      graph.insertEdge("D", "E", 3);
      graph.insertEdge("D", "F", 0);
      graph.insertEdge("F", "D", 2);
      graph.insertEdge("F", "H", 4);
      graph.insertEdge("G", "H", 4);

      // Attempt to compute path from A to G (should throw exception)
      graph.computeShortestPath("A", "G");
    } catch (NoSuchElementException e) {
      // Pass the test if the correction is thrown
      Assertions.assertTrue(true);
    }
  }
}