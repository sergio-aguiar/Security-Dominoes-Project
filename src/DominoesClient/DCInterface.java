package DominoesClient;

import DominoesMisc.DominoesTable;

public interface DCInterface
{
    int createTable(String pseudonym, int playerCap);
    DominoesTable[] listAvailableTables();
    boolean joinTable(String pseudonym, int tableID);
    int joinRandomTable(String pseudonym);
    boolean startGame(String pseudonym, int tableID);
    void disbandTable(String pseudonym, int tableID);
    boolean markAsReady(String pseudonym, int tableID);
    DominoesTable listTableInfo(String pseudonym, int tableID);
    boolean leaveTable(String pseudonym, int tableID);
}
