package se.ifkgoteborg.stat.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Keeps track of statistics for a single game in a structured manner
 * @author Erik
 *
 */
@Entity
@Table(name="game_stats")
public class GameStatistics implements Serializable {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private Integer shotsOnGoalHomeTeam = null;
	private Integer shotsOnGoalAwayTeam = null;
	private Integer shotsOffGoalHomeTeam = null;
	private Integer shotsOffGoalAwayTeam = null;
	private Integer offsidesHomeTeam = null;
	private Integer offsidesAwayTeam = null;
	private Integer cornersHomeTeam = null;
	private Integer cornersAwayTeam = null;
	private Integer freekicksHomeTeam = null;
	private Integer freekicksAwayTeam = null;
	private Integer throwinsHomeTeam = null;
	private Integer throwinsAwayTeam = null;
	private Integer possessionHomeTeam = 50;
	private Integer possessionAwayTeam = 50;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Transient
	public String getShots() {
		return getShotsHomeTeam() + "-" + getShotsAwayTeam();
	}
	
	@Transient
	public String getShotsOnGoal() {
		return getShotsOnGoalHomeTeam() + "-" + getShotsOnGoalAwayTeam();
	}
	
	@Transient
	public String getShotsOffGoal() {
		return getShotsOffGoalHomeTeam() + "-" + getShotsOffGoalAwayTeam();
	}
	
	@Transient
	public String getOffsides() {
		return getOffsidesHomeTeam() + "-" + getOffsidesAwayTeam();
	}
	
	@Transient
	public String getThrowins() {
		return getThrowinsHomeTeam() + "-" + getThrowinsAwayTeam();
	}
	
	@Transient
	public String getFreekicks() {
		return getFreekicksHomeTeam() + "-" + getFreekicksAwayTeam();
	}
	
	@Transient
	public String getPossession() {
		return getPossessionHomeTeam() + "-" + getPossessionAwayTeam();
	}
	
	@Transient
	public String getCorners() {
		return getCornersHomeTeam() + "-" + getCornersAwayTeam();
	}

	@Transient
	public Integer getShotsHomeTeam() {
		return shotsOnGoalHomeTeam + shotsOffGoalHomeTeam;
	}
	
	@Transient
	public Integer getShotsAwayTeam() {
		return shotsOnGoalAwayTeam + shotsOffGoalAwayTeam;
	}
	
	public Integer getShotsOnGoalHomeTeam() {
		return shotsOnGoalHomeTeam;
	}

	public void setShotsOnGoalHomeTeam(Integer shotsOnGoalHomeTeam) {
		this.shotsOnGoalHomeTeam = shotsOnGoalHomeTeam;
	}

	public Integer getShotsOnGoalAwayTeam() {
		return shotsOnGoalAwayTeam;
	}

	public void setShotsOnGoalAwayTeam(Integer shotsOnGoalAwayTeam) {
		this.shotsOnGoalAwayTeam = shotsOnGoalAwayTeam;
	}

	public Integer getShotsOffGoalHomeTeam() {
		return shotsOffGoalHomeTeam;
	}

	public void setShotsOffGoalHomeTeam(Integer shotsOffGoalHomeTeam) {
		this.shotsOffGoalHomeTeam = shotsOffGoalHomeTeam;
	}

	public Integer getShotsOffGoalAwayTeam() {
		return shotsOffGoalAwayTeam;
	}

	public void setShotsOffGoalAwayTeam(Integer shotsOffGoalAwayTeam) {
		this.shotsOffGoalAwayTeam = shotsOffGoalAwayTeam;
	}

	public Integer getOffsidesHomeTeam() {
		return offsidesHomeTeam;
	}

	public void setOffsidesHomeTeam(Integer offsidesHomeTeam) {
		this.offsidesHomeTeam = offsidesHomeTeam;
	}

	public Integer getOffsidesAwayTeam() {
		return offsidesAwayTeam;
	}

	public void setOffsidesAwayTeam(Integer offsidesAwayTeam) {
		this.offsidesAwayTeam = offsidesAwayTeam;
	}

	public Integer getCornersHomeTeam() {
		return cornersHomeTeam;
	}

	public void setCornersHomeTeam(Integer cornersHomeTeam) {
		this.cornersHomeTeam = cornersHomeTeam;
	}

	public Integer getCornersAwayTeam() {
		return cornersAwayTeam;
	}

	public void setCornersAwayTeam(Integer cornersAwayTeam) {
		this.cornersAwayTeam = cornersAwayTeam;
	}

	public Integer getFreekicksHomeTeam() {
		return freekicksHomeTeam;
	}

	public void setFreekicksHomeTeam(Integer freekicksHomeTeam) {
		this.freekicksHomeTeam = freekicksHomeTeam;
	}

	public Integer getFreekicksAwayTeam() {
		return freekicksAwayTeam;
	}

	public void setFreekicksAwayTeam(Integer freekicksAwayTeam) {
		this.freekicksAwayTeam = freekicksAwayTeam;
	}

	public Integer getThrowinsHomeTeam() {
		return throwinsHomeTeam;
	}

	public void setThrowinsHomeTeam(Integer throwinsHomeTeam) {
		this.throwinsHomeTeam = throwinsHomeTeam;
	}

	public Integer getThrowinsAwayTeam() {
		return throwinsAwayTeam;
	}

	public void setThrowinsAwayTeam(Integer throwinsAwayTeam) {
		this.throwinsAwayTeam = throwinsAwayTeam;
	}

	public Integer getPossessionHomeTeam() {
		return possessionHomeTeam;
	}

	public void setPossessionHomeTeam(Integer possessionHomeTeam) {
		this.possessionHomeTeam = possessionHomeTeam;
	}

	public Integer getPossessionAwayTeam() {
		return possessionAwayTeam;
	}

	public void setPossessionAwayTeam(Integer possessionAwayTeam) {
		this.possessionAwayTeam = possessionAwayTeam;
	}
	
	
}
