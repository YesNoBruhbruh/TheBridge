package com.maanraj514.game.state.impl;

import com.maanraj514.game.Game;
import com.maanraj514.game.state.GameStateImpl;
import com.maanraj514.game.state.GameStateListener;
import com.maanraj514.game.state.impl.listeners.PreGameListener;
import com.maanraj514.util.WorldUtil;
import com.maanraj514.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

import static com.maanraj514.utils.ColorUtil.color;

public class EndedState extends GameStateImpl {

    private BukkitTask task;
    private int secondsUntilEnd = 10;

    @Override
    public void onEnable() {
        game.broadcast(color("&eGAME HAS ENDED! " +
                game.getWinnerTeam().getColor() + game.getWinnerTeam().getName() + " WINS!"));

        for (UUID playerUUID : game.getPlayers().keySet()) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null){
                game.addSpectator(player);
            }
        }

        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (secondsUntilEnd <= 0) {
                task.cancel();
                for (UUID playerUUID : game.getPlayers().keySet()) {
                    Player player = Bukkit.getPlayer(playerUUID);
                    if (player != null){
                        if (game.getPlayers().containsKey(playerUUID)) {
                            Bukkit.getScheduler().runTask(plugin, () -> game.removePlayer(player));
                        }
                    }
                }
                this.onDisable();
            } else {
                for (UUID playerUUID : game.getPlayers().keySet()) {
                    Player player = Bukkit.getPlayer(playerUUID);
                    if (player != null){
                        MessageUtil.sendActionbar(color("&eGAME HAS ENDED! " + "teleporting you to lobby in " + secondsUntilEnd + " seconds..."), player);
                    }
                }
            }
            secondsUntilEnd--;
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        plugin.getLogger().info("EndedState disabled!");

        WorldUtil.unloadWorld(game.getWorld().getName(), plugin);
    }

    @Override
    public GameStateListener getListener(Game game) {
        return new PreGameListener(game);
    }
}