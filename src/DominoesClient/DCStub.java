package DominoesClient;

import DominoesMisc.DominoesTable;

public class DCStub implements DCInterface
{
    private final String serverHostName;
    private final int serverHostPort;

    public DCStub(String serverHostName, int serverHostPort)
    {
        this.serverHostName = serverHostName;
        this.serverHostPort = serverHostPort;
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
