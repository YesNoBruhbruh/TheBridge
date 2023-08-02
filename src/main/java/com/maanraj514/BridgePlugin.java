package com.maanraj514;

import com.maanraj514.game.GameManager;
import com.maanraj514.map.MapDatabase;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class BridgePlugin extends Okmeta {

    private MapDatabase mapDatabase;

    @Override
    public void enable() {
        saveDefaultConfig();

        registerClasses();
    }

    @Override
    public void disable() {
    }

    public void registerClasses() {
        mapDatabase = new MapDatabase(this);
        new GameManager(this);
    }

    public Location getLobbyLocation(){
        if (getConfig().getLocation("lobby-location") == null){
            return new Location(Bukkit.getWorlds().get(0), 0, 64, 0);
        }else{
            return getConfig().getLocation("lobby-location");
        }
    }

    public MapDatabase getMapDatabase() {
        return mapDatabase;
    }
}