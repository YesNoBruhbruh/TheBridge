package com.maanraj514.game.state.impl.listeners;

import com.maanraj514.BridgePlugin;
import com.maanraj514.game.Game;
import com.maanraj514.game.state.GameState;
import com.maanraj514.game.state.GameStateListener;
import com.maanraj514.game.state.impl.RoundPlayingState;
import com.maanraj514.model.Pos;
import com.maanraj514.model.Team;
import com.maanraj514.utils.ItemBuilder;
import com.maanraj514.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.*;

import static com.maanraj514.utils.ColorUtil.color;

public class InGameListener extends GameStateListener {

    private final BridgePlugin plugin;

    private final RoundPlayingState state;
    private final Game game;

    private final Map<UUID, UUID> lastDamager;
    private final Map<UUID, BukkitTask> lastDamageTasks;
    private final Map<UUID, BukkitTask> arrowTasks;

    public InGameListener(Game game, RoundPlayingState state, BridgePlugin plugin){
        this.state = state;
        this.game = game;
        this.plugin = plugin;
        this.lastDamager = new HashMap<>();
        this.lastDamageTasks = new HashMap<>();
        this.arrowTasks = new HashMap<>();
    }

    //TODO

    @EventHandler
    private void onDamage(EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.getEntity() instanceof Player) {
            Player player = (Player) entityDamageEvent.getEntity();
            if (game.getPlayers().containsKey(player.getUniqueId())) {
                Team playerTeam = game.getPlayers().get(player.getUniqueId());
                if (entityDamageEvent instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) entityDamageEvent;
                    if (entityDamageByEntityEvent.getDamager() instanceof Player) {
                        Player damager = (Player) entityDamageByEntityEvent.getDamager();
                        if (game.getPlayers().containsKey(damager.getUniqueId())) {
                            // friendly fire will not be tolerated.
                            Team damagerTeam = game.getPlayers().get(damager.getUniqueId());

                            if (playerTeam == damagerTeam){
                                entityDamageEvent.setCancelled(true);
                                return;
                            }
                            lastDamager.put(player.getUniqueId(), damager.getUniqueId());
                            if (lastDamageTasks.containsKey(player.getUniqueId())) {
                                lastDamageTasks.get(player.getUniqueId()).cancel();
                                lastDamageTasks.remove(player.getUniqueId());
                            }
                            lastDamageTasks.put(player.getUniqueId(), Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                lastDamageTasks.remove(player.getUniqueId());
                                lastDamager.remove(player.getUniqueId());
                            }, 1200L));
                        }
                    }
                    if (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FALL) {
                        entityDamageEvent.setCancelled(true);
                    }
                    if (entityDamageByEntityEvent.getDamager() instanceof Projectile){
                        Projectile projectile = (Projectile) entityDamageByEntityEvent.getDamager();
                        if (projectile.getOwnerUniqueId() != null){
                            Player shooter = Bukkit.getPlayer(projectile.getOwnerUniqueId());
                            if (shooter != null && entityDamageByEntityEvent.getFinalDamage() <= player.getHealth()) {
                                double health = player.getHealth() - entityDamageByEntityEvent.getFinalDamage();
                                DecimalFormat formatter = new DecimalFormat("#.#");
                                shooter.sendMessage(color(playerTeam.getColor() + player.getName() + " &7is on &c" + formatter.format(health) + " &7HP!"));
                            }
                        }
                    }
                    if (entityDamageByEntityEvent.getFinalDamage() >= player.getHealth()) {

                        if (entityDamageByEntityEvent.getDamager() instanceof Player) {
                            Player killer = (Player) entityDamageByEntityEvent.getDamager();

                            game.broadcast(color("&c" + player.getName() + " has been killed by " + killer.getName() + "!"));

                            game.resetPlayer(player);
                        } else if (entityDamageByEntityEvent.getDamager() instanceof Projectile){
                            game.broadcast(color("&c" + player.getName() + " has been shot and killed by a projectile!"));

                            game.resetPlayer(player);
                        }
                        entityDamageByEntityEvent.setCancelled(true);
                    }
                }

