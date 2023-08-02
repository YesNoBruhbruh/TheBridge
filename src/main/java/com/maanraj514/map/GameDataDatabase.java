package com.maanraj514.map;

import com.maanraj514.BridgePlugin;
import com.maanraj514.object.GameData;
import com.maanraj514.utils.Messages;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

//TODO this is for getting the map data for the games.
//TODO load all the mapData at startup and put them in the mapDataMap
public class GameDataDatabase {
    private final BridgePlugin plugin;

    //         map      mapData
    private Map<String, GameData> mapDataMap;

    public GameDataDatabase(BridgePlugin plugin){
        this.plugin = plugin;
        this.mapDataMap = new HashMap<>();

        File mapData = new File(plugin.getDataFolder() + "\\mapData");
        if (!mapData.exists()){
            if (!mapData.mkdir()){
                plugin.getLogger().info(Messages.ERROR_DIRECTORY_CREATION);
            }
        }
        //TODO use json to save the mapData and everything else

        // make a for loop for all the files inside mapData and read the mapData inside
        // every single one.
    }

    public void saveData(GameData gameData){
        // create a new json file and write the mapData inside
        // dont forget to save and reload too.
    }

    public GameData getData(String map){
        // get the json file inside the mapData folder
        // and return the data inside.
        return null;
        // use the map to get the mapData
    }

    public void deleteData(String map){
        // delete the json file for mapData and delete it from the Map.
    }
}