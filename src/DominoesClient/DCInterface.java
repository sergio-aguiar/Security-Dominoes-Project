package DominoesClient;

import DominoesMisc.DominoesTable;

public interface DCInterface
{
    public int createTable();
    public DominoesTable[] listAvailableTables();
    public int joinTable();
    public int joinRandomTable();
}
