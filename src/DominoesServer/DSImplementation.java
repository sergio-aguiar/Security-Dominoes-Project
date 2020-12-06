package DominoesServer;

import DominoesClient.DCInterface;
import DominoesMisc.DominoesTable;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class DSImplementation implements DCInterface
{
    private final ReentrantLock reentrantLock;
    private final ArrayList<DominoesTable> dominoesTables;

    public DSImplementation()
    {
        this.reentrantLock = new ReentrantLock();
        this.dominoesTables = new ArrayList<>();
    }

    @Override
    public int createTable(String pseudonym, int playerCap)
    {
        int tableID;
        this.reentrantLock.lock();
        try
        {
            DominoesTable table = new DominoesTable(playerCap, pseudonym);
            tableID = table.getId();
            this.dominoesTables.add(table);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: createTable: " + e.toString());
            tableID = -1;
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return tableID;
    }

    @Override
    public DominoesTable[] listAvailableTables()
    {
        DominoesTable[] tables;
        this.reentrantLock.lock();
        try
        {
            tables = this.dominoesTables.toArray(new DominoesTable[0]);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: listAvailableTables: " + e.toString());
            tables = new DominoesTable[0];
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return tables;
    }

    @Override
    public boolean joinTable(String pseudonym, int tableID)
    {
        boolean joined = false;
        this.reentrantLock.lock();
        try
        {
            joined = this.dominoesTables.get(tableID).joinTable(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: joinTable: " + e.toString());
            joined = false;
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return joined;
    }

    @Override
    public int joinRandomTable(String pseudonym)
    {
        int tableID = -1;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (!table.isFull()) if (table.joinTable(pseudonym))
                tableID = table.getId();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: joinRandomTable: " + e.toString());
            tableID = -1;
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return tableID;
    }

    @Override
    public boolean startGame(String pseudonym, int tableID)
    {
        int started = 0;
        this.reentrantLock.lock();
        try
        {
            DominoesTable table = this.dominoesTables.get(tableID);
            for (boolean ready : table.getReadyStates()) if (!ready)
            {
                started = -1;
                break;
            }
            if (started != -1)
            {
                started = table.getId();
                table.startGame();
            }
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: startGame: " + e.toString());
            started = -1;
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return started >= 0;
    }

    @Override
    public void disbandTable(String pseudonym, int tableID)
    {
        this.reentrantLock.lock();
        try
        {
            this.dominoesTables.removeIf(table -> table.getId() == tableID);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: disbandTable: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
    }

    @Override
    public boolean markAsReady(String pseudonym, int tableID)
    {
        if (tableID < 0) return false;

        boolean marked = false;
        this.reentrantLock.lock();
        try
        {
            DominoesTable table = this.dominoesTables.get(tableID);
            for (int i = 0; i < table.getPlayers().length; i++) if (table.getPlayers()[i].equals(pseudonym))
            {
                table.setReady(pseudonym);
                marked = true;
                break;
            }
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: markAsReady: " + e.toString());
            marked = false;
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return marked;
    }

    @Override
    public DominoesTable listTableInfo(String pseudonym, int tableID)
    {
        DominoesTable dTable = null;
        this.reentrantLock.lock();
        try
        {
            dTable = this.dominoesTables.get(tableID);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: listTableInfo: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }

        System.out.println(dTable);

        return dTable;
    }

    @Override
    public void leaveTable(String pseudonym, int tableID)
    {
        if (tableID < 0) return;

        this.reentrantLock.lock();
        try
        {
            DominoesTable table = this.dominoesTables.get(tableID);
            for (int i = 0; i < table.getPlayers().length; i++)
                if (table.getPlayers()[i] != null && table.getPlayers()[i].equals(pseudonym))
                    table.leaveTable(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: leaveTable: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
    }

    @Override
    public boolean isPlayerTurn(String pseudonym, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            result = this.dominoesTables.get(tableID).isTurn(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: isPlayerTurn: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean hasGameEnded(String pseudonym, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID) result = table.hasEnded();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: hasGameEnded: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean isDeckSorting(String pseudonym, int tableID)
    {
        boolean result = true;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.isDistributing();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: isDeckSorting: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public String drawPiece(String pseudonym, int tableID)
    {
        String result = "Error";
        this.reentrantLock.lock();
        try
        {
            result = this.dominoesTables.get(tableID).distributionDrawPiece(pseudonym);
            this.dominoesTables.get(tableID).incrementTurn();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: drawPiece: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public void returnPiece(String pseudonym, int tableID, String piece)
    {
        this.reentrantLock.lock();
        try
        {
            this.dominoesTables.get(tableID).distributionReturnPiece(pseudonym, piece);
            this.dominoesTables.get(tableID).incrementTurn();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: returnPiece: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
    }

    @Override
    public String swapPiece(String pseudonym, int tableID, String piece)
    {
        String result = "Error";
        this.reentrantLock.lock();
        try
        {
            result = this.dominoesTables.get(tableID).distributionSwapPiece(pseudonym, piece);
            this.dominoesTables.get(tableID).incrementTurn();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: swapPiece: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public void skipTurn(String pseudonym, int tableID)
    {
        this.reentrantLock.lock();
        try
        {
            this.dominoesTables.get(tableID).incrementTurn();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: skipTurn: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
    }

    @Override
    public boolean commitHand(String pseudonym, int tableID, String bitCommitment)
    {
        // TODO: See how to do bit commitment.
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            this.dominoesTables.get(tableID).distributionCommit(pseudonym);
            result = true;
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: commitHand: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }
}
