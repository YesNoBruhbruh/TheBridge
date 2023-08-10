package com.maanraj514.setupwizard.menu;

import com.maanraj514.BridgePlugin;
import com.maanraj514.Okmeta;
import com.maanraj514.menu.Menu;
import com.maanraj514.menu.MenuManager;
import com.maanraj514.menu.PaginatedMenu;
import com.maanraj514.menu.PlayerMenuUtility;
import com.maanraj514.model.Team;
import com.maanraj514.setupwizard.Session;
import com.maanraj514.utils.Hologram;
import com.maanraj514.utils.ItemBuilder;
import com.maanraj514.utils.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import static com.maanraj514.utils.ColorUtil.color;
import static com.maanraj514.utils.ColorUtil.translate;
import static com.maanraj514.utils.MessageUtil.rainbow;

public class TeamOptionsMenu extends Menu {
    private final BridgePlugin plugin;

    private final NamespacedKey setTeamNameKey;
    private final NamespacedKey setTeamSpawnLocationKey;
    private final NamespacedKey setTeamPortalLocationOneKey;
    private final NamespacedKey setTeamPortalLocationTwoKey;
    private final NamespacedKey setRGBKey;
    private final NamespacedKey deleteTeamKey;
    private final NamespacedKey backKey;

    public TeamOptionsMenu(PlayerMenuUtility playerMenuUtility, Okmeta plugin) {
        super(playerMenuUtility, plugin);
        this.plugin = (BridgePlugin) plugin;
        this.setTeamNameKey = new NamespacedKey(plugin, "setTeamNameItem");
        this.setTeamSpawnLocationKey = new NamespacedKey(plugin, "setTeamSpawnLocationItem");
        this.setTeamPortalLocationOneKey = new NamespacedKey(plugin, "setTeamPortalLocationOneItem");
        this.setTeamPortalLocationTwoKey = new NamespacedKey(plugin, "setTeamPortalLocationTwoItem");
        this.setRGBKey = new NamespacedKey(plugin, "setRGBItem");
        this.deleteTeamKey = new NamespacedKey(plugin, "deleteTeamItem");
        this.backKey = new NamespacedKey(plugin, "backItem");
    }

