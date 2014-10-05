package se.ifkgoteborg.stat.rest.model;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatDTO {
	private List<GoalsPerTournamentDTO> goalsPerTournament = new ArrayList<GoalsPerTournamentDTO>();
	private List<GamePositionStatDTO> gamesPerPosition = new ArrayList<GamePositionStatDTO>();
	private List<PlayedWithPlayerDTO> playedWithPlayer = new ArrayList<PlayedWithPlayerDTO>();
	private List<AveragesPerGameAndTournamentDTO> averagesPerTournament = new ArrayList<AveragesPerGameAndTournamentDTO>();

	public List<GoalsPerTournamentDTO> getGoalsPerTournament() {
		return goalsPerTournament;
	}
	public void setGoalsPerTournament(List<GoalsPerTournamentDTO> goalsPerTournament) {
		this.goalsPerTournament = goalsPerTournament;
	}
	public List<GamePositionStatDTO> getGamesPerPosition() {
		return gamesPerPosition;
	}
	public void setGamesPerPosition(List<GamePositionStatDTO> gamesPerPosition) {
		this.gamesPerPosition = gamesPerPosition;
	}
	public List<PlayedWithPlayerDTO> getPlayedWithPlayer() {
		return playedWithPlayer;
	}
	public void setPlayedWithPlayer(List<PlayedWithPlayerDTO> playedWithPlayer) {
		this.playedWithPlayer = playedWithPlayer;
	}
	public List<AveragesPerGameAndTournamentDTO> getAveragesPerTournament() {
		return averagesPerTournament;
	}
	public void setAveragesPerTournament(
			List<AveragesPerGameAndTournamentDTO> averagesPerTournament) {
		this.averagesPerTournament = averagesPerTournament;
	}

    public PlayerTotalsDTO getTotals() {
        PlayerTotalsDTO dto = new PlayerTotalsDTO();
        for(AveragesPerGameAndTournamentDTO avg : getAveragesPerTournament()) {
            dto.setGames(dto.getGames() + avg.getTotalGames());
            dto.setGoals(dto.getGoals() + avg.getGoals());
            dto.setSubstituteIn(dto.getSubstituteIn() + avg.getGamesAsSubstituteIn());
            dto.setSubstituteOut(dto.getSubstituteOut() + avg.getGamesAsSubstituteOut());
            dto.setGoalsAsSubstituteIn(dto.getGoalsAsSubstituteIn() + avg.getGoalsAsSubstituteIn());
            dto.setGoalsAsSubstituteOut(dto.getGoalsAsSubstituteOut() + avg.getGoalsAsSubstituteOut());
        }
        return dto;
    }
}
