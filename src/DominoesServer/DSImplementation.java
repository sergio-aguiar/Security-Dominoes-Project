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
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                for (int i = 0; i < table.getPlayers().length; i++) if (table.getPlayers()[i].equals(pseudonym))
                    table.leaveTable(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: markAsReady: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
    }
}
