package DominoesClient;

import DominoesMisc.*;

public interface DCInterface
{
    int createTable(String pseudonym, int playerCap);
    DominoesTableInfo[] listAvailableTables();
    boolean joinTable(String pseudonym, int tableID);
    int joinRandomTable(String pseudonym);
    boolean startGame(String pseudonym, int tableID);
    void disbandTable(String pseudonym, int tableID);
    boolean markAsReady(String pseudonym, int tableID);
    DominoesTableInfo listTableInfo(String pseudonym, int tableID);
    void leaveTable(String pseudonym, int tableID);
    boolean isPlayerTurn(String pseudonym, int tableID);
    boolean hasGameEnded(String pseudonym, int tableID);
    boolean isDeckSorting(String pseudonym, int tableID);
    boolean canDraw(String pseudonym, int tableID);
    DominoesDeck getDeck(String pseudonym, int tableID);
    boolean returnDeck(String pseudonym, int tableID, DominoesDeck deck, int cardDif);
    void skipTurn(String pseudonym, int tableID);
    boolean commitHand(String pseudonym, int tableID, DominoesCommitData commitData);
    boolean hasPlayerCommitted(String pseudonym, int tableID);
    boolean isHandlingStart(String pseudonym, int tableID);
    void stateHighestDouble(String pseudonym, int tableID, String piece);
    boolean hasDoubleCheckingEnded(String pseudonym, int tableID);
    boolean isRedistributionNeeded(String pseudonym, int tableID);
    DominoesGameState getGameState(String pseudonym, int tableID);
    boolean isResetNeeded(String pseudonym, int tableID);
    boolean playPiece(String pseudonym, int tableID, String targetEndPoint, String piece, String pieceEndPoint);
    String drawCard(String pseudonym, int tableID);
    void denounceCheating(String pseudonym, int tableID);
    boolean isHandlingCheating(String pseudonym, int tableID);
    boolean updateCommitment(String pseudonym, int tableID, DominoesCommitData commitData);
    boolean sendCommitData(String pseudonym, int tableID, DominoesCommitData commitData);
    boolean hasSentCommitData(String pseudonym, int tableID);
    boolean isHandlingAccounting(String pseudonym, int tableID);
    DominoesAccountingInfo getAccountingInfo(String pseudonym, int tableID);
    boolean sendAccountingDecision(String pseudonym, int tableID, boolean decision);
    boolean allSentDecision(String pseudonym, int tableID);
    boolean allAgreedToAccounting(String pseudonym, int tableID);
    void passedProtestMenu(String pseudonym, int tableID);
    boolean allPassedProtestMenu(String pseudonym, int tableID);
}
