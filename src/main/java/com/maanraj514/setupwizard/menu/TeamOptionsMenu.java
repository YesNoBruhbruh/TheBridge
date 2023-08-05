package com.maanraj514.setupwizard.menu;

import com.maanraj514.BridgePlugin;
import com.maanraj514.Okmeta;
import com.maanraj514.menu.Menu;
import com.maanraj514.menu.PlayerMenuUtility;
import com.maanraj514.setupwizard.Session;
import com.maanraj514.utils.ItemBuilder;
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
    private final NamespacedKey deleteTeamKey;

    public TeamOptionsMenu(PlayerMenuUtility playerMenuUtility, Okmeta plugin) {
        super(playerMenuUtility, plugin);
        this.plugin = (BridgePlugin) plugin;
        this.setTeamNameKey = new NamespacedKey(plugin, "setTeamNameItem");
        this.setTeamSpawnLocationKey = new NamespacedKey(plugin, "setTeamSpawnLocationItem");
        this.setTeamPortalLocationOneKey = new NamespacedKey(plugin, "setTeamPortalLocationOneItem");
        this.setTeamPortalLocationTwoKey = new NamespacedKey(plugin, "setTeamPortalLocationTwoItem");
        this.deleteTeamKey = new NamespacedKey(plugin, "deleteTeamItem");
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
                    PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
                    if (persistentDataContainer.has(setTeamNameKey)) {
                        if (!session.getInTeamNameChangeSession().contains(player.getUniqueId())) {
                            session.getInTeamNameChangeSession().add(player.getUniqueId());
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
                    }
                }
            }
        }

    }

    @Override
    public void setMenuItems() {
        ItemStack setTeamNameItem = new ItemBuilder(Material.NAME_TAG)
                .setGlowing(true)
                        .applyPersistentData(persistentDataContainer ->
                            persistentDataContainer.set(setTeamNameKey, PersistentDataType.BOOLEAN, true))
                                .build();

        inventory.setItem(10, setTeamNameItem);

        setFillerItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
    }
}
