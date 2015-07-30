package se.ifkgoteborg.stat.dao;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.ifkgoteborg.stat.controller.GroundDataAvgDto;
import se.ifkgoteborg.stat.controller.GroundDataDto;
import se.ifkgoteborg.stat.controller.PlayerDataDto;
import se.ifkgoteborg.stat.controller.TriviaBean;
import se.ifkgoteborg.stat.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by eriklupander on 2015-07-25.
 */
@Test
public class TriviaBeanTest {

    TriviaBean testee = new TriviaBean();

    Ground g1 = new Ground(1L, "Arena 1");
    Ground g2 = new Ground(2L, "Arena 2");
    Ground g3 = new Ground(3L, "Arena 3");

    Club ejIFK = new Club();
    List<Player> players = new ArrayList<>();

    @BeforeMethod
    public void init() {
        ejIFK.setName("FC OtherTeam");
        players.clear();
        for(int a = 0; a < 100; a++) {
            Player p1 = new Player();
            p1.setId((long)a);
            p1.setName("Player " + a);
            players.add(p1);
        }
    }

    @Test
    public void testFindGroundsWithMostGamesPlayed() {

        List<GroundDataDto> result = testee.findGroundsWithMostGamesPlayed(allGames());
        assertEquals(result.get(0).getKey().getName(), "Arena 1");
        assertEquals(result.get(0).getSize().intValue(), 100);
    }

    @Test
    public void testFindGroundsWithHighestAveragePoints() {
        List<GroundDataAvgDto> result = testee.findGroundsWithHighestAveragePoints(allGames());
        assertEquals(result.get(0).getKey().getName(), "Arena 1");
        assertEquals(result.get(0).getAverage(), 3d);
    }

    @Test
    public void testFindGroundsWithHighestGoalAmountScored() {
        List<GroundDataDto> result = testee.findGroundsWithHighestGoalAmountScored(allGames());
        assertEquals(result.get(0).getKey().getName(), "Arena 1");
        assertEquals(result.get(0).getSize().intValue(), 200);
    }

    @Test
    public void testFindPlayersClosestToNGames() {
        List<PlayerDataDto> result = testee.findPlayersClosestToNGames(allGames(), 50);
        assertEquals(result.size(), 11);
    }


    private List<Game> allGames() {
        List<Game> games = new ArrayList<>();

        for(int a = 0; a < 100; a++) {
            Game g = new Game();
            g.setAwayGoals(2);
            g.setHomeGoals(0);
            g.setHomeTeam(ejIFK);
            g.setGround(g1);
            g.setGameParticipation(buildGP(g, 0));
            games.add(g);
        }

        for(int a = 0; a < 50; a++) {
            Game g = new Game();
            g.setAwayGoals(1);
            g.setHomeGoals(1);

            g.setHomeTeam(ejIFK);
            g.setGround(g2);
            g.setGameParticipation(buildGP(g, 1));
            games.add(g);
        }

        for(int a = 0; a < 20; a++) {
            Game g = new Game();
            g.setAwayGoals(0);
            g.setHomeGoals(1);
            g.setGround(g3);

            g.setHomeTeam(ejIFK);
            g.setGameParticipation(buildGP(g, 2));
            games.add(g);
        }
        Collections.shuffle(games);
        return games;
    }

    private List<GameParticipation> buildGP(Game g, int offset) {
        List<GameParticipation> gpList = new ArrayList<>();
        for(int a = (offset*11); a < (offset*11)+11; a++) {
            GameParticipation gp = new GameParticipation();
            gp.setGame(g);
            gp.setPlayer(players.get(a));
            gpList.add(gp);
        }
        return gpList;
    }

}
