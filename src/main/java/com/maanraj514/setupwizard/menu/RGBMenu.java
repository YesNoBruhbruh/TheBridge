package com.maanraj514.setupwizard.menu;

import com.maanraj514.BridgePlugin;
import com.maanraj514.Okmeta;
import com.maanraj514.menu.Menu;
import com.maanraj514.menu.PlayerMenuUtility;
import com.maanraj514.model.Team;
import com.maanraj514.setupwizard.Session;
import com.maanraj514.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static com.maanraj514.utils.ColorUtil.color;

public class RGBMenu extends Menu {
    private final BridgePlugin plugin;

    private final Session session;

    private final NamespacedKey colorItemKey;

    private final List<ItemStack> itemsToAnimate;

    private int index = 0;

    public RGBMenu(PlayerMenuUtility playerMenuUtility, Okmeta plugin) {
        super(playerMenuUtility, plugin);
        this.plugin = (BridgePlugin) plugin;

        this.session = this.plugin.getWizardManager().getSession(player.getUniqueId());

        this.colorItemKey = new NamespacedKey(plugin, "colorItem");
        itemsToAnimate = new ArrayList<>();

        if (session != null){

            ItemStack redItem = new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                    .setName("&cClick to set armor color!")
                    .applyPersistentData(persistentDataContainer ->
                            persistentDataContainer.set(colorItemKey, PersistentDataType.BOOLEAN, true))
                    .build();
            ItemStack greenItem = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                    .setName("&aClick to set armor color!")
                    .applyPersistentData(persistentDataContainer ->
                            persistentDataContainer.set(colorItemKey, PersistentDataType.BOOLEAN, true))
                    .build();
            ItemStack blueItem = new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE)
                    .setName("&9Click to set armor color!")
                    .applyPersistentData(persistentDataContainer ->
                            persistentDataContainer.set(colorItemKey, PersistentDataType.BOOLEAN, true))
                    .build();

            itemsToAnimate.add(redItem);
            itemsToAnimate.add(greenItem);
            itemsToAnimate.add(blueItem);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!inventory.getViewers().contains(player) || itemsToAnimate.isEmpty()) {
                        plugin.getLogger().info("Cancelled RGB menu runnable");
                        cancel();
                    }
                    if (index >= 3){
                        index = 0;
                    }

                    switch (index){
                        case 0:
                            inventory.setItem(12, itemsToAnimate.get(0));
                            break;
                        case 1:
                            inventory.setItem(12, itemsToAnimate.get(1));
                            break;
                        case 2:
                            inventory.setItem(12, itemsToAnimate.get(2));
                            break;
                    }

                    index++;
                }
            }.runTaskTimerAsynchronously(plugin, 0L, 20L);
        }
    }

    @Override
    public String getMenuName() {
        return color("&cR" + "&aG" + "&9B");
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
        if (itemStack != null) {
            if (itemStack.getItemMeta() != null){
                if (session != null){
                    PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
                    if (persistentDataContainer.has(colorItemKey)) {
                        if (!session.getInArmorColorChangeSession().contains(player.getUniqueId())) {
                            session.getInArmorColorChangeSession().add(player.getUniqueId());
                            player.closeInventory();
                            player.sendMessage(color("&cYou have 10 seconds to set the team armor color!"));

                            new BukkitRunnable() {
                                int timeLeft = 10;
                                @Override
                                public void run() {
                                    if (!session.getInArmorColorChangeSession().contains(player.getUniqueId())) {
                                        cancel();
                                    }
                                    if (timeLeft == 5) {
                                        player.sendMessage(color("&cYou have 5 seconds to set the team armor color!"));
                                    }
                                    if (timeLeft <= 0) {
                                        player.sendMessage(color("&cYou have run out of time to change the team armor color!"));
                                        session.getInArmorColorChangeSession().remove(player.getUniqueId());
                                        cancel();
                                    }
                                    timeLeft--;
                                }
                            }.runTaskTimerAsynchronously(plugin, 0L, 20L);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        if (session != null){
            Team team = session.getTeams().get(playerMenuUtility.getData(PMUData.TEAM_SELECTED, ChatColor.class));

            String currentValue = color(team.getArmorColor() != null ? "&c" + team.getArmorColor().getRed() + " &a" + team.getArmorColor().getGreen() + " &9" + team.getArmorColor().getBlue() : "&cNot set yet!");
            ItemStack currentColorValueItem = new ItemBuilder(Material.LEATHER_CHESTPLATE)
                    .setName(currentValue)
                    .setGlowing(team.getArmorColor() != null)
                    .build();

            inventory.setItem(13, currentColorValueItem);

            setFillerItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
        }
    }
}