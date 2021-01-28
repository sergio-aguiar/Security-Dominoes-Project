package DominoesMisc;

import java.io.Serializable;

public class DominoesSymKeyMatrix implements Serializable
{
    private static final long serialVersionUID = 1103L;

    private final String[] players;
    private final byte[][][] symKeyMatrix;

    public DominoesSymKeyMatrix(String[] players)
    {
        this.players = players;
        this.symKeyMatrix = new byte[players.length][players.length][];
    }

    public String[] getPlayers()
    {
        return this.players;
    }

    public byte[][][] getSymKeyMatrix()
    {
        return this.symKeyMatrix;
    }

    public void updateKey(int player1, int player2, byte[] symKey)
    {
        this.symKeyMatrix[player1][player2] = symKey;
    }
}
