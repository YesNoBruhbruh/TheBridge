package com.maanraj514.game.state.impl;

import com.maanraj514.game.Game;
import com.maanraj514.game.state.GameState;
import com.maanraj514.game.state.GameStateImpl;
import com.maanraj514.game.state.GameStateListener;
import com.maanraj514.game.state.impl.listeners.PreGameListener;
import com.maanraj514.model.Pos;
import com.maanraj514.model.Team;
import com.maanraj514.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class LoadingState extends GameStateImpl {
    @Override
    public void onEnable() {
        plugin.getLogger().info("Loading State Enabled");
        Bukkit.getScheduler().runTask(plugin, () -> {
            for (Team team : game.getGameData().getTeams()){
                for (Pos portalBlockLocation : LocationUtil.posFromTwoPoints(team.getPortalLocationOne(), team.getPortalLocationTwo())) {
                    LocationUtil.posToLocation(portalBlockLocation, game.getWorld()).getBlock().setType(Material.END_PORTAL);
                }
            }
        });
        game.setState(GameState.WAITING);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public GameStateListener getListener(Game game) {
        return new PreGameListener(game);
    }
}