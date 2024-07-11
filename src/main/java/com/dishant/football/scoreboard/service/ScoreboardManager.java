package com.dishant.football.scoreboard.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dishant.football.scoreboard.model.Match;
import com.dishant.football.scoreboard.model.Scoreboard;
import com.dishant.football.scoreboard.model.TeamType;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public final class ScoreboardManager {

	private final Scoreboard scoreboard = new Scoreboard();

	private ScoreboardManager() {
	}

	private static class ScoreboardManagerHelper {
		private static final ScoreboardManager INSTANCE = new ScoreboardManager();
	}

	/**
	 * Returns an instance of ScoreboardManager using the Bill Pugh implementation
	 * for thread safe implementation of singleton.
	 * 
	 * @return ScoreboardManagerHelper.INSTANCE
	 *
	 */
	public static ScoreboardManager getInstance() {
		return ScoreboardManagerHelper.INSTANCE;
	}

	/**
	 * Starts a new match between two teams represented by parameters homeTeam and
	 * awayTeam as the team names. Also adds the match to the scoreboard.
	 * 
	 * @param homeTeam
	 * @param awayTeam
	 * @return id of the newly started match
	 */
	public UUID startMatch(String homeTeam, String awayTeam) {
		return this.scoreboard.addMatch(new Match(homeTeam, awayTeam)).getId();
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
		Match match = this.scoreboard.getMatch(matchId);

		if (TeamType.HOME.equals(scoringTeam)) {
			match.scoreForHome();
		} else if (TeamType.AWAY.equals(scoringTeam)) {
			match.scoreForAway();
		}
		return match.getScore();
	}

	/**
	 * Updates the score of an existing 'In-Progress' match. Identifies the match by
	 * the matchId parameter while identifies the team by the scoringTeam parameter.
	 * 
	 * @param matchId
	 * @param homeScore
	 * @param awayScore
	 * @return the updated score for homeTeam and awayTeam
	 */
	public Map<TeamType, Integer> updateScore(UUID matchId, int homeScore, int awayScore) {
		Match match = this.scoreboard.getMatch(matchId);
		return match.overrideScore(homeScore, awayScore);
	}

	/**
	 * Ends an existing 'In-Progress' match. Identifies the match by the matchId
	 * parameter. Also removes the match from the scoreboard.
	 * 
	 * @param matchId
	 */
	public void endMatch(UUID matchId) {
		this.scoreboard.endMatch(matchId);
	}

	/**
	 * Ends an existing 'In-Progress' match. Identifies the match by the name of the
	 * homeTeam and awayTeam arguments. Also removes the match from the scoreboard.
	 * 
	 * @param homeTeam
	 * @param awayTeam
	 */
	public void endMatch(String homeTeam, String awayTeam) {
		Match match = this.scoreboard.getMatch(homeTeam, awayTeam);
		this.endMatch(match.getId());
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
		List<Match> summary = this.scoreboard.getSummary();
		this.print(summary);
		return summary;
	}

	private void print(List<Match> summary) {
		summary.stream().forEach(match -> log.info("{} {} - {} {}", match.getHome().getName(),
				match.getScore(TeamType.HOME), match.getAway().getName(), match.getScore(TeamType.AWAY)));
	}
}
