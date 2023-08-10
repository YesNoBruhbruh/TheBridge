package com.maanraj514.model;

import com.maanraj514.game.GameMode;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

public class GameData {

    private final String map;

    private final GameMode gameMode;

    private final List<String> authors;

    private long lastEdit;

    private List<Team> teams;

    private Location spectatorSpawn;

    private Pos buildAbleCornerOne;
    private Pos buildAbleCornerTwo;
    private int maxGoals;

    public GameData(String map, GameMode gameMode, List<String> authors, long lastEdit, List<Team> teams, Location spectatorSpawn, Pos buildAbleCornerOne, Pos buildAbleCornerTwo, int maxGoals) {
        this.map = map;
        this.gameMode = gameMode;
        this.authors = authors;
        this.lastEdit = lastEdit;
        this.teams = teams;
        this.spectatorSpawn = spectatorSpawn;
        this.buildAbleCornerOne = buildAbleCornerOne;
        this.buildAbleCornerTwo = buildAbleCornerTwo;
        this.maxGoals = maxGoals;
    }

    public Location getSpectatorSpawn() {
        return spectatorSpawn;
    }

    public String getMap() {
        return map;
    }

    public Pos getBuildAbleCornerOne() {
        return buildAbleCornerOne;
    }

    public Pos getBuildAbleCornerTwo() {
        return buildAbleCornerTwo;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public long getLastEdit() {
        return lastEdit;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setLastEdit(long lastEdit) {
        this.lastEdit = lastEdit;
    }

    public void setSpectatorSpawn(Location spectatorSpawn) {
        this.spectatorSpawn = spectatorSpawn;
    }

    public void setBuildAbleCornerOne(Pos buildAbleCornerOne) {
        this.buildAbleCornerOne = buildAbleCornerOne;
    }

    public void setBuildAbleCornerTwo(Pos buildAbleCornerTwo) {
        this.buildAbleCornerTwo = buildAbleCornerTwo;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public int getMaxGoals() {
        return maxGoals;
    }

    public void setMaxGoals(int maxGoals) {
        this.maxGoals = maxGoals;
    }
}