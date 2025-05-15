// Data class to represent an edge (distance between two cities)
// Moved to a separate file to enhance modularity and facilitate future changes
// related to how connections between cities are represented or calculated.
data class Edge(val node1: Node, val node2: Node, val distance: Int)
