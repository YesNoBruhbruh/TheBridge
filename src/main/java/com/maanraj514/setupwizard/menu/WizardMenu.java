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

import static com.maanraj514.utils.ColorUtil.color;
import static com.maanraj514.utils.MessageUtil.rainbow;

public class WizardMenu implements InventoryHolder {
    private final Inventory inventory;
    private final Session session;

    public WizardMenu(Session session){
        this.session = session;
        this.inventory = Bukkit.createInventory(this, 27, color("&a&lWizard Menu"));

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
                ).build();

        ItemStack setSpectatorSpawnItem = new ItemBuilder(Material.ENDER_EYE)
                .setName("&aSet Spectator Spawn")
                .build();

        ItemStack setBuildAbleCornerOneItem = new ItemBuilder(Material.RED_CONCRETE)
                .setName("&aSet Build Able Corner One")
                .build();

        ItemStack setBuildAbleCornerTwoItem = new ItemBuilder(Material.GREEN_CONCRETE)
                .setName("&aSet Build Able Corner Two")
                .build();

        inventory.setItem(11, setSpectatorSpawnItem);
        inventory.setItem(13, setBuildAbleCornerOneItem);
        inventory.setItem(15, setBuildAbleCornerTwoItem);
        inventory.setItem(22, teamItem);

        for (int i = 0; i < inventory.getSize(); i++){
            if (inventory.getItem(i) == null){
                inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                                .setName(" ")
                        .build());
            }
        }

        session.getPlayer().openInventory(inventory);
    }

    public void handleClick(InventoryClickEvent event){
        if (event.getCurrentItem() != null){
            switch (event.getCurrentItem().getType()) {
                case WHITE_WOOL:
                    new TeamMenu(session);
                    break;
                case ENDER_EYE:
                    this.session.setSpectatorSpawn();
                    break;
                case RED_CONCRETE:
                    this.session.setBuildAbleCornerOne();
                    break;
                case GREEN_CONCRETE:
                    this.session.setBuildAbleCornerTwo();
                    break;
            }
            session.getPlayer().closeInventory();
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
