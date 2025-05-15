import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.geometry.Insets
import javafx.scene.control.ScrollPane

object GUIBackend {

    // Data class representing a connection between two cities with a distance
    data class CityConnection(val city1: String, val city2: String, val distance: String)

    // Function to create the TableView displaying city connections
    fun createTableView(): TableView<CityConnection> {
        return TableView<CityConnection>().apply {
            // Adding columns for city1, city2, and distance with specific widths to look pleasant
            columns.addAll(
                createTableColumn("City 1", "city1", 150.0),
                createTableColumn("City 2", "city2", 150.0),
                createTableColumn("Distance (km)", "distance", 100.0)
            )
            isEditable = false
            prefHeight = 300.0 // Set preferred height for the table
            isVisible = false // Initially, the table is hidden
        }
    }

    // Helper function to create a single TableColumn
    private fun createTableColumn(title: String, property: String, width: Double): TableColumn<CityConnection, String> {
        return TableColumn<CityConnection, String>(title).apply {
            cellValueFactory = PropertyValueFactory(property) // Bind column to data property
            prefWidth = width // Sets column width
        }
    }

    // Function to handle the "Find Distance" button click event
    fun handleFindDistance(city1Field: TextField, city2Field: TextField, tableView: TableView<CityConnection>, graph: Graph) {
        val city1 = city1Field.text.trim() // Gets text from input fields and trim it
        val city2 = city2Field.text.trim()
        tableView.items.clear() // Clear any previous results in the table

        // Search for an edge between the two cities
        val edge = graph.getEdges().find {
            (it.node1.name.equals(city1, true) && it.node2.name.equals(city2, true)) ||
                    (it.node1.name.equals(city2, true) && it.node2.name.equals(city1, true))
        }

        // If an edge is found, displays the distance; otherwise, shows "No distance found"
        if (edge != null) {
            tableView.items.add(CityConnection(edge.node1.name, edge.node2.name, "${edge.distance}"))
        } else {
            tableView.items.add(CityConnection(city1, city2, "No distance found"))
        }

        tableView.isVisible = true // Shows the table with the result
    }

    // Function to handle "List All Distances" button click event
    fun handleListAllDistances(tableView: TableView<CityConnection>, graph: Graph) {
        tableView.items.clear() // Clear any previous data
        graph.getEdges().forEach {
            tableView.items.add(CityConnection(it.node1.name, it.node2.name, "${it.distance}")) //this is how they'll show up in the table
        }
        tableView.isVisible = true // Shows the table with all distances
    }

    // Function to display the Adjacency List, it'll open in a new window so that it's easy to read through
    fun handleDisplayGraphDetails(title: String, graph: Graph) {
        val adjacencyStage = Stage()
        adjacencyStage.title = title

        // Creates a ScrollPane to display the adjacency list
        val scrollPane = ScrollPane()
        val cityDetailsBox = VBox(20.0).apply {
            padding = Insets(10.0) // Adding padding for better spacing and nicer to look
        }

        // Adding header
        cityDetailsBox.children.add(Label("Adjacency List Details").apply {
            style = "-fx-font-size: 18px; -fx-font-weight: bold;"
        })

        // Displaying the number of nodes and edges of this data of capital city distances
        cityDetailsBox.children.add(Label("Total Nodes: ${graph.getNodes().size}").apply {
            style = "-fx-font-size: 14px;"
        })
        cityDetailsBox.children.add(Label("Total Edges: ${graph.getEdges().size}").apply {
            style = "-fx-font-size: 14px;"
        })

        // Loops through the adjacency list and display each city with its connections
        graph.getAdjacencyList().forEach { (node, connections) ->
            cityDetailsBox.children.add(Label(node.name).apply {
                style = "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;"
            })

            // Creates a GridPane to display connected cities and distances
            val gridPane = GridPane().apply {
                hgap = 10.0
                vgap = 5.0
                padding = Insets(5.0, 0.0, 10.0, 10.0)
            }

            gridPane.add(Label("Connected City").apply { style = "-fx-font-weight: bold;" }, 0, 0)
            gridPane.add(Label("Distance (km)").apply { style = "-fx-font-weight: bold;" }, 1, 0)

            // Loops through the connections and add them to the GridPane
            connections.forEachIndexed { index, edge ->
                gridPane.add(Label(edge.node2.name), 0, index + 1)
                gridPane.add(Label("${edge.distance} km"), 1, index + 1)
            }

            cityDetailsBox.children.add(gridPane) // Adding the GridPane to the VBox
        }

        // Set up ScrollPane with the content and show the window
        scrollPane.content = cityDetailsBox
        val scene = Scene(scrollPane, 600.0, 800.0)
        adjacencyStage.scene = scene
        adjacencyStage.show()
    }

    // Function to calculate and display the Minimum Spanning Tree (MST) in a table and a label for the Total MST Distance
    fun handleCalculateMST(tableView: TableView<CityConnection>, detailsLabel: Label, graph: Graph) {
        val mstCalculator = MSTCalculator(graph)
        val mstResult = mstCalculator.calculateMST()
        detailsLabel.text = "MST Total Distance: ${mstResult.totalDistance} km" // the label with the total distance

        tableView.items.clear() // Clear previous data
        mstResult.mstEdges.forEach {
            tableView.items.add(CityConnection(it.node1.name, it.node2.name, "${it.distance} km"))
        }
        tableView.isVisible = true // showing the table
    }

    // Function to visualize the MST graph in a new window
    fun handleShowMSTGraph(graph: Graph) {
        val mstCalculator = MSTCalculator(graph)
        val mstResult = mstCalculator.calculateMST()

        val stage = Stage()
        val graphPane = GraphPane()
        graphPane.drawGraph(mstResult.mstEdges)

        stage.scene = Scene(graphPane, 1000.0, 900.0)
        stage.title = "MST Visualization"
        stage.show()
    }

    // Function to list all direct connections for a specific city
    fun handleListConnections(field: TextField, tableView: TableView<CityConnection>, graph: Graph) {
        val city = field.text.trim() // Get city name from input field
        val cityNode = graph.getNodes().find { it.name.equals(city, true) }

        tableView.items.clear() // Clear any previous results

        // If city is found, list its connections
        if (cityNode != null) {
            val connections = graph.getAdjacencyList()[cityNode]
            connections?.forEach {
                tableView.items.add(CityConnection(city, it.node2.name, "${it.distance}"))
            } ?: tableView.items.add(CityConnection(city, "No direct connections", "N/A"))
        } else {
            tableView.items.add(CityConnection(city, "City not found", "N/A"))
        }
        tableView.isVisible = true // Show the table with the connections to the city
    }

}
