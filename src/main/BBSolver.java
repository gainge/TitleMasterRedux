package main;

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
        this.priorityQueue = new HeapQueue<>(cities.size());    // Start w/ size at the number of cities

        // Create the initial distance matrix
        double[][] initialMatrix = TSPNode.createDistanceMatrix(cities);

        TSPNode genericRoot = new TSPNode(initialMatrix);
        genericRoot.reduceMatrix();

        performBB(genericRoot, getTime(), cities.size());

        return buildOptimalPath(cities); // We'll return the ordered list of TSPNodes built from checking our bssf's partial path
    }



    /* Helper Functions */
    private List<DistanceComparable> buildOptimalPath(List<DistanceComparable> cities) {
        List<DistanceComparable> optimalPath = new ArrayList<>();

        for (Integer city : bssf.getPartialPath()) {
            optimalPath.add(cities.get(city));
        }

        return optimalPath;
    }

    private void performBB(TSPNode genericRoot, double startTime, int numCities) {
        for (int startCityIndex = 0; startCityIndex < numCities; startCityIndex++) {
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

                if (getTime() - startTime>= TIME_LIMIT) {
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


    /* Helper Methods */
    private double getTime() {
        return System.currentTimeMillis() / 1000.0;
    }

}
