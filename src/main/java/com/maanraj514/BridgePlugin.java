package com.maanraj514;

import com.maanraj514.game.GameManager;
import com.maanraj514.map.GameDataDatabase;
import com.maanraj514.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.FileNotFoundException;
import java.io.IOException;

public final class BridgePlugin extends Okmeta {

    private GameDataDatabase gameDataDatabase;

    @Override
    public void enable() {
        saveDefaultConfig();

        registerClasses();

        try{
            gameDataDatabase.loadAllData();
        } catch (FileNotFoundException e) {
            getLogger().info(Messages.ERROR_FILE_NOT_FOUND);
        }
    }

    @Override
    public void disable() {
        try{
            gameDataDatabase.saveAllData();
        } catch (IOException e) {
            getLogger().info(Messages.ERROR_IO);
        }
    }

    public void registerClasses() {
        gameDataDatabase = new GameDataDatabase(this);
        new GameManager(this);
    }

    public Location getLobbyLocation(){
        if (getConfig().getLocation("lobby-location") == null){
            return new Location(Bukkit.getWorlds().get(0), 0, 64, 0);
        }else{
            return getConfig().getLocation("lobby-location");
        }
    }

    public GameDataDatabase getGameDataDatabase() {
        return gameDataDatabase;
    }
}