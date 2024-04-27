# Epidemic Simulation

This Java package simulates the spread of a disease among individuals on a 2D-grid. It models the infection, recovery, and susceptibility of individuals over a specified number of time steps.

## Instructions

To run the simulation, follow these steps:

1. Compile the `EpidemicSimulation.java` file.
2. Execute the compiled class file.
3. Follow the prompts to provide necessary inputs:
   - Number of individuals (`N`), which must be a perfect square.
   - Number of time steps (`T`).
   - Infection rate (`α`, between 0 and 1).
   - Recovery rate (`β`, between 0 and 1).

The simulation will generate a series of text files, each representing the state of the grid at a specific time step. These files will be saved in a directory named "timesteps" in your Downloads folder.

## Components

#### Main Method

The `main` method orchestrates the simulation process. It initializes required objects and variables, obtains user inputs, simulates the epidemic for the specified number of time steps, and prints the summary of each time step.

#### Helper Methods

- `getNumberOfIndividuals`: Prompts the user to enter the number of individuals (`N`).
- `getNumberOfTimeSteps`: Prompts the user to enter the number of time steps (`T`).
- `getInfectionRate`: Prompts the user to enter the infection rate (`α`).
- `getRecoveryRate`: Prompts the user to enter the recovery rate (`β`).
- `initializeGrid`: Initializes the grid with susceptible individuals and sets one individual as patient zero.
- `getInfectionProbability`: Calculates the infection probability for a susceptible cell based on neighboring infected cells.
- `printGrid`: Prints the current state of the grid.
- `saveGridToFile`: Saves the current state of the grid to a text file.

## How to Use

```java
// Compile the EpidemicSimulation.java file
javac EpidemicSimulation.java

// Run the compiled class file
java EpidemicSimulation
