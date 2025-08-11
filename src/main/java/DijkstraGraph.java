// === CS400 File Header Information ===
// Name: Mrinank Panda
// Email: mpanda4@wisc.edu
// Group and Team: <your group name: two letters, and team color>
// Group TA: <name of your group's ta>
// Lecturer: Florian Heimerl
// Notes to Grader: <optional extra notes>

import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
    extends BaseGraph<NodeType, EdgeType>
    implements GraphADT<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode
   * contains data about one specific path between the start node and another
   * node in the graph. The final node in this path is stored in its node
   * field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referened by the predecessor
   * field (this field is null within the SearchNode containing the starting
   * node in its node field).
   *
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost
   * SearchNode has the highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node;
    public double cost;
    public SearchNode predecessor;

    public SearchNode(Node node, double cost, SearchNode predecessor) {
      this.node = node;
      this.cost = cost;
      this.predecessor = predecessor;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost)
        return +1;
      if (cost < other.cost)
        return -1;
      return 0;
    }
  }

  /**
   * Constructor that sets the map that the graph uses.
   */
  public DijkstraGraph() {
    super(new HashtableMap<>());
  }

  /**
   * This helper method creates a network of SearchNodes while computing the
   * shortest path between the provided start and end locations. The
   * SearchNode that is returned by this method is represents the end of the
   * shortest path that is found: it's cost is the cost of that shortest path,
   * and the nodes linked together through predecessor references represent
   * all of the nodes along that shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found
   *                                or when either start or end data do not
   *                                correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end) {
    // If the start or end nodes are not present in the graph, throw the appropriate exception
    if (!containsNode(start) || !containsNode(end)) {
      if (!containsNode(start)) {
        throw new NoSuchElementException("Start node not found in the graph.");
      } else {
        throw new NoSuchElementException("End node not found in the graph.");
      }
    }
    // Create the priority queue needed for dijkstra's algorithm
    PriorityQueue<SearchNode> pq = new PriorityQueue<>();
    // Get the start node from the graph and add it to the priority queue
    Node startNode = nodes.get(start);
    pq.add(new SearchNode(startNode, 0.0, null));
    // Create a list to store the visited nodes in the dijkstra's shortest path
    HashtableMap<Node, Boolean> visitedMap = new HashtableMap<>();
    // While there are still nodes to explore in the priority queue
    while (!pq.isEmpty()) {
      // Get the node with the lowest cost from the priority queue
      SearchNode current = pq.poll();
      // If we already visited this node, we skip it
      if (visitedMap.containsKey(current.node)) {
        continue;
      }
      // Otherwise, mark this node as visited
      visitedMap.put(current.node, true);
      // If this is the destination node, return the final SearchNode
      if (current.node.data.equals(end)) {
        return current;
      }
      // Explore all outgoing edges from the current node
      for (Edge edge : current.node.edgesLeaving) {
        Node neighbor = edge.successor;
        // If the neighbor hasn't been visited yet
        if (!visitedMap.containsKey(neighbor)) {
          // Calculate the cost to reach this neighbor
          double newCost = current.cost + edge.data.doubleValue();
          // Add the neighbor to the priority queue with the updated cost and predecessor
          pq.add(new SearchNode(neighbor, newCost, current));
        }
      }
    }
    // If no path is found, throw the appropriate exception
    throw new NoSuchElementException("No path found between the start and end nodes.");
  }

  /**
   * Returns the list of data values from nodes along the shortest path
   * from the node with the provided start value through the node with the
   * provided end value. This list of data values starts with the start
   * value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shortest path. This
   * method uses Dijkstra's shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end) {
    // Compute the shortest path from the start node to the end node
    SearchNode endNode = computeShortestPath(start, end);
    // Create a list to store all the data within the shortest path from the start node to end node
    List<NodeType> shortestPath = new ArrayList<>();
    // Start with the endNode and then trace back through the predecessors
    SearchNode current = endNode;
    while (current != null) {
      // Add each node's data to the list
      shortestPath.add(current.node.data);
      current = current.predecessor;
    }
    // Reverse the list so that you can get the path from start to end
    Collections.reverse(shortestPath);
    // Return the final path
    return shortestPath;
  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest
   * path freom the node containing the start data to the node containing the
   * end data. This method uses Dijkstra's shortest path algorithm to find
   * this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   */
  public double shortestPathCost(NodeType start, NodeType end) {
    // Compute the shortest path from the start node to the end node
    SearchNode endNode = computeShortestPath(start, end);
    // Return the total cost of the shortest path
    return endNode.cost;
  }

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