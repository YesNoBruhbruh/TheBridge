package com.maanraj514.model;

import com.maanraj514.BridgePlugin;
import com.maanraj514.util.Messages;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigFile {

    private final BridgePlugin plugin;

    private File file;
    private YamlConfiguration configuration;

    public ConfigFile(File dir, String name, BridgePlugin plugin) {
        this.plugin = plugin;
        if (!dir.exists()){
            if (!dir.mkdir()){
                plugin.getLogger().info(Messages.ERROR_DIRECTORY_CREATION);
                return;
            }
        }
        this.file = new File(dir, name + ".yml");
        if (!file.exists()){
            try {
                if (!file.createNewFile()){
                    plugin.getLogger().info(Messages.ERROR_FILE_CREATION);
                }
            } catch (IOException e) {
                plugin.getLogger().info(Messages.ERROR_FILE_CREATION);
            }
        }
        this.configuration = new YamlConfiguration();
        try {
            this.configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().info(Messages.ERROR_FILE_LOAD);
        }
    }

    public void save(){
        if (getConfig() == null){
            plugin.getLogger().info(Messages.NULL);
            return;
        }
        try{
            this.getConfig().save(file);
        } catch (IOException e) {
            plugin.getLogger().info(Messages.ERROR_FILE_SAVE);
        }
    }

    public void reload(){
        if (getConfig() == null){
            plugin.getLogger().info(Messages.NULL);
            return;
        }
        save();
        try{
            this.getConfig().load(file);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().info(Messages.ERROR_FILE_LOAD);
        }
    }

    public FileConfiguration getConfig(){
        if (this.configuration == null || this.file == null){
            plugin.getLogger().info(Messages.NULL);
            return null;
        }
        return this.configuration;
    }
}