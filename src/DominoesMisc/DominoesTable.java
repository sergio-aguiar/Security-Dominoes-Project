package DominoesMisc;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

public class DominoesTable implements Serializable
{
    private static final long serialVersionUID = 1103L;
    private static int gID = 0;

    private final int id;
    private final String[] players;
    private final boolean[] readyStates;

    private boolean started;

    public DominoesTable(int playerCap, String tableLeader) throws DominoesTableException
    {
        if (!this.isValidPlayerCap(playerCap)) throw new DominoesTableException("Invalid player capacity value!");

        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.unlock();
        try
        {
            this.id = gID++;
        }
        catch (Exception e)
        {
            throw new DominoesTableException("Lock Issue!");
        }
        finally
        {
            reentrantLock.unlock();
        }

        this.players = new String[playerCap];
        this.players[0] = tableLeader;
        this.readyStates = new boolean[playerCap];
        this.readyStates[0] = true;

        for (int i = 1; i < playerCap; i++)
        {
            this.players[i] = null;
            this.readyStates[i] = false;
        }
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

    public boolean hasStarted()
    {
        return this.started;
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
        return "DominoesTable{" + "players=" + Arrays.toString(players) + '}';
    }
}
