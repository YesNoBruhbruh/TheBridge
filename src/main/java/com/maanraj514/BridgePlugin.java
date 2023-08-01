package com.maanraj514;

import com.maanraj514.game.GameManager;

public final class BridgePlugin extends Okmeta {

    @Override
    public void enable() {
        saveDefaultConfig();

        new GameManager(this);
    }

    @Override
    public void disable() {
    }
}