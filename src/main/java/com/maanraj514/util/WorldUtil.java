package com.maanraj514.util;

import com.infernalsuite.aswm.api.exceptions.*;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import com.maanraj514.BridgePlugin;
import com.maanraj514.game.Game;
import com.maanraj514.game.state.GameState;
import org.bukkit.Bukkit;

import java.io.IOException;

public class WorldUtil {

    private static int id = 0;

    public static void loadGameWorld(Game game, BridgePlugin plugin) {
        SlimeLoader loader = plugin.getSlimePlugin().getLoader("file");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try{

                SlimeWorld world = plugin.getSlimePlugin().loadWorld(loader, game.getMap(), true, new SlimePropertyMap())
                        .clone(game.getMap() + "-" + id, null);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    plugin.getLogger().info(world.getName());
                    plugin.getSlimePlugin().loadWorld(world);

                    game.setWorld(Bukkit.getWorld(world.getName()));
                    game.setState(GameState.LOADING);
                });
                id++;

            } catch (WorldLockedException | CorruptedWorldException | NewerFormatException | IOException |
                     UnknownWorldException | WorldAlreadyExistsException e) {
                plugin.getLogger().info(Messages.ERROR_SLIME_ERROR);
                e.printStackTrace();
            }
        });
    }

    public static void loadWorld(String worldName, BridgePlugin plugin) {
        if (worldExists(worldName, plugin)){
            plugin.getLogger().info("World " + worldName + " already exists.");
            return;
        }
        SlimeLoader loader = plugin.getSlimePlugin().getLoader("file");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try{
                SlimeWorld world = plugin.getSlimePlugin().loadWorld(loader, worldName, true, new SlimePropertyMap());

                Bukkit.getScheduler().runTask(plugin, () -> plugin.getSlimePlugin().loadWorld(world));
            } catch (WorldLockedException | CorruptedWorldException | NewerFormatException | UnknownWorldException | IOException e) {
                plugin.getLogger().info(Messages.ERROR_SLIME_ERROR);
            }
        });
    }

    public static void unloadWorld(String worldName, BridgePlugin plugin){
        Bukkit.unloadWorld(worldName, false);
        plugin.getLogger().info("World " + worldName + " unloaded.");
    }

    public static boolean worldExists(String name, BridgePlugin plugin){
        SlimeLoader loader = plugin.getSlimePlugin().getLoader("file");
        try {
            return loader.worldExists(name);
        } catch (IOException e) {
            return true;
        }
    }
}