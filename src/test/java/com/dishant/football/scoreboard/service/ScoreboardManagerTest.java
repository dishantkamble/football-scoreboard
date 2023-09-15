package com.dishant.football.scoreboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.dishant.football.scoreboard.model.Match;
import com.dishant.football.scoreboard.model.TeamType;

class ScoreboardManagerTest {

	private ScoreboardManager scoreboardManager;

	@BeforeEach
	void setUpEach() {
		this.scoreboardManager = ScoreboardManager.getInstance();
	}

	@Test
	void testStartNewMatchSuccess() {
		this.scoreboardManager.startNewMatch("teamA", "teamB");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();
	}

	@Test
	void testStartNewMatchSuccessWithMultipleHome() {
		this.scoreboardManager.startNewMatch("teamC", "teamD");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		this.scoreboardManager.startNewMatch("teamE", "teamD");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();
	}

	@Test
	void testStartNewMatchSuccessWithMultipleAway() {
		this.scoreboardManager.startNewMatch("teamF", "teamG");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		this.scoreboardManager.startNewMatch("teamF", "teamH");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();
	}

	@Test
	void testStartNewMatchFailWithIllegalStateException() {
		this.scoreboardManager.startNewMatch("teamI", "teamJ");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		assertThatThrownBy(() -> this.scoreboardManager.startNewMatch("teamI", "teamJ"))
				.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void testUpdateHomeScoreSuccess() {
		UUID matchId = this.scoreboardManager.startNewMatch("teamK", "teamL");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		this.scoreboardManager.updateScore(matchId, TeamType.HOME);
		assertThat(this.scoreboardManager.getScoreboard().getMatches().get(matchId).getScore())
				.containsEntry(TeamType.HOME, 1);
	}

	@Test
	void testUpdateAwayScoreSuccess() {
		UUID matchId = this.scoreboardManager.startNewMatch("teamM", "teamN");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		this.scoreboardManager.updateScore(matchId, TeamType.AWAY);
		assertThat(this.scoreboardManager.getScoreboard().getMatches().get(matchId).getScore())
				.containsEntry(TeamType.AWAY, 1);
	}

	@Test
	void testUpdateAwayScoreFail() {
		UUID matchId = this.scoreboardManager.startNewMatch("teamO", "teamP");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		Map<TeamType, Integer> scores = this.scoreboardManager.updateScore(matchId, null);
		assertThat(scores).containsEntry(TeamType.AWAY, 0).containsEntry(TeamType.HOME, 0);
	}

	@Test
	void testUpdateScoreFail() {
		assertThatThrownBy(() -> this.scoreboardManager.updateScore(UUID.randomUUID(), TeamType.HOME))
				.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void testEndMatchSuccess() {
		UUID matchId = this.scoreboardManager.startNewMatch("teamQ", "teamR");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		this.scoreboardManager.endMatch(matchId);
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isZero();
	}

	@Test
	void testEndMatchFail() {
		assertThatThrownBy(() -> this.scoreboardManager.endMatch(UUID.randomUUID()))
				.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void testSummarySuccess() {
		this.scoreboardManager.startNewMatch("teamS", "teamT");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		assertThat(this.scoreboardManager.summary().size()).isNotZero();
	}

	@Test
	void testMultipleSummarySuccess() {
		this.scoreboardManager.startNewMatch("teamU", "teamV");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		this.scoreboardManager.startNewMatch("teamW", "teamX");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		assertThat(this.scoreboardManager.summary().size()).isNotZero();
	}

	@Test
	void testMultipleSummarySuccessOrdered() {
		this.scoreboardManager.startNewMatch("teamY", "teamZ");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		UUID matchTwoId = this.scoreboardManager.startNewMatch("teamAA", "teamAB");
		this.scoreboardManager.updateScore(matchTwoId, TeamType.HOME);
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		List<Match> orderedSummary = this.scoreboardManager.summary();
		assertThat(orderedSummary.size()).isNotZero();
		assertThat(orderedSummary.get(0).getId()).isEqualTo(matchTwoId);
	}
}
