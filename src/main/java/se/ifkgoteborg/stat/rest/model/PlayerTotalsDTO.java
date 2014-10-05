package se.ifkgoteborg.stat.rest.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-10-05
 * Time: 12:50
 * To change this template use File | Settings | File Templates.
 */
public class PlayerTotalsDTO implements Serializable {

    private Integer games = 0;
    private Integer goals = 0;
    private Integer substituteIn = 0;
    private Integer substituteOut = 0;
    private Integer goalsAsSubstituteIn = 0;
    private Integer goalsAsSubstituteOut = 0;

    public Integer getGames() {
        return games;
    }

    public void setGames(Integer games) {
        this.games = games;
    }

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public Integer getSubstituteIn() {
        return substituteIn;
    }

    public void setSubstituteIn(Integer substituteIn) {
        this.substituteIn = substituteIn;
    }

    public Integer getSubstituteOut() {
        return substituteOut;
    }

    public void setSubstituteOut(Integer substituteOut) {
        this.substituteOut = substituteOut;
    }

    public Integer getGoalsAsSubstituteIn() {
        return goalsAsSubstituteIn;
    }

    public void setGoalsAsSubstituteIn(Integer goalsAsSubstituteIn) {
        this.goalsAsSubstituteIn = goalsAsSubstituteIn;
    }

    public Integer getGoalsAsSubstituteOut() {
        return goalsAsSubstituteOut;
    }

    public void setGoalsAsSubstituteOut(Integer goalsAsSubstituteOut) {
        this.goalsAsSubstituteOut = goalsAsSubstituteOut;
    }
}
