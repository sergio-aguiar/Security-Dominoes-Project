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
    void leaveTable(String pseudonym, int tableID);
    boolean isPlayerTurn(String pseudonym, int tableID);
    boolean hasGameEnded(String pseudonym, int tableID);
    boolean isDeckSorting(String pseudonym, int tableID);
    String drawPiece(String pseudonym, int tableID);
    void returnPiece(String pseudonym, int tableID, String piece);
    String swapPiece(String pseudonym, int tableID, String piece);
    void skipTurn(String pseudonym, int tableID);
    boolean commitHand(String pseudonym, int tableID, String bitCommitment);
    boolean hasPlayerCommitted(String pseudonym, int tableID);
}
