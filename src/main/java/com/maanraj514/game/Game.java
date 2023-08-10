package com.maanraj514.game;

import com.maanraj514.BridgePlugin;
import com.maanraj514.game.state.*;
import com.maanraj514.game.state.impl.*;
import com.maanraj514.model.GameData;
import com.maanraj514.model.Team;
import com.maanraj514.party.Party;
import com.maanraj514.util.Messages;
import com.maanraj514.util.WorldUtil;
import com.maanraj514.utils.ItemBuilder;
import com.maanraj514.utils.LocationUtil;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.*;

import static com.maanraj514.utils.ColorUtil.color;

public class Game {

    private final BridgePlugin plugin;

    private final UUID uuid;
    private final GameMode gameMode;

    private final String map;

    private final Map<UUID, Team> players;
    private final Map<Team, Set<UUID>> teams;
    private final Set<UUID> spectators;
    private final Map<Team, Integer> score;

    private final Map<UUID, FastBoard> scoreboards;

    private final GameData gameData;

    private World world;

    private GameState state = GameState.NONE;
    private final Map<GameState, GameStateImpl> states;
    private final List<BukkitTask> tasks;

    private Team winnerTeam;
    private int maxGoals;

    // never use this outside this class.
    private final Map<ChatColor, Material> colorToMaterialMap;

