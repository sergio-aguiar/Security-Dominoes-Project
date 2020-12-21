package DominoesMisc;

import java.io.Serializable;

public class DominoesCommitData implements Serializable
{
    private static final long serialVersionUID = 1104L;

    private final String[] pieces;
    private final Integer random1;
    private final Integer random2;
    private final Integer bitCommitment;

    public DominoesCommitData(int random1, Integer bitCommitment)
    {
        this.pieces = null;
        this.random1 = random1;
        this.random2 = null;
        this.bitCommitment = bitCommitment;
    }

    public DominoesCommitData(String[] pieces, int random2)
    {
        this.pieces = pieces;
        this.random1 = null;
        this.random2 = random2;
        this.bitCommitment = null;
    }

    public String[] getPieces()
    {
        return this.pieces;
    }

    public Integer getRandom1()
    {
        return random1;
    }

    public Integer getRandom2()
    {
        return random2;
    }

    public Integer getBitCommitment()
    {
        return bitCommitment;
    }

    public boolean hasPieces()
    {
        return this.pieces != null;
    }

    public boolean hasRandom1()
    {
        return this.random1 != null;
    }

    public boolean hasRandom2()
    {
        return this.random2 != null;
    }

    public boolean hasBitCommitment()
    {
        return this.bitCommitment != null;
    }

    public static int generateHash(int random1, int random2, String[] pieces)
    {
        int hash = 97 * (random1 + 89) * (random2 + 83);
        for (String piece : pieces) {
            String[] pieceEnds = piece.split("\\|");
            hash += (79 + (Integer.parseInt(pieceEnds[0]) + Integer.parseInt(pieceEnds[1]))
                    * Integer.parseInt(pieceEnds[1]));
        }
        return hash;
    }
}
