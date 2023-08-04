package com.maanraj514.game.states;

import com.maanraj514.BridgePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

public class RoundStartingState extends GameState{

    private BukkitTask task;
    private int secondsUntilStart = 5;

    @Override
    public void onEnable(BridgePlugin plugin){
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (secondsUntilStart <= 0){
                game.setState(new RoundPlayingState());
            }else{
                game.broadcast("&aRound starts in " + secondsUntilStart + " seconds.");
            }
            secondsUntilStart--;
        }, 0, 20L);
    }

    @Override
    public void onDisable() {
        if (task != null){
            task.cancel();
            task = null;
        }
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (game.getPlayers().containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}