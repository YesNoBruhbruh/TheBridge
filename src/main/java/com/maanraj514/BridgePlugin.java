package com.maanraj514;

import com.infernalsuite.aswm.api.SlimePlugin;
import com.maanraj514.command.PluginCommand;
import com.maanraj514.game.GameManager;
import com.maanraj514.database.GameDataDatabase;
import com.maanraj514.party.PartyManager;
import com.maanraj514.setupwizard.WizardListener;
import com.maanraj514.setupwizard.WizardManager;
import com.maanraj514.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;

public final class BridgePlugin extends Okmeta {

    private final SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

    private GameManager gameManager;
    private GameDataDatabase gameDataDatabase;
    private WizardManager wizardManager;
    private PartyManager partyManager;

    @Override
    public void enable() {
        saveDefaultConfig();

        if (this.slimePlugin == null){
            getLogger().info(Messages.ERROR_SLIME_PLUGIN_NULL);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        registerCommands();
        registerListeners(new WizardListener(this));
        registerClasses();

        gameDataDatabase.loadAllData();
    }

    @Override
    public void disable() {
    }

    public void registerClasses() {
        gameDataDatabase = new GameDataDatabase(this);
        wizardManager = new WizardManager(this);
        gameManager = new GameManager(this);
        partyManager = new PartyManager();
    }

    public void registerCommands() {
        for (Class<? extends PluginCommand> clazz : new Reflections("com.maanraj514.command.commands")
                .getSubTypesOf(PluginCommand.class)){
            try {
                PluginCommand command = clazz.getDeclaredConstructor(this.getClass()).newInstance(this);
                getCommand(command.getCommandInfo().name()).setExecutor(command);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public Location getLobbyLocation(){
        if (getConfig().getLocation("lobby-location") == null){
            return new Location(Bukkit.getWorlds().get(0), 0, 128, 0);
        }else{
            return getConfig().getLocation("lobby-location");
        }
    }

    public GameDataDatabase getGameDataDatabase() {
        return gameDataDatabase;
    }

    public WizardManager getWizardManager() {
        return wizardManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public SlimePlugin getSlimePlugin() {
        return slimePlugin;
    }
}