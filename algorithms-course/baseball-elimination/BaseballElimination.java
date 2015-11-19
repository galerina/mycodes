import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

import java.util.Arrays;
import java.util.ArrayList;

public class BaseballElimination {
    private final String[] teams;
    private final int[] remaining;
    private final int[] wins;
    private final int[] losses;
    private final int[][] games;
    private boolean isEliminated;
    private ArrayList<String> certificateOfElimination;
    
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        String line = in.readLine();
        int numberOfTeams = Integer.parseInt(line);

        teams = new String[numberOfTeams];
        remaining = new int[numberOfTeams];
        wins = new int[numberOfTeams];
        losses = new int[numberOfTeams];
        games = new int[numberOfTeams][numberOfTeams];

        for (int i = 0; i < numberOfTeams; i++) {
            teams[i] = in.readString();
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();

            // StdOut.printf("Team: %s, Wins: %d, Losses: %d, Remaining: %d\n", teamName, wins, losses, remainingGames);
            for (int j = 0; j < numberOfTeams; j++) {
                games[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teams.length;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    // number of wins for a given team
    public int wins(String team) {
        int id = getTeamID(team);
        return wins[id];
    }

    // number of losses for a given team
    public int losses(String team) {
        int id = getTeamID(team);
        return losses[id];
    }
    
    // number of remaining games for given team
    public int remaining(String team) {
        int id = getTeamID(team);
        return remaining[id];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        int id1 = getTeamID(team1);
        int id2 = getTeamID(team2);
        return games[id1][id2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        calculateElimination(team);

        return isEliminated;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        calculateElimination(team);

        return certificateOfElimination;
    }

    private void calculateElimination(String team) {
        int teamID = getTeamID(team);

        // Check for trivial elimination
        for (int i = 0; i < teams.length; i++) {
            if (i != teamID && (wins[i] > (wins[teamID] + remaining[teamID]))) {
                isEliminated = true;
                certificateOfElimination = new ArrayList<String>();
                certificateOfElimination.add(teams[i]);
                return;
            }
        }

        // N = number of teams
        // Vertices 0 to N-1 represent each team.
        // Vertices N to N+N^2-1 represent the games between different
        // teams. A game between i and j is represented by 
        // node = N+min(i,j)*N+max(i,j). Nodes s and t will be the last 
        // two nodes, N+N^2, N+N^2+1
        // The nodes involving teamId will go unused.
        // The graph will contain the following edges:
        //  1) For each team i != teamId, an edge from vertex i to t with 
        //   capacity = #(wins(teamId) + remaining(teamId) - wins(i))
        //  2) For each game (i,j), an edge from s to the (i,j) node
        //   that has capacity = # of games (i,j)
        //  3) For each game (i,j), edges of infinite capacity from node 
        //   (i,j) to i and j
        int vertexCount = teams.length + (teams.length*teams.length) + 2;
        FlowNetwork flowNetwork = new FlowNetwork(vertexCount);
        int s = vertexCount - 2;
        int t = vertexCount - 1;

        for (int i = 0; i < teams.length; i++) {
            if (i != teamID) {
                // Edge type 1
                flowNetwork.addEdge(new FlowEdge(i, t, wins[teamID] + remaining[teamID] - wins[i]));
            }
        }

        int maxFlowFromS = 0;
        for (int i = 0; i < teams.length; i++) {
            for (int j = i + 1; j < teams.length; j++) {
                if (i != teamID && j != teamID) {
                    int vertex = teams.length + i * teams.length + j;

                    // Edge type 2
                    maxFlowFromS += games[i][j];
                    flowNetwork.addEdge(new FlowEdge(s, vertex, games[i][j]));

                    // Edge type 3
                    flowNetwork.addEdge(new FlowEdge(vertex, i, Double.POSITIVE_INFINITY));
                    flowNetwork.addEdge(new FlowEdge(vertex, j, Double.POSITIVE_INFINITY));
                }
            }
        }

        // StdOut.printf("%s\n", flowNetwork.toString());
        FordFulkerson fordFulker = new FordFulkerson(flowNetwork, s, t);
        isEliminated = (maxFlowFromS != fordFulker.value());

        if (isEliminated) {
            certificateOfElimination = new ArrayList<String>();
            for (int i = 0; i < teams.length; i++) {
                if (fordFulker.inCut(i)) {
                    certificateOfElimination.add(teams[i]);
                }
            }
        } else {
            certificateOfElimination = null;
        }
    }


    private int getTeamID(String name) {
        for (int i = 0; i < teams.length; i++) {
            if (teams[i].equals(name)) {
                return i;
            }
        }

        throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            StdOut.printf("Team: %s, wins: %d, remaining: %d, games:\n", team, division.wins(team),
                          division.remaining(team));
            for (String opponent : division.teams()) {
                if (!team.equals(opponent)) {
                    StdOut.printf("  against %s: %d\n", opponent, division.against(team, opponent));
                }
            }

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
