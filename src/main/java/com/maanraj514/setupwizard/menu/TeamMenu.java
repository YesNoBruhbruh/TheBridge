package com.maanraj514.setupwizard.menu;

import com.maanraj514.setupwizard.Session;
import com.maanraj514.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.maanraj514.utils.MessageUtil.rainbow;

public class TeamMenu implements InventoryHolder {

    private final Inventory inventory;
    private final Session session;

    public TeamMenu(Session session){
        this.session = session;
        this.inventory = Bukkit.createInventory(this, 36, rainbow("Team Menu"));

        // set lore to if team is setup edit directly, if not create if clicked.

        setMenuItems();
    }

    private void setMenuItems() {
        ItemStack redTeam = new ItemBuilder(Material.RED_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.RED) ? "&cClick to edit team." : "&aClick to create team"))
                .build();
//        Material.WHITE_WOOL
        // Material.BLUE_WOOL
        // Material.YELLOW_WOOL
        // Material.GRAY_WOOL
        // Material.GREEN_WOOL
        // Material.CYAN_WOOL
        // Material.LIGHT_PURPLE_WOOL
        // Material.PINK_WOOL
//        Material.BLACK_WOOL
//        Material.LIME_WOOL
//        Material.ORANGE_WOOL
//        Material.LIGHT_BLUE_WOOL
//        Material.MAGENTA_WOOL

        // set inventory items

        this.setFillerItem();
    }

    private void setFillerItem() {
        for (int i = 0; i < inventory.getSize(); i++){
            if (inventory.getItem(i) == null){
                inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
            }
        }
    }

    private void reloadItems() {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, null);
        }
        this.setMenuItems();
    }

    public void handleClick(InventoryClickEvent event){
        if (event.getCurrentItem() != null){
            switch (event.getCurrentItem().getType()) {
                // check for the wool.
            }
            reloadItems();
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
