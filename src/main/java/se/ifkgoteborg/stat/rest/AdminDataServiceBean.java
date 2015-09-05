package se.ifkgoteborg.stat.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import se.ifkgoteborg.stat.model.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
@RequestMapping("/rest/admin")
public class AdminDataServiceBean {
	
	@PersistenceContext
	EntityManager em;

    @Autowired
    Environment env;

    @RequestMapping(method = RequestMethod.PUT, value = "/players/{id}", produces = "application/json", consumes = "application/json")
    @Transactional
    public ResponseEntity<Player> savePlayer(@PathVariable Long id, @RequestBody Player player) {
        Player dbPlayer = em.find(Player.class, id);
        if (dbPlayer != null) {
            mergePlayer(player, dbPlayer);

            return new ResponseEntity<Player>(em.merge(dbPlayer), HttpStatus.OK);
        }
        return new  ResponseEntity<Player>(player, HttpStatus.NOT_FOUND);
    }

    private void mergePlayer(Player player, Player dbPlayer) {
        dbPlayer.setBiography(player.getBiography());
        dbPlayer.setDateOfBirth(player.getDateOfBirth());
        dbPlayer.setLength(player.getLength());
        dbPlayer.setWeight(player.getWeight());
        dbPlayer.setMotherClub(player.getMotherClub());
        dbPlayer.setPlayedForClubs(player.getPlayedForClubs());
        dbPlayer.setName(player.getName());
        dbPlayer.setSquadNumber(player.getSquadNumber());

        if(player.getPositionType() != null) {
            PositionType pt = em.find(PositionType.class, player.getPositionType().getId());
            dbPlayer.setPositionType(pt);
        }

        if(player.getNationality() != null) {
            Country c = em.find(Country.class, player.getNationality().getId());
            dbPlayer.setNationality(c);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/player", produces = "application/json", consumes = "application/json")
	public ResponseEntity<Player> savePlayer(Player player) {
		Player dbPlayer = em.find(Player.class, player.getId());
        mergePlayer(player, dbPlayer);

        return new ResponseEntity<Player>(em.merge(dbPlayer), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/game/{gameId}/stats", produces = "application/json", consumes = "application/json")
	public ResponseEntity<GameStatistics> saveGameStats(@PathVariable Long gameId, GameStatistics gs) {
		Game g = em.find(Game.class, gameId);
		GameStatistics dbGs = g.getGameStats();
		dbGs.setCornersAwayTeam(gs.getCornersAwayTeam());
		dbGs.setCornersHomeTeam(gs.getCornersHomeTeam());
		dbGs.setFreekicksAwayTeam(gs.getFreekicksAwayTeam());
		dbGs.setFreekicksHomeTeam(gs.getFreekicksHomeTeam());
		dbGs.setOffsidesAwayTeam(gs.getOffsidesAwayTeam());
		dbGs.setOffsidesHomeTeam(gs.getOffsidesHomeTeam());
		dbGs.setPossessionAwayTeam(gs.getPossessionAwayTeam());
		dbGs.setPossessionHomeTeam(gs.getPossessionHomeTeam());
		dbGs.setShotsOffGoalAwayTeam(gs.getShotsOffGoalAwayTeam());
		dbGs.setShotsOffGoalHomeTeam(gs.getShotsOffGoalHomeTeam());
		dbGs.setShotsOnGoalAwayTeam(gs.getShotsOnGoalAwayTeam());
		dbGs.setShotsOnGoalHomeTeam(gs.getShotsOnGoalHomeTeam());
		dbGs.setThrowinsAwayTeam(gs.getThrowinsAwayTeam());
		dbGs.setThrowinsHomeTeam(gs.getThrowinsHomeTeam());
		return new ResponseEntity<GameStatistics>(em.merge(dbGs), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/games/{gameId}", produces = "application/json", consumes = "application/json")
	@Transactional
    public ResponseEntity<Game> saveGameDetails(@PathVariable Long gameId, @RequestBody Game game) {
		Game db = em.find(Game.class, gameId);
		if(game.getAttendance() != null) {
			db.setAttendance(game.getAttendance());
		}
//		if(game.getAwayGoals() != null) {
//			db.setAwayGoals(game.getAwayGoals());
//		}
//
//		if(game.getAwayGoalsHalftime() != null) {
//			db.setAwayGoalsHalftime(game.getAwayGoalsHalftime());
//		}
		
		if(game.getGameSummary() != null) {
			db.setGameSummary(game.getGameSummary());
		}
		
//		if(game.getHomeGoals() != null) {
//			db.setHomeGoals(game.getHomeGoals());
//		}
//
//		if(game.getHomeGoalsHalftime() != null) {
//			db.setHomeGoalsHalftime(game.getHomeGoalsHalftime());
//		}
		
		// Entities...
		if(game.getGround() != null) {
			if(game.getGround().getId() != null) {
				Ground ground = em.find(Ground.class, game.getGround().getId());
                db.setGround(ground);
//				if(ground != null) {
//					// If same name
//					if(game.getGround().getName().equals(ground.getName())) {
//						db.setGround(ground);
//					} else {
//						ground = new Ground();
//						ground.setName(game.getGround().getName());
//						ground = em.merge(ground);
//						db.setGround(ground);
//					}
//				}
			} else {
				Ground Ground = new Ground();
				Ground.setName(game.getGround().getName());
				Ground = em.merge(Ground);
				db.setGround(Ground);
			}
		}
		
		if(game.getReferee() != null) {
			if(game.getReferee().getId() != null) {
				Referee referee = em.find(Referee.class, game.getReferee().getId());
				
				if(referee != null) {
					// If same name
					if(game.getReferee().getName().equals(referee.getName())) {
						db.setReferee(referee);
					} else {
						referee = new Referee();
						referee.setName(game.getReferee().getName());
						referee = em.merge(referee);
						db.setReferee(referee);
					}
					
				}
			} else {
				Referee referee = new Referee();
				referee.setName(game.getReferee().getName());
				referee = em.merge(referee);
				db.setReferee(referee);
			}
			
		}
        return new ResponseEntity<>(em.merge(db), HttpStatus.OK);
	}
}
