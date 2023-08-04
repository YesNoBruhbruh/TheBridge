package com.maanraj514.setupwizard;

import com.maanraj514.BridgePlugin;
import com.maanraj514.game.GameMode;
import com.maanraj514.model.GameData;
import com.maanraj514.model.Team;
import com.maanraj514.setupwizard.menu.ConfirmExitMenu;
import com.maanraj514.setupwizard.menu.WizardMenu;
import com.maanraj514.util.Messages;
import com.maanraj514.util.WorldUtil;
import com.maanraj514.utils.ColorUtil;
import com.maanraj514.utils.Hologram;
import com.maanraj514.utils.ItemBuilder;
import com.maanraj514.utils.MessageUtil;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.*;

import static com.maanraj514.utils.ColorUtil.color;

public class Session {

    private final BridgePlugin plugin;

    private final UUID playerUUID;

    private GameData gameData;

    private final GameMode gameMode;

    private final Map<ChatColor, Team> teams;
    private final List<Hologram> holograms;

    public Session(UUID playerUUID, String map, GameMode gameMode, BridgePlugin plugin) {
        this.plugin = plugin;
        this.playerUUID = playerUUID;
        this.gameMode = gameMode;
        this.holograms = new ArrayList<>();
        this.teams = new HashMap<>();

        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null){
            MessageUtil.sendMessageWithLines("&bCreating session for " + player.getName(),
                    player,
                    "&e--------------------",
                    "&e--------------------");

            this.gameData = new GameData(
                    map,
                    gameMode,
                    Arrays.asList("Maanraj514"),
                    System.currentTimeMillis(),
                    new HashMap<>(),
                    null,
                    null,
                    null
            );

            player.getInventory().clear();

            if (!WorldUtil.worldExists(map, plugin)){
                player.sendMessage(Messages.ERROR_WORLD_NOT_FOUND);
                plugin.getWizardManager().endSession(player, false);
                return;
            }
            WorldUtil.loadWorld(map, plugin);

            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                player.setGameMode(org.bukkit.GameMode.CREATIVE);
                player.teleportAsync(new Location(Bukkit.getWorld(map), 0, 200, 0));

                ItemStack guiItem = new ItemBuilder(Material.COMPASS).setName("&6Open Gui")
                        .applyPersistentData(persistentDataContainer ->
                                persistentDataContainer.set(new NamespacedKey(plugin, "openGui"),
                                        PersistentDataType.BOOLEAN, true))
                        .build();

                ItemStack centerXZItem = new ItemBuilder(Material.ARROW).setName("&6Center X-Z")
                        .applyPersistentData(persistentDataContainer ->
                                persistentDataContainer.set(new NamespacedKey(plugin, "centerXZ"),
                                        PersistentDataType.BOOLEAN, true))
                        .build();

                ItemStack centerYawPitchItem = new ItemBuilder(Material.ARROW).setName("&6Center Yaw-Pitch")
                        .applyPersistentData(persistentDataContainer ->
                                persistentDataContainer.set(new NamespacedKey(plugin, "centerYawPitch"),
                                        PersistentDataType.BOOLEAN, true))
                        .build();

                ItemStack exitItem = new ItemBuilder(Material.BARRIER).setName("&cExit Wizard")
                        .applyPersistentData(persistentDataContainer ->
                                persistentDataContainer.set(new NamespacedKey(plugin, "exitWizard"),
                                        PersistentDataType.BOOLEAN, true))
                        .build();

                player.getInventory().setItem(0, guiItem);
                player.getInventory().setItem(3, centerXZItem);
                player.getInventory().setItem(5, centerYawPitchItem);
                player.getInventory().setItem(8, exitItem);

                MessageUtil.sendTitle("Created Session:", "you can now edit the positions.", 0, 20, 0, player);
            }, 20);

        } else {
            plugin.getLogger().info(Messages.ERROR_PLAYER_NOT_FOUND);
        }
    }

    public void onInteract(ItemStack itemStack) {
        Player player = Bukkit.getPlayer(playerUUID);

        if (player == null){
            return;
        }

        if (itemStack.getItemMeta() != null){

            if (itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "openGui"))){
                new WizardMenu(this);
            } else if (itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "exitWizard"))){
                new ConfirmExitMenu(player);
            } else if (itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "centerXZ"))){
                Location newLoc1 = player.getLocation();
                newLoc1.setX(newLoc1.getBlockX() + 0.5);
                newLoc1.setZ(newLoc1.getBlockZ() + 0.5);
                player.teleportAsync(newLoc1);
            } else if (itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "centerYawPitch"))){
                Location newLoc2 = player.getLocation();

                // Calculate the centered yaw angle
                float yaw = newLoc2.getYaw();
                float centeredYaw = (float) Math.round(yaw / 45) * 45;

                float pitch = newLoc2.getPitch();
                float centeredPitch = (pitch > 0) ? 0 : -90;

                newLoc2.setYaw(centeredYaw);
                newLoc2.setPitch(centeredPitch);
                player.teleportAsync(newLoc2);
            }
        }
    }

    public void delete(boolean save){
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null){
            return;
        }
        player.getInventory().clear();
        player.teleportAsync(plugin.getLobbyLocation());

        for(Hologram hologram : holograms){
            hologram.destroy();
        }

        WorldUtil.unloadWorld(gameData.getMap());

        if(save){
            gameData.setLastEdit(System.currentTimeMillis());
            gameData.setTeams(this.teams);
            try{
                plugin.getGameDataDatabase().saveData(gameData);
            } catch (IOException e) {
                plugin.getLogger().info(Messages.ERROR_IO);
            }
        }

        MessageUtil.sendTitle("Successfully ended session!", (save ? "&aSaved Map" : "&cDidnt save map"), 0, 20, 0, player);
    }

    public void addTeam(Team team) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        this.teams.put(team.getColor(), team);
        MessageUtil.sendTitle(team.getColor() + "Team Added!", "", 0, 20, 0, player);
    }

    public void removeTeam(Team team) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        this.teams.remove(team.getColor());
        MessageUtil.sendTitle(team.getColor() + "Team Removed!", "", 0, 20, 0, player);
    }

    public void setBuildAbleCornerOne(){
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        for (Hologram hologram : this.holograms) {
            if (hologram.getText().equalsIgnoreCase(color("&6Buildable Corner One"))){
                hologram.destroy();
                break;
            }
        }

        this.gameData.setBuildAbleCornerOne(player.getLocation());
        this.holograms.add(new Hologram(player.getLocation(), color("&6Buildable Corner One")));
        MessageUtil.sendTitle("Buildable Corner One Set!", "", 0, 20, 0, player);
    }

    public void setBuildAbleCornerTwo() {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        for (Hologram hologram : this.holograms) {
            if (hologram.getText().equalsIgnoreCase(color("&6Buildable Corner Two"))){
                hologram.destroy();
                break;
            }
        }

        this.gameData.setBuildAbleCornerTwo(player.getLocation());
        this.holograms.add(new Hologram(player.getLocation(), color("&6Buildable Corner Two")));
        MessageUtil.sendTitle("Buildable Corner Two Set!", "", 0, 20, 0, player);
    }

    public void setSpectatorSpawn(){
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        for (Hologram hologram : this.holograms) {
            if (hologram.getText().equalsIgnoreCase(color("&6Spectator"))){
                hologram.destroy();
                break;
            }
        }

        this.gameData.setSpectatorSpawn(player.getLocation());
        this.holograms.add(new Hologram(player.getLocation(), color("&6Spectator")));
        MessageUtil.sendTitle("Spectator Spawn Set!", "", 0, 20, 0, player);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    public Map<ChatColor, Team> getTeams() {
        return teams;
    }
}