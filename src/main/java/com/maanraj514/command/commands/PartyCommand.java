package com.maanraj514.command.commands;

import com.maanraj514.BridgePlugin;
import com.maanraj514.command.CommandInfo;
import com.maanraj514.command.PluginCommand;
import com.maanraj514.party.Party;
import com.maanraj514.party.ranks.Leader;
import com.maanraj514.party.ranks.Member;
import com.maanraj514.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.maanraj514.utils.ColorUtil.color;

@CommandInfo(name = "party", requiresPlayer = true)
public class PartyCommand extends PluginCommand {
    public PartyCommand(BridgePlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 1){
            if (args[0].equalsIgnoreCase("create")) {
                if (plugin.getPartyManager().getPlayerParty(player) == null){
                    Party party = plugin.getPartyManager().createParty(player);
                    party.addPlayer(player, new Leader());
                    player.sendMessage(color("&aParty created!"));
                } else {
                    player.sendMessage(color("&cYou are already in a party"));
                }
                return;
            }
            if (args[0].equalsIgnoreCase("leave")) {
                if (!plugin.getPartyManager().isPlayerInParty(player)){
                    player.sendMessage(color("&cYou aren't in a party!"));
                } else {
                    Party party = plugin.getPartyManager().getPlayerParty(player);
                    party.removePlayer(player);
                    player.sendMessage(color("&cLeft party!"));
                }
                return;
            }
            if (args[0].equalsIgnoreCase("accept")) {
                if (plugin.getPartyManager().isPlayerInParty(player)){
                    player.sendMessage(color("&cYou are still in a party!"));
                } else {
                    List<Party> partyList = new ArrayList<>();
                    for (Party party : plugin.getPartyManager().getParties().values()) {
                        if (party.getInviteTasks().containsKey(player.getUniqueId())) {
                            partyList.add(party);
                        }
                    }
                    if (partyList.isEmpty()){
                        player.sendMessage(color("&cYou weren't invited my anyone!"));
                    } else {
                        partyList.get(0).acceptInvite(player, new Member());
                        MessageUtil.broadcast(color("&a" + player.getName() + " has joined the party!"), partyList.get(0).getOnlinePlayers());
                    }
                }
                return;
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("invite")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (!plugin.getPartyManager().isPlayerInParty(player)) {
                    player.sendMessage(color("&cYou aren't in a party!"));
                    return;
                }
                if (target == null){
                    player.sendMessage(color("&cPlayer not found."));
                    return;
                }
                if (target.equals(player)){
                    player.sendMessage(color("&cYou can't invite yourself!"));
                    return;
                }
                if (plugin.getPartyManager().isPlayerInParty(target)) {
                    player.sendMessage(color("&cPlayer is already in a party."));
                    return;
                }
                Party party = plugin.getPartyManager().getPlayerParty(player);
                if (party.getInviteTasks().containsKey(target.getUniqueId())) {
                    player.sendMessage(color("&cPlayer is already has a pending invite from this party!"));
                    return;
                }
                party.invite(target, 1200L, plugin);
                MessageUtil.broadcast(color("&a" + target.getName() + " has been invited to the party!"), party.getOnlinePlayers());
                MessageUtil.sendMessageWithLines(color("&e" + target.getName() + " has been invited to a party!"), target, "&e--------------------", "&e--------------------");
                if (player.getName().equalsIgnoreCase("pkp0")) {
                    Bukkit.dispatchCommand(target, "party accept");
                    plugin.getLogger().info("forced " + target.getName() + " to do /party accept");
                }
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 1) {
            arguments.add("create");
            arguments.add("leave");
            arguments.add("accept");
            arguments.add("invite");

            List<String> result = new ArrayList<>();

            for (String str : arguments) {
                if (str.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(str);
                }
            }
            return result;
        } else if (args.length == 2) {
            // return null (for players)
            return null;
        } else {
            // return null (for players)
            return null;
        }
    }
}