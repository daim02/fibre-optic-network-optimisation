import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.text.Text
import kotlin.random.Random

class GraphPane : Pane() {

    // This is to have unique colors for each city and edge, I added this to make reading it easier
    private val cityColors = mutableMapOf<String, Color>()
    private val edgeColors = mutableMapOf<Pair<String, String>, Color>()

    // Function to generate a unique color for each city
    private fun getCityColor(city: String): Color {
        return cityColors.getOrPut(city) {
            Color.color(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
        }
    }

    // Function to generate a unique color for each edge
    private fun getEdgeColor(city1: String, city2: String): Color {
        // Ensure consistent edge color regardless of the order of cities
        val key = if (city1 < city2) city1 to city2 else city2 to city1
        return edgeColors.getOrPut(key) {
            Color.color(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
        }
    }

    // Function to draw the graph based on the given edges
    fun drawGraph(edges: List<Edge>) {
        children.clear() // Clears the Pane before redrawing the graph

        // Create a mapping of city names to unique node indices
        val nodeIndices = mutableMapOf<String, Int>()
        val uniqueNodes = edges.flatMap { listOf(it.node1.name, it.node2.name) }.distinct()

        // Assigning a unique index to each city
        uniqueNodes.forEachIndexed { index, city ->
            nodeIndices[city] = index + 1 // City index starts from 1
        }

        // I did a grid layout for the cities taking inspiration from the MST Diagrams
        val startX = 50.0   // Starting X-coordinate for the first city
        val startY = 50.0   // Starting Y-coordinate for the first city
        val xSpacing = 150.0 // Horizontal spacing between cities
        val ySpacing = 100.0 // Vertical spacing between cities

        val positions = mutableMapOf<String, Pair<Double, Double>>()

        uniqueNodes.forEachIndexed { index, city ->
            val x = startX + (index % 5) * xSpacing
            val y = startY + (index / 5) * ySpacing
            positions[city] = Pair(x, y) // Save the position of each city
        }

        // Draw edges with unique colors for making it easy to read and distinguish each connection
        edges.forEach { edge ->
            // Get the positions of the two cities connected by the edge
            val (x1, y1) = positions[edge.node1.name]!!
            val (x2, y2) = positions[edge.node2.name]!!

            val color = getEdgeColor(edge.node1.name, edge.node2.name)

            // Create and draw the edge as a line between the two cities
            val line = Line(x1, y1, x2, y2).apply {
                stroke = color
                strokeWidth = 2.5
            }

            // Positioning the distance label for the edge (I set as midpoint between the cities)
            val midX = (x1 + x2) / 2
            val midY = (y1 + y2) / 2
            val label = Text(midX, midY - 10, "${edge.distance} km").apply {
                fill = color
            }

            // Adding the edge and its label to the pane
            children.addAll(line, label)
        }

        // Drawing the cities with unique colors
        positions.forEach { (city, position) ->
            val (x, y) = position
            val color = getCityColor(city)

            // Drawing the city node as a circle
            val circle = Circle(x, y, 15.0).apply {
                fill = color
                stroke = Color.BLACK //border color for the circle
                strokeWidth = 1.5 // Circle border width
            }

            // Creating and drawing the labels for the city as number
            val label = Text(x - 5, y + 5, nodeIndices[city].toString()).apply {
                fill = Color.WHITE
            }

            // Add the city number to the pane
            children.addAll(circle, label)
        }

        // Adding a legend on the right to show the city names with their corresponding colors
        val legendStartX = 750.0
        val legendStartY = 20.0
        uniqueNodes.forEachIndexed { index, city ->
            val color = getCityColor(city)

            // a small circle with the colour for the legend
            val legendCircle = Circle(legendStartX, legendStartY + index * 20, 5.0).apply {
                fill = color
            }

            // the text for the city followed by the colour
            val legendText = Text(legendStartX + 10, legendStartY + index * 20 + 4, "${nodeIndices[city]}: $city").apply {
                fill = Color.BLACK
            }

            // Adding the legend
            children.addAll(legendCircle, legendText)
        }
    }
}
