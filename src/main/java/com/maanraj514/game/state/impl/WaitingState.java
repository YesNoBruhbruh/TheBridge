package com.maanraj514.game.state.impl;

import com.maanraj514.game.Game;
import com.maanraj514.game.state.GameStateImpl;
import com.maanraj514.game.state.GameStateListener;
import com.maanraj514.game.state.impl.listeners.PreGameListener;

public class WaitingState extends GameStateImpl {
    @Override
    public void onEnable() {
        plugin.getLogger().info("Waiting State Enabled");
    }

    @Override
    public void onDisable() {
        plugin.getLogger().info("Waiting State Disabled");
    }

    @Override
    public GameStateListener getListener(Game game) {
        return new PreGameListener(game);
    }
}