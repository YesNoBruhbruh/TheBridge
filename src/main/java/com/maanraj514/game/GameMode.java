package com.maanraj514.game;

public enum GameMode {

    DUELS(1, 2, 2),
    DUOS_TWO_TEAMS(2, 4, 3),
    SOLO_THREE_TEAMS(1, 3, 3),
    SOLO_EIGHT_TEAMS(1, 8, 8);

    private final int playersPerTeam;
    private final int maxPlayers;
    private final int minPlayers;

    GameMode(int playersPerTeam, int maxPlayers, int minPlayers) {
        this.playersPerTeam = playersPerTeam;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
    }

    public int getPlayersPerTeam() {
        return playersPerTeam;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }
}