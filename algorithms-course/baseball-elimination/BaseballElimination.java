import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class BaseballElimination {
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        String line = in.readLine();
        int numberOfTeams = Integer.parseInt(line);

        for (int i = 0; i < numberOfTeams; i++) {
            line = in.readLine();
            String[] parts = line.split(" +");
            String teamName = parts[0];
            int wins = Integer.parseInt(parts[1]);
            int losses = Integer.parseInt(parts[2]);
            int remainingGames = Integer.parseInt(parts[3]);

            StdOut.printf("Team: %s, Wins: %d, Losses: %d, Remaining: %d\n", teamName, wins, losses, remainingGames);

            for (int j = 0; j < numberOfTeams; j++) {
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return 0;
    }

    // all teams
    public Iterable<String> teams() {
        return null;
    }

    // number of wins for a given team
    public int wins(String team) {
        return 0;
    }
    
    // number of remaining games for given team
    public int remaining(String team) {
        return 0;
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return 0;
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
