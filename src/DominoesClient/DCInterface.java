package DominoesClient;

import DominoesMisc.DominoesTable;

public interface DCInterface
{
    public int createTable(String pseudonym, int playerCap);
    public DominoesTable[] listAvailableTables(String pseudonym);
    public boolean joinTable(String pseudonym, int tableID);
    public int joinRandomTable(String pseudonym);
}
