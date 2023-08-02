package com.maanraj514.game;

import com.maanraj514.BridgePlugin;
import com.maanraj514.game.states.*;
import com.maanraj514.object.MapData;
import com.maanraj514.object.Team;
import com.maanraj514.party.Party;
import com.maanraj514.utils.Messages;
import com.maanraj514.utils.WorldUtil;
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

    private final Map<UUID, FastBoard> scoreboards;

    private final MapData mapData;

    private World world;

    private GameState gameState;
    private final List<BukkitTask> tasks;

    private Team winnerTeam;

    public Game(String map, GameMode gameMode, UUID uuid, BridgePlugin plugin){
        this.plugin = plugin;
        this.map = map;
        this.gameMode = gameMode;
        this.uuid = uuid;
        this.players = new HashMap<>();
        this.teams = new HashMap<>();
        this.spectators = new HashSet<>();
        this.scoreboards = new HashMap<>();
        this.tasks = new ArrayList<>();

        this.tasks.add(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (FastBoard board : this.scoreboards.values()){
                updateBoard(board);
            }
        },0, 10));

        this.mapData = plugin.getMapDatabase().getMapData(this.map, plugin);

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
        if (this.players.size() == this.gameMode.getMaxPlayers()){
            player.sendMessage(Messages.ERROR_GAME_FULL);
            return;
        }

        player.getInventory().clear();
        player.setGameMode(org.bukkit.GameMode.ADVENTURE);

        FastBoard board = new FastBoard(player);
        board.updateTitle("&b&lTHE BRIDGE");

        this.scoreboards.put(player.getUniqueId(), board);

        //TODO do the algorithm for checking add the player to the teams

        this.assignSpawnLocation(player);

        if (this.players.size() == this.gameMode.getMinPlayers()){
            setState(new LoadingState());
        }

        broadcast(color("&a" + player.getName() + " has joined the game! (" + players.size() + "/" + gameMode.getMaxPlayers() + ")"));
    }

    public void addParty(Party party){
        //TODO
    }

    public void addSpectator(Player player){
        this.spectators.add(player.getUniqueId());
        player.getInventory().clear();
        player.setGameMode(org.bukkit.GameMode.SPECTATOR);
        player.teleportAsync(mapData.getSpectatorSpawn());
    }

    // used for removing someone from the game.

    // if we remove the player with the quit boolean to true, we remove them from the lists,
    // hashmaps, and teleport them to the lobby. If the quit boolean is false, we just
    // check for the winning team and set that.
    public void removePlayer(Player player, boolean quit){
        if (quit){
            this.players.remove(player.getUniqueId());
            this.scoreboards.remove(player.getUniqueId()).delete();
            this.teams.remove(this.players.get(player.getUniqueId()));
            this.spectators.remove(player.getUniqueId());

            player.getInventory().clear();
            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
            player.teleportAsync(plugin.getLobbyLocation());

            if (gameState instanceof StartingState || gameState instanceof PlayingState){
                broadcast(color("&a" + player.getName() + " has left the game! (" + players.size() + "/" + gameMode.getMaxPlayers() + ")"));
            }

            if (gameState instanceof StartingState && this.players.size() < gameMode.getMinPlayers()){
                setState(new WaitingState());
            }
        }
        // this means they just died and didn't quit.
        if (gameState instanceof PlayingState){
            PlayingState playingState = (PlayingState) gameState;
            playingState.getAliveTeams().remove(this.players.get(player.getUniqueId()));
            if (playingState.getAliveTeams().size() == 1){
                this.winnerTeam = playingState.getAliveTeams().keySet().stream().findFirst().orElse(null);
                setState(new EndedState());
            }
        }
    }

    public void assignSpawnLocation(Player player){
        if (this.players.get(player.getUniqueId()) == null){
            player.sendMessage(Messages.ERROR_GAME_PLAYER_NOT_FOUND);
            return;
        }
        Team team = this.players.get(player.getUniqueId());
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
        //TODO
        // check which state it is and put lines based on it.
        return lines;
    }

    public String getMap() {
        return map;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}