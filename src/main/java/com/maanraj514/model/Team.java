package com.maanraj514.model;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;

public class Team {

    private String name;
    private Location spawnLocation;
    private Pos portalLocationOne;
    private Pos portalLocationTwo;
    private ChatColor color;
    private Color armorColor;

    public Team(ChatColor color) {
        this.color = color;
    }

    public Team(String name, Location spawnLocation, Pos portalLocationOne, Pos portalLocationTwo, ChatColor color, Color armorColor) {
        this.name = name;
        this.spawnLocation = spawnLocation;
        this.portalLocationOne = portalLocationOne;
        this.portalLocationTwo = portalLocationTwo;
        this.color = color;
        this.armorColor = armorColor;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public ChatColor getColor() {
        return color;
    }

    public Pos getPortalLocationOne() {
        return portalLocationOne;
    }

    public Pos getPortalLocationTwo() {
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

    public void setPortalLocationOne(Pos portalLocationOne) {
        this.portalLocationOne = portalLocationOne;
    }

    public void setPortalLocationTwo(Pos portalLocationTwo) {
        this.portalLocationTwo = portalLocationTwo;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public Color getArmorColor() {
        return armorColor;
    }

    public void setArmorColor(Color armorColor) {
        this.armorColor = armorColor;
    }
}