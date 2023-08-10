package com.maanraj514.command;

import com.maanraj514.BridgePlugin;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.maanraj514.utils.ColorUtil.color;

public abstract class PluginCommand implements CommandExecutor, TabExecutor {
    protected BridgePlugin plugin;
    private final CommandInfo commandInfo;

    public PluginCommand(BridgePlugin plugin) {
        this.plugin = plugin;
        commandInfo = getClass().getDeclaredAnnotation(CommandInfo.class);
        Objects.requireNonNull(commandInfo, "Commands must have CommandInfo annotation");
    }

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, final String label, final String[] args) {
        if (!commandInfo.permission().isEmpty()){
            if (!sender.hasPermission(commandInfo.permission())){
                sender.sendMessage(color("&cYou do not have permission to execute this command!"));
                return true;
            }
        }

        if (commandInfo.requiresPlayer()){
            if (!(sender instanceof Player)){
                sender.sendMessage(color("&cYou must be a player to execute this command!"));
                return true;
            }

            execute((Player) sender, args);
            return true;
        }

        execute(sender, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return onTabComplete(commandSender, strings);
    }

    public void execute(Player player, String[] args) {}
    public void execute(CommandSender sender, String[] args) {}

    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}