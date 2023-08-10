package com.maanraj514.setupwizard;

import com.maanraj514.BridgePlugin;
import com.maanraj514.menu.MenuManager;
import com.maanraj514.menu.PlayerMenuUtility;
import com.maanraj514.model.Team;
import com.maanraj514.setupwizard.menu.PMUData;
import com.maanraj514.setupwizard.menu.TeamOptionsMenu;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        MenuManager.openMenu(TeamOptionsMenu.class, player);
                    }
                }.runTask(plugin);
            } else if (session.getInArmorColorChangeSession().contains(player.getUniqueId())) {
                event.setCancelled(true);
                PlayerMenuUtility playerMenuUtility = MenuManager.getPlayerMenuUtility(player);
                Team team = session.getTeams().get(playerMenuUtility.getData(PMUData.TEAM_SELECTED, ChatColor.class));
                String[] values = event.getMessage().split(" ");
                Color color = Color.fromRGB(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
                team.setArmorColor(color);
                player.sendMessage(color("&aTeam armor color has been set to " + color));
                session.getInArmorColorChangeSession().remove(player.getUniqueId());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        MenuManager.openMenu(TeamOptionsMenu.class, player);
                    }
                }.runTask(plugin);
            }
        }
    }
}