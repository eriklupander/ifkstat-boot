package se.ifkgoteborg.stat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="game")
public class Game {
	
	@Id
	@GeneratedValue
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	private Date dateOfGame;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private TournamentSeason tournamentSeason;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Club homeTeam;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Club awayTeam;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Ground ground;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Referee referee;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="game")
	@JsonIgnore
	private List<GameEvent> events = new ArrayList<GameEvent>();
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="game")
	@JsonIgnore
	private List<GameParticipation> gameParticipation = new ArrayList<GameParticipation>();
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	private Formation formation;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@JsonIgnore
	private List<GameNote> gameNotes = new ArrayList<GameNote>();
	
	private Integer attendance;
	
	@Lob
	private String gameSummary;
	
	private Integer homeGoals = 0;
	private Integer awayGoals = 0;
	private Integer homeGoalsHalftime = 0;
	private Integer awayGoalsHalftime = 0;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JsonIgnore
	private GameStatistics gameStats = new GameStatistics();
	
	public Date getDateOfGame() {
		return dateOfGame;
	}	
	public void setDateOfGame(Date dateOfGame) {
		this.dateOfGame = dateOfGame;
	}
	public Club getHomeTeam() {
		return homeTeam;
	}
	public void setHomeTeam(Club homeTeam) {
		this.homeTeam = homeTeam;
	}
	public Club getAwayTeam() {
		return awayTeam;
	}
	public void setAwayTeam(Club awayTeam) {
		this.awayTeam = awayTeam;
	}
	public Referee getReferee() {
		return referee;
	}
	public void setReferee(Referee referee) {
		this.referee = referee;
	}
	public List<GameEvent> getEvents() {
		return events;
	}
	public void setEvents(List<GameEvent> events) {
		this.events = events;
	}
	public Ground getGround() {
		return ground;
	}
	public void setGround(Ground ground) {
		this.ground = ground;
	}
	public Integer getAttendance() {
		return attendance;
	}
	public void setAttendance(Integer attendance) {
		this.attendance = attendance;
	}
	public String getGameSummary() {
		return gameSummary;
	}
	public void setGameSummary(String gameSummary) {
		this.gameSummary = gameSummary;
	}

	public Integer getHomeGoals() {
		return homeGoals;
	}

	public void setHomeGoals(Integer homeGoals) {
		this.homeGoals = homeGoals;
	}

	public Integer getAwayGoals() {
		return awayGoals;
	}

	public void setAwayGoals(Integer awayGoals) {
		this.awayGoals = awayGoals;
	}

	public Integer getHomeGoalsHalftime() {
		return homeGoalsHalftime;
	}

	public void setHomeGoalsHalftime(Integer homeGoalsHalftime) {
		this.homeGoalsHalftime = homeGoalsHalftime != null ? homeGoalsHalftime : 0;
	}

	public Integer getAwayGoalsHalftime() {
		return awayGoalsHalftime != null ? awayGoalsHalftime : 0;
	}

	public void setAwayGoalsHalftime(Integer awayGoalsHalftime) {
		this.awayGoalsHalftime = awayGoalsHalftime;
	}

	public List<GameParticipation> getGameParticipation() {
		return gameParticipation;
	}

	public void setGameParticipation(List<GameParticipation> gameParticipation) {
		this.gameParticipation = gameParticipation;
	}

	public Formation getFormation() {
		return formation;
	}

	public void setFormation(Formation formation) {
		this.formation = formation;
	}

	public TournamentSeason getTournamentSeason() {
		return tournamentSeason;
	}

	public void setTournamentSeason(TournamentSeason tournamentSeason) {
		this.tournamentSeason = tournamentSeason;
	}
	

	public List<GameNote> getGameNotes() {
		return gameNotes;
	}

	public void setGameNotes(List<GameNote> gameNotes) {
		this.gameNotes = gameNotes;
	}

	public GameStatistics getGameStats() {
		if(gameStats == null) {
			gameStats = new GameStatistics();
		}
		return gameStats;
	}

	public void setGameStats(GameStatistics gameStats) {
		this.gameStats = gameStats;
	}

	@Transient
	public String getResultStr() {
		return getHomeGoals() + "-" + getAwayGoals() + " (" + getHomeGoalsHalftime() + "-" + getAwayGoalsHalftime() + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
