package com.maanraj514.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maanraj514.BridgePlugin;
import com.maanraj514.model.GameData;
import com.maanraj514.model.Team;
import com.maanraj514.utils.FileUtil;
import com.maanraj514.util.Messages;
import com.maanraj514.utils.LocationUtil;
import org.json.simple.parser.JSONParser;

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

    public void saveData(GameData gameData) throws IOException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(gameDataFolder.getAbsolutePath() + "/" + gameData.getMap() + ".json");
        file.getParentFile().mkdir();
        file.createNewFile();
        Writer writer = new FileWriter(file, false);

        List<String> toJson = new ArrayList<>();
        toJson.add(gameData.getMap());
        toJson.add(gameData.getGameMode().toString());
        toJson.addAll(gameData.getAuthors());
        toJson.add(gameData.getLastEdit() + "");
        toJson.add("spectatorSpawn: " + LocationUtil.locationToString(gameData.getSpectatorSpawn(), true));
        toJson.add("buildAbleCornerOne: " + LocationUtil.locationToString(gameData.getBuildAbleCornerOne(), false));
        toJson.add("buildAbleCornerTwo: " + LocationUtil.locationToString(gameData.getBuildAbleCornerTwo(), false));

        for (Team team : gameData.getTeams()) {
            List<String> teamToJson = new ArrayList<>();

            if (team.getName() == null){
                teamToJson.add(team.getColor().name());
            } else {
                teamToJson.add(team.getName());
            }

            gson.toJson(teamToJson, writer);
        }

        gson.toJson(toJson, writer);
        writer.flush();
        writer.close();
    }

    public GameData getData(String map){
        if (this.gameDataMap.isEmpty()){
            return null;
        }
        for (GameData gameData : this.gameDataMap.values()){
            if (gameData.getMap().equalsIgnoreCase(map)){
                return gameData;
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

    // recommended to keep this to 1 usage. (onDisable())
    public void saveAllData() throws IOException {
        if (this.gameDataMap.isEmpty()){
            return;
        }
        for (GameData gameData : this.gameDataMap.values()){
            saveData(gameData);
        }
    }

    // recommended to keep this to 1 usage. (onEnable())
    public void loadAllData() throws FileNotFoundException {
        File[] files = gameDataFolder.listFiles();
        if (files == null){
            plugin.getLogger().info(Messages.NULL);
            return;
        }

//        Gson gson = new Gson();
//        for (File file : files){
//            if (file.exists()){
//                Reader reader = new FileReader(file);
//                GameData gameData = gson.fromJson(reader, GameData.class);
//                this.gameDataMap.put(gameData.getMap(), gameData);
//            }
//        }
    }
}