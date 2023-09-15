# football-scoreboard

Library to manage football scoreboard

## Assumptions

- Start of a new `Match` assumes
  - Initial score of 0 – 0 [`HOME` - `AWAY`]
  - `Match` is added to the `Scoreboard`
- `Update` score
  - Receives a pair of absolute scores, `HOME` team score and `AWAY` team score
- `Finish` match
  - Applies only to currently in-progress `Match`
  - Removes a `Match` from the `Scoreboard`
- Get `Summary` of `Matches` in-progress
  - Shows results ordered by their TOTAL score
  - `Matches` with the same TOTAL score will be returned ordered by the most recently started `Match` in the `Scoreboard`

## Execution

- Solution is managed using an instance of `ScoreboardManager`
- `ScoreboardManager` can be instantiated using the `getInstance()` `static` method. **This is due to the fact that `ScoreboardManager` is a `singleton class`**
- `ScoreboardManager` has the following methods for managing the lifecycle of the Matches and Scoreboard
  - `startNewMatch`
    - Parameters: Two strings representing names of the `HOME` and `AWAY` teams respectively
    - Returns: `UUID` representing the newly started `Match` Id
  - `updateScore`
    - Parameters: `UUID` representing the `Match` Id, and the `TeamType` representing an `Enum` for `HOME` and `AWAY` teams
    - Returns: A `Map` with keys representing an `Enum` for `HOME` and `AWAY` teams, while values representing `Scores` respectively
  - `endMatch`
    - Parameters: `UUID` representing the `Match` Id
    - Returns: `Void`
  - `summary`
    - Parameters: `N.A`
    - Returns: A `List` of all `Matches` in-progress
