package com.maanraj514.map;

import org.bukkit.World;

public interface MapInterface {

    boolean load();
    void unload();
    void delete(String name);

    boolean isLoaded();
    World getWorld();
}