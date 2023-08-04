package com.maanraj514.setupwizard;

import com.maanraj514.BridgePlugin;
import com.maanraj514.game.GameMode;
import com.maanraj514.model.GameData;
import com.maanraj514.model.Team;
import com.maanraj514.util.Messages;
import com.maanraj514.util.WorldUtil;
import com.maanraj514.utils.ColorUtil;
import com.maanraj514.utils.Hologram;
import com.maanraj514.utils.ItemBuilder;
import com.maanraj514.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Session {

    private final BridgePlugin plugin;

    private final UUID playerUUID;

    private GameData gameData;

    private final GameMode gameMode;

    private final List<Hologram> holograms;

    public Session(UUID playerUUID, String map, GameMode gameMode, BridgePlugin plugin) {
        this.plugin = plugin;
        this.playerUUID = playerUUID;
        this.gameMode = gameMode;
        this.holograms = new ArrayList<>();

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
                    new ArrayList<>(),
                    null,
                    null,
                    null
            );

            player.getInventory().clear();

            if (!WorldUtil.worldExists(map, plugin)){
                player.sendMessage(Messages.ERROR_WORLD_NOT_FOUND);
                plugin.getWizardManager().endSession(player);
                return;
            }
            WorldUtil.loadWorld(map, plugin);

            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                player.setGameMode(org.bukkit.GameMode.CREATIVE);
                player.teleportAsync(new Location(Bukkit.getWorld(map), 0, 200, 0));

                ItemStack guiItem = new ItemBuilder(Material.COMPASS).setName("&6Open Gui").build();
                ItemStack centerXZItem = new ItemBuilder(Material.ARROW).setName("&6Center X-Z").build();
                ItemStack centerYawPitchItem = new ItemBuilder(Material.ARROW).setName("&6Center Yaw-Pitch").build();
                ItemStack exitItem = new ItemBuilder(Material.BARRIER).setName("&cExit Wizard").build();

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
        switch (itemStack.getType()){
            case COMPASS:
                new WizardGui(this);
                break;
            case BARRIER:
                new ConfirmExitGui(player);
                break;
            case ARROW:
                if(itemStack.hasItemMeta()){
                    switch (ChatColor.stripColor(itemStack.getItemMeta().getDisplayName())){
                        case "Center X-Z":
                            Location newLoc1 = player.getLocation();
                            newLoc1.setX(newLoc1.getBlockX() + 0.5);
                            newLoc1.setZ(newLoc1.getBlockZ() + 0.5);
                            player.teleportAsync(newLoc1);
                            break;
                        case "Center Yaw-Pitch":
                            Location newLoc2 = player.getLocation();
                            newLoc2.setYaw(180);
                            newLoc2.setPitch(180);
                            player.teleportAsync(newLoc2);
                            // TODO because idk how yet
                            break;
                    }
                }
                break;
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
            try{
                plugin.getGameDataDatabase().saveData(gameData);
            } catch (IOException e) {
                plugin.getLogger().info(Messages.ERROR_IO);
            }
        }

        MessageUtil.sendTitle("Successfully ended session!", (save ? "&aSaved Map" : "&cDidnt save map"), 0, 20, 0, player);
    }

    public void addTeam(Team team) {
    }

    public void removeTeam(Team team) {
    }

    public void setBuildAbleCornerOne(){
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        this.gameData.setBuildAbleCornerOne(player.getLocation());
        this.holograms.add(new Hologram(player.getLocation(), ColorUtil.color("&6Buildable Corner One")));
        MessageUtil.sendTitle("Buildable Corner One Set!", "", 0, 20, 0, player);
    }

    public void setBuildAbleCornerTwo() {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        this.gameData.setBuildAbleCornerTwo(player.getLocation());
        this.holograms.add(new Hologram(player.getLocation(), ColorUtil.color("&6Buildable Corner Two")));
        MessageUtil.sendTitle("Buildable Corner Two Set!", "", 0, 20, 0, player);
    }

    public void setSpectatorSpawn(){
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return;

        this.gameData.setSpectatorSpawn(player.getLocation());
        this.holograms.add(new Hologram(player.getLocation(), ColorUtil.color("&6Spectator")));
        MessageUtil.sendTitle("Spectator Spawn Set!", "", 0, 20, 0, player);
    }
}