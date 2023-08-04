package com.maanraj514.model;

import org.bukkit.Location;

import java.util.List;

public class GameData {

    private final String map;

    private final List<String> authors;

    private final long lastEdit;

    private final List<Team> teams;

    private final Location spectatorSpawn;

    private final Location buildAbleCornerOne;
    private final Location buildAbleCornerTwo;

    public GameData(String map, List<String> authors, long lastEdit, List<Team> teams, Location spectatorSpawn, Location buildAbleCornerOne, Location buildAbleCornerTwo) {
        this.map = map;
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
}