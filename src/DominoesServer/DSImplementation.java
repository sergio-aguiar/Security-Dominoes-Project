package DominoesServer;

import DominoesClient.DCInterface;
import DominoesMisc.DominoesTable;

import java.util.concurrent.locks.ReentrantLock;

public class DSImplementation implements DCInterface
{
    private final ReentrantLock reentrantLock;

    public DSImplementation()
    {
        this.reentrantLock = new ReentrantLock();
    }

    @Override
    public int createTable()
    {
        return 0;
    }

    @Override
    public DominoesTable[] listAvailableTables()
    {
        return new DominoesTable[0];
    }

    @Override
    public int joinTable()
    {
        return 0;
    }

    @Override
    public int joinRandomTable()
    {
        return 0;
    }
}
