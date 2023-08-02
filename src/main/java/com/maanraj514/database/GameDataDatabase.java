package com.maanraj514.database;

import com.google.gson.Gson;
import com.maanraj514.BridgePlugin;
import com.maanraj514.model.GameData;
import com.maanraj514.utils.FileUtil;
import com.maanraj514.util.Messages;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        Gson gson = new Gson();
        File file = new File(gameDataFolder.getAbsolutePath() + "/" + gameData.getMap() + ".json");
        file.getParentFile().mkdir();
        file.createNewFile();
        Writer writer = new FileWriter(file, false);
        gson.toJson(gameData, writer);
        writer.flush();
        writer.close();
    }

    public GameData getData(String map){
        for (GameData gameData : this.gameDataMap.values()){
            if (gameData.getMap().equalsIgnoreCase(map)){
                return gameData;
            }
        }
        return null;
    }

    public void deleteData(String map){
        for (GameData gameData : this.gameDataMap.values()){
            if (gameData.getMap().equalsIgnoreCase(map)){
                this.gameDataMap.remove(gameData.getMap());
                FileUtil.delete(new File(gameDataFolder.getAbsolutePath() + "/" + map + ".json"));
                break;
            }
        }
    }

    public void saveAllData() throws IOException {
        for (GameData gameData : this.gameDataMap.values()){
            saveData(gameData);
        }
    }

    public void loadAllData() throws FileNotFoundException {
        if (gameDataFolder.listFiles() == null){
            plugin.getLogger().info(Messages.NULL);
            return;
        }

        Gson gson = new Gson();
        for (File file : Objects.requireNonNull(gameDataFolder.listFiles())){
            if (file.exists()){
                Reader reader = new FileReader(file);
                GameData gameData = gson.fromJson(reader, GameData.class);
                this.gameDataMap.put(gameData.getMap(), gameData);
            }
        }
    }

    public Map<String, GameData> getGameDataMap(){
        return this.gameDataMap;
    }
}