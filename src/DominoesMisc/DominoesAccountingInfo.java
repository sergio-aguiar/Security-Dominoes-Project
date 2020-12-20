package DominoesMisc;

import java.io.Serializable;
import java.util.Arrays;

public class DominoesAccountingInfo implements Serializable
{
    private static final long serialVersionUID = 1104L;

    private final String[] players;
    private final int[] results;

    public DominoesAccountingInfo(int playerCap)
    {
        this.players = new String[playerCap];
        this.results = new int[playerCap];

        for (int i = 0; i < playerCap; i++)
        {
            this.players[0] = null;
            this.results[0] = 0;
        }
    }

    public void setAccountingInfo(int player, String pseudonym, int result)
    {
        this.players[player] = pseudonym;
        this.results[player] = result;
    }

    public String[] getPlayers()
    {
        return this.players;
    }

    public int[] getResults()
    {
        return this.results;
    }

    @Override
    public String toString()
    {
        return "DominoesAccountingInfo{" +
                "players=" + Arrays.toString(this.players) +
                ", results=" + Arrays.toString(this.results) +
                '}';
    }
}
