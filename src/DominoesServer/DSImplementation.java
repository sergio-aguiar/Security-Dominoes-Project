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
            this.dominoesTables.add(new DominoesTable(playerCap, pseudonym));
            tableID = this.dominoesTables.size() - 1;
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
    public DominoesTable[] listAvailableTables(String pseudonym)
    {
        DominoesTable[] tables;
        this.reentrantLock.lock();
        try
        {
            tables = (DominoesTable[]) this.dominoesTables.toArray();
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
        boolean joined;
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
            for (DominoesTable table : this.dominoesTables)
                if (!table.isFull()) if (table.joinTable(pseudonym))
                {
                    System.out.println(this.dominoesTables.indexOf(table));
                    tableID = this.dominoesTables.indexOf(table);
                }
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: joinTable: " + e.toString());
            tableID = -1;
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return tableID;
    }
}
