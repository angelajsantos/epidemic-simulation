package groupAssignments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;

public class EpidemicSimulation {
	
	public static void main(String[] args) throws IOException {
		// Initialize required objects and variables
        Scanner scnr= new Scanner(System.in);
        Random random = new Random();

        // Get required inputs from the user
        int N = getNumberOfIndividuals(scnr);
        int gridSize = (int) Math.sqrt(N);
        int T = getNumberOfTimeSteps(scnr);
        double alpha = getInfectionRate(scnr);
        double beta = getRecoveryRate(scnr);

        // Initialize the grid
        char[][][] gridData = initializeGrid(gridSize, random);
        char[][] grid = gridData[0];
        int patientZeroX = gridData[1][0][0];
        int patientZeroY = gridData[1][0][1];

        // Simulate the epidemic for T timesteps
        for (int t = 0; t < T; t++) {
        	char[][] newGrid = new char[gridSize][gridSize];
            int infectedCount = 0;
            int recoveredCount = 0;
            int susceptibleCount = 0;

            // Iterate through each cell in the grid
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    char current = grid[i][j];

                    // Update the cell's status based on the current status
                    if (current == 'I') {
                        if (t == 0 && i == patientZeroX && j == patientZeroY) { // Keep patient zero infected at timestep 0
                            newGrid[i][j] = 'I';
                            infectedCount++;
                        } else if (random.nextDouble() < beta) {
                            newGrid[i][j] = 'R';
                            recoveredCount++;
                        } else {
                            newGrid[i][j] = 'I';
                            infectedCount++;
                        }
                    } else if (current == 'S') {
                        if (t == 0) {
                            newGrid[i][j] = 'S';
                            susceptibleCount++;
                        } else {
                            double infectionProbability = getInfectionProbability(grid, i, j, gridSize, alpha);
                            if (random.nextDouble() < infectionProbability) {
                                newGrid[i][j] = 'I';
                                infectedCount++;
                            } else {
                                newGrid[i][j] = 'S';
                                susceptibleCount++;
                            }
                        }
                    } else {
                        newGrid[i][j] = 'R';
                        recoveredCount++;
                    }
                }
            }

            // Update the grid and save it to a file
            grid = newGrid;
            saveGridToFile(grid, gridSize, t);
            
            // Print the summary of the current timestep
            System.out.printf("Timestep %d: Infected: %d, Recovered: %d, Susceptible: %d, Ratio: %.2f\n",
                    t, infectedCount, recoveredCount, susceptibleCount, (double) infectedCount / N);
            printGrid(grid, gridSize);
        }
    }

	// Method to get the number of individuals (N) from the user
	private static int getNumberOfIndividuals(Scanner scnr) {
	    System.out.print("Enter the number of individuals (N, must be a perfect square): ");
	    while (true) {
	        try {
	            int N = scnr.nextInt();
	            double sqrt = Math.sqrt(N);
	
	            if (sqrt - Math.floor(sqrt) == 0) {
	                return N;
	            } else {
	                System.out.print("Invalid input. Please enter a perfect square: ");
	            }
	        } catch (InputMismatchException e) {
	            System.out.print("Invalid input. Please enter a valid number: ");
	            scnr.nextLine();
	        }
	    }
	}
	
	// Method to get the number of time steps (T) from the user
	private static int getNumberOfTimeSteps(Scanner scnr) {
	    System.out.print("Enter the number of time steps (T): ");
	    while (true) {
	        try {
	            int T = scnr.nextInt();
	            if (T > 0) {
	                return T;
	            } else {
	                System.out.print("Invalid input. Please enter a positive number: ");
	            }
	        } catch (InputMismatchException e) {
	            System.out.print("Invalid input. Please enter a valid number: ");
	            scnr.nextLine();
	        }
	    }
	}
	
	// Method to get the infection rate (α) from the user
	private static double getInfectionRate(Scanner scnr) {
	    System.out.print("Enter the infection rate (α, 0 <= α <= 1): ");
	    while (true) {
	        try {
	            double alpha = scnr.nextDouble();
	            if (alpha >= 0 && alpha <= 1) {
	                return alpha;
	            } else {
	                System.out.print("Invalid input. Please enter a value between 0 and 1: ");
	            }
	        } catch (InputMismatchException e) {
	            System.out.print("Invalid input. Please enter a valid number: ");
	            scnr.nextLine();
	        }
	    }
	}
	
	// Method to get the recovery rate (β) from the user
	private static double getRecoveryRate(Scanner scnr) {
	    System.out.print("Enter the recovery rate (β, 0 <= β <= 1): ");
	    while (true) {
	        try {
	            double beta = scnr.nextDouble();
	            if (beta >= 0 && beta <= 1) {
	                return beta;
	            } else {
	                System.out.print("Invalid input. Please enter a value between 0 and 1: ");
	            }
	        } catch (InputMismatchException e) {
	            System.out.print("Invalid input. Please enter a valid number: ");
	            scnr.nextLine();
	        }
	    }
	}

	// Method to initialize the grid
	private static char[][][] initializeGrid(int gridSize, Random random) {
	    char[][] grid = new char[gridSize][gridSize];

	    // Set all individuals as susceptible
	    for (int i = 0; i < gridSize; i++) {
	        for (int j = 0; j < gridSize; j++) {
	            grid[i][j] = 'S';
	        }
	    }

	    // Set patient zero as infected at a random position
	    int patientZeroX = random.nextInt(gridSize);
	    int patientZeroY = random.nextInt(gridSize);
	    grid[patientZeroX][patientZeroY] = 'I';

	    return new char[][][]{grid, new char[][]{{(char) patientZeroX, (char) patientZeroY}}};
	}
	
	// Method to calculate the infection probability for a susceptible cell
    private static double getInfectionProbability(char[][] grid, int x, int y, int gridSize, double alpha) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int infectedNeighbors = 0;

        // Check neighboring cells for infected individuals
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            // If the neighboring cell is within the grid and infected, increment infectedNeighbors
            if (newX >= 0 && newY >= 0 && newX < gridSize && newY < gridSize) {
                if (grid[newX][newY] == 'I') {
                    infectedNeighbors++;
                }
            }
        }
        
        // Calculate and return the infection probability
        return infectedNeighbors * alpha;
    }

    // Method to print the grid
    private static void printGrid(char[][] grid, int gridSize) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("----------------------------------------------------");
    }

    // Method to save the grid to a file
    private static void saveGridToFile(char[][] grid, int gridSize, int timestep) throws IOException {
    	
        String userHome = System.getProperty("user.home");
        String downloadsPath = userHome + "\\Downloads\\timesteps\\";
        // Create a directory named "timesteps" if it doesn't already exist
        
        File folder = new File(downloadsPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Save the grid to a file within the "timesteps" directory
        File file = new File(downloadsPath + "timestep_" + timestep + ".txt");
        FileWriter writer = new FileWriter(file);

        // Write the grid to the file
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                writer.write(grid[i][j] + " ");
            }
            writer.write(System.lineSeparator());
        }
        writer.close();
    }
}

