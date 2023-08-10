package com.maanraj514.setupwizard.menu;

import com.maanraj514.BridgePlugin;
import com.maanraj514.Okmeta;
import com.maanraj514.menu.Menu;
import com.maanraj514.menu.PlayerMenuUtility;
import com.maanraj514.setupwizard.Session;
import com.maanraj514.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import static com.maanraj514.utils.ColorUtil.color;

public class GoalsMenu extends Menu {
    private BridgePlugin plugin;

    private final NamespacedKey maxGoalsItemKey;
    private final NamespacedKey incrementMaxGoalsItemKey;
    private final NamespacedKey decrementMaxGoalsItemKey;

    private Session session;

    public GoalsMenu(PlayerMenuUtility playerMenuUtility, Okmeta plugin) {
        super(playerMenuUtility, plugin);
        this.plugin = (BridgePlugin) plugin;
        Session session = ((BridgePlugin) plugin).getWizardManager().getSession(player.getUniqueId());
        if (session != null){
            this.session = session;
        }
        this.maxGoalsItemKey = new NamespacedKey(plugin, "maxGoalsItem");
        this.incrementMaxGoalsItemKey = new NamespacedKey(plugin, "incrementMaxGoalsItem");
        this.decrementMaxGoalsItemKey = new NamespacedKey(plugin, "decrementMaxGoalsItem");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!inventory.getViewers().contains(player)){
                    plugin.getLogger().info("Cancelled team menu runnable");
                    cancel();
                } else {
                    reloadItems();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

    @Override
    public String getMenuName() {
        return color("&bChange maximum goals!");
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
            if (itemStack.getItemMeta() != null){
                if (session != null){
                    PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
                    if (persistentDataContainer.has(incrementMaxGoalsItemKey)) {
                        session.incrementMaxGoals();
                    } else if (persistentDataContainer.has(decrementMaxGoalsItemKey)) {
                        session.decrementMaxGoals();
                    }
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        String maxGoalsLore = color("&eCurrent max goals: " + (session != null ? session.getMaxGoals() : "null"));
        ItemStack maxGoalsItem = new ItemBuilder(Material.GLOWSTONE_DUST)
                .setName("&bMaximum Goals")
                .addLoreLine(color("&eCurrent max goals: " + maxGoalsLore))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(maxGoalsItemKey, PersistentDataType.BOOLEAN, true))
                .build();

        ItemStack incrementMaxGoalsItem = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                .setName("&aClick to increment max goals!")
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(incrementMaxGoalsItemKey, PersistentDataType.BOOLEAN, true))
                        .build();

        ItemStack decrementMaxGoalsItem = new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .setName("&cClick to decrement max goals!")
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(decrementMaxGoalsItemKey, PersistentDataType.BOOLEAN, true))
                        .build();

        inventory.setItem(13, maxGoalsItem);
        inventory.setItem(14, incrementMaxGoalsItem);
        inventory.setItem(12, decrementMaxGoalsItem);

        setFillerItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
    }
}