    public Game(String map, UUID uuid, BridgePlugin plugin){
        this.plugin = plugin;
        this.map = map;
        this.uuid = uuid;
        this.players = new HashMap<>();
        this.teams = new HashMap<>();
        this.spectators = new HashSet<>();
        this.scoreboards = new HashMap<>();
        this.tasks = new ArrayList<>();
        this.score = new HashMap<>();
        this.states = new HashMap<>();
        this.colorToMaterialMap = new HashMap<>();

        this.colorToMaterialMap.put(ChatColor.RED, Material.RED_CONCRETE);
        this.colorToMaterialMap.put(ChatColor.WHITE, Material.WHITE_CONCRETE);
        this.colorToMaterialMap.put(ChatColor.BLUE, Material.BLUE_CONCRETE);
        this.colorToMaterialMap.put(ChatColor.YELLOW, Material.YELLOW_CONCRETE);
        this.colorToMaterialMap.put(ChatColor.GRAY, Material.LIGHT_GRAY_CONCRETE);
        this.colorToMaterialMap.put(ChatColor.DARK_GREEN, Material.GREEN_CONCRETE);
        this.colorToMaterialMap.put(ChatColor.DARK_AQUA, Material.CYAN_CONCRETE);
        this.colorToMaterialMap.put(ChatColor.DARK_PURPLE, Material.PURPLE_CONCRETE);
        this.colorToMaterialMap.put(ChatColor.LIGHT_PURPLE, Material.PINK_CONCRETE);
        this.colorToMaterialMap.put(ChatColor.BLACK, Material.BLACK_CONCRETE);
        this.colorToMaterialMap.put(ChatColor.GREEN, Material.LIME_CONCRETE);
        this.colorToMaterialMap.put(ChatColor.GOLD, Material.ORANGE_CONCRETE);
        this.colorToMaterialMap.put(ChatColor.AQUA, Material.LIGHT_BLUE_CONCRETE);
        this.colorToMaterialMap.put(ChatColor.DARK_GRAY, Material.GRAY_CONCRETE);

        this.states.put(GameState.NONE, new NoneState());
        this.states.put(GameState.LOADING, new LoadingState());
        this.states.put(GameState.WAITING, new WaitingState());
        this.states.put(GameState.STARTING, new StartingState());
        this.states.put(GameState.ROUND_STARTING, new RoundStartingState());
        this.states.put(GameState.ROUND_PLAYING, new RoundPlayingState());
        this.states.put(GameState.ENDED, new EndedState());

        this.tasks.add(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (FastBoard board : this.scoreboards.values()){
                updateBoard(board);
            }
        },0L, 10L));

        this.gameData = plugin.getGameDataDatabase().getData(this.map);
        this.gameMode = this.gameData.getGameMode();
        this.maxGoals = this.gameData.getMaxGoals();

        WorldUtil.loadGameWorld(this, plugin);
    }

    public void addPlayer(Player player){
        if (state == GameState.LOADING){
            player.sendMessage(Messages.ERROR_GAME_STATE_LOADING);
            Bukkit.getScheduler().runTaskLater(plugin, () -> this.addPlayer(player), 40L);
            return;
        }
        if (state == GameState.ENDED){
            player.sendMessage(Messages.ERROR_GAME_STATE_ENDED);
            return;
        }
        if (state == GameState.ROUND_STARTING){
            player.sendMessage(Messages.ERROR_GAME_STATE_ROUND_STARTING);
            return;
        }
        if (this.players.size() == this.gameMode.getMaxPlayers()){
            player.sendMessage(Messages.ERROR_GAME_FULL);
            return;
        }
        plugin.getLogger().info("addPlayer method was called for " + player.getName());

        player.setHealth(20.0);
        player.getInventory().clear();
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        player.setFoodLevel(20);

        FastBoard board = new FastBoard(player);
        board.updateTitle(color("&b&lTHE BRIDGE"));

        this.scoreboards.put(player.getUniqueId(), board);

        for (Team team : this.gameData.getTeams()){
            // first we can check if the team already exists.
            if (this.teams.containsKey(team)){
                // this means the team exists.
                int teamSize = this.teams.get(team).size() + 1;
                if (teamSize < this.gameMode.getPlayersPerTeam()) {
                    // this means the team's size is still less than the gameMode's players per team.
                    // add them to the existing team.
                    this.teams.get(team).add(player.getUniqueId());
                    this.players.put(player.getUniqueId(), team);
                    Bukkit.getLogger().info(player.getName() + " got assigned to an existing team " + team.getColor());
                    break;
                } else if (teamSize == this.gameMode.getPlayersPerTeam()) {
                    // this means the team's size is equal to the gameMode's players per team.
                    // create a new team.
                    this.teams.put(team, new HashSet<>());
                    this.teams.get(team).add(player.getUniqueId());
                    this.players.put(player.getUniqueId(), team);
                    Bukkit.getLogger().info(player.getName() + " got assigned to new team because of existing team being full " + team.getColor());
                    break;
                }
            } else {
                // create a new team because one doesn't exist yet.
                this.teams.put(team, new HashSet<>());
                this.teams.get(team).add(player.getUniqueId());
                this.players.put(player.getUniqueId(), team);
                Bukkit.getLogger().info(player.getName() + " got assigned to a new team " + team.getColor());
                break;
            }
        }

        this.assignSpawnLocation(player);

        Team playerTeam = this.players.get(player.getUniqueId());

        ChatColor color = playerTeam.getColor();

        String displayName = color + "[" + color + playerTeam.getName() + color + "] " + color + player.getName();
        String prefix = color + "[" + color + playerTeam.getName() + color + "] ";

        ScoreboardManager sm = Bukkit.getScoreboardManager();
        Scoreboard s = sm.getNewScoreboard();

        Objective objective = s.registerNewObjective("showhealth", Criterias.HEALTH);

        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);

        objective.setDisplayName(ChatColor.DARK_RED + "❤");

        setNick(player, prefix, playerTeam);

        player.setScoreboard(s);

        player.setDisplayName(displayName);
        player.setPlayerListName(displayName);

        if (this.players.size() == this.gameMode.getMinPlayers()){
            setState(GameState.STARTING);
        }

        broadcast(color("&a" + player.getName() + " has joined the game! (" + players.size() + "/" + gameMode.getMaxPlayers() + ")"));
    }

    public void addParty(Party party){
        // for now we just use addPlayer()
        if (party != null){
            if (!party.getOnlinePlayers().isEmpty()){
                for (UUID playerUUID : party.getOnlinePlayers()){
                    if (Bukkit.getPlayer(playerUUID) != null){
                        addPlayer(Bukkit.getPlayer(playerUUID));
                    }
                }
            }
        }
        //TODO
    }

    public void addSpectator(Player player){
        this.spectators.add(player.getUniqueId());
        player.getInventory().clear();
        player.setGameMode(org.bukkit.GameMode.SPECTATOR);
        Location location = new Location(world, gameData.getSpectatorSpawn().getX(),
                gameData.getSpectatorSpawn().getY(),
                gameData.getSpectatorSpawn().getZ(),
                gameData.getSpectatorSpawn().getYaw(),
                gameData.getSpectatorSpawn().getPitch());

        player.teleportAsync(location);
    }

    public void removePlayer(Player player){
        this.players.remove(player.getUniqueId());
        this.scoreboards.remove(player.getUniqueId()).delete();

        Team team = this.players.get(player.getUniqueId());
        if (team != null){
            if (this.teams.get(team) != null){
                this.teams.get(team).remove(player.getUniqueId());
                if (this.teams.get(team).isEmpty()){
                    this.teams.remove(team);
                }
            }
        }

        this.spectators.remove(player.getUniqueId());

        player.getInventory().clear();
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        player.teleportAsync(plugin.getLobbyLocation());

        if (state == GameState.STARTING || state == GameState.ROUND_PLAYING || state == GameState.ROUND_STARTING){
            broadcast(color("&a" + player.getName() + " has left the game! (" + players.size() + "/" + gameMode.getMaxPlayers() + ")"));
        }

        if (state == GameState.STARTING && this.players.size() < gameMode.getMinPlayers()){
            setState(GameState.WAITING);
        }

        if (state == GameState.ROUND_PLAYING){
            // if everyone else left, end the game
            if (teams.size() == 1){
                this.winnerTeam = team;
                setState(GameState.ENDED);
            }
        }
    }

    private void assignSpawnLocation(Player player){
        Team team = this.players.get(player.getUniqueId());
        if (team == null){
            player.sendMessage(Messages.ERROR_PLAYER_TEAM_NOT_FOUND);
            return;
        }
        Location spawnLocation = new Location(world, team.getSpawnLocation().getX(), team.getSpawnLocation().getY(), team.getSpawnLocation().getZ(), team.getSpawnLocation().getYaw(), team.getSpawnLocation().getPitch());
        player.teleportAsync(spawnLocation);
    }

    public void setState(GameState state) {
        if(this.state == state) return;
        this.states.get(this.state).post(this);
        this.state = state;
        this.states.get(this.state).pre(this, plugin);
    }

    public void broadcast(String message){
        for (UUID playerUUID : this.players.keySet()) {
            if (Bukkit.getPlayer(playerUUID) != null){
                Bukkit.getPlayer(playerUUID).sendMessage(message);
            }
        }
    }

    public void delete(){
        for (FastBoard board : this.scoreboards.values()){
            board.delete();
        }
        for (BukkitTask task : this.tasks){
            task.cancel();
        }
    }

    public void updateBoard(FastBoard board){
        board.updateLines(getLines(board.getPlayer()));
    }

    public List<String> getLines(Player player){
        List<String> lines = new ArrayList<>();
        Team team = this.players.get(player.getUniqueId());
        if (team == null){
            return lines;
        }
        if (state == GameState.ROUND_PLAYING || state == GameState.ROUND_STARTING) {
            lines.add("");
            for (Team teams : this.teams.keySet()) {
                int teamScore = score.get(teams);
                ChatColor color = teams.getColor();
                StringBuilder scoreLine = new StringBuilder(color + "[" + color + teams.getName() + color + "] ");
                for (int i = 0; i < maxGoals; i++) {
                    if (i < teamScore) {
                        scoreLine.append(color).append("★");
                    } else {
                        scoreLine.append("&7☆");
                    }
                }
                lines.add(color(scoreLine.toString()));
            }
        }
        lines.add("");
        lines.add(color(team.getColor()
                + "You are in " + team.getName()));
        lines.add("");
        lines.add("Mode: " + gameMode.name());
        lines.add("");
        lines.add(color("&ejovanpls.h4ck.me"));
        return lines;
    }

    public void resetPlayer(Player player) {
        if (this.players.containsKey(player.getUniqueId())) {
            this.assignSpawnLocation(player);
            player.setHealth(20);
            this.addKit(player);
        }
    }

    private void addKit(Player player) {
        if (this.players.containsKey(player.getUniqueId())) {
            Team team = this.players.get(player.getUniqueId());
            if (team != null) {
                plugin.getLogger().info("addKit method: " + team.getColor() + " " + team.getName());

                player.getInventory().clear();

                player.getInventory().setItem(0, new ItemBuilder(Material.IRON_SWORD)
                        .setName(color("&b&lSword"))
                        .build());
                player.getInventory().setItem(1, new ItemBuilder(Material.BOW)
                        .setName(color("&b&lBow"))
                        .build());

                ItemStack blocks = new ItemBuilder(this.colorToMaterialMap.get(team.getColor())).build();

                blocks.setAmount(64);

                ItemStack arrows = new ItemBuilder(Material.ARROW).build();
                arrows.setAmount(1);

                ItemStack pickaxe = new ItemBuilder(Material.DIAMOND_PICKAXE)
                        .addEnchant(Enchantment.DIG_SPEED, 2, true)
                        .build();
                ItemStack gaps = new ItemBuilder(Material.GOLDEN_APPLE)
                        .build();
                gaps.setAmount(6);

                ItemStack chestPlate = new ItemBuilder(Material.LEATHER_CHESTPLATE)
                        .setLeatherColor(team.getArmorColor())
                                .build();
                ItemStack leggings = new ItemBuilder(Material.LEATHER_LEGGINGS)
                        .setLeatherColor(team.getArmorColor())
                        .build();
                ItemStack boots = new ItemBuilder(Material.LEATHER_BOOTS)
                        .setLeatherColor(team.getArmorColor())
                        .build();

                player.getEquipment().setChestplate(chestPlate);
                player.getEquipment().setLeggings(leggings);
                player.getEquipment().setBoots(boots);

                player.getInventory().setItem(2, blocks);
                player.getInventory().setItem(3, blocks);
                player.getInventory().setItem(4, pickaxe);
                player.getInventory().setItem(5, gaps);
                player.getInventory().setItem(8, arrows);
            }
        }
    }

    private void setNick(Player player, String nick, Team tream) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.canSee(player)) {
                continue;
            }
            Scoreboard scoreboard = online.getScoreboard();
            if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            }
            org.bukkit.scoreboard.Team team = scoreboard.getTeam(player.getName());
            if (team == null) {
                team = scoreboard.registerNewTeam(player.getName());
            }
            team.setPrefix(nick);
            team.setSuffix("");
            team.setColor(tream.getColor());
            team.addEntry(player.getName());
            online.setScoreboard(scoreboard);
        }
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.canSee(player)) {
                continue;
            }
            if (online.getUniqueId().equals(player.getUniqueId()))
                continue;
            Scoreboard scoreboard = player.getScoreboard();
            if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            }
            org.bukkit.scoreboard.Team team = scoreboard.getTeam(online.getName());
            if (team == null) {
                team = scoreboard.registerNewTeam(online.getName());
            }

            team.setPrefix(player.getScoreboard().getTeam(online.getName()).getPrefix());
            team.setSuffix("");
            team.setColor(tream.getColor());
            team.addEntry(online.getName());
            player.setScoreboard(scoreboard);
        }
    }

    public Map<Team, Set<UUID>> getTeams() {
        return teams;
    }

    public Map<UUID, Team> getPlayers() {
        return players;
    }

    public String getMap() {
        return map;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Team getWinnerTeam() {
        return winnerTeam;
    }

    public void setWinnerTeam(Team winnerTeam) {
        this.winnerTeam = winnerTeam;
    }

    public Map<Team, Integer> getScore() {
        return score;
    }

    public GameData getGameData() {
        return gameData;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public World getWorld() {
        return world;
    }

    public int getMaxGoals() {
        return maxGoals;
    }

    public GameState getState() {
        return state;
    }
}