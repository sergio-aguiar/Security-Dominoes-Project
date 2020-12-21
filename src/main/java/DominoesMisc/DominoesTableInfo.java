package DominoesMisc;

import java.io.Serializable;

public class DominoesTableInfo implements Serializable
{
    private static final long serialVersionUID = 1108L;

    private final int id;
    private final String[] players;
    private final boolean[] readyStates;

    public DominoesTableInfo(int id, String[] players, boolean[] readyStates)
    {
        this.id = id;
        this.players = players;
        this.readyStates = readyStates;
    }

    public int getId()
    {
        return this.id;
    }

    public String[] getPlayers()
    {
        return this.players;
    }

    public boolean[] getReadyStates()
    {
        return this.readyStates;
    }

    @Override
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder("Table #" + this.id + ":\n");
        for (int i = 0; i < this.players.length; i++)
        {
            if (this.players[i] != null) {
                toReturn.append((this.readyStates[i]) ? "  <READY>  " : "<NOT READY>");
                toReturn.append(" - ").append(this.players[i]).append("\n");
            } else {
                toReturn.append("<NOT READY> - EMPTY SLOT\n");
            }
        }

        return toReturn.toString();
    }
}
