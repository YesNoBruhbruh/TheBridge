package com.maanraj514.game;

import com.maanraj514.BridgePlugin;
import com.maanraj514.game.states.*;
import com.maanraj514.model.GameData;
import com.maanraj514.model.Team;
import com.maanraj514.party.Party;
import com.maanraj514.util.Messages;
import com.maanraj514.util.WorldUtil;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

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

    private GameState gameState;
    private final List<BukkitTask> tasks;

    private Team winnerTeam;

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

        this.tasks.add(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (FastBoard board : this.scoreboards.values()){
                updateBoard(board);
            }
        },0, 20));

        this.gameData = plugin.getGameDataDatabase().getData(this.map);
        this.gameMode = this.gameData.getGameMode();

        WorldUtil.loadGameWorld(this, plugin);
    }

    public void addPlayer(Player player){
        if (gameState instanceof LoadingState){
            player.sendMessage(Messages.ERROR_GAME_STATE_LOADING);
            return;
        }
        if (gameState instanceof EndedState){
            player.sendMessage(Messages.ERROR_GAME_STATE_ENDED);
            return;
        }
        if (gameState instanceof RoundStartingState){
            player.sendMessage(Messages.ERROR_GAME_STATE_ROUND_STARTING);
            return;
        }
        if (this.players.size() == this.gameMode.getMaxPlayers()){
            player.sendMessage(Messages.ERROR_GAME_FULL);
            return;
        }

        player.setHealth(20.0);
        player.getInventory().clear();
        player.setGameMode(org.bukkit.GameMode.ADVENTURE);

        FastBoard board = new FastBoard(player);
        board.updateTitle("&b&lTHE BRIDGE");

        this.scoreboards.put(player.getUniqueId(), board);

        for (Team team : this.gameData.getTeams()){
            // first we can check if the team already exists.
            if (this.teams.containsKey(team)){
                // this means the team exists.
                if (this.teams.get(team).size() < this.gameMode.getPlayersPerTeam()) {
                    // this means the team's size is still less than the gameMode's players per team.
                    // add them to the existing team.
                    this.teams.get(team).add(player.getUniqueId());
                    this.players.put(player.getUniqueId(), team);
                    Bukkit.getPlayer(player.getName() + " got assigned to " + team.getColor());
                    break;
                } else if (this.teams.get(team).size() == this.gameMode.getPlayersPerTeam()) {
                    // this means the team's size is equal to the gameMode's players per team.
                    // create a new team.
                    this.teams.put(team, new HashSet<>());
                    this.teams.get(team).add(player.getUniqueId());
                    this.players.put(player.getUniqueId(), team);
                    Bukkit.getPlayer(player.getName() + " got assigned to " + team.getColor());
                    break;
                }
            } else {
                // create a new team because one doesn't exist yet.
                this.teams.put(team, new HashSet<>());
                this.teams.get(team).add(player.getUniqueId());
                this.players.put(player.getUniqueId(), team);
                Bukkit.getPlayer(player.getName() + " got assigned to " + team.getColor());
                break;
            }
        }

        this.assignSpawnLocation(player);

        if (this.players.size() == this.gameMode.getMinPlayers()){
            setState(new LoadingState());
        }

        broadcast(color("&a" + player.getName() + " has joined the game! (" + players.size() + "/" + gameMode.getMaxPlayers() + ")"));
    }

    public void addParty(Party party){
        // for now we just use addPlayer()
        for (UUID playerUUID : party.getOnlinePlayers()){
            if (Bukkit.getPlayer(playerUUID) != null){
                addPlayer(Bukkit.getPlayer(playerUUID));
            }
        }
        //TODO
    }

    public void addSpectator(Player player){
        this.spectators.add(player.getUniqueId());
        player.getInventory().clear();
        player.setGameMode(org.bukkit.GameMode.SPECTATOR);
        player.teleportAsync(gameData.getSpectatorSpawn());
    }

    public void removePlayer(Player player){
        this.players.remove(player.getUniqueId());
        this.scoreboards.remove(player.getUniqueId()).delete();

        Team team = this.players.get(player.getUniqueId());
        this.teams.get(team).remove(player.getUniqueId());

        if (this.teams.get(team).isEmpty()){
            this.teams.remove(team);
        }

        this.spectators.remove(player.getUniqueId());

        player.getInventory().clear();
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        player.teleportAsync(plugin.getLobbyLocation());

        if (gameState instanceof StartingState || gameState instanceof RoundPlayingState || gameState instanceof RoundStartingState){
            broadcast(color("&a" + player.getName() + " has left the game! (" + players.size() + "/" + gameMode.getMaxPlayers() + ")"));
        }

        if (gameState instanceof StartingState && this.players.size() < gameMode.getMinPlayers()){
            setState(new WaitingState());
        }

        if (gameState instanceof RoundPlayingState){
            // if everyone else left, end the game
            if (teams.size() == 1){
                this.winnerTeam = team;
                setState(new EndedState());
            }
        }
    }

    public void assignSpawnLocation(Player player){
        Team team = this.players.get(player.getUniqueId());
        if (team == null){
            player.sendMessage(Messages.ERROR_PLAYER_TEAM_NOT_FOUND);
            return;
        }
        player.teleportAsync(team.getSpawnLocation());
    }

    public void setState(GameState gameState) {
        if(this.gameState.getClass() == gameState.getClass()) return;
        this.gameState.onDisable();
        this.gameState = gameState;
        this.gameState.setGame(this);
        this.gameState.onEnable(plugin);
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
        board.updateLines(getLines());
    }

    public List<String> getLines(){
        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add(color("&a&lTest Line"));
        lines.add("");

        //TODO
        // check which state it is and put lines based on it.
        return lines;
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
}