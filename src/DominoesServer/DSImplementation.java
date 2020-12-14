package DominoesServer;

import DominoesClient.DCInterface;
import DominoesMisc.DominoesDeck;
import DominoesMisc.DominoesGameState;
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
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                joined = table.joinTable(pseudonym);
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
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
            {
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
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
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
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID) dTable = table;
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
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
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
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.isTurn(pseudonym);
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
                result = !table.haveAllCommitted();
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
    public boolean canDraw(String pseudonym, int tableID)
    {
        boolean result = true;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.canDraw(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: canDraw: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public DominoesDeck getDeck(String pseudonym, int tableID)
    {
        DominoesDeck dDeck = null;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID) dDeck = table.getDeck();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: getDeck: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return dDeck;
    }

    @Override
    public boolean returnDeck(String pseudonym, int tableID, DominoesDeck deck, int cardDif)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
            {
                table.setDeck(deck);
                table.handleCardDif(pseudonym, cardDif);
                result = true;
                table.incrementTurn();
            }
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: returnDeck: " + e.toString());
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
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID) table.incrementTurn();
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
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
            {
                result = table.distributionCommit(pseudonym);
                if (result) table.incrementTurn();
            }
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

    @Override
    public boolean hasPlayerCommitted(String pseudonym, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.hasPlayerCommitted(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: hasPlayerCommitted: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean isHandlingStart(String pseudonym, int tableID)
    {
        return false;
    }

    @Override
    public void stateHighestDouble(String pseudonym, int tableID, String piece)
    {

    }

    @Override
    public boolean hasDoubleCheckingEnded(String pseudonym, int tableID)
    {
        return false;
    }

    @Override
    public boolean isRedistributionNeeded(String pseudonym, int tableID)
    {
        return false;
    }

    @Override
    public DominoesGameState getGameState(String pseudonym, int tableID)
    {
        return null;
    }
}
