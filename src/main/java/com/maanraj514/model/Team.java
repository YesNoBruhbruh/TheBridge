package com.maanraj514.model;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Team {

    private String name;
    private Location spawnLocation;
    private Location portalLocationOne;
    private Location portalLocationTwo;
    private ChatColor color;

    public Team() {
    }

    public Team(String name, Location spawnLocation, Location portalLocationOne, Location portalLocationTwo, ChatColor color) {
        this.name = name;
        this.spawnLocation = spawnLocation;
        this.portalLocationOne = portalLocationOne;
        this.portalLocationTwo = portalLocationTwo;
        this.color = color;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public ChatColor getColor() {
        return color;
    }

    public Location getPortalLocationOne() {
        return portalLocationOne;
    }

    public Location getPortalLocationTwo() {
        return portalLocationTwo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public void setPortalLocationOne(Location portalLocationOne) {
        this.portalLocationOne = portalLocationOne;
    }

    public void setPortalLocationTwo(Location portalLocationTwo) {
        this.portalLocationTwo = portalLocationTwo;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }
}