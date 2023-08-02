package com.maanraj514.utils;

import com.maanraj514.BridgePlugin;
import com.maanraj514.game.Game;
import com.maanraj514.game.states.LoadingState;
import com.maanraj514.map.LocalGameMap;
import com.maanraj514.map.MapInterface;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import java.io.File;

public class WorldUtil {

    public static void loadGameWorld(Game game, BridgePlugin plugin) {
        File gameWorldsFolder = new File(plugin.getDataFolder() + "\\gameWorlds");
        if (!gameWorldsFolder.exists()){
            if (!gameWorldsFolder.mkdir()){
                plugin.getLogger().info(Messages.ERROR_DIRECTORY_CREATION);
            }
            plugin.getLogger().info("Created gameWorlds folder. But nothing is inside so returning.");
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
//            loadWorld(game.getMap(), plugin); not sure if this is needed.

            // also if the world folder needs its uid.dat to be removed,
            // make sure to do that automatically.
            MapInterface map = new LocalGameMap(gameWorldsFolder, game.getMap(), true);

            game.setWorld(map.getWorld());
            game.setState(new LoadingState());
        });
    }

    public static void loadWorld(String worldName, BridgePlugin plugin) {
        if (worldExists(worldName)){
            plugin.getLogger().info("World " + worldName + " already exists.");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> new WorldCreator(worldName));
    }

    public static void unloadWorld(String name){
        Bukkit.unloadWorld(name, false);
    }

    public static boolean worldExists(String name){
        return Bukkit.getWorld(name) != null;
    }
}