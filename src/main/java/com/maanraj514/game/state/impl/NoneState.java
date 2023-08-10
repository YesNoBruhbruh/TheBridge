package com.maanraj514.game.state.impl;

import com.maanraj514.game.Game;
import com.maanraj514.game.state.GameStateImpl;
import com.maanraj514.game.state.GameStateListener;
import com.maanraj514.game.state.impl.listeners.PreGameListener;

public class NoneState extends GameStateImpl {
    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public GameStateListener getListener(Game game) {
        return new PreGameListener(game);
    }
}