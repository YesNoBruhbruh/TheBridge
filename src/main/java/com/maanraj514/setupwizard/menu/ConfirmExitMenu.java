package com.maanraj514.setupwizard.menu;

import com.maanraj514.BridgePlugin;
import com.maanraj514.Okmeta;
import com.maanraj514.menu.Menu;
import com.maanraj514.menu.PlayerMenuUtility;
import com.maanraj514.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.maanraj514.utils.ColorUtil.color;

public class ConfirmExitMenu extends Menu {
    private final BridgePlugin plugin;

    private NamespacedKey yesItemKey;
    private NamespacedKey noItemKey;

    public ConfirmExitMenu(PlayerMenuUtility playerMenuUtility, Okmeta plugin) {
        super(playerMenuUtility, plugin);
        this.plugin = (BridgePlugin) plugin;
        this.yesItemKey = new NamespacedKey(plugin, "yesItem");
        this.noItemKey = new NamespacedKey(plugin, "noItem");
    }

    @Override
    public String getMenuName() {
        return color("&aConfirm Exit");
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {
        ItemStack itemStack = inventoryClickEvent.getCurrentItem();
        if (itemStack != null){
            if (itemStack.getItemMeta() != null) {
                if (plugin.getWizardManager().hasSession(player.getUniqueId())){
                    PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();

                    if (persistentDataContainer.has(yesItemKey)) {
                        plugin.getWizardManager().endSession((Player) inventoryClickEvent.getWhoClicked(), true);
                    } else if (persistentDataContainer.has(noItemKey)) {
                        plugin.getWizardManager().endSession((Player) inventoryClickEvent.getWhoClicked(), false);
                    }
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack yes = new ItemBuilder(Material.GREEN_CONCRETE)
                .setName("&aSave Changes")
                .addLoreLine("&7Changed will be saved to the database.")
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(yesItemKey, PersistentDataType.BOOLEAN, true))
                .build();
        ItemStack no = new ItemBuilder(Material.RED_CONCRETE)
                .setName("&cDont Save Changes")
                .addLoreLine("&7Nothing will be saved. Changes are lost forever!")
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(noItemKey, PersistentDataType.BOOLEAN, true))
                .build();

        inventory.setItem(11, yes);
        inventory.setItem(15, no);

        setFillerItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
    }
}
