package com.dishant.football.scoreboard.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class Scoreboard {

	private final Map<UUID, Match> matches = new HashMap<>();

	public void validateMatch(UUID matchId) {
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
