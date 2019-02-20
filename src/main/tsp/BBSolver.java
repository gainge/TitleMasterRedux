package main.tsp;

import main.util.Util;
import main.queue.HeapQueue;
import main.queue.PriorityQueue;

import java.util.ArrayList;
import java.util.List;

public class BBSolver {

    public static int TIME_LIMIT = 20;

    /* Data Members */
    private PriorityQueue<TSPNode> priorityQueue;
    private TSPNode bssf;
    private double bssfLowerBound = Double.MAX_VALUE;



    public BBSolver() {
        // Default Constructor
    }


    /* Member Functions */
    public List<DistanceComparable> solve(List<DistanceComparable> cities) {
        // Start from all cities, classic TSP style
        List<Integer> startingCities = new ArrayList<>();

        for (int i = 0; i < cities.size(); i++) {
            startingCities.add(i);
        }

        return solve(cities, startingCities);
    }


    public List<DistanceComparable> solveForPath(List<DistanceComparable> cities, int dummyCityIndex) {
        // Only start from the dummy city, otherwise the bounding function won't create children properly
        List<Integer> startingCities = new ArrayList<>();
        startingCities.add(dummyCityIndex);

        return solve(cities, startingCities);
    }


    private List<DistanceComparable> solve(List<DistanceComparable> cities, List<Integer> startingCities) {
        this.priorityQueue = new HeapQueue<>(cities.size());    // Start w/ size at the number of cities

        // Create the initial distance matrix
        double[][] initialMatrix = TSPNode.createDistanceMatrix(cities);

        TSPNode genericRoot = new TSPNode(initialMatrix);
        genericRoot.reduceMatrix();

        findGreedySolution(initialMatrix, 0);

        performBB(genericRoot, Util.getTime(), startingCities);

        return buildOptimalPath(cities); // We'll return the ordered list of TSPNodes built from checking our bssf's partial path
    }


    /* Helper Functions */
    private void findGreedySolution(double[][] adjacency, int startingCity) {
        TSPNode greedySolution = new TSPNode(adjacency);

        int currentCity = startingCity;
        double greedyCost = 0;

        List<Integer> path = new ArrayList<>();
        path.add(startingCity);

        List<Integer> remainingCities = new ArrayList<>();
        for (int i = 0; i < adjacency.length; i++) {
            if (i == startingCity) continue;
            remainingCities.add(i);
        }

        while (true) {
            double minCost = Double.MAX_VALUE;
            int minCity = -1;

            for (Integer currentDestination : remainingCities) {
                if (path.contains(currentDestination)) continue;    // Move on
                if (currentDestination == currentCity) continue;    // no me gusta

                // Check to see if it's the min
                if (adjacency[currentCity][currentDestination] < minCost) {
                    minCost = adjacency[currentCity][currentDestination];
                    minCity = currentDestination;
                }

            }

            // Log this edge as being travelled
            greedyCost += minCost;
            path.add(minCity);
            currentCity = minCity;

            if (path.size() >= adjacency.length) break;
        }

        path.add(path.get(0));  // idk if this is how I did it or not

        greedySolution.setLowerBound(greedyCost);
        bssf = greedySolution;
        bssfLowerBound = greedyCost;
    }

    private List<DistanceComparable> buildOptimalPath(List<DistanceComparable> cities) {
        List<DistanceComparable> optimalPath = new ArrayList<>();

        for (Integer city : bssf.getPartialPath()) {
            optimalPath.add(cities.get(city));
        }

        return optimalPath;
    }

    private void performBB(TSPNode genericRoot, double startTime, List<Integer> startingCities) {
        for (Integer startCityIndex : startingCities) {
            // Create a new partial path for this dude, a new generic root starting who knows where
            List<Integer> partialPath = new ArrayList<>();
            partialPath.add(startCityIndex);

            genericRoot.setPartialPath(partialPath);
            genericRoot.setCurrentCity(startCityIndex);

            // Add this homie to our priority queue
            priorityQueue.add(genericRoot);


            // Now we get into the main loop!
            while (true) {
                TSPNode currentNode = priorityQueue.poll();
                if (currentNode == null) break;

                if (Util.getTime() - startTime >= TIME_LIMIT) {
                    return;     // Time is up!
                }

                if (currentNode.getLowerBound() > bssfLowerBound) {
                    continue;   // Ignore if outdated
                }

                // Otherwise, let's generate some child states
                List<Integer> possibleChildren = currentNode.getPossibleChildren();

                for (Integer childCity : possibleChildren) {
                    TSPNode childNode = currentNode.getChildForDestinationCity(childCity);

                    // If this child has potential
                    if (childNode.getLowerBound() < bssfLowerBound) {
                        if (childNode.hasCompletePath()) {
                            bssf = childNode;
                            bssfLowerBound = childNode.getLowerBound();
                        } else {
                            priorityQueue.add(childNode);   // Not complete, but we want to investigate their children
                        }
                    }
                } // End child loop
            } // End queue loop
        } // End random starting city loop
    }


}
