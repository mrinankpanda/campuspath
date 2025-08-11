import java.util.List;
import java.util.NoSuchElementException;

public class Frontend implements FrontendInterface{

    private BackendInterface backend;
    
    /**
     * Constructor for Frontend. Requires working backend.
     * @param backend
     */
    public Frontend(BackendInterface backend){
        this.backend = backend;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateShortestPathPromptHTML() {
        // Generates HTML for start and end input fields and a button
        String startLabel = """
        <label for="start">
            Start
        </label>
        """;
        String startInput = """ 
        <input id="start" type="text"/>
        """;
        String endLabel = """
        <label for="end">
            End
        </label>
        """;
        String endInput = """ 
        <input id="end" type="text"/>
        """;
        String button = """  
        <button> Find Shortest Path </button>
        """;
        return startLabel + startInput + endLabel + endInput + button;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateShortestPathResponseHTML(String start, String end) {
        // Creates HTML response showing the shortest path and travel time
        String startParagraph = String.format("""
        <p>
            Finding shortest path from %s to %s:
        </p>
        """, start, end);

        if (start.equals("") || end.equals("")){
            return startParagraph + "<p>Error: Incomplete input</p>";
        }
    
        List<String> path = backend.findLocationsOnShortestPath(start, end);

        //No path exists
        if (path == null || path.isEmpty()) {
            return startParagraph + "<p>Error: Could not find a valid path.</p>";
        }
    
        //Build html list from pathList 
        StringBuilder pathList = new StringBuilder();
        pathList.append("<ol>");
        for (String p : path) {
            pathList.append("<li>" + p + "</li>");
        }
        pathList.append("</ol>");
    
        List<Double> pathTimes = backend.findTimesOnShortestPath(start, end);
        if (pathTimes == null || pathTimes.isEmpty()) {
            return startParagraph + "<p>Error: Could not retrieve travel times.</p>";
        }
    
        //Converts pathtimes List to the sum.
        double time = pathTimes.stream().mapToDouble(Double::doubleValue).sum();
        String timeParagraph = String.format("""
        <p>
            Total travel time: %.2f seconds.
        </p>
        """, time);
    
        return startParagraph + pathList + timeParagraph;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateFurthestDestinationFromPromptHTML() {
        // Generates HTML for the "from" input field and button
        String fromLabel = """
        <label for="from">
            From
        </label>
        """;
        String fromInput = """ 
        <input id="from" type="text"/>
        """;
        String button = """  
        <button> Furthest Destination From </button>
        """;
        return fromLabel + fromInput + button;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateFurthestDestinationFromResponseHTML(String start) {
        // Creates HTML response showing the furthest destination and path
        String startParagraph = String.format("""
        <p>
            Furthest Destination From %s:
        </p>
        """, start);

        if (start.equals("")){
            return startParagraph + "<p>Error: Incomplete input</p>";
        }
	String furthestDest;
	try {
     	   furthestDest = backend.getFurthestDestinationFrom(start);
	}
	catch (NoSuchElementException e){
             return startParagraph + "<p>Error: Invalid Input</p>";
	}
        if (furthestDest == null) {
            return startParagraph + "<p>Error: Could not determine the furthest destination.</p>";
        }
    
        String destinationParagraph = String.format("""
        <p>
            Furthest destination found: %s.
        </p>
        """, furthestDest);
	    //Gets shortest path    
        List<String> path = backend.findLocationsOnShortestPath(start, furthestDest);
        //No Path Exists
        if (path == null || path.isEmpty()) {
            return startParagraph + destinationParagraph + "<p>Error: Could not find a valid path.</p>";
        }
    
        StringBuilder pathList = new StringBuilder();
        pathList.append("<ol>");
        for (String p : path) {
            pathList.append("<li>" + p + "</li>");
        }
        pathList.append("</ol>");
    
        return startParagraph + destinationParagraph + pathList;
    }
}