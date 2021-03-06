package DominoesMisc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class DominoesGameState implements Serializable
{
    private static final long serialVersionUID = 1104L;

    private final HashSet<String> playedPieces;
    private final ArrayList<String> endPoints;
    private final ArrayList<ArrayList<String>> pastMoves;
    private final boolean[] cheaters;
    private final boolean[] denounced;

    private int winner;

    public DominoesGameState(int playerCap)
    {
        this.playedPieces = new HashSet<>();
        this.endPoints = new ArrayList<>();
        this.pastMoves = new ArrayList<>();
        this.cheaters = new boolean[playerCap];
        this.denounced = new boolean[playerCap];

        for (int i = 0; i < playerCap; i++)
        {
            this.pastMoves.add(new ArrayList<>());
            this.cheaters[i] = false;
            this.denounced[i] = false;
        }

        this.winner = -1;
    }

    public boolean playPiece(String targetEndPoint, String piece, String pieceEndPoint,  int player)
    {
        boolean valid = false;

        if (targetEndPoint.equals("First") && this.playedPieces.isEmpty())
        {
            String[] edges = piece.split("\\|");

            if (!edges[0].equals(edges[1]))
            {
                valid = false;
                this.endPoints.add(edges[0]);
                this.endPoints.add(edges[1]);
            }
            else
            {
                valid = true;
                for (int i = 0; i < 4; i++) this.endPoints.add(edges[0]);
            }
        }
        else if (targetEndPoint.equals("First"))
        {
            System.out.println("\n[CLIENT] Unexpected Error...");
            System.exit(703);
        }
        else
        {
            valid = this.endPoints.contains(targetEndPoint)
                    && targetEndPoint.equals(pieceEndPoint)
                    && piece.contains(targetEndPoint);

            String[] edges = piece.split("\\|");

            if (valid)
            {
                if (edges[0].equals(edges[1]))
                {
                    this.endPoints.add(edges[0]);
                    this.endPoints.add(edges[1]);
                }
                else
                {
                    if (edges[0].equals(targetEndPoint))
                    {
                        this.endPoints.remove(edges[0]);
                        this.endPoints.add(edges[1]);
                    }
                    else
                    {
                        this.endPoints.remove(edges[1]);
                        this.endPoints.add(edges[0]);
                    }
                }
            }
            else
            {
                if (edges[0].equals(edges[1]))
                {
                    if (!this.endPoints.contains(edges[0]))
                    {
                        this.endPoints.remove(0);
                        this.endPoints.add(edges[0]);
                    }
                    this.endPoints.add(edges[0]);
                    this.endPoints.add(edges[0]);
                }
                else
                {
                    if (!this.endPoints.contains(targetEndPoint)) this.endPoints.remove(0);
                    else this.endPoints.remove(targetEndPoint);

                    if (edges[0].equals(pieceEndPoint)) this.endPoints.add(edges[1]);
                    else if (edges[1].equals(pieceEndPoint)) this.endPoints.add(edges[0]);
                    else
                    {
                        System.out.println("\n[CLIENT] Unexpected Error...");
                        System.exit(703);
                    }
                }
            }
        }
        
        this.playedPieces.add(piece);
        this.pastMoves.get(player).add(piece);

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

    public boolean[] getDenounced()
    {
        return this.denounced;
    }

    public void setWinner(int winner)
    {
        this.winner = winner;
    }

    public void setDenounced(int player)
    {
        this.denounced[player] = true;
    }

    public void setCheater(int player)
    {
        this.cheaters[player] = true;
    }

    public boolean isCheater(int player)
    {
        return this.cheaters[player];
    }

    @Override
    public String toString()
    {
        return "DominoesGameState{" +
                "playedPieces=" + this.playedPieces +
                ", endPoints=" + this.endPoints +
                ", pastMoves" + this.pastMoves +
                '}';
    }
}
