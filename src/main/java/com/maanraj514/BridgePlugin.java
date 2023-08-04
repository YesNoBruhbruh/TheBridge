package com.maanraj514;

import com.infernalsuite.aswm.api.SlimePlugin;
import com.maanraj514.game.GameManager;
import com.maanraj514.database.GameDataDatabase;
import com.maanraj514.setupwizard.WizardManager;
import com.maanraj514.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.FileNotFoundException;
import java.io.IOException;

public final class BridgePlugin extends Okmeta {

    private final SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

    private GameDataDatabase gameDataDatabase;
    private WizardManager wizardManager;

    @Override
    public void enable() {
        saveDefaultConfig();

        if (this.slimePlugin == null){
            getLogger().info(Messages.ERROR_SLIME_PLUGIN_NULL);
            return;
        }

        registerClasses();

        try{
            gameDataDatabase.loadAllData();
        } catch (FileNotFoundException e) {
            getLogger().info(Messages.ERROR_FILE_NOT_FOUND);
        }
    }

    @Override
    public void disable() {
        try{
            gameDataDatabase.saveAllData();
        } catch (IOException e) {
            getLogger().info(Messages.ERROR_IO);
        }
    }

    public void registerClasses() {
        gameDataDatabase = new GameDataDatabase(this);
        wizardManager = new WizardManager();
        new GameManager(this);
    }

    public Location getLobbyLocation(){
        if (getConfig().getLocation("lobby-location") == null){
            return new Location(Bukkit.getWorlds().get(0), 0, 64, 0);
        }else{
            return getConfig().getLocation("lobby-location");
        }
    }

    public SlimePlugin getSlimePlugin() {
        return slimePlugin;
    }

    public GameDataDatabase getGameDataDatabase() {
        return gameDataDatabase;
    }

    public WizardManager getWizardManager() {
        return wizardManager;
    }
}