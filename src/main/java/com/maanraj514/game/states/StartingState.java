package com.maanraj514.game.states;

import com.maanraj514.BridgePlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class StartingState extends GameState{

    private BukkitTask task;

    private int secondsUntilStart = 10;

    @Override
    public void onEnable(BridgePlugin plugin){
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (secondsUntilStart <= 0){
                game.setState(new RoundPlayingState());
                task.cancel();
            } else {
                game.broadcast("&aGame starts in " + secondsUntilStart + " seconds.");
            }
            secondsUntilStart--;
        }, 0L, 20L);
    }

    @Override
    public void onDisable(){
        if (task != null){
            task.cancel();
            task = null;
        }
    }
}