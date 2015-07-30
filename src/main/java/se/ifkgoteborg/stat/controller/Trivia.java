package se.ifkgoteborg.stat.controller;

import se.ifkgoteborg.stat.model.Game;
import se.ifkgoteborg.stat.model.Ground;
import se.ifkgoteborg.stat.model.Player;
import se.ifkgoteborg.stat.model.PositionType;

import java.util.List;
import java.util.Map;

/**
 * Created by eriklupander on 2015-07-25.
 */
public interface Trivia {

    List<PlayerDataDto> findPlayersClosestToNGames(List<Game> resultList, int size);
    List<PlayerDataDto> findPlayersClosestToNGoals(List<Game> resultList, int goals);

    List<Player> findPlayerStartingAtPosForNthTime(PositionType positionType, int games);

    List<GroundDataDto> findGroundsWithMostGamesPlayed(List<Game> allGames);
    List<GroundDataAvgDto> findGroundsWithHighestAveragePoints(List<Game> allGames);
    List<GroundDataDto> findGroundsWithHighestGoalAmountScored(List<Game> resultList);
}
