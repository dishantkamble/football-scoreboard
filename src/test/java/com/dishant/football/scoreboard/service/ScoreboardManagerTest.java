package com.dishant.football.scoreboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.dishant.football.scoreboard.exception.ForbiddenException;
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
		this.scoreboardManager.startMatch("teamA", "teamB");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();
	}

	@Test
	void testStartNewMatchSuccessWithMultipleHome() {
		this.scoreboardManager.startMatch("teamC", "teamD");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		this.scoreboardManager.startMatch("teamE", "teamD");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();
	}

	@Test
	void testStartNewMatchSuccessWithMultipleAway() {
		this.scoreboardManager.startMatch("teamF", "teamG");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		this.scoreboardManager.startMatch("teamF", "teamH");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();
	}

	@Test
	void testStartNewMatchFailWithForbiddenException() {
		this.scoreboardManager.startMatch("teamI", "teamJ");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		assertThatThrownBy(() -> this.scoreboardManager.startMatch("teamI", "teamJ"))
				.isInstanceOf(ForbiddenException.class);
	}

	@Test
	void testUpdateScoreSuccess() {
		UUID matchId = this.scoreboardManager.startMatch("teamK1", "teamL1");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		this.scoreboardManager.updateScore(matchId, 1, 0);
		assertThat(this.scoreboardManager.getScoreboard().getMatches().get(matchId).getScore())
				.containsEntry(TeamType.HOME, 1);
	}

	@Test
	void testUpdateHomeScoreSuccess() {
		UUID matchId = this.scoreboardManager.startMatch("teamK", "teamL");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		this.scoreboardManager.updateScore(matchId, TeamType.HOME);
		assertThat(this.scoreboardManager.getScoreboard().getMatches().get(matchId).getScore())
				.containsEntry(TeamType.HOME, 1);
	}

	@Test
	void testUpdateAwayScoreSuccess() {
		UUID matchId = this.scoreboardManager.startMatch("teamM", "teamN");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		this.scoreboardManager.updateScore(matchId, TeamType.AWAY);
		assertThat(this.scoreboardManager.getScoreboard().getMatches().get(matchId).getScore())
				.containsEntry(TeamType.AWAY, 1);
	}

	@Test
	void testUpdateAwayScoreFail() {
		UUID matchId = this.scoreboardManager.startMatch("teamO", "teamP");
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
		UUID matchId = this.scoreboardManager.startMatch("teamQ", "teamR");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		this.scoreboardManager.endMatch(matchId);
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isZero();
	}

	@Test
	void testEndMatchByNameSuccess() {
		this.scoreboardManager.startMatch("teamQ1", "teamR1");
		int ongoingMatches = this.scoreboardManager.getScoreboard().getMatches().size();
		assertThat(ongoingMatches).isNotZero();

		this.scoreboardManager.endMatch("teamQ1", "teamR1");
		assertThat(this.scoreboardManager.getScoreboard().getMatches()).hasSize(ongoingMatches - 1);
	}

	@Test
	void testEndMatchFail() {
		assertThatThrownBy(() -> this.scoreboardManager.endMatch(UUID.randomUUID()))
				.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void testSummarySuccess() {
		this.scoreboardManager.startMatch("teamS", "teamT");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		assertThat(this.scoreboardManager.summary().size()).isNotZero();
	}

	@Test
	void testMultipleSummarySuccess() {
		this.scoreboardManager.startMatch("teamU", "teamV");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		this.scoreboardManager.startMatch("teamW", "teamX");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		assertThat(this.scoreboardManager.summary().size()).isNotZero();
	}

	@Test
	void testMultipleSummarySuccessOrdered() {
		this.scoreboardManager.startMatch("teamY", "teamZ");
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		UUID matchTwoId = this.scoreboardManager.startMatch("teamAA", "teamAB");
		this.scoreboardManager.updateScore(matchTwoId, TeamType.HOME);
		assertThat(this.scoreboardManager.getScoreboard().getMatches().size()).isNotZero();

		List<Match> orderedSummary = this.scoreboardManager.summary();
		assertThat(orderedSummary.size()).isNotZero();
		assertThat(orderedSummary.get(0).getId()).isEqualTo(matchTwoId);
	}
}
