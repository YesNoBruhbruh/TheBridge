package com.maanraj514.game.state.impl;

import com.maanraj514.game.Game;
import com.maanraj514.game.state.GameState;
import com.maanraj514.game.state.GameStateImpl;
import com.maanraj514.game.state.GameStateListener;
import com.maanraj514.game.state.impl.listeners.PreGameListener;
import com.maanraj514.model.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

import static com.maanraj514.utils.ColorUtil.color;

public class StartingState extends GameStateImpl {

    private BukkitTask task;
    private int secondsLeft = 10;

    @Override
    public void onEnable() {
        plugin.getLogger().info("Starting state enabled.");

        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (secondsLeft <= 0) {
                task.cancel();
                game.setState(GameState.ROUND_PLAYING);
                for (UUID playerUUID : game.getPlayers().keySet()) {
                    Player player = Bukkit.getPlayer(playerUUID);
                    if (player != null) {
                        game.resetPlayer(player);
                    }
                }
            } else {
                game.broadcast(color("&aGame starting in " + secondsLeft + " seconds..."));
            }
            secondsLeft--;
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }

        for (Team team : game.getTeams().keySet()) {
            if (!game.getScore().containsKey(team)) {
                game.getScore().put(team, 0);
            }
        }
    }

    @Override
    public GameStateListener getListener(Game game) {
        return new PreGameListener(game);
    }
}