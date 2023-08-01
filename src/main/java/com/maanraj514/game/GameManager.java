package com.maanraj514.game;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameManager {

    private final Map<UUID, Game> games;

    public GameManager(){
        this.games = new HashMap<>();
    }

    public void createGame(String map, GameMode mode){
        UUID uuid = UUID.randomUUID();
        this.games.put(uuid, new Game(map, mode, uuid));
    }

    public void deleteGame(UUID uuid){
        this.games.remove(uuid).delete();
    }

    public Game getGame(UUID uuid){
        return this.games.get(uuid);
    }

    public Map<UUID, Game> getGames(){
        return this.games;
    }
}