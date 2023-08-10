package com.maanraj514.game.state.impl;

import com.maanraj514.game.Game;
import com.maanraj514.game.state.GameStateImpl;
import com.maanraj514.game.state.GameStateListener;
import com.maanraj514.game.state.impl.listeners.InGameListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.maanraj514.utils.ColorUtil.color;

public class RoundPlayingState extends GameStateImpl {
    @Override
    public void onEnable() {
        game.broadcast(color("&cGAME HAS STARTED!"));

        for (UUID playerUUID : game.getPlayers().keySet()) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null){
                game.resetPlayer(player);
            }
        }
    }

    @Override
    public void onDisable() {
        for (UUID playerUUID : game.getPlayers().keySet()) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null){
                game.resetPlayer(player);
                plugin.getLogger().info("assign spawn location for player: " + player.getName());
            }
        }
    }

    @Override
    public GameStateListener getListener(Game game) {
        return new InGameListener(game, this, plugin);
    }
}