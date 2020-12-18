package DominoesClient;

import DominoesMisc.DominoesDeck;
import DominoesMisc.DominoesGameState;
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
    boolean canDraw(String pseudonym, int tableID);
    DominoesDeck getDeck(String pseudonym, int tableID);
    boolean returnDeck(String pseudonym, int tableID, DominoesDeck deck, int cardDif);
    void skipTurn(String pseudonym, int tableID);
    boolean commitHand(String pseudonym, int tableID, String bitCommitment);
    boolean hasPlayerCommitted(String pseudonym, int tableID);
    boolean isHandlingStart(String pseudonym, int tableID);
    void stateHighestDouble(String pseudonym, int tableID, String piece);
    boolean hasDoubleCheckingEnded(String pseudonym, int tableID);
    boolean isRedistributionNeeded(String pseudonym, int tableID);
    DominoesGameState getGameState(String pseudonym, int tableID);
    boolean isResetNeeded(String pseudonym, int tableID);
    boolean playPiece(String pseudonym, int tableID, String targetEndPoint, String piece, String pieceEndPoint);
}
