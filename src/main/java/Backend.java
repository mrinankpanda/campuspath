import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The Backend class implements the BackendInterface and provides functionalities for managing a
 * graph data structure representing locations and paths.
 */

public class Backend implements BackendInterface {

  private GraphADT<String, Double> graph; // Graph data structure storing locations as nodes and paths as weighted edges

  /**
   * This method is a constructor which initializes the backend with a given graph.
   * @param graph the graph data structure used by the backend
   */

  public Backend(GraphADT<String, Double> graph) {
    this.graph = graph;
  }

  /**
   * Loads graph data from a dot file.  If a graph was previously loaded, this method should first
   * delete the contents (nodes and edges) of the existing graph before loading a new one.
   *
   * @param filename the path to a dot file to read graph data from
   * @throws IOException if there was any problem reading from this file
   */
  @Override
  public void loadGraphData(String filename) throws IOException {
    // Remove all existing nodes in the graph before loading new data
    ArrayList<String> vertices = new ArrayList<>(graph.getAllNodes());
    for (String vertex : vertices) {
      graph.removeNode(vertex); // Remove each node from the graph
    }
    // Read and process the .dot file line by line
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        line = line.trim();
        // Skip lines that don't contain edge definitions
        if (!line.contains("->") || !line.contains("[seconds=")) {
          continue;
        }
        // Split the line to separate source and target+weight
        String[] parts = line.split("->");
        if (parts.length < 2) {continue;}
        // Extract and clean the source node name
        String source = parts[0].trim().replaceAll("\"", "");
        String[] restParts = parts[1].split("\\[seconds=");
        String target = restParts[0].trim().replaceAll("\"", "").replaceAll(";", "");
        // Extract the weight value, keeping only digits and decimal points
        double weight = Double.parseDouble(restParts[1].replaceAll("[^0-9.]", ""));
        // Ensure both nodes exist before adding the edge
        if (!graph.containsNode(source)) {
          graph.insertNode(source);
        }
        if (!graph.containsNode(target)) {
          graph.insertNode(target);
        }
        // Add the edge between the nodes
        graph.insertEdge(source, target, weight);
      }
    }
  }

  /**
   * Returns a list of all locations (node data) available in the graph.
   *
   * @return list of all location names
   */
  @Override
  public List<String> getListOfAllLocations() {
    // Use the graph's getAllNodes method to return the list of all location names.
    return graph.getAllNodes();
  }

  /**
   * Return the sequence of locations along the shortest path from startLocation to endLocation, or
   * an empty list if no such path exists.
   *
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list with the nodes along the shortest path from startLocation to endLocation, or an
   * empty list if no such path exists
   */
  @Override
  public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {
    try {
      // Call the graph's shortestPathData method to find the sequence of locations
      return graph.shortestPathData(startLocation, endLocation);
    } catch (NoSuchElementException e) {
      // If no path exists or locations are invalid, return an empty list
      return new ArrayList<>();
    }
  }

  /**
   * Return the walking times in seconds between each two nodes on the shortest path from
   * startLocation to endLocation, or an empty list of no such path exists.
   *
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list with the walking times in seconds between two nodes along the shortest path from
   * startLocation to endLocation, or an empty list if no such path exists
   */
  @Override
  public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {
    // First get the list of locations on the shortest path
    List<String> shortestPath = findLocationsOnShortestPath(startLocation, endLocation);
    // Initialize the list to store walking times between consecutive locations
    List<Double> timesOnShortestPath = new ArrayList<>();
    // If no path exists, return the empty list immediately
    if (shortestPath.isEmpty()) {
      return timesOnShortestPath;
    }
    // Iterate through adjacent pairs of locations in the path
    for (int i = 0; i < shortestPath.size() - 1; i++) {
      try {
        // Get the edge weight (walking time) between current location and the next one
        timesOnShortestPath.add(graph.getEdge(shortestPath.get(i), shortestPath.get(i + 1)));
        // If there's any error getting the edge, return an empty list
      } catch (NoSuchElementException e) {
        return new ArrayList<>();
      }
    }
    return timesOnShortestPath;
  }

  /**
   * Returns the most distant location (the one that takes the longest time to reach) when comparing
   * all shortest paths that begin from the provided startLocation.
   *
   * @param startLocation the location to find the most distant location from
   * @return the most distant location (the one that takes the longest time to reach which following
   * the shortest path)
   * @throws NoSuchElementException if startLocation does not exist, or if there are no other
   *                                locations that can be reached from there
   */
  @Override
  public String getFurthestDestinationFrom(String startLocation) throws NoSuchElementException {
    // Validate that the start location exists in the graph
    if(!graph.containsNode(startLocation)) {
      throw new NoSuchElementException("Start location does not exist");
    }
    // Get all locations in the graph
    List<String> allLocations = graph.getAllNodes();
    // Variables to track the furthest location and its distance
    String furthest = null;
    double maxDistance = Double.NEGATIVE_INFINITY;
    // Examine each location to find the furthest one
    for (String location : allLocations) {
      // Skip the start location itself
      if (location.equals(startLocation)) {
        continue;
      }
      try {
        // Calculate the shortest path cost (time) to this location
        double distance = graph.shortestPathCost(startLocation, location);
        // Update the furthest location if this one is more distant
        if (distance > maxDistance) {
          maxDistance = distance;
          furthest = location;
        }
      } catch (NoSuchElementException e) {
        // Skip locations that can't be reached from the start
        continue;
      }
    }
    // If no reachable location was found, throw an exception
    if (furthest == null) {
      throw new NoSuchElementException("No locations can be reached from the start location");
    }
    return furthest;
  }
}