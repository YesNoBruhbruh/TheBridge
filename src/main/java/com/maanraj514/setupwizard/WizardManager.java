package com.maanraj514.setupwizard;

import com.maanraj514.BridgePlugin;
import com.maanraj514.game.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        this.sessions.get(player.getUniqueId()).delete(save);
        this.sessions.remove(player.getUniqueId());
    }

    public Session getSession(UUID uuid){
        return sessions.get(uuid);
    }

    public boolean hasSession(UUID uuid){
        return sessions.containsKey(uuid);
    }
}