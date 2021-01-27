package DominoesClient;

import DominoesMisc.*;

public interface DCInterface
{
    int createTable(String pseudonym, byte[] cipheredSessionID, int playerCap);
    // TODO: Cipher SessionID
    DominoesTableInfo[] listAvailableTables(String pseudonym, byte[] cipheredSessionID);
    // TODO: Cipher SessionID
    boolean joinTable(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    int joinRandomTable(String pseudonym);
    // TODO: Cipher SessionID
    boolean startGame(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    void disbandTable(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    boolean markAsReady(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    DominoesTableInfo listTableInfo(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    void leaveTable(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    boolean isPlayerTurn(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    boolean hasGameEnded(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    boolean isDeckSorting(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    boolean canDraw(String pseudonym, int tableID);
    // TODO: Cipher SessionID, cipher DominoesDeck
    DominoesDeck getDeck(String pseudonym, int tableID);
    // TODO: Cipher SessionID, cipher DominoesDeck, pieceDif
    boolean returnDeck(String pseudonym, int tableID, DominoesDeck deck, int pieceDif);
    // TODO: Cipher SessionID
    void skipTurn(String pseudonym, int tableID);
    // TODO: Cipher SessionID, commitData
    boolean commitHand(String pseudonym, int tableID, DominoesCommitData commitData);
    // TODO: Cipher SessionID
    boolean hasPlayerCommitted(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    boolean isHandlingStart(String pseudonym, int tableID);
    // TODO: Cipher SessionID, piece
    void stateHighestDouble(String pseudonym, int tableID, String piece);
    // TODO: Cipher SessionID
    boolean hasDoubleCheckingEnded(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    boolean isRedistributionNeeded(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    DominoesGameState getGameState(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    boolean isResetNeeded(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    boolean playPiece(String pseudonym, int tableID, String targetEndPoint, String piece, String pieceEndPoint);
    // TODO: Cipher SessionID, piece
    String drawPiece(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    void denounceCheating(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    boolean isHandlingCheating(String pseudonym, int tableID);
    // TODO: Cipher SessionID, commitData
    boolean updateCommitment(String pseudonym, int tableID, DominoesCommitData commitData);
    // TODO: Cipher SessionID, commitData
    boolean sendCommitData(String pseudonym, int tableID, DominoesCommitData commitData);
    // TODO: Cipher SessionID
    boolean hasSentCommitData(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    boolean isHandlingAccounting(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    DominoesAccountingInfo getAccountingInfo(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    boolean sendAccountingDecision(String pseudonym, int tableID, boolean decision);
    // TODO: Cipher SessionID
    boolean allSentDecision(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    boolean allAgreedToAccounting(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    void passedProtestMenu(String pseudonym, int tableID);
    // TODO: Cipher SessionID
    boolean allPassedProtestMenu(String pseudonym, int tableID);

    boolean isUserRegistered(String user);
    boolean registerUser(String user);
    byte[] greetServer(String pseudonym, byte[] publicKey);
    byte[] getServerPublicKey();
    // TODO: Cipher SessionID
    boolean sendSessionID(String pseudonym, byte[] cipheredSessionID);
}
