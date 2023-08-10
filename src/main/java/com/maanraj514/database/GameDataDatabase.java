package com.maanraj514.database;

import com.maanraj514.BridgePlugin;
import com.maanraj514.game.GameMode;
import com.maanraj514.model.ConfigFile;
import com.maanraj514.model.GameData;
import com.maanraj514.model.Pos;
import com.maanraj514.model.Team;
import com.maanraj514.util.WorldUtil;
import com.maanraj514.utils.FileUtil;
import com.maanraj514.util.Messages;
import com.maanraj514.utils.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameDataDatabase {

    private final BridgePlugin plugin;

    //         map      mapData
    private final Map<String, GameData> gameDataMap;

    private final File gameDataFolder;

    public GameDataDatabase(BridgePlugin plugin){
        this.plugin = plugin;
        this.gameDataMap = new HashMap<>();

        gameDataFolder = new File(plugin.getDataFolder().getAbsolutePath() + "/gameDataFolder");
        if (!gameDataFolder.exists()){
            if (!gameDataFolder.mkdir()){
                plugin.getLogger().info(Messages.ERROR_DIRECTORY_CREATION);
            }
        }
    }

    public void saveData(GameData gameData) {

        ConfigFile configFile = new ConfigFile(gameDataFolder, gameData.getMap() + ".yml", plugin);

        if (!WorldUtil.worldExists(gameData.getMap(), plugin)) {
            plugin.getLogger().info("World " + gameData.getMap() + " does not exist");
            return;
        }

        configFile.getConfig().set("map", gameData.getMap());
        configFile.getConfig().set("gameMode", gameData.getGameMode().toString());
        configFile.getConfig().set("authors", gameData.getAuthors());
        configFile.getConfig().set("lastEdit", gameData.getLastEdit());
        configFile.getConfig().set("spectatorSpawn", LocationUtil.locationToString(gameData.getSpectatorSpawn(), true));
        configFile.getConfig().set("buildAbleCornerOne", gameData.getBuildAbleCornerOne().toString());
        configFile.getConfig().set("buildAbleCornerTwo", gameData.getBuildAbleCornerTwo().toString());
        configFile.getConfig().set("teams", "");
        for (Team team : gameData.getTeams()) {
            String teamName = (team.getName() == null ? team.getColor().name() : team.getName());
            configFile.getConfig().set("teams." + teamName + ".color", team.getColor().name());
            configFile.getConfig().set("teams." + teamName + ".spawnLocation", LocationUtil.locationToString(team.getSpawnLocation(), true));
            configFile.getConfig().set("teams." + teamName + ".portalLocationOne", team.getPortalLocationOne().toString());
            configFile.getConfig().set("teams." + teamName + ".portalLocationTwo", team.getPortalLocationTwo().toString());
            configFile.getConfig().set("teams." + teamName + ".armorColor.red", team.getArmorColor().getRed());
            configFile.getConfig().set("teams." + teamName + ".armorColor.green", team.getArmorColor().getGreen());
            configFile.getConfig().set("teams." + teamName + ".armorColor.blue", team.getArmorColor().getBlue());
        }
        configFile.getConfig().set("maxGoals", gameData.getMaxGoals());
        configFile.reload();
    }

    public GameData getData(String map){
        if (this.gameDataMap.isEmpty()){
            return null;
        }
        for (String map1 : this.gameDataMap.keySet()){
            if (map1.equalsIgnoreCase(map)){
                return this.gameDataMap.get(map1);
            }
        }
        return null;
    }

    public void deleteData(String map){
        if (this.gameDataMap.isEmpty()){
            return;
        }
        for (GameData gameData : this.gameDataMap.values()){
            if (gameData.getMap().equalsIgnoreCase(map)){
                this.gameDataMap.remove(gameData.getMap());
                FileUtil.delete(new File(gameDataFolder.getAbsolutePath() + "/" + map + ".yml"));
                break;
            }
        }
    }

    // recommended to keep this to 1 usage
    public void saveAllData() {
        if (this.gameDataMap.isEmpty()){
            return;
        }
        for (GameData gameData : this.gameDataMap.values()){
            saveData(gameData);
        }
        plugin.getLogger().info("Saving all data...");
    }

    // recommended to keep this to 1 usage. (onEnable())
    public void loadAllData() {
        File[] files = gameDataFolder.listFiles();
        if (files == null){
            plugin.getLogger().info(Messages.NULL);
            return;
        }

        for (File file : files) {
            if (file.exists()) {
                if (!file.isDirectory()) {
                    ConfigFile configFile = new ConfigFile(gameDataFolder, file.getName(), plugin);

                    String map = configFile.getConfig().getString("map");
                    GameMode gameMode = GameMode.valueOf(configFile.getConfig().getString("gameMode").toUpperCase());
                    List<String> authors = configFile.getConfig().getStringList("authors");
                    long lastEdit = configFile.getConfig().getLong("lastEdit");
                    List<Team> teams = new ArrayList<>();

                    for (String teamConfigName : configFile.getConfig().getConfigurationSection("teams").getKeys(false)) {
                        ConfigurationSection section = configFile.getConfig().getConfigurationSection("teams." + teamConfigName);

                        ChatColor color = ChatColor.valueOf(section.getString("color"));
                        Location spawnLocation = LocationUtil.stringToLocation(section.getString("spawnLocation"));
                        Pos portalLocationOne = LocationUtil.stringToPos(section.getString("portalLocationOne"));
                        Pos portalLocationTwo = LocationUtil.stringToPos(section.getString("portalLocationTwo"));

                        Color armorColor = Color.fromRGB(section.getInt("armorColor.red"), section.getInt("armorColor.green"), section.getInt("armorColor.blue"));

                        Team team = new Team(teamConfigName, spawnLocation, portalLocationOne, portalLocationTwo, color, armorColor);

                        teams.add(team);
                    }

                    Location spectatorSpawn = LocationUtil.stringToLocation(configFile.getConfig().getString("spectatorSpawn"));
                    Pos buildAbleCornerOne = LocationUtil.stringToPos(configFile.getConfig().getString("buildAbleCornerOne"));
                    Pos buildAbleCornerTwo = LocationUtil.stringToPos(configFile.getConfig().getString("buildAbleCornerTwo"));
                    int maxGoals = configFile.getConfig().getInt("maxGoals");

                    GameData gameData = new GameData(map, gameMode, authors, lastEdit, teams, spectatorSpawn, buildAbleCornerOne, buildAbleCornerTwo, maxGoals);
                    this.gameDataMap.put(map, gameData);

                    plugin.getLogger().info("loaded " + map);
                    plugin.getLogger().info("gameMode: " + gameMode);
                    plugin.getLogger().info("authors: " + authors);
                    plugin.getLogger().info("lastEdit: " + lastEdit);
                    plugin.getLogger().info("spectatorSpawn: " + spectatorSpawn);
                    plugin.getLogger().info("buildAbleCornerOne: " + buildAbleCornerOne);
                    plugin.getLogger().info("buildAbleCornerTwo: " + buildAbleCornerTwo);
                    plugin.getLogger().info("maxGoals: " + maxGoals);
                    plugin.getLogger().info("teams: " + teams);
                } else {
                    plugin.getLogger().info("test 2");
                }
            } else {
                plugin.getLogger().info(" test 1");
            }
        }
    }
}