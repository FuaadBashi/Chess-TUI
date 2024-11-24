package ws.aperture.chess.model;

import java.util.ArrayList;

public final class Player {

    private String name;
    private int rating;
    private int dumbRating;
    private int totalGames;
    private int ongoingGames;
    private ArrayList<Game> gameHistory;


    public Player(String name) {
        this.name = name;
        rating = 100;
        dumbRating = 0;
        totalGames = 0;
        ongoingGames = 0;
        gameHistory = new ArrayList<Game>();
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    public int getDumbRating() {
        return dumbRating;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getOngoingGames() {
        return ongoingGames;
    }

    public void startNewGame() {
        totalGames++;
        ongoingGames++;
    }

    public ArrayList<Game> getGameHistory() {
        return gameHistory;
    }    
}