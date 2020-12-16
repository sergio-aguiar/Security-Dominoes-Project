package DominoesMisc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class DominoesGameState implements Serializable
{
    private static final long serialVersionUID = 1104L;

    private final HashSet<String> playedPieces;
    private final ArrayList<String> endPoints;

    private int winner;

    public DominoesGameState()
    {
        this.playedPieces = new HashSet<>();
        this.endPoints = new ArrayList<>();
        this.winner = -1;
    }

    public boolean playPiece(String endPoint, String piece)
    {
        boolean valid = this.endPoints.contains(endPoint);
        this.playedPieces.add(piece);

        String[] edges = piece.split("\\|");
        if (edges[0].equals(endPoint))
        {
            if (edges[0].equals(edges[1])) this.endPoints.add(edges[0]);
            this.endPoints.add(edges[1]);
        }
        else if (edges[1].equals(endPoint)) this.endPoints.add(edges[0]);

        return valid;
    }

    public HashSet<String> getPlayedPieces()
    {
        return this.playedPieces;
    }

    public ArrayList<String> getEndPoints()
    {
        return this.endPoints;
    }

    public int getWinner()
    {
        return this.winner;
    }

    @Override
    public String toString()
    {
        return "DominoesGameState{" +
                "playedPieces=" + playedPieces +
                ", endPoints=" + endPoints +
                '}';
    }
}
