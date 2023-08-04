package com.maanraj514.game.states;

import com.maanraj514.BridgePlugin;
import com.maanraj514.model.Team;
import com.maanraj514.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public class RoundPlayingState extends GameState {

    @Override
    public void onEnable(BridgePlugin plugin){
        game.broadcast("&cGAME HAS STARTED!");
    }

    @Override
    public void onDisable(){
    }

    @EventHandler
    private void onScore(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        if (game.getPlayers().containsKey(player.getUniqueId()) && event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            Location location = event.getFrom();

            // check if they accidentally went into their own portal lol

            for (Team team : game.getTeams().keySet()){
                List<Location> portalBlocks = LocationUtil.locationsFromTwoPoints(team.getPortalLocationTwo(), team.getPortalLocationTwo());

                for (Location portalBlockLocation : portalBlocks) {

                    if (LocationUtil.coordinatesMatch(location, portalBlockLocation)) {

                        Team playerTeam = game.getPlayers().get(player.getUniqueId());

                        // checks if the team doesn't contain that player.
                        // if doesn't that means they went inside some other team's portal
                        // we give the player's team a point.
                        if (!game.getTeams().get(team).contains(player.getUniqueId())){
                            game.getScore().put(playerTeam, game.getScore().get(playerTeam) + 1);
                            if (game.getScore().get(playerTeam) == 5){
                                game.setWinnerTeam(playerTeam);
                                game.setState(new EndedState());
                            } else {
                                game.setState(new RoundStartingState());
                            }
                        }
                        player.teleportAsync(playerTeam.getSpawnLocation());
                        break;
                    }
                }
            }
        }
    }
}