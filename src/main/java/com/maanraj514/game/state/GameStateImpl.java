package com.maanraj514.game.state;

import com.maanraj514.BridgePlugin;
import com.maanraj514.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public abstract class GameStateImpl {

    protected Game game;
    protected BridgePlugin plugin;
    private GameStateListener listener;

    public void pre(Game game, BridgePlugin plugin){
        this.plugin = plugin;
        this.game = game;
        GameStateListener listener1 = getListener(game);
        if(listener1 != null){
            Bukkit.getPluginManager().registerEvents(listener1, plugin);
            this.listener = listener1;
        }
        this.onEnable();
    }

    public void post(Game game){
        this.game = game;
        if(listener != null){
            HandlerList.unregisterAll(listener);
        }
        this.onDisable();
    }

    abstract public void onEnable();
    abstract public void onDisable();
    abstract public GameStateListener getListener(Game game);
}