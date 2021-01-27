package DominoesClient;

import DominoesMisc.*;

public interface DCInterface
{
    int createTable(String pseudonym, byte[] cipheredSessionID, int playerCap);
    DominoesTableInfo[] listAvailableTables(String pseudonym, byte[] cipheredSessionID);
    boolean joinTable(String pseudonym, byte[] cipheredSessionID, int tableID);
    int joinRandomTable(String pseudonym, byte[] cipheredSessionID);
    boolean startGame(String pseudonym, byte[] cipheredSessionID, int tableID);
    void disbandTable(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean markAsReady(String pseudonym, byte[] cipheredSessionID, int tableID);
    DominoesTableInfo listTableInfo(String pseudonym, byte[] cipheredSessionID, int tableID);
    void leaveTable(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean isPlayerTurn(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean hasGameEnded(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean isDeckSorting(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean canDraw(String pseudonym, byte[] cipheredSessionID, int tableID);
    // TODO: Cipher DominoesDeck
    DominoesDeck getDeck(String pseudonym, byte[] cipheredSessionID, int tableID);
    // TODO: Cipher DominoesDeck, pieceDif
    boolean returnDeck(String pseudonym, byte[] cipheredSessionID, int tableID, DominoesDeck deck, int pieceDif);
    void skipTurn(String pseudonym, byte[] cipheredSessionID, int tableID);
    // TODO: Cipher commitData
    boolean commitHand(String pseudonym, byte[] cipheredSessionID, int tableID, DominoesCommitData commitData);
    boolean hasPlayerCommitted(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean isHandlingStart(String pseudonym, byte[] cipheredSessionID, int tableID);
    // TODO: Cipher piece
    void stateHighestDouble(String pseudonym, byte[] cipheredSessionID, int tableID, String piece);
    boolean hasDoubleCheckingEnded(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean isRedistributionNeeded(String pseudonym, byte[] cipheredSessionID, int tableID);
    DominoesGameState getGameState(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean isResetNeeded(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean playPiece(String pseudonym, byte[] cipheredSessionID, int tableID, String targetEndPoint, String piece,
                      String pieceEndPoint);
    // TODO: Cipher piece
    String drawPiece(String pseudonym, byte[] cipheredSessionID, int tableID);
    void denounceCheating(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean isHandlingCheating(String pseudonym, byte[] cipheredSessionID, int tableID);
    // TODO: Cipher commitData
    boolean updateCommitment(String pseudonym, byte[] cipheredSessionID, int tableID, DominoesCommitData commitData);
    // TODO: Cipher commitData
    boolean sendCommitData(String pseudonym, byte[] cipheredSessionID, int tableID, DominoesCommitData commitData);
    boolean hasSentCommitData(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean isHandlingAccounting(String pseudonym, byte[] cipheredSessionID, int tableID);
    DominoesAccountingInfo getAccountingInfo(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean sendAccountingDecision(String pseudonym, byte[] cipheredSessionID, int tableID, boolean decision);
    boolean allSentDecision(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean allAgreedToAccounting(String pseudonym, byte[] cipheredSessionID, int tableID);
    void passedProtestMenu(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean allPassedProtestMenu(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean isUserRegistered(String user);
    boolean registerUser(String user);
    byte[] greetServer(String pseudonym, byte[] publicKey);
    byte[] getServerPublicKey();
    boolean sendSessionID(String pseudonym, byte[] cipheredSessionID);
}
