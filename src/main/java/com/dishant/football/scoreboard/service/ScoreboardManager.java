package com.dishant.football.scoreboard.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dishant.football.scoreboard.model.Match;
import com.dishant.football.scoreboard.model.Scoreboard;
import com.dishant.football.scoreboard.model.TeamType;

import lombok.Getter;

@Getter
public final class ScoreboardManager {

	private static ScoreboardManager instance;

	private final Scoreboard scoreboard = new Scoreboard();

	private ScoreboardManager() {
	}

	static {
		try {
			instance = new ScoreboardManager();
		} catch (Exception ex) {
			throw new RuntimeException("Exception while creating ScoreboardManager instance", ex);
		}
	}

	public static ScoreboardManager getInstance() {
		return instance;
	}

	/**
	 * Starts a new match between two teams represented by parameters homeTeam and
	 * awayteam as the team names. Also adds the match to the scoreboard.
	 * 
	 * @param homeTeam
	 * @param awayTeam
	 * @return matchId of the newly started match
	 */
	public UUID startNewMatch(String homeTeam, String awayTeam) {
		if (this.scoreboard.getMatches().values().stream()
				.filter(match -> homeTeam.equalsIgnoreCase(match.getHome().getName())
						&& awayTeam.equalsIgnoreCase(match.getAway().getName()))
				.count() > 0) {
			throw new IllegalStateException(
					"Match already 'In-Progress' for Home = " + homeTeam + " and Away = " + awayTeam);
		}

		Match match = new Match(homeTeam, awayTeam);
		this.scoreboard.getMatches().put(match.getId(), match);
		return match.getId();
	}

	/**
	 * Updates the score of an existing 'In-Progress' match. Identifies the match by
	 * the matchId parameter while identifies the team by the scoringTeam parameter.
	 * 
	 * @param matchId
	 * @param scoringTeam
	 * @return the updated score for homeTeam and awayTeam
	 */
	public Map<TeamType, Integer> updateScore(UUID matchId, TeamType scoringTeam) {
		this.scoreboard.validateMatch(matchId);

		Match validMatch = this.scoreboard.getMatches().get(matchId);

		if (TeamType.HOME.equals(scoringTeam)) {
			validMatch.scoreForHome();
		} else if (TeamType.AWAY.equals(scoringTeam)) {
			validMatch.scoreForAway();
		}
		return validMatch.getScore();
	}

	/**
	 * Ends an existing 'In-Progress' match. Identifies the match by the matchId
	 * parameter. Also removes the match from the scoreboard.
	 * 
	 * @param matchId
	 */
	public void endMatch(UUID matchId) {
		this.scoreboard.validateMatch(matchId);
		this.scoreboard.getMatches().get(matchId).endMatch();
		this.scoreboard.getMatches().remove(matchId);
	}

	/**
	 * Provides a summary of all 'In-Progress' matches. These are ordered in
	 * descending based on the total scores. If the total score of matches are
	 * identical, the list is then sorted based on their startTime in descending
	 * order.
	 * 
	 * @return List of 'In-Progress' matches in sorted order.
	 */
	public List<Match> summary() {
		return this.scoreboard.getSummary();
	}
}
