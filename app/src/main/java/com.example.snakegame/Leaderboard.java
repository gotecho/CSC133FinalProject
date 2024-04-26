package com.example.snakegame;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
    private List<Player> players;

    public Leaderboard() {
        players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
        Collections.sort(players);
    }

    // Method to get the list of players
    public List<Player> getPlayers() {
        return new ArrayList<>(players);  // Return a copy of the list to prevent external modification
    }

    public void display() {
        for (Player player : players) {
            System.out.println(player);
        }
    }

    public boolean isShown(boolean flag){
        return flag;
    }
}