//                if (entityDamageEvent.getFinalDamage() >= player.getHealth()) {
//                    // just use this if necessary
//                    switch (entityDamageEvent.getCause()) {
//                        case FALL:
////                            if (lastDamager.containsKey(player.getUniqueId())) {
////                                OfflinePlayer damager = Bukkit.getOfflinePlayer(lastDamager.get(player.getUniqueId()));
////
////                                game.broadcast(color("&c" + player.getName() + " &rwas pushed off by &c" + damager.getName() + "!"));
////                            } else {
////                                game.broadcast(color("&c" + player.getName() + " &rdied due to fall damage!"));
////                            }
//                            break;
//                    }
//                }
            }
        }
    }

    @EventHandler
    private void onScore(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        if (game.getPlayers().containsKey(player.getUniqueId()) && event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            Location from = event.getFrom();
            from.setX(from.getBlockX() + 0.5);
            from.setZ(from.getBlockZ() + 0.5);

            Team playerTeam = game.getPlayers().get(player.getUniqueId());

            event.setCancelled(true);

            for (Team team : game.getTeams().keySet()){
                List<Location> portalBlocks = new ArrayList<>();

                for (Pos pos : LocationUtil.posFromTwoPoints(team.getPortalLocationOne(), team.getPortalLocationTwo())) {
                    Location location1 = LocationUtil.posToLocation(pos, game.getWorld());
                    location1.setX(location1.getBlockX() + 0.5);
                    location1.setZ(location1.getBlockZ() + 0.5);
                    portalBlocks.add(location1);
                }

                for (Location portalBlockLocation : portalBlocks) {

                    if (LocationUtil.coordinatesMatch(from, portalBlockLocation)) {

                        if (!game.getTeams().get(team).contains(player.getUniqueId())){

                            game.getScore().put(playerTeam, game.getScore().get(playerTeam) + 1);

                            game.broadcast(color("&6" + player.getName() + " has scored a point! "  + "(" + game.getScore().get(playerTeam) + "/" + game.getMaxGoals() + ")"));
                            if (game.getScore().get(playerTeam) == game.getMaxGoals()){
                                game.setWinnerTeam(playerTeam);
                                game.setState(GameState.ENDED);
                            } else {
                                game.setState(GameState.ROUND_STARTING);
                            }
                        } else {
                            game.resetPlayer(player);
                        }
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    private void onShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (!game.getPlayers().containsKey(player.getUniqueId())) return;

        if (arrowTasks.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            return;
        }

        int duration = 5;
        BukkitTask task = new BukkitRunnable() {
            int countdown = 5;
            @Override
            public void run() {
                if (player.isDead() || !game.getPlayers().containsKey(player.getUniqueId()) || game.getState() != GameState.ROUND_PLAYING) {
                    player.setExp(0F);
                    player.setLevel(0);
                    cancel();
                }

                if (countdown <= 0) {
                    player.setExp(0F);
                    player.setLevel(0);
                    arrowTasks.remove(player.getUniqueId());
                    player.getInventory().addItem(new ItemBuilder(Material.ARROW).build());
                    cancel();
                }
                player.setExp(((float) countdown / duration));
                player.setLevel(countdown);
                countdown--;
            }
        }.runTaskTimer(plugin, 0L, 20L);

        arrowTasks.put(player.getUniqueId(), task);
    }

    @EventHandler
    private void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (game.getPlayers().containsKey(player.getUniqueId())) {
            List<Pos> buildAbleLocations =
                    new ArrayList<>(LocationUtil.posFromTwoPoints(
                            game.getGameData().getBuildAbleCornerOne(),
                            game.getGameData().getBuildAbleCornerTwo()));

            Location blockLocation = event.getBlock().getLocation();

            Pos pos = LocationUtil.locationToPos(blockLocation);

            boolean isPossible = false;

            for (Pos buildAblePos : buildAbleLocations) {
                if (LocationUtil.coordinatesMatch(pos, buildAblePos)) {
                    isPossible = true;
                    break;
                }
            }

            if (!isPossible){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (game.getPlayers().containsKey(player.getUniqueId())) {
            List<Pos> buildAbleLocations =
                    new ArrayList<>(LocationUtil.posFromTwoPoints(
                            game.getGameData().getBuildAbleCornerOne(),
                            game.getGameData().getBuildAbleCornerTwo()));

            Location blockLocation = event.getBlock().getLocation();

            Pos pos = LocationUtil.locationToPos(blockLocation);

            boolean isPossible = false;

            for (Pos buildAblePos : buildAbleLocations) {
                if (LocationUtil.coordinatesMatch(pos, buildAblePos)) {
                    isPossible = true;
                    break;
                }
            }

            if (!isPossible){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onConsume(PlayerItemConsumeEvent event) {
        ItemStack itemStack = event.getItem();
        Player player = event.getPlayer();
        if (!game.getPlayers().containsKey(player.getUniqueId())){
            return;
        }
        if (itemStack.getType() == Material.GOLDEN_APPLE) {
            player.setHealth(20);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                        player.removePotionEffect(PotionEffectType.REGENERATION);
                    }
                }
            }.runTaskLater(plugin, 10L);
        }
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (game.getPlayers().containsKey(player.getUniqueId())){
            game.removePlayer(player);
        }
    }

    @EventHandler
    private void onFoodChange(FoodLevelChangeEvent event){
        if(game.getPlayers().containsKey(event.getEntity().getUniqueId())){
            event.setFoodLevel(20);
        }
    }

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent event) {
        if(game.getPlayers().containsKey(event.getPlayer().getUniqueId())){
            event.setCancelled(true);
        }
    }
}