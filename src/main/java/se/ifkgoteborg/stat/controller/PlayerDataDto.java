package se.ifkgoteborg.stat.controller;

import se.ifkgoteborg.stat.model.GameParticipation;
import se.ifkgoteborg.stat.model.Player;

import java.util.List;

/**
 * Created by eriklupander on 2015-07-30.
 */
public class PlayerDataDto {
    private final Player player;
    private final Integer count;

    public PlayerDataDto(Player player, Integer count) {
        this.player = player;
        this.count = count;
    }

    public Player getPlayer() {
        return player;
    }

    public Integer getCount() {
        return count;
    }
}
