package com.maanraj514.command.commands;

import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.maanraj514.BridgePlugin;
import com.maanraj514.command.CommandInfo;
import com.maanraj514.command.PluginCommand;
import com.maanraj514.game.Game;
import com.maanraj514.game.GameMode;
import com.maanraj514.util.WorldUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.maanraj514.utils.ColorUtil.color;

@CommandInfo(name = "setup", permission = "thebridge.command.setup", requiresPlayer = true)
public class GameSetupCommand extends PluginCommand {

    public GameSetupCommand(BridgePlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(Player player, String[] args) {
        // /setup <MapName> <GameMode> = args.length == 2;
        if (args.length == 2) {

            if (plugin.getWizardManager().hasSession(player.getUniqueId())) {
                player.sendMessage(color("&cYou are already in a setup session!"));
                return;
            }

            if (!WorldUtil.worldExists(args[0], plugin)) {
                player.sendMessage(color("&cThat world doesnt exist! make sure to import it into slime world manager!"));
                return;
            }

            Game gameToSetup = plugin.getGameManager().getGame(args[0]);

            if (gameToSetup != null){
                player.sendMessage(color("&cThat game is already exists!"));
                return;
            }

            GameMode gameMode = GameMode.valueOf(args[1].toUpperCase());

            if (gameMode == null){
                player.sendMessage(color("&cThat gameMode doesn't exist! if you think this is an error, contact developers!"));
                return;
            }

            plugin.getWizardManager().createSession(player, args[0], gameMode);
        } else {
            player.sendMessage(color("&cCorrect usage is, /setup <mapName> <gameMode>"));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> arguments = new ArrayList<>();

        if (args.length == 1) {

            for (SlimeWorld slimeWorld : plugin.getSlimePlugin().getLoadedWorlds()) {
                arguments.add(slimeWorld.getName());
            }

            List<String> result = new ArrayList<>();

            for (String str : arguments) {
                if (str.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(str);
                }
            }
            return result;

        } else if (args.length == 2) {

            for (GameMode gameMode : GameMode.values()) {
                arguments.add(gameMode.name());
            }
            List<String> result = new ArrayList<>();

            for (String str : arguments) {
                if (str.toLowerCase().startsWith(args[1].toLowerCase())) {
                    result.add(str);
                }
            }

            return result;
        } else {
            return null;
        }
    }
}