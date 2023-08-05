package com.maanraj514.setupwizard;

import com.maanraj514.BridgePlugin;
import com.maanraj514.menu.MenuManager;
import com.maanraj514.menu.PlayerMenuUtility;
import com.maanraj514.model.Team;
import com.maanraj514.setupwizard.menu.PMUData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static com.maanraj514.utils.ColorUtil.color;

public class WizardListener implements Listener {

    private final BridgePlugin plugin;

    public WizardListener(BridgePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getItem() != null){
            if(plugin.getWizardManager().hasSession(player.getUniqueId())){
                plugin.getWizardManager().getSession(player.getUniqueId()).onInteract(event.getItem());
            }
        }
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        if (plugin.getWizardManager().hasSession(player.getUniqueId())) {
            Session session = plugin.getWizardManager().getSession(player.getUniqueId());
            if (session.getInTeamNameChangeSession().contains(player.getUniqueId())) {
                event.setCancelled(true);
                PlayerMenuUtility playerMenuUtility = MenuManager.getPlayerMenuUtility(player);
                Team team = session.getTeams().get(playerMenuUtility.getData(PMUData.TEAM_SELECTED, ChatColor.class));
                team.setName(event.getMessage());
                player.sendMessage(color("&aTeam name has been set to " + event.getMessage()));
                session.getInTeamNameChangeSession().remove(player.getUniqueId());
            }
        }
    }
}