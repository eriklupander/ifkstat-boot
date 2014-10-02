package se.ifkgoteborg.stat.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.ifkgoteborg.stat.rest.model.PlayerGamesPerTournamentSeasonDTO;
import se.ifkgoteborg.stat.model.*;
import se.ifkgoteborg.stat.rest.model.*;

import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-10-01
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/rest/view")
public class DataServiceBean {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static final String JSON = "application/json";

    private static final String GAMES_PER_PLAYER_TOURNAMENT =
            "SELECT t.name, COUNT(pg.id) as appearances, " +
                    "	COUNT(ge2.id) as inbytt, COUNT(ge3.id) as utbytt, t.id  " +
                    "	FROM player p   " +
                    "	INNER JOIN player_game pg ON pg.player_id=p.id  " +
                    "	INNER JOIN game g ON g.id = pg.game_id  " +
                    "	INNER JOIN tournament_season ts ON ts.id = g.tournamentseason_id  " +
                    "	INNER JOIN tournament t ON t.id = ts.tournament_id  " +
                    "	LEFT OUTER JOIN game_event ge2 ON ge2.player_id=p.id AND ge2.game_id=g.id AND ge2.EVENTTYPE = 'SUBSTITUTION_IN'  " +
                    "	LEFT OUTER JOIN game_event ge3 ON ge3.player_id=p.id AND ge3.game_id=g.id AND ge3.EVENTTYPE = 'SUBSTITUTION_OUT'  " +
                    "	WHERE p.id = :id " +
                    "	GROUP BY t.name, t.id ORDER BY t.name";

    private static final String GOALS_PER_PLAYER_TOURNAMENT =
            "SELECT t.name, COUNT(ge.id) as goals, COUNT(ge4.id) as goals_as_subst " +
                    "	, COUNT(ge5.id) as goals_as_subst_out " +
                    "	FROM player p   " +
                    "	INNER JOIN player_game pg ON pg.player_id=p.id  " +
                    "	INNER JOIN game g ON g.id = pg.game_id  " +
                    "	INNER JOIN tournament_season ts ON ts.id = g.tournamentseason_id  " +
                    "	INNER JOIN tournament t ON t.id = ts.tournament_id  " +
                    "	LEFT OUTER JOIN game_event ge ON ge.player_id=p.id AND ge.game_id=g.id AND ge.EVENTTYPE ='GOAL'  " +
                    "	LEFT OUTER JOIN game_event ge4 ON ge4.player_id=p.id AND ge.player_id=p.id AND ge4.game_id=g.id AND (ge4.EVENTTYPE ='SUBSTITUTION_IN' AND ge.EVENTTYPE ='GOAL')  " +
                    "	LEFT OUTER JOIN game_event ge5 ON ge5.player_id=p.id AND ge.player_id=p.id AND ge5.game_id=g.id AND (ge5.EVENTTYPE ='SUBSTITUTION_OUT' AND ge.EVENTTYPE ='GOAL')  " +
                    "	WHERE p.id=:id " +
                    "	GROUP BY t.name ORDER BY t.name";

    @PersistenceContext
    javax.persistence.EntityManager em;

