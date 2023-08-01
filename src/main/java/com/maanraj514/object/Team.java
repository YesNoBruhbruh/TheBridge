package com.maanraj514.object;

import com.maanraj514.utils.LocationUtil;
import com.maanraj514.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class Team {

    private final String name;
    private final Location spawnLocation;
    private final Location portalLocationOne;
    private final Location portalLocationTwo;
    private final ChatColor color;

    public Team(ConfigurationSection section){
        this.name = section.getString("name");
        this.spawnLocation = LocationUtil.stringToLocation(Objects.requireNonNull(section.getString("spawnLocation")));
        this.portalLocationOne = LocationUtil.stringToLocation(Objects.requireNonNull(section.getString("portalLocationOne")));
        this.portalLocationTwo = LocationUtil.stringToLocation(Objects.requireNonNull(section.getString("portalLocationTwo")));
        this.color = ChatColor.valueOf(Objects.requireNonNull(section.getString("color")).toUpperCase());
    }

    public void write(ConfigurationSection section){
        section.set("name", name);
        section.set("spawnLocation", LocationUtil.locationToString(this.spawnLocation, false));
        section.set("portalLocationOne", LocationUtil.locationToString(this.portalLocationOne, false));
        section.set("portalLocationTwo", LocationUtil.locationToString(this.portalLocationTwo, false));
        section.set("color", color.toString().toUpperCase());
    }
}