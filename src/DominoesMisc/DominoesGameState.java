package DominoesMisc;

import java.util.HashSet;

public class DominoesGameState
{
    private final HashSet<String> playedPieces;
    private final HashSet<String> endPoints;

    public DominoesGameState()
    {
        this.playedPieces = new HashSet<>();
        this.endPoints = new HashSet<>();
    }

    public boolean playPiece(String endPoint, String piece)
    {
        this.playedPieces.add(piece);

        String[] edges = piece.split("\\|");
        if (!edges[0].equals(endPoint)) this.endPoints.add(edges[0]);
        else this.endPoints.add(edges[1]);
        
        return this.endPoints.contains(endPoint);
    }

    public HashSet<String> getPlayedPieces()
    {
        return playedPieces;
    }

    public HashSet<String> getEndPoints()
    {
        return endPoints;
    }
}
