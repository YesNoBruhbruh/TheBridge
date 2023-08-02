package com.maanraj514.util;

import com.infernalsuite.aswm.api.exceptions.CorruptedWorldException;
import com.infernalsuite.aswm.api.exceptions.NewerFormatException;
import com.infernalsuite.aswm.api.exceptions.UnknownWorldException;
import com.infernalsuite.aswm.api.exceptions.WorldLockedException;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import com.maanraj514.BridgePlugin;
import com.maanraj514.game.Game;
import com.maanraj514.game.states.LoadingState;
import org.bukkit.Bukkit;

import java.io.IOException;

public class WorldUtil {

    private static int id = 0;

    public static void loadGameWorld(Game game, BridgePlugin plugin) {
        SlimeLoader loader = plugin.getSlimePlugin().getLoader("file");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try{
                SlimeWorld world = plugin.getSlimePlugin().loadWorld(loader, game.getMap(), true, new SlimePropertyMap()).clone(game.getMap() + "-" + id);

                plugin.getSlimePlugin().loadWorld(world);

                id++;

                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                    game.setWorld(Bukkit.getWorld(world.getName()));
                    game.setState(new LoadingState());
                }, 40);

            } catch (WorldLockedException | CorruptedWorldException | NewerFormatException | IOException | UnknownWorldException e) {
                plugin.getLogger().info(Messages.ERROR_SLIME_ERROR);
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

    public static void unloadWorld(String name){
        Bukkit.unloadWorld(name, false);
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