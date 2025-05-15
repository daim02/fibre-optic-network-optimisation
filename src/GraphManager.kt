import java.io.File


// Class to represent the graph structure
class Graph {
    // Nodes are stored in a mutable set to avoid duplicates
    private val nodes = mutableSetOf<Node>()

    // Edges are stored in a mutable list
    private val edges = mutableListOf<Edge>()

    // Adjacency list maps nodes to a list of their connected edges
    private val adjacencyList = mutableMapOf<Node, MutableList<Edge>>()

    // Method to add a node to the graph
    fun addNode(node: Node) {
        // Add node to the nodes set, ensuring uniqueness
        nodes.add(node)
        // Initialize its adjacency list if it doesn't exist
        adjacencyList.putIfAbsent(node, mutableListOf())
    }

    // Method to add an edge and its nodes to the graph
    fun addEdge(edge: Edge) {
        // Checks if the edge already exists (since it's undirected, check both directions)
        if (!edges.contains(edge) && !edges.contains(Edge(edge.node2, edge.node1, edge.distance))) {
            // Adding edge to the edge list
            edges.add(edge)
            // Ensure both nodes are part of the graph
            addNode(edge.node1)
            addNode(edge.node2)

            // Adding edge to the adjacency list for both directions
            adjacencyList[edge.node1]?.add(edge)
            adjacencyList[edge.node2]?.add(Edge(edge.node2, edge.node1, edge.distance)) // Reverse edge
        }
    }

    // Method to get all nodes in the graph
    fun getNodes(): Set<Node> = nodes

    // Method to get all edges in the graph
    fun getEdges(): List<Edge> = edges

    // Method to retrieve the adjacency list of the graph
    fun getAdjacencyList(): Map<Node, List<Edge>> = adjacencyList
}

// Function to load distances from a CSV file and create the graph
fun loadGraphFromFile(filePath: String): Graph {
    val graph = Graph()  // Create a new empty graph
    val file = File(filePath)

    if (file.exists()) {
        // Read the file and process each line
        file.readLines()
            .filter { it.isNotBlank() }  // Ignore empty lines
            .forEach { line ->
                val parts = line.split(",")  // Split by comma to extract city names and distance
                if (parts.size == 3) {
                    val city1 = Node(parts[0].trim())  // Trim and create Node for city1
                    val city2 = Node(parts[1].trim())  // Trim and create Node for city2
                    val distance = parts[2].trim().toIntOrNull() ?: 0  // Try to parse distance to an integer

                    // If the distance is positive, create and add the edge
                    if (distance > 0) {
                        graph.addEdge(Edge(city1, city2, distance))
                    }
                }
            }
    } else {
        // Handle case where file doesn't exist
        println("File not found at $filePath")
    }

    return graph
}

// Extension function to format the adjacency list into a human-readable string
fun Graph.getFormattedAdjacencyList(): String {
    return buildString {
        append("Adjacency List:\n")

        // Iterate through the adjacency list and format each node with its connected edges
        this@getFormattedAdjacencyList.getAdjacencyList().forEach { (node, edges) ->
            append("${node.name}:\n")
            edges.forEach { edge ->
                append("  -> ${edge.node2.name} (${edge.distance} km)\n")  // Append each connected edge
            }
        }
    }
}
