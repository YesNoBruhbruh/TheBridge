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

    private Location buildAbleCornerOne;
    private Location buildAbleCornerTwo;

    public GameData(String map, GameMode gameMode, List<String> authors, long lastEdit, List<Team> teams, Location spectatorSpawn, Location buildAbleCornerOne, Location buildAbleCornerTwo) {
        this.map = map;
        this.gameMode = gameMode;
        this.authors = authors;
        this.lastEdit = lastEdit;
        this.teams = teams;
        this.spectatorSpawn = spectatorSpawn;
        this.buildAbleCornerOne = buildAbleCornerOne;
        this.buildAbleCornerTwo = buildAbleCornerTwo;
    }

    public Location getSpectatorSpawn() {
        return spectatorSpawn;
    }

    public String getMap() {
        return map;
    }

    public Location getBuildAbleCornerOne() {
        return buildAbleCornerOne;
    }

    public Location getBuildAbleCornerTwo() {
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

    public void setBuildAbleCornerOne(Location buildAbleCornerOne) {
        this.buildAbleCornerOne = buildAbleCornerOne;
    }

    public void setBuildAbleCornerTwo(Location buildAbleCornerTwo) {
        this.buildAbleCornerTwo = buildAbleCornerTwo;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}