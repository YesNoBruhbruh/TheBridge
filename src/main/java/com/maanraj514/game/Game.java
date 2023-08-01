package com.maanraj514.game;

import com.maanraj514.BridgePlugin;
import com.maanraj514.object.MapData;
import com.maanraj514.object.Team;
import com.maanraj514.utils.WorldUtil;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

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
    private final List<BukkitTask> tasks;

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

        this.tasks.add(Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (FastBoard board : this.scoreboards.values()){
                for (UUID playerUUID : this.scoreboards.keySet()){
                    updateBoard(board, Bukkit.getPlayer(playerUUID));
                }
            }
        },0, 10));

        this.mapData = // load game's file.

        WorldUtil.loadGameWorld(this, plugin);
    }

    public void delete(){
        // this should delete all tasks and boards.
    }

    public String getMap() {
        return map;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
