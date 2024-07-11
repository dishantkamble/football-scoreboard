package com.dishant.football.scoreboard.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.dishant.football.scoreboard.exception.ForbiddenException;
import com.dishant.football.scoreboard.exception.NotFoundException;

import lombok.Data;

@Data
public class Scoreboard {

	private final Map<UUID, Match> matches = new HashMap<>();

	public Match getMatch(UUID matchId) {
		this.validate(matchId);
		return this.matches.get(matchId);
	}

	public Match getMatch(String homeTeam, String awayTeam) {
		return this.matches.values().stream()
				.filter(m -> m.getHome().getName().equalsIgnoreCase(homeTeam)
						&& m.getAway().getName().equalsIgnoreCase(awayTeam))
				.findFirst().orElseThrow(() -> new NotFoundException(
						"Match not found between Home: " + homeTeam + " and Away: " + awayTeam));
	}

	public Match addMatch(Match match) {
		if (this.matches.values().stream().filter(m -> match.getHome().getName().equalsIgnoreCase(m.getHome().getName())
				&& match.getAway().getName().equalsIgnoreCase(m.getAway().getName())).count() > 0) {
			throw new ForbiddenException("Match already 'In-Progress' between Home = " + match.getHome().getName()
					+ " and Away = " + match.getAway().getName());
		}
		this.matches.put(match.getId(), match);
		return match;
	}

	public void endMatch(UUID matchId) {
		this.validate(matchId);
		this.matches.get(matchId).end();
		this.matches.remove(matchId);
	}

	private void validate(UUID matchId) {
		if (!this.matches.containsKey(matchId)) {
			throw new IllegalStateException("Match Does not Exist");
		}
	}

	public List<Match> getSummary() {
		List<Match> summary = this.matches.values().stream().collect(Collectors.toList());

		if (summary.size() <= 1) {
			return summary;
		}

		summary.sort((Match m1, Match m2) -> {
			int goalComparision = this.compareGoalsDescending(m1, m2);

			if (goalComparision == 0) {
				return m2.getStartedAt().compareTo(m1.getStartedAt());
			}
			return goalComparision;
		});
		return summary;
	}

	private int compareGoalsDescending(Match m1, Match m2) {
		int totalGoalsM1 = m1.getScore().values().stream().mapToInt(Integer::intValue).sum();
		int totalGoalsM2 = m2.getScore().values().stream().mapToInt(Integer::intValue).sum();
		return totalGoalsM2 - totalGoalsM1;
	}
}
