package main.tsp;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked") // this might be playing with fire, lol
public class TSPNode implements Comparable<TSPNode> {

    /* CONSTANTS */
    private static double INFINITY = Double.MAX_VALUE;

    /* Static Members */

    /* Data Members */
    private double[][] matrix;
    private double lowerBound;
    private List<Integer> partialPath;
    private int currentCity;

    /* Constructors */
    public TSPNode(double[][] matrix) {
        this.matrix = matrix;
        this.lowerBound = 0;
        this.partialPath = new ArrayList<>();
    }

    public TSPNode(double[][] matrix, double lowerBound) {
        this(matrix);
        this.lowerBound = lowerBound;
    }

    public TSPNode(double[][] matrix, double lowerBound, List<Integer> partialPath) {
        this(matrix, lowerBound);
        this.partialPath = partialPath;
    }

    public static double[][] createDistanceMatrix(List<DistanceComparable> cities) {
        double[][] distanceMatrix = new double[cities.size()][cities.size()];

        for (int row = 0; row < cities.size(); row++) {
            for (int col = 0; col < cities.size(); col++) {
                if (row == col) {
                    distanceMatrix[row][col] = INFINITY;
                } else {
                    distanceMatrix[row][col] = cities.get(row).getDistance(cities.get(col));
                    // Make the distance reflexive, we're bidirectional here, baby
                    distanceMatrix[col][row] = distanceMatrix[row][col];
                }
            }
        }

        return distanceMatrix;
    }


    // Also need a method for generating a child
    public TSPNode getChildForDestinationCity(int childCity) {
        double[][] childMatrix = copyMatrix(this.matrix);

        // Initialize the child's bound
        double childBound = this.lowerBound;

        // Blot out the edge we're taking, and add that to the bound
        childBound += childMatrix[this.currentCity][childCity];

        // Make sure that the city we're leaving and arriving to aren't considered in future path construction
        invalidateCities(childMatrix, this.currentCity, childCity);

        // Build the child's new partial path
        List<Integer> childPath = new ArrayList<>(this.partialPath);
        childPath.add(childCity);

        // Create the child and clean up any loose ends
        TSPNode child = new TSPNode(childMatrix, childBound, childPath);
        child.setCurrentCity(childCity);

        // Reduce the child's matrix one last time, to make sure they're good to go for the future
        child.reduceMatrix();

        return child;
    }


    public void reduceMatrix()
    {
        // First we go through every row, and reduce if the min != 0 or infinity
        for (int row = 0; row < matrix.length; row++)
        {
            double min = getMinForRow(matrix, row);

            if (min != 0 && min != INFINITY)
            {
                // Increase the bound and reduce the row!
                lowerBound += min;
                reduceRow(matrix, row, min);
            }
        }

        // Now we do the same for the columns!
        for (int col = 0; col < matrix.length; col++)
        {
            double min = getMinForColumn(matrix, col);

            if (min != 0 && min != INFINITY)
            {
                lowerBound += min;
                reduceColumn(matrix, col, min);
            }
        }
    }


    public List<Integer> getPossibleChildren() {
        // Actually, shouldn't this just loop through the indices of the matrix to build the child list?
        List<Integer> children = new ArrayList<>();

        // Count up them children
        // Base case is that this is a near complete path, we can go back to the start
        if (partialPath.size() == matrix.length) {
            if (cityIsAccessible(partialPath.get(0))) {
                children.add(partialPath.get(0));
            }
        }
        else {
            // We need to count manually
            for (int i = 0; i < matrix.length; i++) {
                if (i == currentCity) continue;             // Can't link to ourselves
                if (partialPath.contains(i)) continue;      // Can't go back to where we've already gone
                if (!cityIsAccessible(i)) continue;         // Can't go to an infinity spot

                children.add(i);
            }
        }

        return children;
    }

    public boolean cityIsAccessible(int cityNum) {
        return (this.matrix[currentCity][cityNum] != INFINITY);
    }

    public boolean hasCompletePath() {
        return (partialPath.size() == matrix.length + 1);
    }


    @Override
    public int compareTo(TSPNode o) {
        return Double.compare(this.getValue(), o.getValue());   // We're compared by our lower bounds!
    }

    public double getValue() {
        return lowerBound / Math.log(partialPath.size() + 2) / Math.log(3);
//        return lowerBound * (1 - (partialPath.size() / matrix.length));
    }


    /* Accessor Methods */
    //region getters and setters
    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    // TODO: Update the lowerbound and figure out what to do here, lol
    public double getLowerBound() {
        return this.lowerBound;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public List<Integer> getPartialPath() {
        return partialPath;
    }

    public void setPartialPath(List<Integer> partialPath) {
        this.partialPath = partialPath;
    }

    public int getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(int currentCity) {
        this.currentCity = currentCity;
    }

    public int getNumCities() {
        return matrix.length;
    }

    //endregion


    /* Helper Methods */
    // Mainly for like, matrix reduction stuff
    private void invalidateCities(double[][] childMatrix, int departingCity, int arrivingCity) {
        // Handle this edge on each side of the diagonal
        childMatrix[departingCity][arrivingCity] = INFINITY;
        childMatrix[arrivingCity][departingCity] = INFINITY;

        // Blot out the departing row! (this city's row)
        for (int i = 0; i < this.matrix.length; i++) {
            childMatrix[departingCity][i] = INFINITY;
        }

        // Blot out the incoming column (column of child's city)
        for (int i = 0; i < this.matrix.length; i++) {
            childMatrix[i][arrivingCity] = INFINITY;
        }
    }

    private void reduceRow(double[][] reduced, int rowNumber, double minValue) {
        for (int i = 0; i < reduced.length; i++)
        {
            if (reduced[rowNumber][i] == INFINITY) continue;
            reduced[rowNumber][i] = reduced[rowNumber][i] - minValue;
        }
    }

    private void reduceColumn(double[][] reduced, int columnNumber, double minValue) {
        for (int i = 0; i < reduced.length; i++)
        {
            if (reduced[i][columnNumber] == INFINITY) continue;
            reduced[i][columnNumber] = reduced[i][columnNumber] - minValue;
        }
    }

    private double getMinForRow(double[][] matrix, int rowNumber) {
        double min = INFINITY;

        for (int i = 0; i < matrix.length; i++)
        {
            if (matrix[rowNumber][i] < min)
            {
                min = matrix[rowNumber][i];
            }
        }

        return min;
    }

    private double getMinForColumn(double[][] matrix, int columnNumber) {
        double min = INFINITY;

        for (int i = 0; i < matrix.length; i++)
        {
            if (matrix[i][columnNumber] < min)
            {
                min = matrix[i][columnNumber];
            }
        }

        return min;
    }


    private double[][] copyMatrix(double[][] originalMatrix) {
        double[][] clone = new double[originalMatrix.length][];

        for (int i = 0; i < clone.length; i++) {
            clone[i] = this.matrix[i].clone();
        }

        return clone;
    }


}
