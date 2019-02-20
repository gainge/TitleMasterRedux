package main.driver;

import main.tsp.DistanceComparable;

import java.util.Objects;

public class MatchupNode implements DistanceComparable<MatchupNode> {

    /* Data Members */
    private String player1;
    private String player2;
    private String description;

    private boolean isDummyCity;

    public MatchupNode() {
        // Default constructor
        this.player1 = "temp1";
        this.player2 = "temp2";

        this.isDummyCity = false;
    }

    public MatchupNode(String p1, String p2) {
        this();
        this.player1 = p1;
        this.player2 = p2;
    }


    /* Accessor Methods */
    //region getters/setters
    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDummyCity() {
        return isDummyCity;
    }

    public void setDummyCity(boolean dummyCity) {
        isDummyCity = dummyCity;
    }

    //endregion

    public String toString() {
        if (this.isDummyCity()) return "DUMMY CITY";
        StringBuilder str = new StringBuilder();
        str.append(this.player1);
        str.append(" vs ");
        str.append(this.player2);

        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchupNode)) return false;
        MatchupNode that = (MatchupNode) o;
        return isDummyCity == that.isDummyCity &&
                Objects.equals(player1, that.player1) &&
                Objects.equals(player2, that.player2) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player1, player2, description, isDummyCity);
    }

    @Override
    public double getDistance(MatchupNode other) {
        if (other == this) return Double.MAX_VALUE;

        double cost = 20;

        if (this.isDummyCity() || other.isDummyCity()) {
            return 0;
        }

        if (this.player1.equals(other.player1)) {
            cost /= 2;
        }
        if (this.player2.equals(other.player2)) {
            cost /= 2;
        }

        return cost;
    }
}