    @RequestMapping(method = RequestMethod.GET, value = "/players/{id}", produces = JSON)
    public Player getPlayer(@PathVariable Long id) {
        Player p = em.find(Player.class, id);

        // Lazy loading fix. I don't like it at all.
        p.getGames().size();

        return p;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/squadseasons/{id}/squad", produces = JSON)
    public List<Player> getSquadOfSeason(@PathVariable  Long id) {
        return em.createQuery("select p from Player p WHERE p.clubs.season.id = :id")
                .setParameter("id", id)
                .getResultList();
    }

    @RequestMapping(value = "/games/{id}", method=RequestMethod.GET, produces = JSON)
    public Game getGame(@PathVariable Long id) {
        return em.find(Game.class, id);
    }

//	
//	public List<GameParticipation> getGameParticipation(Long id) {
//		Game g = em.find(Game.class, id);
//		g.getGameParticipation().size();
//		return g.getGameParticipation();
//	}


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/players/{id}/games", method=RequestMethod.GET, produces = JSON)
    public List<Game> getGamesOfPlayer(@PathVariable Long id) {
        return em.createQuery("select g from Game g join g.gameParticipation gp join gp.player p WHERE p.id = :id")
                .setParameter("id", id)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/players/{id}/tournaments/{tournamentId}/games", method=RequestMethod.GET, produces = JSON)
    public List<Game> getGamesOfPlayerInTournament(@PathVariable Long id, @PathVariable Long tournamentId) {
        return em.createQuery("select g from Game g join g.gameParticipation gp join gp.player p WHERE p.id = :id AND g.tournamentSeason.tournament.id = :tournamentId")
                .setParameter("id", id)
                .setParameter("tournamentId", tournamentId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/players/{id}/tournaments/{tournamentId}/games/substin", method=RequestMethod.GET, produces = JSON)
    public List<Game> getGamesOfPlayerInTournamentSubstIn(@PathVariable Long id, @PathVariable Long tournamentId) {
        return em.createQuery("select DISTINCT(g) from Game g join g.gameParticipation gp " +
                "join gp.player p " +
                "join g.events ge " +
                "WHERE p.id = :id AND g.tournamentSeason.tournament.id = :tournamentId AND ge.eventType = 'SUBSTITUTION_IN' and ge.player.id=:id")
                .setParameter("id", id)
                .setParameter("tournamentId", tournamentId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/players/{id}/tournaments/{tournamentId}/games/substout", method=RequestMethod.GET, produces = JSON)
    public List<Game> getGamesOfPlayerInTournamentSubstOut(@PathVariable Long id, @PathVariable Long tournamentId) {
        return em.createQuery("select DISTINCT(g) from Game g join g.gameParticipation gp " +
                "join gp.player p " +
                "join g.events ge " +
                "WHERE p.id = :id AND g.tournamentSeason.tournament.id = :tournamentId AND ge.eventType = 'SUBSTITUTION_OUT' and ge.player.id=:id")
                .setParameter("id", id)
                .setParameter("tournamentId", tournamentId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/players/{id}/tournaments/{tournamentId}/games/goals", method=RequestMethod.GET, produces = JSON)
    public List<Game> getGamesOfPlayerInTournamentGoalScored(@PathVariable Long id,
                                                             @PathVariable Long tournamentId) {
        return em.createQuery("select DISTINCT(g) from Game g join g.gameParticipation gp " +
                "join gp.player p " +
                "join g.events ge " +
                "WHERE p.id = :id AND g.tournamentSeason.tournament.id = :tournamentId AND ge.eventType = 'GOAL' and ge.player.id=:id")
                .setParameter("id", id)
                .setParameter("tournamentId", tournamentId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/tournamentseasons/{id}/games", method=RequestMethod.GET, produces = JSON)
    public List<Game> getGamesOfTournamentSeason(@PathVariable Long id) {
        return em.createQuery("select g from Game g WHERE g.tournamentSeason.id = :id")
                .setParameter("id", id)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/squadseasons", method=RequestMethod.GET, produces = JSON)
    public List<SquadSeason> getSquadSeason() {
        return em.createQuery("select ss from SquadSeason ss").getResultList();
    }

    @RequestMapping(value = "/tournamentseasons", method=RequestMethod.GET, produces = JSON)
    public List<TournamentSeason> getTournamentSeasons() {
        return em.createQuery("select ts from TournamentSeason ts").getResultList();
    }

    @RequestMapping(value = "/tournaments", method=RequestMethod.GET, produces = JSON)
    public List<Tournament> getTournaments() {
        return em.createQuery("select t from Tournament t").getResultList();
    }

    @RequestMapping(value = "/grounds", method=RequestMethod.GET, produces = JSON)
    public List<Ground> getGrounds() {
        return em.createQuery("select g from Ground g ORDER BY g.name").getResultList();
    }

    @RequestMapping(value = "/referees", method=RequestMethod.GET, produces = JSON)
    public List<Referee> getReferees() {
        return em.createQuery("select r from Referee r ORDER BY r.name").getResultList();
    }

    @RequestMapping(value = "/tournaments/{id}/seasons", method=RequestMethod.GET, produces = JSON)
    public List<TournamentSeason> getSeasonsOfTournament(@PathVariable Long id) {
        return em.createQuery("select ts from TournamentSeason ts WHERE ts.tournament.id = :id")
                .setParameter("id", id)
                .getResultList();
    }

    @RequestMapping(value = "/games/{id}/participations", method=RequestMethod.GET, produces = JSON)
    public List<GameParticipation> getGameParticipation(@PathVariable Long id) {
        return em.createQuery("select gp from GameParticipation gp WHERE gp.game.id = :id")
                .setParameter("id", id)
                .getResultList();
    }

    @RequestMapping(value = "/games/{id}/notes", method=RequestMethod.GET, produces = JSON)
    public List<GameNote> getGameNotes(@PathVariable Long id) {
        return em.createQuery("select gn from Game g join g.gameNotes gn WHERE g.id = :id")
                .setParameter("id", id)
                .getResultList();
    }

    @RequestMapping(value = "/games/{id}/events", method=RequestMethod.GET, produces = JSON)
    public List<GameEvent> getGameEvents(@PathVariable Long id) {
        return em.createQuery("select ge from GameEvent ge WHERE ge.game.id = :id")
                .setParameter("id", id)
                .getResultList();
    }

    @RequestMapping(value = "/clubs/{id}/games", method=RequestMethod.GET, produces = JSON)
    public List<Game> getGamesVsClub(@PathVariable Long id) {
        return em.createQuery("select g from Game g WHERE (g.homeTeam.id = :id OR g.awayTeam.id = :id)")
                .setParameter("id", id)
                .getResultList();
    }

    @RequestMapping(value = "/players/{id}/stats", method=RequestMethod.GET, produces = JSON)
    public PlayerStatDTO getPlayerStats(@PathVariable Long id) {
        PlayerStatDTO dto = new PlayerStatDTO();

        Query q1 = em.createNativeQuery(
                "SELECT t.name, COUNT(ge.id) as goals FROM player p " +
                        "INNER JOIN game_event ge ON ge.player_id=p.id  " +
                        "INNER JOIN game g ON g.id = ge.game_id " +
                        "INNER JOIN tournament_season ts ON ts.id = g.tournamentseason_id " +
                        "INNER JOIN tournament t ON t.id = ts.tournament_id " +
                        "WHERE ge.EVENTTYPE ='GOAL' AND p.id=" + id + " GROUP BY t.id, t.name ORDER BY goals DESC");
        List<Object[]> res1 = q1.getResultList();
        for(Object[] row : res1) {
            dto.getGoalsPerTournament().add(new GoalsPerTournamentDTO((String) row[0], (Number) row[1]));
        }

        Query q2 = em.createNativeQuery(
                "select pos.name, count(pos.id) as gcount from player p " +
                        "inner join player_game pg ON pg.player_id=p.id  " +
                        "inner join formation_position fp ON pg.formationposition_id=fp.id " +
                        "inner join position pos ON pos.id=fp.position_id " +
                        "WHERE p.id = " + id + " " +
                        "GROUP BY p.name, pos.name " +
                        "ORDER BY gcount DESC");
        List<Object[]> res2 = q2.getResultList();
        for(Object[] row : res2) {
            dto.getGamesPerPosition().add(new GamePositionStatDTO((String) row[0], (Number) row[1]));
        }

        Query q3 = em.createNativeQuery(GAMES_PER_PLAYER_TOURNAMENT)
                .setParameter("id", id);
        List<Object[]> res3 = q3.getResultList();

        Query q4 = em.createNativeQuery(GOALS_PER_PLAYER_TOURNAMENT)
                .setParameter("id", id);
        List<Object[]> res4 = q4.getResultList();

        for(int a = 0; a < res3.size(); a++) {

            Object[] row3 = res3.get(a);
            Object[] row4 = res4.get(a);

            AveragesPerGameAndTournamentDTO avDto = new AveragesPerGameAndTournamentDTO();
            avDto.setTournamentName((String) row3[0]);
            avDto.setGoals( ((Number) row4[1]).intValue());
            avDto.setTotalGames( ((Number) row3[1]).intValue());
            avDto.setGamesAsSubstituteIn( ((Number) row3[2]).intValue());
            avDto.setGamesAsSubstituteOut( ((Number) row3[3]).intValue());
            avDto.setGoalsAsSubstituteIn( ((Number) row4[2]).intValue());
            avDto.setGoalsAsSubstituteOut( ((Number) row4[3]).intValue());
            avDto.setTournamentId( ((Number) row3[4]).longValue());
            dto.getAveragesPerTournament().add(avDto);
        }

        return dto;
    }

    @RequestMapping(value = "/players/{id}/positions", method=RequestMethod.GET, produces = JSON)
    public List<PlayerPositionStatsDTO> getPlayerPositionStats(@PathVariable Long id) {
        String sql ="SELECT DISTINCT(pos.name) as posname, f.name as fname, j1.games, j2.goals FROM " +
                "player p  " +
                "inner join player_game pg ON pg.player_id=p.id " +
                "INNER JOIN formation_position fp ON pg.formationposition_id=fp.id " +
                "INNER JOIN formation f ON f.id=fp.formation_id " +
                "INNER JOIN position pos ON pos.id=fp.position_id " +
                " " +
                "LEFT OUTER JOIN " +
                "				(select  p.id as playerId, pos.name as posname, f.id as fid, count(pg.id) as games from player p  " +
                "					inner join player_game pg ON pg.player_id=p.id " +
                "					inner join formation_position fp ON pg.formationposition_id=fp.id   " +
                "					inner join formation f ON f.id=fp.formation_id " +
                "					inner join position pos ON pos.id=fp.position_id " +
                "					WHERE p.id= :id " +
                "					GROUP BY (p.id, pos.name, f.id) " +
                "			 	) j1  " +
                "			 	ON j1.playerId=p.id AND j1.posname = pos.name AND j1.fid=f.id " +
                " " +
                "LEFT OUTER JOIN " +
                "				(select  p.id as playerId, pos.name as posname, f.id as fid, count(ge.id) as goals from player p  " +
                "					inner join player_game pg ON pg.player_id=p.id " +
                "					inner join formation_position fp ON pg.formationposition_id=fp.id   " +
                "					inner join formation f ON f.id=fp.formation_id " +
                "					inner join position pos ON pos.id=fp.position_id " +
                "					inner join game g ON g.id=pg.game_id " +
                "					left outer join game_event ge ON ge.player_id=p.id AND ge.game_id=g.id " +
                "					WHERE p.id=:id  AND ge.eventtype='GOAL' " +
                "					GROUP BY (p.id, pos.name, f.id) " +
                "			 	) j2  " +
                "			 	ON j2.playerId=p.id AND j2.posname = pos.name AND j2.fid=f.id " +
                "WHERE p.id= :id "; // + 
        //"GROUP BY (p.name, pos.name, f.name)";

        Query q4 = em.createNativeQuery(sql)
                .setParameter("id", id);
        List<Object[]> res = q4.getResultList();

        List<PlayerPositionStatsDTO> retList = new ArrayList<PlayerPositionStatsDTO>();
        for(int a = 0; a < res.size(); a++) {

            Object[] row = res.get(a);

            PlayerPositionStatsDTO avDto = new PlayerPositionStatsDTO();
            avDto.setPositionName((String) row[0]);
            avDto.setFormationName((String) row[1]);
            avDto.setGames( row[2] != null ? ((Number) row[2]).intValue() : 0);
            avDto.setGoals( row[3] != null ? ((Number) row[3]).intValue() : 0);
            retList.add(avDto);
        }
        return retList;
    }

    private String fullStatsSql = " SELECT" +
            "     'STARTER' as participationType," +
            "     ts.seasonName," +
            "     t.name," +
            "     COUNT(g2.id)  + COUNT(g5.id) wins," +
            "     COUNT(g3.id)  + COUNT(g4.id) losses," +
            "     COUNT(g6.id)  + COUNT(g7.id) draws" +
            " FROM" +
            "     player p" +
            " INNER JOIN" +
            "     player_game pg" +
            "         ON pg.player_id=p.id" +
            " INNER JOIN" +
            "     game g" +
            "         ON g.id=pg.game_id" +
            " INNER JOIN" +
            "     tournament_season ts ON ts.id=g.tournamentseason_id" +
            " INNER JOIN" +
            "     tournament t ON t.id=ts.tournament_id" +
            " LEFT OUTER JOIN" +
            "     game g2" +
            "         ON g2.id=pg.game_id" +
            "         AND g2.homeGoals > g2.awayGoals" +
            "         AND g2.hometeam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g3" +
            "         ON g3.id=pg.game_id" +
            "         AND g3.homeGoals < g3.awayGoals" +
            "         AND g3.hometeam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g6" +
            "         ON g6.id=pg.game_id" +
            "         AND g6.homeGoals = g6.awayGoals" +
            "         AND g6.hometeam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g4" +
            "         ON g4.id=pg.game_id" +
            "         AND g4.homeGoals > g4.awayGoals" +
            "         AND g4.awayteam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g5" +
            "         ON g5.id=pg.game_id" +
            "         AND g5.homeGoals < g5.awayGoals" +
            "         AND g5.awayteam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g7" +
            "         ON g7.id=pg.game_id" +
            "         AND g7.homeGoals = g7.awayGoals" +
            "         AND g7.awayteam_id=:defaultClubId" +
            " WHERE" +
            "     p.id=:playerId" +
            "     AND pg.participationtype IN (" +
            "         'STARTER','SUBSTITUTE_OUT'" +
            "     )" +
            "     GROUP BY ('STARTER',ts.seasonName, t.name)" +
            " UNION" +
            " SELECT" +
            "     'SUBSTITUTE' as participationType," +
            "     ts.seasonName," +
            "     t.name," +
            "     COUNT(g2.id)  + COUNT(g5.id) wins," +
            "     COUNT(g3.id)  + COUNT(g4.id) losses," +
            "     COUNT(g6.id)  + COUNT(g7.id) draws" +
            " FROM" +
            "     player p" +
            " INNER JOIN" +
            "     player_game pg" +
            "         ON pg.player_id=p.id" +
            " INNER JOIN" +
            "     game g" +
            "         ON g.id=pg.game_id" +
            " INNER JOIN" +
            "     tournament_season ts ON g.tournamentseason_id=ts.id" +
            " INNER JOIN" +
            "     tournament t ON t.id=ts.tournament_id" +
            " LEFT OUTER JOIN" +
            "     game g2" +
            "         ON g2.id=pg.game_id" +
            "         AND g2.homeGoals > g2.awayGoals" +
            "         AND g2.hometeam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g3" +
            "         ON g3.id=pg.game_id" +
            "         AND g3.homeGoals < g3.awayGoals" +
            "         AND g3.hometeam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g6" +
            "         ON g6.id=pg.game_id" +
            "         AND g6.homeGoals = g6.awayGoals" +
            "         AND g6.hometeam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g4" +
            "         ON g4.id=pg.game_id" +
            "         AND g4.homeGoals > g4.awayGoals" +
            "         AND g4.awayteam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g5" +
            "         ON g5.id=pg.game_id" +
            "         AND g5.homeGoals < g5.awayGoals" +
            "         AND g5.awayteam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g7" +
            "         ON g7.id=pg.game_id" +
            "         AND g7.homeGoals = g7.awayGoals" +
            "         AND g7.awayteam_id=:defaultClubId" +
            " WHERE" +
            "     p.id=:playerId" +
            "     AND pg.participationtype IN (" +
            "         'SUBSTITUTE_IN'" +
            "     )" +
            "     GROUP BY ('SUBSTITUTE',ts.seasonName, t.name)" +
            " UNION" +
            " SELECT" +
            "     'NO_PART' c1," +
            "     ts.seasonName," +
            "     t.name," +
            "     COUNT(g2.id)  + COUNT(g5.id) wins," +
            "     COUNT(g3.id)  + COUNT(g4.id) losses," +
            "     COUNT(g6.id)  + COUNT(g7.id) draws" +
            " FROM" +
            "     game g" +
            " INNER JOIN" +
            "     tournament_season ts" +
            "         ON ts.id=g.tournamentseason_id" +
            "  INNER JOIN" +
            "     tournament t ON t.id=ts.tournament_id" +
            " LEFT OUTER JOIN" +
            "     game g2" +
            "         ON g2.id=g.id" +
            "         AND g2.homeGoals > g2.awayGoals" +
            "         AND g2.hometeam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g3" +
            "         ON g3.id=g.id" +
            "         AND g3.homeGoals < g3.awayGoals" +
            "         AND g3.hometeam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g6" +
            "         ON g6.id=g.id" +
            "         AND g6.homeGoals = g6.awayGoals" +
            "         AND g6.hometeam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g4" +
            "         ON g4.id=g.id" +
            "         AND g4.homeGoals > g4.awayGoals" +
            "         AND g4.awayteam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g5" +
            "         ON g5.id=g.id" +
            "         AND g5.homeGoals < g5.awayGoals" +
            "         AND g5.awayteam_id=:defaultClubId" +
            " LEFT OUTER JOIN" +
            "     game g7" +
            "         ON g7.id=g.id" +
            "         AND g7.homeGoals = g7.awayGoals" +
            "         AND g7.awayteam_id=:defaultClubId" +
            " WHERE" +
            "     g.id NOT IN (" +
            "         SELECT" +
            "             g.id" +
            "         FROM" +
            "             player_game pg" +
            "         INNER JOIN" +
            "             game g" +
            "                 ON g.id=pg.game_id" +
            "         WHERE" +
            "             pg.player_id=:playerId" +
            "     )" +
            "     AND ts.id IN (" +
            "         SELECT" +
            "             DISTINCT(ts.id)" +
            "         FROM" +
            "             game g" +
            "         INNER JOIN" +
            "             tournament_season ts" +
            "                 ON ts.id=g.tournamentseason_id" +
            "         INNER JOIN" +
            "             player_game pg" +
            "                 ON pg.game_id=g.id" +
            "         WHERE" +
            "             pg.player_id=:playerId" +
            "     )" +
            "     GROUP BY ('NO_PART',ts.seasonName, t.name)";


    @RequestMapping(value = "/players/{id}/resultstats/full", method=RequestMethod.GET, produces = JSON)
    public List<PlayerResultStatDTO> getFullPlayerResultStats(@PathVariable Long id) {
        Query q4 = em.createNativeQuery(fullStatsSql)
                .setParameter("playerId", id)
                .setParameter("defaultClubId", defaultClub());
        List<Object[]> rows = (List<Object[]>) q4.getResultList();

        List<PlayerResultStatDTO> retList = new ArrayList<PlayerResultStatDTO>();
        for(Object[] row : rows) {
            PlayerResultStatDTO dto = new PlayerResultStatDTO();
            dto.setParticipationType((String) row[0]);
            dto.setSeason( (String) row[1]);
            dto.setTournament( (String) row[2]);
            dto.setWins(formatNumber(row[3]));
            dto.setLosses(formatNumber(row[4]));
            dto.setDraws(formatNumber(row[5]));

            retList.add(dto);
        }
        return retList;
    }

    @RequestMapping(value = "/players/{id}/resultstats", method=RequestMethod.GET, produces = JSON)
    public List<PlayerResultStatDTO> getPlayerResultStats(@PathVariable Long id) {
        String sql = "SELECT 'STARTER' as participationType, COUNT(g2.id)  + COUNT(g5.id) wins, COUNT(g3.id)  + COUNT(g4.id) losses, COUNT(g6.id)  + COUNT(g7.id) draws FROM player p " +
                "INNER JOIN player_game pg ON pg.player_id=p.id " +
                "INNER JOIN game g ON g.id=pg.game_id " +
                "LEFT OUTER JOIN game g2 ON g2.id=pg.game_id AND g2.homeGoals > g2.awayGoals AND g2.hometeam_id=:defaultClubId " +
                "LEFT OUTER JOIN game g3 ON g3.id=pg.game_id AND g3.homeGoals < g3.awayGoals AND g3.hometeam_id=:defaultClubId " +
                "LEFT OUTER JOIN game g6 ON g6.id=pg.game_id AND g6.homeGoals = g6.awayGoals AND g6.hometeam_id=:defaultClubId " +
                "LEFT OUTER JOIN game g4 ON g4.id=pg.game_id AND g4.homeGoals > g4.awayGoals AND g4.awayteam_id=:defaultClubId " +
                "LEFT OUTER JOIN game g5 ON g5.id=pg.game_id AND g5.homeGoals < g5.awayGoals AND g5.awayteam_id=:defaultClubId " +
                "LEFT OUTER JOIN game g7 ON g7.id=pg.game_id AND g7.homeGoals = g7.awayGoals AND g7.awayteam_id=:defaultClubId " +
                "WHERE p.id=:id AND pg.participationtype IN ('STARTER','SUBSTITUTE_OUT') " +
                "UNION " +
                "SELECT  'SUBSTITUTE' as participationType, COUNT(g2.id)  + COUNT(g5.id) wins, COUNT(g3.id)  + COUNT(g4.id) losses, COUNT(g6.id)  + COUNT(g7.id) draws FROM player p " +
                "INNER JOIN player_game pg ON pg.player_id=p.id " +
                "INNER JOIN game g ON g.id=pg.game_id " +
                "LEFT OUTER JOIN game g2 ON g2.id=pg.game_id AND g2.homeGoals > g2.awayGoals AND g2.hometeam_id=:defaultClubId " +
                "LEFT OUTER JOIN game g3 ON g3.id=pg.game_id AND g3.homeGoals < g3.awayGoals AND g3.hometeam_id=:defaultClubId " +
                "LEFT OUTER JOIN game g6 ON g6.id=pg.game_id AND g6.homeGoals = g6.awayGoals AND g6.hometeam_id=:defaultClubId " +
                "LEFT OUTER JOIN game g4 ON g4.id=pg.game_id AND g4.homeGoals > g4.awayGoals AND g4.awayteam_id=:defaultClubId " +
                "LEFT OUTER JOIN game g5 ON g5.id=pg.game_id AND g5.homeGoals < g5.awayGoals AND g5.awayteam_id=:defaultClubId " +
                "LEFT OUTER JOIN game g7 ON g7.id=pg.game_id AND g7.homeGoals = g7.awayGoals AND g7.awayteam_id=:defaultClubId " +
                "WHERE p.id=:id AND pg.participationtype IN ('SUBSTITUTE_IN')" +
                "" +
                "UNION " +
                "SELECT 'NO_PART', COUNT(g2.id)  + COUNT(g5.id) wins, COUNT(g3.id)  + COUNT(g4.id) losses, COUNT(g6.id)  + COUNT(g7.id) draws FROM game g " +
                "INNER JOIN tournament_season ts ON ts.id=g.tournamentseason_id " +
                "LEFT OUTER JOIN game g2 ON g2.id=g.id AND g2.homeGoals > g2.awayGoals AND g2.hometeam_id=:defaultClubId " +
                "LEFT OUTER JOIN game g3 ON g3.id=g.id AND g3.homeGoals < g3.awayGoals AND g3.hometeam_id=:defaultClubId  " +
                "LEFT OUTER JOIN game g6 ON g6.id=g.id AND g6.homeGoals = g6.awayGoals AND g6.hometeam_id=:defaultClubId  " +
                "LEFT OUTER JOIN game g4 ON g4.id=g.id AND g4.homeGoals > g4.awayGoals AND g4.awayteam_id=:defaultClubId  " +
                "LEFT OUTER JOIN game g5 ON g5.id=g.id AND g5.homeGoals < g5.awayGoals AND g5.awayteam_id=:defaultClubId  " +
                "LEFT OUTER JOIN game g7 ON g7.id=g.id AND g7.homeGoals = g7.awayGoals AND g7.awayteam_id=:defaultClubId " +
                "WHERE g.id NOT IN ( " +
                "SELECT g.id FROM player_game pg INNER JOIN game g ON g.id=pg.game_id WHERE pg.player_id=:id " +
                ") " +
                "AND ts.id IN " +
                "( " +
                "SELECT DISTINCT(ts.id) FROM game g " +
                "INNER JOIN tournament_season ts ON ts.id=g.tournamentseason_id " +
                "INNER JOIN player_game pg ON pg.game_id=g.id " +
                "WHERE pg.player_id=:id " +
                ")";

        Query q4 = em.createNativeQuery(sql)
                .setParameter("id", id)
                .setParameter("defaultClubId", defaultClub());
        List<Object[]> rows = (List<Object[]>) q4.getResultList();

        List<PlayerResultStatDTO> retList = new ArrayList<PlayerResultStatDTO>();
        for(Object[] row : rows) {
            PlayerResultStatDTO dto = new PlayerResultStatDTO();
            dto.setParticipationType((String) row[0]);
            dto.setWins(formatNumber(row[1]));
            dto.setLosses(formatNumber(row[2]));
            dto.setDraws(formatNumber(row[3]));

            retList.add(dto);
        }
        return retList;
    }

    private Integer formatNumber(Object object) {
        if(object instanceof BigInteger) {
            return ((BigInteger) object).intValue();
        } else if(object instanceof BigDecimal) {
            return ((BigDecimal) object).intValue();
        }
        return ((Number) object).intValue();
    }

    @RequestMapping(value = "/players/{id}/gamespertournaments", method=RequestMethod.GET, produces = JSON)
    public List<PlayerGamesPerTournamentSeasonDTO> getPlayerGamesPerSeason(@PathVariable Long id) {
        String sql = "SELECT DISTINCT(ts.id), t.name, ts.seasonName, COUNT(pg.id), COUNT(g.id)  FROM game g " +
                "INNER JOIN tournament_season ts ON ts.id=g.tournamentseason_id " +
                "INNER JOIN tournament t ON t.id=ts.tournament_id " +
                "LEFT OUTER JOIN player_game pg ON pg.game_id=g.id AND  pg.player_id=:id " +
                "GROUP BY ts.id, t.name, ts.seasonName " +
                "HAVING COUNT(pg.id) > 0 " +
                "ORDER BY ts.seasonName DESC, t.name";

        Query q4 = em.createNativeQuery(sql)
                .setParameter("id", id);
        List<Object[]> rows = (List<Object[]>) q4.getResultList();

        List<PlayerGamesPerTournamentSeasonDTO> retList = new ArrayList<PlayerGamesPerTournamentSeasonDTO>();
        for(Object[] row : rows) {
            PlayerGamesPerTournamentSeasonDTO dto = new PlayerGamesPerTournamentSeasonDTO();
            dto.setId(formatLong(row[0]));
            dto.setTournamentName( (String) row[1]);
            dto.setSeasonName( (String) row[2]);
            dto.setGamesPlayed(formatNumber(row[3]));
            dto.setTotalGames(formatNumber(row[4]));
            retList.add(dto);
        }
        return retList;
    }

    private Long formatLong(Object object) {
        if(object instanceof BigInteger) {
            return ((BigInteger) object).longValue();
        } else if(object instanceof BigDecimal) {
            return ((BigDecimal) object).longValue();
        }
        return ((Number) object).longValue();
    }

    private String getParticipation(String pType) {
        if(pType.equals("STARTER") || pType.equals("SUBSTITUTE_OUT")) {
            return "STARTER";
        } else {
            return "SUBSTITUTE";
        }
    }

    @RequestMapping(value = "/positiontypes", method=RequestMethod.GET, produces = JSON)
    public List<PositionType> getPositionTypes() {
        return em.createQuery("select pt from PositionType pt").getResultList();
    }

    @RequestMapping(value = "/countries", method=RequestMethod.GET, produces = JSON)
    public List<PositionType> getCountries() {
        return em.createQuery("select c from Country c ORDER BY c.name").getResultList();
    }

    @RequestMapping(value = "/players", method=RequestMethod.GET, produces = JSON)
    public List<Player> getPlayers() {
        return em.createQuery("select p from Player p ORDER BY p.name").getResultList();
    }

    @RequestMapping(value = "/games", method=RequestMethod.GET, produces = JSON)
    public List<Game> getGames() {
        return em.createQuery("select g from Game g ORDER BY g.dateOfGame DESC").getResultList();
    }

    @RequestMapping(value = "/players/summaries", method=RequestMethod.GET, produces = JSON)
    public Collection<PlayerSummaryDTO> getPlayerSummaries() {
        String sql = "select p.id, p.name, COUNT(pg.id) as games, MIN(g.dateOfGame) as firstGame, MAX(g.dateOfGame) as lastGame FROM player p " +
                "INNER JOIN player_game pg ON pg.player_id=p.id " +
                "INNER JOIN game g ON pg.game_id=g.id " +
                "GROUP BY p.id, p.name ORDER BY p.name";

        Query q1 = em.createNativeQuery(sql);
        List<Object[]> res1 = q1.getResultList();
        Map<Long, PlayerSummaryDTO> map = new LinkedHashMap<Long, PlayerSummaryDTO>();
        for(Object[] row : res1) {
            PlayerSummaryDTO dto = new PlayerSummaryDTO();
            dto.setId( ((Number) row[0]).longValue());
            dto.setName((String) row[1]);
            dto.setGames( ((Number) row[2]).intValue());
            dto.setFirstGame(formatDate(row[3]));
            dto.setLastGame(formatDate(row[4]));
            map.put(dto.getId(), dto);
        }

        // Get the goals in a separate query...
        String sql2 = "select p.id, COUNT(ge.id) as goals FROM player p " +
                "LEFT OUTER JOIN game_event ge ON ge.player_id=p.id AND ge.eventType = 'GOAL' " +
                "GROUP BY p.id";

        Query q2 = em.createNativeQuery(sql2);
        List<Object[]> res2 = q2.getResultList();

        // Fugly, we assume the rows are matching the result from above. Reason for this:
        // Doing the goals thing in a sub-select makes the query take many seconds instead of
        // milliseconds. This is much faster. And more fugly.
        for(Object[] row : res2) {
            Long id = ((Number) row[0]).longValue();
            Integer goals = ((Number) row[1]).intValue();

            if(map.containsKey(id)) {
                map.get(id).setGoals(goals);
            } else {
                // WARN!
                //System.out.println("Could not find playerId in map: " + id);
            }

        }

        return map.values();
    }

    private Date formatDate(Object date) {
        if(date instanceof java.sql.Date) {
            return new Date(((java.sql.Date) date).getTime());
        }
        if(date instanceof java.sql.Timestamp) {
            return new Date(((java.sql.Timestamp) date).getTime());
        }
        return (Date) date;
    }

    @RequestMapping(value = "/games/{date}", method=RequestMethod.GET, produces = JSON)
    public List<Game> getGamesOfDate(@PathVariable String date) {
        try {
            Date dateOfGame = sdf.parse(date);

            return em.createQuery("SELECT g FROM Game g WHERE g.dateOfGame = :dateOfGame")
                    .setParameter("dateOfGame", dateOfGame)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<Game>();
        }

    }

    private String statSql = "SELECT c.name, c.id, j1.g1, j2.g2, j1.hg1, j1.ag1, j2.hg2, j2.ag2, j1.wins1, j1.draws1, j1.losses1, j2.wins2, j2.draws2, j2.losses2 FROM club c " +
            "	LEFT OUTER JOIN " +
            "	( " +
            "	SELECT c1.id as j1id, count(g.id) as g1, sum(g.homeGoals) as hg1, sum(g.awayGoals) as ag1, COUNT(g2.id) as wins1, COUNT(g3.id) as draws1, COUNT(g4.id) as losses1  " +
            "	FROM game g " +
            "	LEFT OUTER JOIN club c1 ON c1.id=g.hometeam_id " +
            "	LEFT OUTER JOIN game g2 ON g2.id=g.id AND g2.homegoals > g2.awaygoals " +
            "	LEFT OUTER JOIN game g3 ON g3.id=g.id AND g3.homegoals = g3.awaygoals " +
            "	LEFT OUTER JOIN game g4 ON g4.id=g.id AND g4.homegoals < g4.awaygoals " +
            "	WHERE c1.id <> :defaultClubId " +
            "	GROUP BY c1.id " +
            "	) j1 " +
            "	ON j1id=c.id " +
            "	 " +
            "	LEFT OUTER JOIN " +
            "	 " +
            "	(SELECT c2.id as j2id, count(g.id) as g2, sum(g.homeGoals) as hg2, sum(g.awayGoals) as ag2, COUNT(g2.id) as wins2, COUNT(g3.id) as draws2, COUNT(g4.id) as losses2   " +
            "	FROM game g " +
            "	LEFT OUTER JOIN club c2 ON c2.id=g.awayteam_id " +
            "	LEFT OUTER JOIN game g2 ON g2.id=g.id AND g2.homegoals > g2.awaygoals " +
            "	LEFT OUTER JOIN game g3 ON g3.id=g.id AND g3.homegoals = g3.awaygoals " +
            "	LEFT OUTER JOIN game g4 ON g4.id=g.id AND g4.homegoals < g4.awaygoals " +
            "	WHERE c2.id <> :defaultClubId " +
            "	GROUP BY c2.id " +
            "	) j2 " +
            "	ON j2.j2id = c.id";

    private Long defaultClubId = null;

    private Long defaultClub() {
        if(this.defaultClubId != null) {
            return this.defaultClubId;
        }
        this.defaultClubId = formatLong(em.createQuery("SELECT c.id FROM Club c WHERE c.defaultClub=true").getSingleResult());
        return this.defaultClubId;
    }

    @RequestMapping(value = "/clubs/stats", method=RequestMethod.GET, produces = JSON)
    public List<ClubStatDTO> getAllClubStatistics() {
        List<Object[]> rows = em.createNativeQuery(statSql).
                setParameter("defaultClubId", defaultClub()).
                getResultList();

        List<ClubStatDTO> res = new ArrayList<ClubStatDTO>();

        for(Object[] row : rows) {
            ClubStatDTO dto = new ClubStatDTO();
            dto.setClubName( (String) row[0]);
            dto.setClubId( ((Number) row[1]).longValue());

            // c.name, c.id, j1.g1, j2.g2, j1.hg, j1.ag, j2.hg, j2.ag, j1.wins, j1.draws, j1.losses, j2.wins, j2.draws, j2.losses
            // IFK away team first
            dto.setAwayGames(getInt(row[2]));
            dto.setHomeGames(getInt(row[3]));
            dto.setAwayGoalsConceded(getInt(row[4]));
            dto.setAwayGoalsScored(getInt(row[5]));
            dto.setHomeGoalsScored(getInt(row[6]));
            dto.setHomeGoalsConceded(getInt(row[7]));

            dto.setAwayLosses(getInt(row[8]));
            dto.setAwayDraws(getInt(row[9]));
            dto.setAwayWins(getInt(row[10]));

            dto.setHomeWins(getInt(row[11]));
            dto.setHomeDraws(getInt(row[12]));
            dto.setHomeLosses(getInt(row[13]));

            res.add(dto);
        }

        return res;
    }


    private Integer getInt(Object object) {
        if(object == null) {
            return 0;
        }
        if(object instanceof Number) {
            return ((Number) object).intValue();
        }
        return 0;
    }

    private String singleClubStatSql = "SELECT c.name, c.id, j1.g1, j2.g2, j1.hg1, j1.ag1, j2.hg2, j2.ag2, j1.wins1, j1.draws1, j1.losses1, j2.wins2, j2.draws2, j2.losses2 FROM club c " +
            "	LEFT OUTER JOIN " +
            "	( " +
            "	SELECT c1.id as j1id, count(g.id) as g1, sum(g.homeGoals) as hg1, sum(g.awayGoals) as ag1, COUNT(g2.id) as wins1, COUNT(g3.id) as draws1, COUNT(g4.id) as losses1  " +
            "	FROM game g " +
            "	LEFT OUTER JOIN club c1 ON c1.id=g.hometeam_id " +
            "	LEFT OUTER JOIN game g2 ON g2.id=g.id AND g2.homegoals > g2.awaygoals " +
            "	LEFT OUTER JOIN game g3 ON g3.id=g.id AND g3.homegoals = g3.awaygoals " +
            "	LEFT OUTER JOIN game g4 ON g4.id=g.id AND g4.homegoals < g4.awaygoals " +
            "	WHERE c1.id = :id" +
            "	GROUP BY c1.id " +
            "	) as j1 " +
            "	ON j1id=c.id " +
            "	 " +
            "	LEFT OUTER JOIN " +
            "	 " +
            "	(SELECT c2.id as j2id, count(g.id) as g2, sum(g.homeGoals) as hg2, sum(g.awayGoals) as ag2, COUNT(g2.id) as wins2, COUNT(g3.id) as draws2, COUNT(g4.id) as losses2   " +
            "	FROM game g " +
            "	LEFT OUTER JOIN club c2 ON c2.id=g.awayteam_id " +
            "	LEFT OUTER JOIN game g2 ON g2.id=g.id AND g2.homegoals > g2.awaygoals " +
            "	LEFT OUTER JOIN game g3 ON g3.id=g.id AND g3.homegoals = g3.awaygoals " +
            "	LEFT OUTER JOIN game g4 ON g4.id=g.id AND g4.homegoals < g4.awaygoals " +
            "	WHERE c2.id = :id " +
            "	GROUP BY c2.id " +
            "	) j2 " +
            "	ON j2.j2id = c.id AND c.id = :id";

    @RequestMapping(value = "/clubs/{id}/stats", method=RequestMethod.GET, produces = JSON)
    public ClubStatDTO getClubStatistics(@PathVariable Integer id) {

        Object[] row = (Object[]) em.createNativeQuery(singleClubStatSql)
                .setParameter("id", id)
                .getSingleResult();

        ClubStatDTO dto = new ClubStatDTO();
        dto.setClubName( (String) row[0]);
        dto.setClubId( ((Number) row[1]).longValue());

        // IFK away team first
        dto.setAwayGames(getInt(row[2]));
        dto.setHomeGames(getInt(row[3]));
        dto.setAwayGoalsConceded(getInt(row[4]));
        dto.setAwayGoalsScored(getInt(row[5]));
        dto.setHomeGoalsScored(getInt(row[6]));
        dto.setHomeGoalsConceded(getInt(row[7]));

        dto.setAwayLosses(getInt(row[8]));
        dto.setAwayDraws(getInt(row[9]));
        dto.setAwayWins(getInt(row[10]));

        dto.setHomeWins(getInt(row[11]));
        dto.setHomeDraws(getInt(row[12]));
        dto.setHomeLosses(getInt(row[13]));

        return dto;
    }

    @RequestMapping(value = "/seasons", method=RequestMethod.GET, produces = JSON)
    public List<SquadSeasonDTO> getSeasons() {
        List<SquadSeason> seasons = em.createQuery("SELECT ss FROM SquadSeason ss").getResultList();
        List<SquadSeasonDTO> retList = new ArrayList<SquadSeasonDTO>();
        for(SquadSeason ss : seasons) {
            retList.add(new SquadSeasonDTO(ss.getId(), ss.getName(), ss.getStartYear(), ss.getEndYear()));
        }
        return retList;
    }

    @RequestMapping(value = "/seasons/{id}", method=RequestMethod.GET, produces = JSON)
    public FullSquadSeasonDTO getSeason(@PathVariable Long id) {
        SquadSeason ss = em.find(SquadSeason.class, id);
        FullSquadSeasonDTO dto = new FullSquadSeasonDTO(ss.getId(), ss.getName(), ss.getStartYear(), ss.getEndYear());

        List<PlayerSeasonDTO> players = new ArrayList<PlayerSeasonDTO>();
        for(PlayedForClub pfc : ss.getSquad()) {
            PlayerSeasonDTO player = new PlayerSeasonDTO();
            player.setId(pfc.getPlayer().getId());
            player.setName(pfc.getPlayer().getName());
            player.setSquadNr(pfc.getSquadNr());
            player.setGames(countGamesOfSeason(pfc.getPlayer().getGames(), ss.getId()));
            player.setGoals(countGoalsOfSeason(pfc.getPlayer().getGames(), ss.getId()));
            players.add(player);
        }
        dto.setSquad(players);

        List<TournamentSeasonDTO> tsList = new ArrayList<TournamentSeasonDTO>();
        for(TournamentSeason ts : ss.getTournamentSeasons()) {
            TournamentSeasonDTO tsDto = new TournamentSeasonDTO();
            tsDto.setId(ts.getId());
            tsDto.setName(ts.getTournament().getName() + " " + ts.getSeasonName());
            tsDto.setGames(ts.getGames().size());
            tsDto.setGoalsScored(getGoalsScored(ts.getGames()));
            tsDto.setGoalsConceded(getGoalsConceded(ts.getGames()));
            tsList.add(tsDto);
        }
        dto.setTournamentSeasons(tsList);
        return dto;
    }

    private Integer countGoalsOfSeason(List<GameParticipation> games, Long id) {
        Integer goals = 0;
        for(GameParticipation gp : games) {
            if(gp.getGame().getTournamentSeason().getSeason().getId().equals(id)) {
                Long playerId = gp.getPlayer().getId();
                for(GameEvent ge : gp.getGame().getEvents()) {
                    if(ge.getEventType() == GameEvent.EventType.GOAL && ge.getPlayer().getId().equals(playerId)) {
                        goals++;
                    }
                }
            }
        }
        return goals;
    }

    private Integer countGamesOfSeason(List<GameParticipation> games, Long id) {
        Integer gameCount = 0;
        for(GameParticipation gp : games) {
            if(gp.getGame().getTournamentSeason().getSeason().getId().equals(id)) {
                gameCount++;
            }
        }
        return gameCount;
    }

    private Integer getGoalsConceded(List<Game> games) {
        Integer goals = 0;
        for(Game g : games) {
            if(g.getHomeTeam().getDefaultClub()) {
                goals += g.getAwayGoals();
            } else {
                goals += g.getHomeGoals();
            }
        }
        return goals;
    }

    private Integer getGoalsScored(List<Game> games) {
        Integer goals = 0;
        for(Game g : games) {
            if(g.getHomeTeam().getDefaultClub()) {
                goals += g.getHomeGoals();
            } else {
                goals += g.getAwayGoals();
            }
        }
        return goals;
    }

    @RequestMapping(value = "/clubs", method=RequestMethod.GET, produces = JSON)
    public List<ClubDTO> getClubs() {
        List<Club> clubs = em.createQuery("SELECT c FROM Club c ORDER BY c.name").getResultList();
        List<ClubDTO> retList = new ArrayList<ClubDTO>();
        for(Club c : clubs) {
            ClubDTO dto = new ClubDTO();
            dto.setId(c.getId());
            dto.setName(c.getName());
            retList.add(dto);
        }
        return retList;
    }
}
