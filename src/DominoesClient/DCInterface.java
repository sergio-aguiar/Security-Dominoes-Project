package DominoesClient;

import DominoesMisc.DominoesTable;

public interface DCInterface
{
    int createTable(String pseudonym, int playerCap);
    DominoesTable[] listAvailableTables();
    boolean joinTable(String pseudonym, int tableID);
    int joinRandomTable(String pseudonym);
    boolean startGame();
    void disbandTable();
    void markAsReady(String pseudonym);
    DominoesTable listTableInfo(int tableID);
    void leaveTable(String pseudonym);
}