    @Override
    public String getMenuName() {
        return color(rainbow("Team Options"));
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
            if (itemStack.getItemMeta() != null) {
                if (plugin.getWizardManager().hasSession(player.getUniqueId())) {
                    Session session = plugin.getWizardManager().getSession(player.getUniqueId());
                    Team team = session.getTeams().get(playerMenuUtility.getData(PMUData.TEAM_SELECTED, ChatColor.class));
                    PlayerMenuUtility playerMenuUtility = MenuManager.getPlayerMenuUtility(player);
                    PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
                    if (persistentDataContainer.has(setTeamNameKey)) {
                        if (!session.getInTeamNameChangeSession().contains(player.getUniqueId())) {
                            session.getInTeamNameChangeSession().add(player.getUniqueId());
                            player.closeInventory();
                            player.sendMessage(color("&cYou have 10 seconds to set a team name!"));

                            new BukkitRunnable() {
                                int timeLeft = 10;
                                @Override
                                public void run() {
                                    if (!session.getInTeamNameChangeSession().contains(player.getUniqueId())) {
                                        cancel();
                                    }
                                    if (timeLeft == 5) {
                                        player.sendMessage(color("&cYou have 5 seconds to set a team name!"));
                                    }
                                    if (timeLeft <= 0) {
                                        player.sendMessage(color("&cYou have run out of time to change team name!"));
                                        session.getInTeamNameChangeSession().remove(player.getUniqueId());
                                        cancel();
                                    }
                                    timeLeft--;
                                }
                            }.runTaskTimerAsynchronously(plugin, 0L, 20L);
                        }
                    } else if (persistentDataContainer.has(backKey)) {
                        MenuManager.openMenu(TeamMenu.class, player);
                        playerMenuUtility.setData(PMUData.TEAM_SELECTED, null);
                    } else if (persistentDataContainer.has(deleteTeamKey)) {
                        session.removeTeam(team);
                        playerMenuUtility.setData(PMUData.TEAM_SELECTED, null);
                        MenuManager.openMenu(TeamMenu.class, player);
                    } else if (persistentDataContainer.has(setTeamSpawnLocationKey)) {

                        for (Hologram hologram : session.getHolograms()) {
                            if (hologram.getText().equalsIgnoreCase(color(team.getColor() + "Team Spawn Location"))) {
                                hologram.destroy();
                            }
                        }

                        String teamName = team.getName() != null ? team.getName() : team.getColor().name();

                        session.getHolograms().add(new Hologram(player.getLocation(), color(team.getColor() + "Team Spawn Location")));
                        team.setSpawnLocation(player.getLocation());
                        player.sendMessage(color(team.getColor() + teamName +  " spawn location set!"));
                    } else if (persistentDataContainer.has(setTeamPortalLocationOneKey)) {

                        for (Hologram hologram : session.getHolograms()) {
                            if (hologram.getText().equalsIgnoreCase(color(team.getColor() + "Team Portal Location One"))) {
                                hologram.destroy();
                            }
                        }

                        String teamName = team.getName() != null ? team.getName() : team.getColor().name();

                        session.getHolograms().add(new Hologram(player.getLocation(), color(team.getColor() + "Team Portal Location One")));
                        team.setPortalLocationOne(LocationUtil.locationToPos(player.getLocation()));
                        player.sendMessage(color(team.getColor() + teamName + " Team portal Location One Set!"));
                    } else if (persistentDataContainer.has(setTeamPortalLocationTwoKey)) {

                        for (Hologram hologram : session.getHolograms()) {
                            if (hologram.getText().equalsIgnoreCase(color(team.getColor() + "Team Portal Location Two"))) {
                                hologram.destroy();
                            }
                        }

                        String teamName = team.getName() != null ? team.getName() : team.getColor().name();

                        session.getHolograms().add(new Hologram(player.getLocation(), color(team.getColor() + "Team Portal Location Two")));
                        team.setPortalLocationTwo(LocationUtil.locationToPos(player.getLocation()));
                        player.sendMessage(color(team.getColor() + teamName + " Team portal Location Two Set!"));
                    } else if (persistentDataContainer.has(setRGBKey)) {
                        MenuManager.openMenu(RGBMenu.class, player);
                    }
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack setTeamNameItem = new ItemBuilder(Material.NAME_TAG)
                .setName(rainbow("Set Team Name"))
                .setGlowing(true)
                        .applyPersistentData(persistentDataContainer ->
                            persistentDataContainer.set(setTeamNameKey, PersistentDataType.BOOLEAN, true))
                                .build();

        ItemStack setTeamSpawnLocationItem = new ItemBuilder(Material.ENDER_PEARL)
                .setName(rainbow("Set Team Spawn Location"))
                .setGlowing(true)
                .applyPersistentData(persistentDataContainer ->
                    persistentDataContainer.set(setTeamSpawnLocationKey, PersistentDataType.BOOLEAN, true))
                .build();

        ItemStack setTeamPortalLocationOneItem = new ItemBuilder(Material.RED_CONCRETE_POWDER)
                .setName(color("&bSet Team Portal Location 1"))
                .setGlowing(true)
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(setTeamPortalLocationOneKey, PersistentDataType.BOOLEAN, true))
                .build();

        ItemStack setTeamPortalLocationTwoItem = new ItemBuilder(Material.GREEN_CONCRETE_POWDER)
                .setName(color("&bSet Team Portal Location 2"))
                .setGlowing(true)
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(setTeamPortalLocationTwoKey, PersistentDataType.BOOLEAN, true))
                .build();

        ItemStack setRGBItem = new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .setName("&bSet RGB")
                .setGlowing(true)
                .setLeatherColor(Color.ORANGE)
                .applyPersistentData(persistentDataContainer ->
                        persistentDataContainer.set(setRGBKey, PersistentDataType.BOOLEAN, true))
                .build();

        ItemStack deleteTeamItem = new ItemBuilder(Material.BARRIER)
                .setName(color("&cDelete this team."))
                .setGlowing(true)
                .applyPersistentData(persistentDataContainer ->
                    persistentDataContainer.set(deleteTeamKey, PersistentDataType.BOOLEAN, true))
                .build();

        ItemStack backItem = new ItemBuilder(Material.ARROW)
                .setName("&cBack")
                .setGlowing(true)
                        .applyPersistentData(persistentDataContainer ->
                            persistentDataContainer.set(backKey, PersistentDataType.BOOLEAN, true))
                                .build();

        inventory.setItem(10, setTeamNameItem);
        inventory.setItem(12, setTeamSpawnLocationItem);
        inventory.setItem(14, setTeamPortalLocationOneItem);
        inventory.setItem(16, setTeamPortalLocationTwoItem);
        inventory.setItem(20, setRGBItem);
        inventory.setItem(22, deleteTeamItem);
        inventory.setItem(31, backItem);

        setFillerItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
    }
}
