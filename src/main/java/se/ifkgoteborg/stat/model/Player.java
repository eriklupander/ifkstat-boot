package se.ifkgoteborg.stat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Entity
@Table(name="player")
@NamedQuery(name="allPlayers", query="select p from Player p ORDER BY p.name")
public class Player {

    @Id
    @GeneratedValue
	private Long id;
	
	private String name;
	private String fullName;
	
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	
	@ManyToOne(optional = true)
	private Country nationality;
	private Integer length;
	private Integer weight;
	
	private Integer squadNumber = -1;
	
	@Lob
	private String biography;
	
	private Integer otherCompetitiveGames = 0;
	private Integer otherPracticeGames = 0;
	
	private String motherClub;
	private String playedForClubs;
	
	private String imageUrl;
	
	@ManyToOne
	private PositionType positionType;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="player")
	@JsonIgnore
	private List<PlayedForClub> clubs = new ArrayList<PlayedForClub>();
	
	@OneToMany(mappedBy="player")
	@JsonIgnore
	private List<GameParticipation> games = new ArrayList<GameParticipation>();
	
	@OneToMany
	private List<PlayerImage> playerImages;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public Country getNationality() {
		return nationality;
	}
	public void setNationality(Country nationality) {
		this.nationality = nationality;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getBiography() {
		return biography;
	}
	public void setBiography(String biography) {
		this.biography = biography;
	}
	

	public Integer getSquadNumber() {
		if(squadNumber == null) {
			return -1;
		}
		return squadNumber;
	}

	public void setSquadNumber(Integer squadNumber) {
		this.squadNumber = squadNumber;
	}

	
	public PositionType getPositionType() {
		return positionType;
	}

	public void setPositionType(PositionType positionType) {
		this.positionType = positionType;
	}

	public List<PlayedForClub> getClubs() {
		return clubs;
	}

	public void setClubs(List<PlayedForClub> clubs) {
		this.clubs = clubs;
	}

	public Integer getOtherCompetitiveGames() {
		return otherCompetitiveGames;
	}

	public void setOtherCompetitiveGames(Integer otherCompetitiveGames) {
		this.otherCompetitiveGames = otherCompetitiveGames;
	}

	public Integer getOtherPracticeGames() {
		return otherPracticeGames;
	}

	public void setOtherPracticeGames(Integer otherPracticeGames) {
		this.otherPracticeGames = otherPracticeGames;
	}
	
	public String toString() {
		return this.name;
	}
	
	

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getMotherClub() {
		return motherClub;
	}

	public void setMotherClub(String motherClub) {
		this.motherClub = motherClub;
	}

	public String getPlayedForClubs() {
		return playedForClubs;
	}

	public void setPlayedForClubs(String playedForClubs) {
		this.playedForClubs = playedForClubs;
	}

	public List<GameParticipation> getGames() {
		return games;
	}

	public void setGames(List<GameParticipation> games) {
		this.games = games;
	}

	@JsonIgnore
	public List<PlayerImage> getPlayerImages() {
		return playerImages;
	}

	public void setPlayerImages(List<PlayerImage> playerImages) {
		this.playerImages = playerImages;
	}

	@Transient
	public Integer getNumberOfGames() {
		try {
			return games.size();
		} catch (Throwable e) {
			return 0;
		}
	}
	
	// Dummy setter to make Jackson not barf
	public void setNumberOfGames(Integer numberOfGames) {
		
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
		Player other = (Player) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
