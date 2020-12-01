package DominoesMisc;

import java.io.Serializable;
import java.util.Arrays;

public class DominoesTable implements Serializable
{
    private static final long serialVersionUID = 1103L;
    private static int gID = 0;

    private final int id;
    private final String[] players;
    private final boolean[] readyStates;

    private boolean started;
    private int turn;

    public DominoesTable(int playerCap, String tableLeader) throws DominoesTableException
    {
        if (!this.isValidPlayerCap(playerCap)) throw new DominoesTableException("Invalid player capacity value!");

        this.id = gID++;

        this.players = new String[playerCap];
        this.players[0] = tableLeader;
        this.readyStates = new boolean[playerCap];
        this.readyStates[0] = true;

        for (int i = 1; i < playerCap; i++)
        {
            this.players[i] = null;
            this.readyStates[i] = false;
        }

        this.started = false;
        this.turn = 0;
    }

    private boolean isValidPlayerCap(int playerCap)
    {
        return playerCap > 1 && playerCap < 8;
    }

    public boolean joinTable(String pseudonym)
    {
        boolean joined = false;
        for (int i = 1; i < this.players.length; i++) if (this.players[i] == null) {
            this.players[i] = pseudonym;
            joined = true;
            break;
        }
        return joined;
    }

    public boolean leaveTable(String pseudonym)
    {
        boolean left = false;
        for (int i = 1; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
        {
            this.players[i] = null;
            left = true;
            break;
        }
        return left;
    }

    public void performTurn()
    {
        if (this.turn == this.players.length - 1) this.turn = 0;
        else this.turn++;
    }

    public boolean isTurn(String pseudonym)
    {
        System.out.println("Turn: " + this.turn);
        System.out.println("Players: " + Arrays.toString(this.players));
        System.out.println("Players[Turn]: " + this.players[this.turn]);
        System.out.println("Pseudonym: " + pseudonym);

        return this.players[this.turn].equals(pseudonym);
    }

    public int getId()
    {
        return this.id;
    }

    public int getPlayerCap()
    {
        return this.players.length;
    }

    public String[] getPlayers()
    {
        return this.players;
    }

    public boolean[] getReadyStates()
    {
        return this.readyStates;
    }

    public void setReady(String pseudonym)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i] != null && this.players[i].equals(pseudonym))
        {
            this.readyStates[i] = true;
            break;
        }
    }

    public boolean hasStarted()
    {
        return this.started;
    }

    public int getTurn()
    {
        return this.turn;
    }

    public void startGame()
    {
        this.started = true;
    }

    public boolean isFull()
    {
        for (String player : this.players) if (player == null) return false;
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder("Table ID = " + this.id + "\n");
        for (int i = 0; i < this.players.length; i++) if (this.players[i] != null)
        {
            toReturn.append((this.readyStates[i]) ? "<READY>" : "<NOT READY>");
            toReturn.append(" ").append(this.players[i]).append("\n");
        }

        return toReturn.toString();
    }
}
