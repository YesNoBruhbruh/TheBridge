package com.maanraj514.game;

import com.maanraj514.BridgePlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameManager {

    private final BridgePlugin plugin;

    private final Map<UUID, Game> games;

    public GameManager(BridgePlugin plugin){
        this.games = new HashMap<>();
        this.plugin = plugin;
    }

    public void createGame(String map, GameMode mode){
        UUID uuid = UUID.randomUUID();
        this.games.put(uuid, new Game(map, uuid, plugin));
    }

    public void deleteGame(UUID uuid){
        this.games.remove(uuid).delete();
    }

    public Game getGame(UUID uuid){
        return this.games.get(uuid);
    }

    public Game getGame(String map) {
        for (Game game : this.games.values()) {
            if (game.getMap().equalsIgnoreCase(map)) {
                return game;
            }
        }
        return null;
    }

    public Map<UUID, Game> getGames(){
        return this.games;
    }
}