import java.util.PriorityQueue

// Data class to represent the result of MST (Minimum Spanning Tree) calculation
data class MSTResult(val totalDistance: Int, val mstEdges: List<Edge>)

class MSTCalculator(private val graph: Graph) {

    // Function to calculate the Minimum Spanning Tree (MST) by using Prim's Algorithm
    // Prim's algorithm builds the minimum spanning tree by starting at any node and adding the shortest edge
    // that connects a new node to the tree, repeating until all nodes are connected.

    fun calculateMST(): MSTResult {
        // If the graph has no nodes, this will return a default result (total distance = 0 and no edges)
        if (graph.getNodes().isEmpty()) return MSTResult(0, emptyList())

        // List to store the edges of the MST
        val mstEdges = mutableListOf<Edge>()

        // Setting to track the visited nodes
        val visitedNodes = mutableSetOf<Node>()

        // Priority queue to select the edge with the smallest distance at each step
        val priorityQueue = PriorityQueue<Edge>(compareBy { it.distance })

        // Start with the first node in the graph
        val startNode = graph.getNodes().first()
        visitedNodes.add(startNode)

        // Add all edges from the start node to the priority queue
        priorityQueue.addAll(graph.getAdjacencyList()[startNode] ?: emptyList())

        // While there are still nodes to visit and the priority queue is not empty
        while (priorityQueue.isNotEmpty() && visitedNodes.size < graph.getNodes().size) {
            // Poll the edge with the smallest distance
            val smallestEdge = priorityQueue.poll()

            // If the destination node of the edge is already visited, skip it
            if (visitedNodes.contains(smallestEdge.node2)) continue

            // Add the edge to the MST
            mstEdges.add(smallestEdge)
            // Mark the destination node as visited
            visitedNodes.add(smallestEdge.node2)

            // Add all edges from the newly visited node to the priority queue
            priorityQueue.addAll(graph.getAdjacencyList()[smallestEdge.node2] ?: emptyList())
        }

        // Calculates the total distance of the MST by summing the distances of all edges in mstEdges
        val totalDistance = mstEdges.sumOf { it.distance }

        // Returns the MST result with total distance and the list of MST edges
        return MSTResult(totalDistance, mstEdges)
    }

}
