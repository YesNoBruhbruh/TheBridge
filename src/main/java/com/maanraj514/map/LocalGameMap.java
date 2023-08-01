package com.maanraj514.map;

import com.maanraj514.utils.FileUtil;
import com.maanraj514.utils.Messages;
import org.bukkit.*;

import java.io.File;
import java.io.IOException;

import static com.maanraj514.utils.ColorUtil.color;

public class LocalGameMap implements MapInterface{
    private final File sourceWorldFolder;
    private File activeWorldFolder;

    private World bukkitWorld;

    public LocalGameMap(File worldFolder, String worldName, boolean loadOnInit) {
        this.sourceWorldFolder = new File(
                worldFolder,
                worldName
        );

        if (loadOnInit) load();
    }

    @Override
    public boolean load() {
        if (isLoaded()) return true;
        this.activeWorldFolder = new File(
                Bukkit.getWorldContainer().getParentFile(), sourceWorldFolder.getName() + ".active." + System.currentTimeMillis());
        try {
            FileUtil.copy(sourceWorldFolder, activeWorldFolder);
        } catch (IOException e) {
            Bukkit.getLogger().info(Messages.ERROR_GAME_MAP_LOAD);
            return false;
        }

        this.bukkitWorld = Bukkit.createWorld(new WorldCreator(activeWorldFolder.getName()));

        if (bukkitWorld != null) {
            this.bukkitWorld.setDifficulty(Difficulty.HARD);
            this.bukkitWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            this.bukkitWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            this.bukkitWorld.setAutoSave(false);
        }
        return isLoaded();
    }

    @Override
    public void unload() {
        if (bukkitWorld != null){
            if (activeWorldFolder != null) {
                Bukkit.unloadWorld(bukkitWorld, false);
                FileUtil.delete(activeWorldFolder);
            }
        }
        bukkitWorld = null;
        activeWorldFolder = null;
    }

    @Override
    public void delete(String name) {
        World world = Bukkit.getWorld(name);
        if (world == null) {
            return;
        }
        File activeWorldFolder = world.getWorldFolder();
        if (activeWorldFolder.exists()){
            Bukkit.unloadWorld(world, false);
            FileUtil.delete(activeWorldFolder);
            Bukkit.getConsoleSender().sendMessage(color("&aSuccessfully deleted the world and the world file"));
        }
    }

    @Override
    public boolean isLoaded() {
        return getWorld() != null;
    }

    @Override
    public World getWorld() {
        return bukkitWorld;
    }
}