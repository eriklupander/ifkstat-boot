package se.ifkgoteborg.stat.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import se.ifkgoteborg.stat.model.Game;
import se.ifkgoteborg.stat.model.Ground;
import se.ifkgoteborg.stat.model.Player;
import se.ifkgoteborg.stat.model.PositionType;

/**
 * Uses functional-style collection processing to extract stuff from lists of games and/or players.
 *
 * Created by eriklupander on 2015-07-25.
 */
public class TriviaBean implements Trivia {

    private static final Logger log = Logger.getLogger(TriviaBean.class);

    @Override
    public List<Player> findPlayersClosestToNGames(int games) {
        return null;
    }

    @Override
    public List<Player> findPlayersClosestToNGoals(int goals) {
        return null;
    }

    @Override
    public List<Player> findPlayerStartingAtPosForNthTime(PositionType positionType, int games) {
        return null;
    }

    @Override
    public List<GroundDataDto> findGroundsWithMostGamesPlayed(List<Game> resultList) {

        // First, group by ground.
        Map<Ground, List<Game>> collectedGrounds = resultList.stream()
                .collect(Collectors.groupingBy(game -> game.getGround()));

        // Then sort the map into a list of entries by number of games per ground.
        List<Map.Entry<Ground, List<Game>>> sorted = collectedGrounds.entrySet().stream()
                .sorted((entry1, entry2) -> ((Integer) entry2.getValue().size()).compareTo(entry1.getValue().size()))
                .collect(Collectors.toList());

        // Finally, transform to a DTO with ground -> size.
        return sorted.stream().map(entry -> new GroundDataDto(entry.getKey(), entry.getValue().size()))
                .collect(Collectors.toList());

    }

    @Override
    public List<GroundDataAvgDto> findGroundsWithHighestAveragePoints(List<Game> resultList) {
        // First, group by ground.
        Map<Ground, List<Game>> collectedGrounds = resultList.stream()
                .collect(Collectors.groupingBy(game -> game.getGround()));

        List<GroundDataAvgDto> collected = collectedGrounds.entrySet().stream()
                .map(e1 -> {
                    Double snittpoangBorta1 = e1.getValue().stream().filter(g -> !g.getHomeTeam().getName().equalsIgnoreCase("ifk göteborg"))
                            .collect(Collectors.averagingDouble(game -> {
                                if (game.getHomeGoals() < game.getAwayGoals()) return 3d;
                                if (game.getHomeGoals() == game.getAwayGoals()) return 1d;
                                return 0d;
                            }));

                    return new GroundDataAvgDto(e1.getKey(), snittpoangBorta1);
                })
                .collect(Collectors.toList());

        // Finally, transform to a DTO with ground -> size.
        return collected.stream()
                .sorted((gda1, gda2) -> gda2.getAverage().compareTo(gda1.getAverage()))
                .collect(Collectors.toList());
    }

    @Override
    public List<GroundDataDto> findGroundsWithHighestGoalAmountScored(List<Game> resultList) {
        // First, group by ground.
        Map<Ground, List<Game>> collectedGrounds = resultList.stream()
                .collect(Collectors.groupingBy(game -> game.getGround()));

        List<GroundDataDto> collected = collectedGrounds.entrySet().stream()
                .map(e1 -> {
                    Integer antalMal = e1.getValue().stream().filter(g -> !g.getHomeTeam().getName().equalsIgnoreCase("ifk göteborg"))
                            .collect(Collectors.summingInt(game -> game.getAwayGoals()));

                    return new GroundDataDto(e1.getKey(), antalMal);
                })
                .collect(Collectors.toList());

        // Finally, transform to a DTO with ground -> size.
        return collected.stream()
                .sorted( (gda1, gda2) -> gda2.getSize().compareTo(gda1.getSize()))
                .collect(Collectors.toList());
    }
}
