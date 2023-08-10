package com.maanraj514.setupwizard.menu;

import com.maanraj514.BridgePlugin;
import com.maanraj514.Okmeta;
import com.maanraj514.menu.Menu;
import com.maanraj514.menu.PlayerMenuUtility;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ConfirmDeleteTeamMenu extends Menu {
    private final BridgePlugin plugin;

    public ConfirmDeleteTeamMenu(PlayerMenuUtility playerMenuUtility, Okmeta plugin) {
        super(playerMenuUtility, plugin);
        this.plugin = (BridgePlugin) plugin;
    }

    @Override
    public String getMenuName() {
        return null;
    }

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public boolean cancelAllClicks() {
        return false;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public void setMenuItems() {

    }
}
