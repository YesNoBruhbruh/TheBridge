package com.maanraj514.game.states;

import com.maanraj514.BridgePlugin;

public class EndedState extends GameState{

    @Override
    public void onEnable(BridgePlugin plugin) {
        game.broadcast("&eGAME HAS ENDED! " + game.getWinnerTeam().getColor() + " WINS!");
    }

    @Override
    public void onDisable() {
    }
}