package com.maanraj514.command.commands;

import com.maanraj514.BridgePlugin;
import com.maanraj514.command.CommandInfo;
import com.maanraj514.command.PluginCommand;
import com.maanraj514.game.Game;
import com.maanraj514.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.maanraj514.utils.ColorUtil.color;

@CommandInfo(name = "join", requiresPlayer = true)
public class GameJoinCommand extends PluginCommand {
    public GameJoinCommand(BridgePlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1){
            player.sendMessage(color("&cUsage: /join <map>"));
            return;
        }
        //TODO this is hard coded for NOW.
        if (plugin.getPartyManager().isPlayerInParty(player)) {
            Party party = plugin.getPartyManager().getPlayerParty(player);
            Game game = new Game("thebridgev2", UUID.randomUUID(), plugin);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                game.addParty(party);
            }, 20L);
        }
    }

    //TODO tab completer for all the games loaded
}