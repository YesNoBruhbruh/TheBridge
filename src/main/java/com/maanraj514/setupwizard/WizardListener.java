package com.maanraj514.setupwizard;

import com.maanraj514.BridgePlugin;
import com.maanraj514.setupwizard.menu.ConfirmExitMenu;
import com.maanraj514.setupwizard.menu.TeamMenu;
import com.maanraj514.setupwizard.menu.WizardMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

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
    public void onClick(InventoryClickEvent event){
        if(event.getCurrentItem() != null && !(event.getCurrentItem().getType().equals(Material.AIR))){
            InventoryHolder holder = event.getInventory().getHolder();

            if(holder instanceof WizardMenu){
                event.setCancelled(true);
                ((WizardMenu) holder).handleClick(event);
            } else if(holder instanceof ConfirmExitMenu){
                event.setCancelled(true);
                ((ConfirmExitMenu) holder).handleClick(event, plugin);
            } else if(holder instanceof TeamMenu) {
                event.setCancelled(true);
                ((TeamMenu) holder).handleClick(event);
            }
        }
    }
}