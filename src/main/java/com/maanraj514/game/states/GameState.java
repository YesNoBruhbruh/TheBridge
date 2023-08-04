package com.maanraj514.game.states;

import com.maanraj514.BridgePlugin;
import com.maanraj514.game.Game;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class GameState implements Listener {

    protected Game game;

    public void onEnable(BridgePlugin plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void onDisable(){
        HandlerList.unregisterAll(this);
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame(){
        return game;
    }
}