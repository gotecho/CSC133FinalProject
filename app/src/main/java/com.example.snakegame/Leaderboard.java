package com.example.snakegame;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;
import android.content.Context;

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
        // Returns a copy of the list but limited to the top 5 players
        int endIndex = Math.min(players.size(), 5);
        return new ArrayList<>(players.subList(0, endIndex));
    }


    public void display() {
        for (Player player : players) {
            System.out.println(player);
        }
    }

    public boolean isShown(boolean flag){
        return flag;
    }

    public void saveToPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        StringBuilder builder = new StringBuilder();
        for (Player player : players) {
            builder.append(player.getName()).append(",").append(player.getScore()).append(";");
        }
        editor.putString("leaderboard", builder.toString());
        editor.apply();
    }
    public void loadFromPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        String data = prefs.getString("leaderboard", "");
        if (!data.isEmpty()) {
            String[] playersData = data.split(";");
            for (String playerData : playersData) {
                String[] details = playerData.split(",");
                if (details.length == 2) {
                    addPlayer(new Player(details[0], Integer.parseInt(details[1])));
                }
            }
        }
    }
}