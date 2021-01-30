package DominoesClient;

import DominoesMisc.*;

import java.util.Stack;

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
    DominoesDeck getDeck(String pseudonym, byte[] cipheredSessionID, int tableID);
    // TODO: Missing cipher fro server to 1st client
    boolean returnDeck(String pseudonym, byte[] cipheredSessionID, int tableID, DominoesDeck deck, byte[] pieceDif);
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
    // TODO: Cipher key
    byte[] sendSessionID(String pseudonym, byte[] cipheredSessionID);
    boolean hasKeySortingStarted(String pseudonym, byte[] cipheredSessionID, int tableID);
    void startKeySorting(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean hasKeySortingEnded(String pseudonym, byte[] cipheredSessionID, int tableID);
    byte[][] getPlayerPublicKeys(String pseudonym, byte[] cipheredSessionID, int tableID);
    DominoesSymKeyMatrix getSymKeyDistributionMatrix(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean returnSymKeyDistributionMatrix(String pseudonym, byte[] cipheredSessionID, int tableID,
                                           DominoesSymKeyMatrix symKeyMatrix);
    byte[][] getSessionSymKeys(String pseudonym, byte[] cipheredSessionID, int tableID);

    boolean hasDeckBeenProtected(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean sendDeckProtectionPrivateKey(String pseudonym, byte[] cipheredSessionID, int tableID,
                                         byte[] deckProtectionPrivateKey);
    void notifyDeckProtected(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean haveAllSentDeckProtectionPrivateKeys(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean hasSentDeckProtectionPrivateKey(String pseudonym, byte[] cipheredSessionID, int tableID);
    Stack<byte[]> getDeckProtectionKeyStack(String pseudonym, byte[] cipheredSessionID, int tableID);
    boolean isDeckSentFromServer(String pseudonym, byte[] cipheredSessionID, int tableID);
    byte[] getLastTurn(String pseudonym, byte[] cipheredSessionID, int tableID);
    byte[] getNextTurn(String pseudonym, byte[] cipheredSessionID, int tableID);
}
