package com.maanraj514.setupwizard.menu;

import com.maanraj514.BridgePlugin;
import com.maanraj514.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import static com.maanraj514.utils.ColorUtil.color;

public class ConfirmExitMenu implements InventoryHolder {
    private final Inventory inventory;

    public ConfirmExitMenu(Player player){
        this.inventory = Bukkit.createInventory(this, 27, color("&aConfirm Exit"));

        ItemStack yes = new ItemBuilder(Material.GREEN_CONCRETE).setName("&aSave Changes").addLoreLine("&7Changed will be saved to the database.").build();
        ItemStack no = new ItemBuilder(Material.RED_CONCRETE).setName("&cDont Save Changes").addLoreLine("&7Nothing will be saved. Changes are lost forever!").build();

        inventory.setItem(11, yes);
        inventory.setItem(15, no);

        for (int i = 0; i < inventory.getSize(); i++){
            if (inventory.getItem(i) == null){
                inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                        .setName(" ")
                        .build());
            }
        }

        player.openInventory(inventory);
    }

    public void handleClick(InventoryClickEvent event, BridgePlugin plugin){
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() != null){
            if (event.getCurrentItem().getType() == Material.RED_CONCRETE) {
                plugin.getWizardManager().endSession(player, false);
            } else if (event.getCurrentItem().getType() == Material.GREEN_CONCRETE) {
                plugin.getWizardManager().endSession(player, true);
            }
            player.closeInventory();
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
