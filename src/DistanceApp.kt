import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

class DistanceApp : Application() {

    // Initially no graph is loaded (it will be loaded once the user selects a file)
    private var graph: Graph? = null

    // Creating a label for displaying details (this one is needed for the MST Total Distance Label that shows on top of the table)
    private lateinit var detailsLabel: Label

    // Creating a table to display city connections
    private lateinit var tableView: TableView<GUIBackend.CityConnection>

    override fun start(primaryStage: Stage) {
        // Starting the details label with an empty string and applying some basic styling
        detailsLabel = Label("").apply {
            style = "-fx-font-size: 14px; -fx-font-weight: bold;"
        }

        // Starting the table view (it's created in GUIBackend which handles all the complex operations of the GUI)
        tableView = GUIBackend.createTableView()

        // File Chooser Button to allow the user to load their own data file
        val loadFileButton = Button("Load Data File").apply {
            setOnAction {
                // This is to choose the type of file
                val fileChooser = FileChooser().apply {
                    extensionFilters.add(FileChooser.ExtensionFilter("Text Files", "*.txt"))
                }
                val selectedFile = fileChooser.showOpenDialog(primaryStage)
                if (selectedFile != null) {
                    // Loading the graph from the selected file
                    graph = loadGraphFromFile(selectedFile.absolutePath)
                    // Update the table with the new graph data
                    if (graph != null) {
                        GUIBackend.handleListAllDistances(tableView, graph!!)
                    } else {
                        showError("Failed to load graph from the selected file.")
                    }
                }
            }
        }

        // Label to explain the data format expected in the file
        val fileFormatLabel = Label("File format: City1, City2, distance between them (one per line)").apply {
            style = "-fx-font-size: 12px; -fx-font-style: italic;"
        }

        // -- DISTANCE CALCULATION SECTION --
        val city1Label = Label("First City:")
        val city2Label = Label("Second City:")
        val city1Field = TextField()
        val city2Field = TextField()

        // Button to trigger the distance calculation
        val distanceButton = Button("Find Distance").apply {
            setOnAction {
                // Only calculate distance if the graph is loaded
                if (graph != null) {
                    GUIBackend.handleFindDistance(city1Field, city2Field, tableView, graph!!)
                } else {
                    showError("Please load a data file first.")
                }
            }
        }

        // GridPane for layout of the distance calculation section
        val distanceGrid = GridPane().apply {
            add(city1Label, 0, 0) // First city label
            add(city1Field, 1, 0) // Input field for the first city
            add(city2Label, 0, 1) // Second city label
            add(city2Field, 1, 1) // Input field for the second city
            add(distanceButton, 1, 2) // Button to calculate the distance
            hgap = 10.0 // Horizontal gap between elements
            vgap = 10.0 // Vertical gap between elements
            padding = Insets(15.0) // Padding around the grid
        }

        // TitledPane for the distance calculation section
        val distanceSection = TitledPane("Distance Calculation", distanceGrid).apply {
            isCollapsible = false // This is to not allow collapsing
        }

        // -- LIST CONNECTIONS SECTION --
        val connectedCityLabel = Label("City:")
        val connectedCityField = TextField()

        // Button to view the connections for the input city
        val listConnectionsButton = Button("View").apply {
            setOnAction {
                // Only list connections if the graph is loaded
                if (graph != null) {
                    GUIBackend.handleListConnections(connectedCityField, tableView, graph!!)
                } else {
                    showError("Please load a data file first.")
                }
            }
        }

        // GridPane for layout of the list connections section
        val connectionsGrid = GridPane().apply {
            add(connectedCityLabel, 0, 0) // City label
            add(connectedCityField, 1, 0) // Input field for the city
            add(listConnectionsButton, 1, 1) // Button to list connections
            hgap = 10.0
            vgap = 10.0
            padding = Insets(15.0)
        }

        // TitledPane for the list connections section
        val connectionsSection = TitledPane("List all connections to a specific city", connectionsGrid).apply {
            isCollapsible = false
        }

        // -- GRAPH OPERATIONS SECTION --
        // VBox layout to hold the buttons for the various graph operations
        val graphOperationsVBox = VBox(10.0).apply {
            children.addAll(
                Button("List All Distances").apply {
                    setOnAction {
                        if (graph != null) {
                            GUIBackend.handleListAllDistances(tableView, graph!!)
                        } else {
                            showError("Please load a data file first.")
                        }
                    }
                },
                Button("Display Graph Details").apply {
                    setOnAction {
                        if (graph != null) {
                            GUIBackend.handleDisplayGraphDetails("Graph Details", graph!!)
                        } else {
                            showError("Please load a data file first.")
                        }
                    }
                },
                Button("Show MST Table").apply {
                    setOnAction {
                        if (graph != null) {
                            GUIBackend.handleCalculateMST(tableView, detailsLabel, graph!!)
                        } else {
                            showError("Please load a data file first.")
                        }
                    }
                },
                Button("Show MST Graph").apply {
                    setOnAction {
                        if (graph != null) {
                            GUIBackend.handleShowMSTGraph(graph!!)
                        } else {
                            showError("Please load a data file first.")
                        }
                    }
                }
            )
            alignment = Pos.CENTER_LEFT // Align the buttons to the left
            padding = Insets(15.0) // Padding around the VBox
        }

        // TitledPane for the graph operations section
        val graphOperationsSection = TitledPane("Graph Operations", graphOperationsVBox).apply {
            isCollapsible = false
        }

        // -- MAIN LAYOUT --
        // This contains all sections and the table view
        val mainLayout =
            VBox(15.0, loadFileButton, fileFormatLabel, distanceSection, connectionsSection, graphOperationsSection, detailsLabel, tableView).apply {
                padding = Insets(20.0) // Padding around the entire layout
                alignment = Pos.TOP_CENTER // Aligning everything to the top center
            }

        // Setting the scene with the main layout and setting the window size and title
        primaryStage.scene = Scene(mainLayout, 600.0, 700.0)
        primaryStage.title = "European Capital Distance Application by Group 42"
        primaryStage.show()
    }

    // This function is to display error messages in a new window , like when the data file is missing
    fun showError(message: String) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = "Error"
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }
}

fun main() {
    Application.launch(DistanceApp::class.java)
}
