package se.ifkgoteborg.stat.rest.model;

public class PlayedWithPlayerDTO {
	private Long id;
	private String name;
	private Number gamesWithPlayer;
	
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
	public Number getGamesWithPlayer() {
		return gamesWithPlayer;
	}
	public void setGamesWithPlayer(Number gamesWithPlayer) {
		this.gamesWithPlayer = gamesWithPlayer;
	}
	
	
}
