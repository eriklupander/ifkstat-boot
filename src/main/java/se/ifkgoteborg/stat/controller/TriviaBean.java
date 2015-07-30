package se.ifkgoteborg.stat.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import se.ifkgoteborg.stat.model.*;

/**
 * Uses functional-style collection processing to extract stuff from lists of games and/or players.
 *
 * Created by eriklupander on 2015-07-25.
 */
public class TriviaBean implements Trivia {

    private static final Logger log = Logger.getLogger(TriviaBean.class);

    @Override
    public List<PlayerDataDto> findPlayersClosestToNGames(List<Game> resultList, int size) {

        // Map<Player, List<Game>> gamesPerPlayer =
        // Sum games per player. This is probably hugely expensive...

        // Start by flat-mapping all GP's into one huge list and then group them by Player
        Map<Player, List<GameParticipation>> collect = resultList.stream()
                .flatMap(game -> game.getGameParticipation().stream())
                .collect(Collectors.groupingBy(gp -> gp.getPlayer()));

         return collect.entrySet().stream()
                 .filter(entry -> entry.getValue().size() < size)
                 .sorted((e1, e2) -> new Integer(e2.getValue().size()).compareTo(e1.getValue().size()))
                 .map(entry -> new PlayerDataDto(entry.getKey(), entry.getValue().size()))
                 .collect(Collectors.toList());
    }

    @Override
    public List<PlayerDataDto> findPlayersClosestToNGoals(List<Game> resultList, int goals) {
        Map<Player, List<GameEvent>> collect = resultList.stream().flatMap(g -> g.getEvents().stream())
                .filter(ge -> ge.getEventType() == GameEvent.EventType.GOAL)
                .collect(Collectors.groupingBy(ge -> ge.getPlayer()));

        return collect.entrySet().stream()
                .filter(entry -> entry.getValue().size() < goals)
                .sorted((e1, e2) -> new Integer(e2.getValue().size()).compareTo(e1.getValue().size()))
                .map(entry -> new PlayerDataDto(entry.getKey(), entry.getValue().size()))
                .collect(Collectors.toList());
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
