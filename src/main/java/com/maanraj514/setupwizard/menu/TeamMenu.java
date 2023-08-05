package com.maanraj514.setupwizard.menu;

import com.maanraj514.BridgePlugin;
import com.maanraj514.Okmeta;
import com.maanraj514.menu.Menu;
import com.maanraj514.menu.MenuManager;
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

import static com.maanraj514.utils.MessageUtil.rainbow;

public class TeamMenu extends Menu {
    private final BridgePlugin plugin;
    private final Session session;

    private final NamespacedKey redTeamItemKey;
    private final NamespacedKey whiteTeamItemKey;
    private final NamespacedKey blueTeamItemKey;
    private final NamespacedKey yellowTeamItemKey;
    private final NamespacedKey grayTeamItemKey;
    private final NamespacedKey greenTeamItemKey;
    private final NamespacedKey cyanTeamItemKey;
    private final NamespacedKey purpleTeamItemKey;
    private final NamespacedKey pinkTeamItemKey;
    private final NamespacedKey blackTeamItemKey;
    private final NamespacedKey limeTeamItemKey;
    private final NamespacedKey orangeTeamItemKey;
    private final NamespacedKey aquaTeamItemKey;
    private final NamespacedKey darkGrayTeamItemKey;


    public TeamMenu(PlayerMenuUtility playerMenuUtility, Okmeta plugin) {
        super(playerMenuUtility, plugin);
        this.plugin = (BridgePlugin) plugin;
        this.session = this.plugin.getWizardManager().getSession(player.getUniqueId());

        this.redTeamItemKey = new NamespacedKey(plugin, "redTeamItem");
        this.whiteTeamItemKey = new NamespacedKey(plugin, "whiteTeamItem");
        this.blueTeamItemKey = new NamespacedKey(plugin, "blueTeamItem");
        this.yellowTeamItemKey = new NamespacedKey(plugin, "yellowTeamItem");
        this.grayTeamItemKey = new NamespacedKey(plugin, "grayTeamItem");
        this.greenTeamItemKey = new NamespacedKey(plugin, "greenTeamItem");
        this.cyanTeamItemKey = new NamespacedKey(plugin, "cyanTeamItem");
        this.purpleTeamItemKey = new NamespacedKey(plugin, "purpleTeamItem");
        this.pinkTeamItemKey = new NamespacedKey(plugin, "pinkTeamItem");
        this.blackTeamItemKey = new NamespacedKey(plugin, "blackTeamItem");
        this.limeTeamItemKey = new NamespacedKey(plugin, "limeTeamItem");
        this.orangeTeamItemKey = new NamespacedKey(plugin, "orangeTeamItem");
        this.aquaTeamItemKey = new NamespacedKey(plugin, "aquaTeamItem");
        this.darkGrayTeamItemKey = new NamespacedKey(plugin, "darkGrayTeamItem");
    }

    @Override
    public String getMenuName() {
        return rainbow("Team Menu");
    }

