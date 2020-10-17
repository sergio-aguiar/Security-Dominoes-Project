package DominoesClient;

import DominoesMisc.DominoesTable;

public interface DCInterface
{
    int createTable(String pseudonym, int playerCap);
    DominoesTable[] listAvailableTables();
    boolean joinTable(String pseudonym, int tableID);
    int joinRandomTable(String pseudonym);
    boolean startGame(int tableID);
    void disbandTable(int tableID);
    boolean markAsReady(String pseudonym, int tableID);
    DominoesTable listTableInfo(int tableID);
    boolean leaveTable(String pseudonym, int tableID);
}
