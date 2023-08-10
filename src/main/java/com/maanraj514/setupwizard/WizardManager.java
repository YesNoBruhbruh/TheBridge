package com.maanraj514.setupwizard;

import com.maanraj514.BridgePlugin;
import com.maanraj514.game.GameMode;
import com.maanraj514.model.GameData;
import com.maanraj514.model.Team;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.maanraj514.utils.ColorUtil.color;

public class WizardManager {

    private final BridgePlugin plugin;

    private final Map<UUID, Session> sessions;

    public WizardManager(BridgePlugin plugin){
        this.sessions = new HashMap<>();
        this.plugin = plugin;
    }

    public void createSession(Player player, String map, GameMode gameMode){
        this.sessions.put(player.getUniqueId(), new Session(player.getUniqueId(), map, gameMode, plugin));
    }

    public void endSession(Player player, boolean save){
        if (save){
            if (getSession(player.getUniqueId()) != null){
                Session session = getSession(player.getUniqueId());
                if (session.getTeams().isEmpty() || session.getTeams().size() == 1){
                    player.sendMessage(color("&cIn order to save, you have to have atleast 2 teams!"));
                    return;
                }

                for (Team team : session.getTeams().values()) {
                    String teamName = team.getName() != null ? team.getName() : team.getColor().name();
                    if (team.getSpawnLocation() == null){
                        player.sendMessage(color("&c" + teamName + "'s spawn location doesn't exist! set it in order to save!"));
                        return;
                    }
                    if (team.getPortalLocationOne() == null){
                        player.sendMessage(color("&c" + teamName + "'s portal location One doesn't exist! set it in order to save!"));
                        return;
                    }
                    if (team.getPortalLocationTwo() == null){
                        player.sendMessage(color("&c" + teamName + "'s portal location Two doesn't exist! set it in order to save!"));
                        return;
                    }
                    if (team.getArmorColor() == null){
                        Color color = Color.fromRGB(0, 0, 0);
                        team.setArmorColor(color);
                    }
                    session.getTeams().put(team.getColor(), team);
                }

                if (session.getGameData().getSpectatorSpawn() == null){
                    player.sendMessage(color("&cSet spectator spawn in order to save!"));
                    return;
                }
                if (session.getGameData().getBuildAbleCornerOne() == null) {
                    player.sendMessage(color("&cSet buildAbleCornerOne in order to save!"));
                    return;
                }
                if (session.getGameData().getBuildAbleCornerTwo() == null) {
                    player.sendMessage(color("&cSet buildAbleCornerTwo in order to save!"));
                    return;
                }
                this.sessions.get(player.getUniqueId()).delete(true);
                this.sessions.remove(player.getUniqueId());
                player.sendMessage(color("&aSuccessfully saved and exited!"));
            }
        } else {
            this.sessions.get(player.getUniqueId()).delete(false);
            this.sessions.remove(player.getUniqueId());
            player.sendMessage(color("&cExited session."));
        }
    }

    public Session getSession(UUID uuid){
        return sessions.get(uuid);
    }

    public boolean hasSession(UUID uuid){
        return sessions.containsKey(uuid);
    }
}