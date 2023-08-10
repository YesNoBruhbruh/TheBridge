package com.maanraj514.setupwizard.menu;

import com.maanraj514.BridgePlugin;
import com.maanraj514.Okmeta;
import com.maanraj514.menu.Menu;
import com.maanraj514.menu.MenuManager;
import com.maanraj514.menu.PlayerMenuUtility;
import com.maanraj514.setupwizard.Session;
import com.maanraj514.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.maanraj514.utils.ColorUtil.color;
import static com.maanraj514.utils.MessageUtil.rainbow;

public class WizardMenu extends Menu {
    private final BridgePlugin plugin;

    private final NamespacedKey teamItemKey;
    private final NamespacedKey setSpectatorSpawnItemKey;
    private final NamespacedKey setBuildAbleCornerOneItemKey;
    private final NamespacedKey setBuildAbleCornerTwoItemKey;

    public WizardMenu(PlayerMenuUtility playerMenuUtility, Okmeta plugin) {
        super(playerMenuUtility, plugin);
        this.plugin = (BridgePlugin) plugin;
        this.teamItemKey = new NamespacedKey(plugin, "teamItem");
        this.setSpectatorSpawnItemKey = new NamespacedKey(plugin, "spectatorSpawnItem");
        this.setBuildAbleCornerOneItemKey = new NamespacedKey(plugin, "buildAbleCornerOneItem");
        this.setBuildAbleCornerTwoItemKey = new NamespacedKey(plugin, "buildAbleCornerTwoItem");
    }

    @Override
    public String getMenuName() {
        return color("&a&lWizard Menu");
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
        if (plugin.getWizardManager().hasSession(inventoryClickEvent.getWhoClicked().getUniqueId())) {
            Session session = plugin.getWizardManager().getSession(inventoryClickEvent.getWhoClicked().getUniqueId());
            if (itemStack != null){
                if (itemStack.getItemMeta() != null) {
                    PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
                    if (persistentDataContainer.has(teamItemKey)) {
                        MenuManager.openMenu(TeamMenu.class, player);
                    } else if (persistentDataContainer.has(setSpectatorSpawnItemKey)) {
                        session.setSpectatorSpawn();
                    } else if (persistentDataContainer.has(setBuildAbleCornerOneItemKey)) {
                        session.setBuildAbleCornerOne();
                    } else if (persistentDataContainer.has(setBuildAbleCornerTwoItemKey)) {
                        session.setBuildAbleCornerTwo();
                    }
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack teamItem = new ItemBuilder(Material.WHITE_WOOL)
                .setName(rainbow("Team"))
                .addLoreLine(rainbow("Available Teams are: "))
                .addLoreLines(ChatColor.RED + "RED",
                        ChatColor.BLUE + "BLUE",
                        ChatColor.AQUA + "AQUA",
                        ChatColor.GREEN + "GREEN",
                        ChatColor.GRAY + "GRAY",
                        ChatColor.LIGHT_PURPLE + "LIGHT_PURPLE",
                        ChatColor.YELLOW + "YELLOW",
                        ChatColor.GREEN + "GREEN",
                        ChatColor.BLACK + "BLACK",
                        ChatColor.WHITE + "WHITE",
                        ChatColor.DARK_PURPLE + "DARK_PURPLE",
                        ChatColor.GOLD + "GOLD",
                        ChatColor.DARK_GREEN + "DARK_GREEN",
                        ChatColor.DARK_AQUA + "DARK_AQUA",
                        ChatColor.DARK_RED + "DARK_RED",
                        ChatColor.DARK_GRAY + "DARK_GRAY",
                        ChatColor.DARK_BLUE + "DARK_BLUE"
                )
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(teamItemKey, PersistentDataType.BOOLEAN, true))
                .build();

        ItemStack setSpectatorSpawnItem = new ItemBuilder(Material.ENDER_EYE)
                .setName("&aSet Spectator Spawn")
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(setSpectatorSpawnItemKey, PersistentDataType.BOOLEAN, true))
                .build();

        ItemStack setBuildAbleCornerOneItem = new ItemBuilder(Material.RED_CONCRETE)
                .setName("&aSet Build Able Corner One")
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(setBuildAbleCornerOneItemKey, PersistentDataType.BOOLEAN, true))
                .build();

        ItemStack setBuildAbleCornerTwoItem = new ItemBuilder(Material.GREEN_CONCRETE)
                .setName("&aSet Build Able Corner Two")
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(setBuildAbleCornerTwoItemKey, PersistentDataType.BOOLEAN, true))
                .build();

        inventory.setItem(11, setSpectatorSpawnItem);
        inventory.setItem(13, setBuildAbleCornerOneItem);
        inventory.setItem(15, setBuildAbleCornerTwoItem);
        inventory.setItem(22, teamItem);

        setFillerItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
    }
}