    @Override
    public int getSlots() {
        return 36;
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
                if (plugin.getWizardManager().hasSession(player.getUniqueId())){
                    PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
                    if (persistentDataContainer.has(redTeamItemKey)) {
                        if (!session.getTeams().containsKey(ChatColor.RED)){
                            Team team = new Team(ChatColor.RED);
                            session.addTeam(team);
                            playerMenuUtility.setData(PMUData.TEAM_SELECTED, team.getColor());
                        } else {
                            MenuManager.openMenu(TeamOptionsMenu.class, player);
                        }
                    } else if (persistentDataContainer.has(whiteTeamItemKey)) {
                        if (!session.getTeams().containsKey(ChatColor.WHITE)){
                            Team team = new Team(ChatColor.WHITE);
                            session.addTeam(team);
                            playerMenuUtility.setData(PMUData.TEAM_SELECTED, team.getColor());
                        } else {
                            MenuManager.openMenu(TeamOptionsMenu.class, player);
                        }
                    } else if (persistentDataContainer.has(blueTeamItemKey)) {
                        if (!session.getTeams().containsKey(ChatColor.BLUE)){
                            Team team = new Team(ChatColor.BLUE);
                            session.addTeam(team);
                            playerMenuUtility.setData(PMUData.TEAM_SELECTED, team.getColor());
                        } else {
                            MenuManager.openMenu(TeamOptionsMenu.class, player);
                        }
                    } else if (persistentDataContainer.has(yellowTeamItemKey)) {
                        if (!session.getTeams().containsKey(ChatColor.YELLOW)){
                            Team team = new Team(ChatColor.YELLOW);
                            session.addTeam(team);
                            playerMenuUtility.setData(PMUData.TEAM_SELECTED, team.getColor());
                        } else {
                            MenuManager.openMenu(TeamOptionsMenu.class, player);
                        }
                    } else if (persistentDataContainer.has(grayTeamItemKey)) {
                        if (!session.getTeams().containsKey(ChatColor.GRAY)){
                            Team team = new Team(ChatColor.GRAY);
                            session.addTeam(team);
                            playerMenuUtility.setData(PMUData.TEAM_SELECTED, team.getColor());
                        } else {
                            MenuManager.openMenu(TeamOptionsMenu.class, player);
                        }
                    } else if (persistentDataContainer.has(greenTeamItemKey)) {
                        if (!session.getTeams().containsKey(ChatColor.GREEN)){
                            Team team = new Team(ChatColor.GREEN);
                            session.addTeam(team);
                            playerMenuUtility.setData(PMUData.TEAM_SELECTED, team.getColor());
                        } else {
                            MenuManager.openMenu(TeamOptionsMenu.class, player);
                        }
                    } else if (persistentDataContainer.has(cyanTeamItemKey)) {
                        if (!session.getTeams().containsKey(ChatColor.DARK_AQUA)){
                            Team team = new Team(ChatColor.DARK_AQUA);
                            session.addTeam(team);
                            playerMenuUtility.setData(PMUData.TEAM_SELECTED, team.getColor());
                        } else {
                            MenuManager.openMenu(TeamOptionsMenu.class, player);
                        }
                    } else if (persistentDataContainer.has(purpleTeamItemKey)) {
                        if (!session.getTeams().containsKey(ChatColor.DARK_PURPLE)){
                            Team team = new Team(ChatColor.DARK_PURPLE);
                            session.addTeam(team);
                            playerMenuUtility.setData(PMUData.TEAM_SELECTED, team.getColor());
                        } else {
                            MenuManager.openMenu(TeamOptionsMenu.class, player);
                        }
                    } else if (persistentDataContainer.has(pinkTeamItemKey)) {
                        if (!session.getTeams().containsKey(ChatColor.LIGHT_PURPLE)){
                            Team team = new Team(ChatColor.LIGHT_PURPLE);
                            session.addTeam(team);
                            playerMenuUtility.setData(PMUData.TEAM_SELECTED, team.getColor());
                        } else {
                            MenuManager.openMenu(TeamOptionsMenu.class, player);
                        }
                    } else if (persistentDataContainer.has(blackTeamItemKey)) {
                        if (!session.getTeams().containsKey(ChatColor.BLACK)){
                            Team team = new Team(ChatColor.BLACK);
                            session.addTeam(team);
                            playerMenuUtility.setData(PMUData.TEAM_SELECTED, team.getColor());
                        } else {
                            MenuManager.openMenu(TeamOptionsMenu.class, player);
                        }
                    } else if (persistentDataContainer.has(limeTeamItemKey)) {
                        if (!session.getTeams().containsKey(ChatColor.GREEN)){
                            Team team = new Team(ChatColor.GREEN);
                            session.addTeam(team);
                            playerMenuUtility.setData(PMUData.TEAM_SELECTED, team.getColor());
                        } else {
                            MenuManager.openMenu(TeamOptionsMenu.class, player);
                        }
                    } else if (persistentDataContainer.has(orangeTeamItemKey)) {
                        if (!session.getTeams().containsKey(ChatColor.GOLD)){
                            Team team = new Team(ChatColor.GOLD);
                            session.addTeam(team);
                            playerMenuUtility.setData(PMUData.TEAM_SELECTED, team.getColor());
                        } else {
                            MenuManager.openMenu(TeamOptionsMenu.class, player);
                        }
                    } else if (persistentDataContainer.has(aquaTeamItemKey)) {
                        if (!session.getTeams().containsKey(ChatColor.AQUA)){
                            Team team = new Team(ChatColor.AQUA);
                            session.addTeam(team);
                            playerMenuUtility.setData(PMUData.TEAM_SELECTED, team.getColor());
                        } else {
                            MenuManager.openMenu(TeamOptionsMenu.class, player);
                        }
                    } else if (persistentDataContainer.has(darkGrayTeamItemKey)) {
                        if (!session.getTeams().containsKey(ChatColor.DARK_GRAY)){
                            Team team = new Team(ChatColor.DARK_GRAY);
                            session.addTeam(team);
                            playerMenuUtility.setData(PMUData.TEAM_SELECTED, team.getColor());
                        } else {
                            MenuManager.openMenu(TeamOptionsMenu.class, player);
                        }
                    }
                    reloadItems();
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack redTeamItem = new ItemBuilder(Material.RED_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.RED) ? "&cClick to edit team." : "&aClick to create team"))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(redTeamItemKey, PersistentDataType.BOOLEAN, true))
                .build();
        ItemStack whiteTeamItem = new ItemBuilder(Material.WHITE_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.WHITE) ? "&cClick to edit team." : "&aClick to create team"))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(whiteTeamItemKey, PersistentDataType.BOOLEAN, true))
                .build();
        ItemStack blueTeamItem = new ItemBuilder(Material.BLUE_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.BLUE) ? "&cClick to edit team." : "&aClick to create team"))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(blueTeamItemKey, PersistentDataType.BOOLEAN, true))
                .build();
        ItemStack yellowTeamItem = new ItemBuilder(Material.YELLOW_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.YELLOW) ? "&cClick to edit team." : "&aClick to create team"))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(yellowTeamItemKey, PersistentDataType.BOOLEAN, true))
                .build();
        ItemStack grayTeamItem = new ItemBuilder(Material.GRAY_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.DARK_GRAY) ? "&cClick to edit team." : "&aClick to create team"))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(grayTeamItemKey, PersistentDataType.BOOLEAN, true))
                .build();
        ItemStack greenTeamItem = new ItemBuilder(Material.GREEN_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.DARK_GREEN) ? "&cClick to edit team." : "&aClick to create team"))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(greenTeamItemKey, PersistentDataType.BOOLEAN, true))
                .build();
        ItemStack cyanTeamItem = new ItemBuilder(Material.CYAN_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.DARK_AQUA) ? "&cClick to edit team." : "&aClick to create team"))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(cyanTeamItemKey, PersistentDataType.BOOLEAN, true))
                .build();
        ItemStack purpleTeamItem = new ItemBuilder(Material.PURPLE_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.DARK_PURPLE) ? "&cClick to edit team." : "&aClick to create team"))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(purpleTeamItemKey, PersistentDataType.BOOLEAN, true))
                .build();
        ItemStack pinkTeamItem = new ItemBuilder(Material.PINK_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.LIGHT_PURPLE) ? "&cClick to edit team." : "&aClick to create team"))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(pinkTeamItemKey, PersistentDataType.BOOLEAN, true))
                .build();
        ItemStack blackTeamItem = new ItemBuilder(Material.BLACK_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.BLACK) ? "&cClick to edit team." : "&aClick to create team"))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(blackTeamItemKey, PersistentDataType.BOOLEAN, true))
                .build();
        ItemStack limeTeamItem = new ItemBuilder(Material.LIME_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.GREEN) ? "&cClick to edit team." : "&aClick to create team"))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(limeTeamItemKey, PersistentDataType.BOOLEAN, true))
                .build();
        ItemStack orangeTeamItem = new ItemBuilder(Material.ORANGE_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.GOLD) ? "&cClick to edit team." : "&aClick to create team"))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(orangeTeamItemKey, PersistentDataType.BOOLEAN, true))
                .build();
        ItemStack aquaTeamItem = new ItemBuilder(Material.LIGHT_BLUE_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.AQUA) ? "&cClick to edit team." : "&aClick to create team"))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(aquaTeamItemKey, PersistentDataType.BOOLEAN, true))
                .build();
        ItemStack darkGrayTeamItem = new ItemBuilder(Material.LIGHT_GRAY_WOOL)
                .addLoreLine((session.getTeams().containsKey(ChatColor.GRAY) ? "&cClick to edit team." : "&aClick to create team"))
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(darkGrayTeamItemKey, PersistentDataType.BOOLEAN, true))
                .build();

        inventory.setItem(10, redTeamItem);
        inventory.setItem(11, whiteTeamItem);
        inventory.setItem(12, blueTeamItem);
        inventory.setItem(13, yellowTeamItem);
        inventory.setItem(14, grayTeamItem);
        inventory.setItem(15, greenTeamItem);
        inventory.setItem(16, cyanTeamItem);
        inventory.setItem(19, purpleTeamItem);
        inventory.setItem(20, pinkTeamItem);
        inventory.setItem(21, blackTeamItem);
        inventory.setItem(22, limeTeamItem);
        inventory.setItem(23, orangeTeamItem);
        inventory.setItem(24, aquaTeamItem);
        inventory.setItem(25, darkGrayTeamItem);


        setFillerItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
    }
}