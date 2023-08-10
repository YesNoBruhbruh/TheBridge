package com.maanraj514.game.state.impl;

import com.maanraj514.game.Game;
import com.maanraj514.game.state.GameState;
import com.maanraj514.game.state.GameStateImpl;
import com.maanraj514.game.state.GameStateListener;
import com.maanraj514.game.state.impl.listeners.PreGameListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

import static com.maanraj514.utils.ColorUtil.color;

public class RoundStartingState extends GameStateImpl {

    private BukkitTask task;

    private int secondsUntilStart;

    @Override
    public void onEnable() {
        plugin.getLogger().info("RoundStartingState enabled!");

        this.secondsUntilStart = 5;

        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (secondsUntilStart <= 0) {
                task.cancel();
                game.setState(GameState.ROUND_PLAYING);
                for (UUID playerUUID : game.getPlayers().keySet()) {
                    Player player = Bukkit.getPlayer(playerUUID);
                    if (player != null) {
                        game.resetPlayer(player);
                    }
                }
            } else {
                game.broadcast(color("&cRound starting in &e" + secondsUntilStart + " &cseconds!"));
            }
            secondsUntilStart--;
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }

    @Override
    public GameStateListener getListener(Game game) {
        return new PreGameListener(game);
    }
}