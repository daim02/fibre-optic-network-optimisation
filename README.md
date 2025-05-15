# Fibre Optic Network Optimisation

A Kotlin-based desktop application to compute the optimal high-speed fibre-optic network between European capitals using a Minimum Spanning Tree (MST).

## 🧠 Project Summary

This project simulates a real-world telecoms network expansion scenario. Given a list of capital cities and distances between them, it:

- Loads and validates the CSV dataset.
- Represents the network as a weighted graph.
- Computes the **Minimum Spanning Tree** using **Prim’s Algorithm**.
- Displays the result both in tabular and **graphical visualisation**.
- Supports user queries for distances and connections between cities.

## ✅ Features

| Feature | Status | Description |
|--------|--------|-------------|
| CSV File Upload | ✅ | Load city-to-city distance file (.txt in `City1,City2,Distance` format). |
| Graph Representation | ✅ | Graph structure built with nodes and edges (adjacency list). |
| MST Calculation | ✅ | Prim's Algorithm used to compute minimum cable length. |
| MST Visualisation | ✅ | JavaFX-based visual graph of the MST. |
| Distance Query | ✅ | Returns distance between two user-input cities. |
| Connection Lookup | ✅ | Shows all cities directly connected to a selected city. |
| Robust Error Handling | ✅ | Friendly error messages for invalid input, file issues, etc. |

## 🛠 Technologies Used

- **Language**: Kotlin
- **UI Framework**: JavaFX
- **Algorithm**: Prim’s MST
- **IDE**: IntelliJ IDEA

## 📄 Dataset Format

Accepted `.txt` format:
- City1,City2,Distance
- Paris,Berlin,1050
- Madrid,Rome,1365

## 🧪 How to Run

1. Clone the repo:
```bash
git clone https://github.com/daim02/fibre-optic-network-optimisation.git
```

2. Open in IntelliJ IDEA.

3. Run ```Main.kt```.

4. Use the GUI to load a data file provided in data folder and explore features.
