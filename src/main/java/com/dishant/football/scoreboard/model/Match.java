package com.dishant.football.scoreboard.model;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Match {

	private UUID id;

	private MatchStatus status;

	private Instant startedAt;

	private Team home;

	private Team away;

	private Map<TeamType, Integer> score = new EnumMap<>(TeamType.class);

	public Match(String homeTeam, String awayTeam) {
		this.id = UUID.randomUUID();
		this.status = MatchStatus.INPROGRESS;
		this.home = new Team(homeTeam, TeamType.HOME);
		this.away = new Team(awayTeam, TeamType.AWAY);
		this.startedAt = Instant.now();
		this.score.put(TeamType.HOME, 0);
		this.score.put(TeamType.AWAY, 0);
	}

	public Map<TeamType, Integer> scoreForHome() {
		this.score.put(TeamType.HOME, this.score.get(TeamType.HOME) + 1);
		return this.score;
	}

	public Map<TeamType, Integer> scoreForAway() {
		this.score.put(TeamType.AWAY, this.score.get(TeamType.AWAY) + 1);
		return this.score;
	}

	public void endMatch() {
		this.status = MatchStatus.COMPLETED;
	}
}
