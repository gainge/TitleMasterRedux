package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    public void run(String[] args) {
        List<DistanceComparable> cities = buildNodeList("src/test.txt");

        BBSolver solver = new BBSolver();
        BBSolver.TIME_LIMIT = 20;

        double startTime = Util.getTime();

        List<DistanceComparable> optimalPath = solver.solve(cities);

        double endTime = Util.getTime();

        for (DistanceComparable matchup : optimalPath) {
            System.out.println(matchup.toString());
        }

        System.out.println(String.format("Elapsed Time: %3f seconds", endTime - startTime ));

    }




    private List<DistanceComparable> buildNodeList(String filePath) {
        List<DistanceComparable> cities = new ArrayList<>();

        // Add the dummy city
        MatchupNode city = new MatchupNode();
        city.setDummyCity(true);
        city.setPlayer1("DUMMY");
        city.setPlayer2("City");
        city.setDescription("Dummy Description");
        cities.add(city);

        Scanner sc = null;

        try {
            File inFile = new File(filePath);
            sc = new Scanner(inFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (sc != null) {
            // Read in the data from the file!
            while (sc.hasNext()) {
                String curLine = sc.nextLine();
                String[] lineElements = curLine.split("\\s+");

                if (lineElements.length < 2) continue;

                // Otherwise, we should parse the line, and add the city!
                city = new MatchupNode();
                city.setPlayer1(lineElements[0].toLowerCase()); // First Player
                city.setPlayer2(lineElements[2].toLowerCase()); // Second Player
                city.setDescription(curLine);
                cities.add(city);
            }

            // Add the dummy city, to allow for path computation rather than cycle
        }


        return cities;
    }


    public static void main(String[] args) {
        new Main().run(args);
    }
}
