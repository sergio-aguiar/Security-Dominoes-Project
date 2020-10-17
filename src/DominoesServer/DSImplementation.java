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
            DominoesTable[] tables = this.dominoesTables.toArray(new DominoesTable[0]);
            for (int i = 0; i < tables.length; i++) if (!tables[i].isFull())
            {
                this.dominoesTables.get(i).joinTable(pseudonym);
                tableID = i;
                break;
            }
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
    public boolean startGame(int tableID) {
        boolean started = true;
        this.reentrantLock.lock();
        try
        {
            for (boolean ready : this.dominoesTables.get(tableID).getReadyStates()) if (!ready) {
                started = false;
                break;
            }
            if (started) this.dominoesTables.get(tableID).startGame();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: startGame: " + e.toString());
            started = false;
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return started;
    }

    @Override
    public void disbandTable() {

    }

    @Override
    public void markAsReady(String pseudonym) {

    }

    @Override
    public DominoesTable listTableInfo(int tableID) {
        return null;
    }

    @Override
    public void leaveTable(String pseudonym) {

    }
}
