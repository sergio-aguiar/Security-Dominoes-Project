package DominoesClient;

import DominoesMisc.*;
import DominoesSecurity.DominoesCryptoAsym;
import DominoesSecurity.DominoesCryptoSym;
import DominoesSecurity.DominoesSignature;

import java.security.Key;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DCThread extends Thread
{
    public enum DCStates
    {
        AT_MAIN_MENUS("AMM"),
        AT_TABLE_LEADER_MENU("ATLM"),
        AT_TABLE_GUEST_MENU("ATGM"),
        AWAITING_GAME_START("AGS"),
        AT_PIECE_DISTRIBUTION("APD");

        private final String description;

        DCStates(String description)
        {
            this.description = description;
        }

        @Override
        public String toString() {
            return this.description;
        }
    }

    private static final Scanner sc = new Scanner(System.in);

    private final ReentrantLock reentrantLock;
    private final Condition turnCondition;

    private final String pseudonym;
    private final Key privateKey;
    private final Key publicKey;
    private final DCInterface dcInterface;
    private final ArrayList<String> gamePieces;
    private final byte[] sessionPrivateKey;
    private final byte[] sessionPublicKey;

    private int tableID;
    private int sessionID;
    private byte[] signedSessionID;
    private byte[] cipheredSignedSessionID;
    private byte[] serverPublicKey;
    private byte[] serverSessionSymKey;
    private byte[] deckDistributionPrivateKey;
    private byte[] deckDistributionPublicKey;
    private byte[][] playerPublicKeys;
    private byte[][] playerSessionSymKeys;
    private Stack<byte[]> protectionStack;

    private int bitCommitRandom1;
    private int bitCommitRandom2;
    private ArrayList<String> committedPieces;
    private boolean knowsCommittedCards;

    public DCThread(String pseudonym, Key privateKey, Key publicKey, int sessionID, DCInterface dcInterface)
    {
        this.reentrantLock = new ReentrantLock(true);
        this.turnCondition = this.reentrantLock.newCondition();
        this.pseudonym = pseudonym;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.sessionID = sessionID;
        this.signedSessionID = signSessionID();
        this.serverPublicKey = null;
        this.serverSessionSymKey = null;
        this.playerPublicKeys = null;
        this.playerSessionSymKeys = null;
        this.cipheredSignedSessionID = null;
        this.dcInterface = dcInterface;
        this.gamePieces = new ArrayList<>();
        this.protectionStack = null;

        Map<String, byte[]> keys = DominoesCryptoAsym.GenerateAsymKeys();
        this.sessionPrivateKey = keys.get("private");
        this.sessionPublicKey = keys.get("public");

        this.tableID = -1;
        randomizeCommitData();
        this.committedPieces = null;
        this.knowsCommittedCards = false;
    }

    @Override
    public void run()
    {
        System.out.println("[CLIENT] Dominoes Client starting...");

        establishSession();

        while (true)
        {
            int option1 = clientMainMenu();
            switch (option1)
            {
                case 1:
                    int option2 = clientPlayerCapMenu();
                    boolean exit1 = false;
                    switch (option2)
                    {
                        case 1:
                            System.out.println("\n[CLIENT] Creating a dominoes table...");
                            this.tableID = this.dcInterface.createTable(this.pseudonym, this.cipheredSignedSessionID,
                                    2);
                            break;
                        case 2:
                            System.out.println("\n[CLIENT] Creating a dominoes table...");
                            this.tableID = this.dcInterface.createTable(this.pseudonym, this.cipheredSignedSessionID,
                                    3);
                            break;
                        case 3:
                            System.out.println("\n[CLIENT] Creating a dominoes table...");
                            this.tableID = this.dcInterface.createTable(this.pseudonym, this.cipheredSignedSessionID,
                                    4);
                            break;
                        case 4:
                            exit1 = true;
                            break;
                        default:
                            System.out.println("\n[CLIENT] Unexpected Error...");
                            System.exit(703);
                    }

                    if (!exit1)
                    {
                        boolean exit2 = false;
                        do
                        {
                            int option3 = clientTableMenu(true);
                            switch (option3)
                            {
                                case 1:
                                    if (this.dcInterface.startGame(this.pseudonym, this.cipheredSignedSessionID,
                                            this.tableID))
                                    {
                                        System.out.println("\n[CLIENT] Starting Game...");
                                        gameLogic();
                                        exit2 = true;
                                    }
                                    else
                                    {
                                        System.out.println("\n[CLIENT] Could not start the game.\n" +
                                                "[CLIENT] Some players have yet to ready up.");
                                    }
                                    break;
                                case 2:
                                    System.out.println("\n[CLIENT] Fetching Table Information...");
                                    System.out.print(
                                            this.dcInterface.listTableInfo(this.pseudonym, this.cipheredSignedSessionID,
                                                    this.tableID).toString());
                                    break;
                                case 3:
                                    System.out.println("\n[CLIENT] Disbanding Table...");
                                    this.dcInterface.disbandTable(this.pseudonym, this.cipheredSignedSessionID,
                                            this.tableID);
                                    this.tableID = -1;
                                    exit2 = true;
                                    break;
                                default:
                                    System.out.println("\n[CLIENT] Unexpected Error...");
                                    System.exit(703);
                            }
                        }
                        while (!exit2);
                    }
                    break;
                case 2:
                    System.out.print("\n[CLIENT] Fetching available dominoes tables...");

                    DominoesTableInfo[] tmpTables = this.dcInterface.listAvailableTables(this.pseudonym,
                            this.cipheredSignedSessionID);
                    if (tmpTables.length == 0) System.out.println("\n[CLIENT] No available tables found.");
                    else for (DominoesTableInfo table : tmpTables) System.out.print("\n" + table.toString());

                    break;
                case 3:
                case 4:
                    boolean noTables = false;
                    while (this.tableID < 0)
                    {
                        if (option1 == 3)
                        {
                            int tableToJoin;

                            do
                            {
                                System.out.print("\n[CLIENT] Table ID: ");
                                tableToJoin = getMenuOption();

                                if (tableToJoin < 0) System.out.println("\n[CLIENT] Invalid table ID.");
                            }
                            while (tableToJoin < 0);

                            if (this.dcInterface.joinTable(this.pseudonym, this.cipheredSignedSessionID, tableToJoin))
                            {
                                System.out.println("\n[CLIENT] Joining dominoes table #" + tableToJoin + "...");
                                this.tableID = tableToJoin;
                            }
                            else
                            {
                                System.out.println("\n[CLIENT] Could not join a table with the specified ID.");
                                noTables = true;
                                break;
                            }
                        }
                        else
                        {
                            System.out.println("\n[CLIENT] Joining a random dominoes table...");
                            this.tableID = this.dcInterface.joinRandomTable(this.pseudonym,
                                    this.cipheredSignedSessionID);

                            if (this.tableID == -1)
                            {
                                System.out.println("[CLIENT] No available tables to join.");
                                noTables = true;
                                break;
                            }
                        }
                    }

                    if (noTables) break;

                    boolean exit3 = false;
                    do
                    {
                        int option4 = clientTableMenu(false);
                        switch (option4)
                        {
                            case 1:
                                System.out.print("\n[CLIENT] Marking self as ready...");

                                if (this.dcInterface.markAsReady(this.pseudonym, this.cipheredSignedSessionID,
                                        this.tableID))
                                {
                                    System.out.println("\n[CLIENT] Awaiting game Start...");
                                    gameLogic();
                                    exit3 = true;
                                }
                                else
                                {
                                    System.out.println("\n[CLIENT] The table was disbanded.");
                                    exit3 = true;
                                }
                                break;
                            case 2:
                                System.out.println("\n[CLIENT] Listing Table Information...");
                                DominoesTableInfo tmpTable = this.dcInterface.listTableInfo(this.pseudonym,
                                        this.cipheredSignedSessionID, this.tableID);

                                if (tmpTable != null) System.out.println(tmpTable.toString());
                                else
                                {
                                    System.out.println("[CLIENT] The table was disbanded.");
                                    exit3 = true;
                                }
                                break;
                            case 3:
                                System.out.println("\n[CLIENT] Leaving Table...");
                                this.dcInterface.leaveTable(this.pseudonym, this.cipheredSignedSessionID, this.tableID);
                                exit3 = true;
                                break;
                            default:
                                System.out.println("\n[CLIENT] Unexpected Error...");
                                System.exit(703);
                        }
                    }
                    while (!exit3);

                    this.tableID = -1;
                    break;
                case 5:
                    System.out.println("\n[CLIENT] Showing score...");

                    // TODO: SHOW SCORE

                    break;
                case 6:
                    System.out.println("\n[CLIENT] Shutting down...");
                    System.exit(702);
                default:
                    System.out.println("\n[CLIENT] Unexpected Error...");
                    System.exit(703);
            }
        }
    }

    private void gameLogic()
    {
        while (!this.dcInterface.hasGameEnded(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
        {
            System.out.println("\nBACK UP HERE");
            while ((!this.dcInterface.hasGameEnded(this.pseudonym, this.cipheredSignedSessionID, this.tableID)
                    && (!this.dcInterface.isPlayerTurn(this.pseudonym, this.cipheredSignedSessionID, this.tableID)
                    || this.dcInterface.isResetNeeded(this.pseudonym, this.cipheredSignedSessionID, this.tableID))))
            {
                this.reentrantLock.lock();
                try
                {
                    synchronized (this)
                    {
                        this.turnCondition.awaitNanos(100000);
                    }
                }
                catch (Exception e)
                {
                    System.out.println("DCThread: gameLogic: " + e.toString());
                    System.exit(703);
                }
                finally
                {
                    this.reentrantLock.unlock();
                }
            }

            if (this.dcInterface.hasGameEnded(this.pseudonym, this.cipheredSignedSessionID ,this.tableID)) break;

            if (!this.dcInterface.hasKeySortingEnded(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
            {
                System.out.println("I MADE IT TO KEY SEY SORTING 1!");

                if (!this.dcInterface.hasKeySortingStarted(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
                    this.dcInterface.startKeySorting(this.pseudonym, this.cipheredSignedSessionID, this.tableID);
                else
                {
                    this.playerPublicKeys = this.dcInterface.getPlayerPublicKeys(this.pseudonym,
                            this.cipheredSignedSessionID, this.tableID);

                    System.out.println("Player public keys:");
                    for (byte[] key : this.playerPublicKeys) System.out.println(Arrays.toString(key));

                    DominoesSymKeyMatrix symKeyMatrix = this.dcInterface.getSymKeyDistributionMatrix(this.pseudonym,
                            this.cipheredSignedSessionID, this.tableID);

                    System.out.println("Matrix before edited:");
                    for (byte[][] key : symKeyMatrix.getSymKeyMatrix()) System.out.println(Arrays.toString(key));

                    for (int i = 0; i < symKeyMatrix.getPlayers().length; i++)
                        if (symKeyMatrix.getPlayers()[i].equals(this.pseudonym))
                        {
                            for (int j = i + 1; j < symKeyMatrix.getSymKeyMatrix()[i].length; j++)
                            {
                                byte[] symKey = DominoesCryptoSym.GenerateSymKeys(Long.toString(
                                        System.currentTimeMillis()));

                                symKeyMatrix.updateKey(i, j, DominoesCryptoAsym.AsymCipher(symKey,
                                        this.sessionPublicKey));

                                symKeyMatrix.updateKey(j, i, DominoesCryptoAsym.AsymCipher(symKey,
                                        this.playerPublicKeys[j]));
                            }
                        }

                    System.out.println("Matrix after edited:");
                    for (byte[][] key : symKeyMatrix.getSymKeyMatrix()) System.out.println(Arrays.toString(key));

                    if (!this.dcInterface.returnSymKeyDistributionMatrix(this.pseudonym, this.cipheredSignedSessionID,
                            this.tableID, symKeyMatrix))
                    {
                        System.out.println("\n[CLIENT] Unexpected Error...");
                        System.exit(703);
                    }

                }
                continue;
            }
            else
            {
                System.out.println("I MADE IT TO KEY SEY SORTING 2!");

                this.playerSessionSymKeys = this.dcInterface.getSessionSymKeys(this.pseudonym,
                        this.cipheredSignedSessionID, this.tableID);

                System.out.println("Sym keys while ciphered:");
                for (byte[] key : this.playerSessionSymKeys) System.out.println(Arrays.toString(key));

                for (int i = 0; i < this.playerSessionSymKeys.length; i++) if (this.playerSessionSymKeys[i] != null)
                    this.playerSessionSymKeys[i] = (byte[]) DominoesCryptoAsym.AsymDecipher(
                            this.playerSessionSymKeys[i], this.sessionPrivateKey);

                System.out.println("Sym keys ready to be used:");
                for (byte[] key : this.playerSessionSymKeys) System.out.println(Arrays.toString(key));
            }

            System.out.println("I MADE IT AFTER KEY SORTING!");

            if (!this.dcInterface.hasPlayerCommitted(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
            {
                if (!this.dcInterface.hasDeckBeenProtected(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
                {
                    DominoesDeck deck = this.dcInterface.getDeck(this.pseudonym, this.cipheredSignedSessionID,
                            this.tableID);

                    Map<String, byte[]> keys = DominoesCryptoAsym.GenerateAsymKeys();
                    this.deckDistributionPrivateKey = keys.get("private");
                    this.deckDistributionPublicKey = keys.get("public");

                    System.out.println("Private key: " + Arrays.toString(this.deckDistributionPrivateKey));

                    deck.asymCipher(this.deckDistributionPublicKey);

                    this.dcInterface.notifyDeckProtected(this.pseudonym, this.cipheredSignedSessionID, this.tableID);

                    if (!this.dcInterface.returnDeck(this.pseudonym, this.cipheredSignedSessionID,
                            this.tableID, deck, DominoesCryptoSym.SymCipher(0, this.serverSessionSymKey)))
                    {
                        System.out.println("\n[CLIENT] Unexpected Error...");
                        System.exit(703);
                    }
                }
                else if (this.dcInterface.isDeckSorting(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
                {
                    DominoesDeck deck = this.dcInterface.getDeck(this.pseudonym,
                            this.cipheredSignedSessionID, this.tableID);

                    int lastPlayer = (int) DominoesCryptoSym.SymDecipher(this.dcInterface.getLastTurn(this.pseudonym,
                            this.cipheredSignedSessionID, this.tableID), this.serverSessionSymKey);

                    int nextPlayer = (int) DominoesCryptoSym.SymDecipher(this.dcInterface.getNextTurn(this.pseudonym,
                            this.cipheredSignedSessionID, this.tableID), this.serverSessionSymKey);

                    System.out.println("Last player: " + lastPlayer);
                    System.out.println("Next player: " + nextPlayer);

                    if (this.dcInterface.isDeckSentFromServer(this.pseudonym, this.cipheredSignedSessionID,
                            this.tableID))
                    {
                        // System.out.println("DECIPHERING DECK WITH: " + Arrays.toString(this.serverSessionSymKey));
                        // deck.symDecipher(this.serverSessionSymKey);
                    }
                    else
                    {
                        System.out.println("DECIPHERING DECK WITH: " +
                                Arrays.toString(this.playerSessionSymKeys[lastPlayer]));
                        deck.symDecipher(this.playerSessionSymKeys[lastPlayer]);
                    }

                    System.out.println("DECK AFTER DECIPHER: " + deck);

                    int option = clientPieceDistributionMenu();
                    switch (option)
                    {
                        case 1:
                            System.out.println("\n[CLIENT] Drawing a piece...");

                            if (this.dcInterface.canDraw(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
                            {
                                String tile1 = deck.drawPiece();
                                if (tile1 != null) this.gamePieces.add(tile1);
                                else System.out.println("\n[CLIENT] No pieces left to draw.");

                                if (this.playerSessionSymKeys[nextPlayer] != null)
                                    deck.symCipher(this.playerSessionSymKeys[nextPlayer]);

                                if (!this.dcInterface.returnDeck(this.pseudonym, this.cipheredSignedSessionID,
                                        this.tableID, deck, DominoesCryptoSym.SymCipher(1,
                                                this.serverSessionSymKey)))
                                {
                                    System.out.println("\n[CLIENT] Unexpected Error...");
                                    System.exit(703);
                                }

                                System.out.println("PLAYER PIECES: ");
                                System.out.println(this.gamePieces.toString());
                            }
                            else System.out.println("\n[CLIENT] Could not draw a piece. Hand full.");

                            break;
                        case 2:
                            System.out.println("\n[CLIENT] Returning a piece...");
                            if (this.gamePieces.size() > 0)
                            {
                                String tile2 = getTileToReturn();
                                deck.returnTile(tile2);
                                this.gamePieces.remove(tile2);

                                if (this.playerSessionSymKeys[nextPlayer] != null)
                                    deck.symCipher(this.playerSessionSymKeys[nextPlayer]);

                                if (!this.dcInterface.returnDeck(this.pseudonym, this.cipheredSignedSessionID,
                                        this.tableID, deck, DominoesCryptoSym.SymCipher(-1,
                                                this.serverSessionSymKey)))
                                {
                                    System.out.println("\n[CLIENT] Unexpected Error...");
                                    System.exit(703);
                                }

                                System.out.println("PLAYER PIECES: ");
                                System.out.println(this.gamePieces.toString());
                            }
                            else System.out.println("\n[CLIENT] There are no pieces to return.");

                            break;
                        case 3:
                            System.out.println("\n[CLIENT] Swapping a piece...");
                            if (this.gamePieces.size() > 0)
                            {
                                String tile3 = getTileToReturn();
                                this.gamePieces.add(deck.swapTile(tile3));
                                this.gamePieces.remove(tile3);

                                if (this.playerSessionSymKeys[nextPlayer] != null)
                                    deck.symCipher(this.playerSessionSymKeys[nextPlayer]);

                                if (!this.dcInterface.returnDeck(this.pseudonym, this.cipheredSignedSessionID,
                                        this.tableID, deck, DominoesCryptoSym.SymCipher(0,
                                                this.serverSessionSymKey)))
                                {
                                    System.out.println("\n[CLIENT] Unexpected Error...");
                                    System.exit(703);
                                }

                                System.out.println("PLAYER PIECES: ");
                                System.out.println(this.gamePieces.toString());
                            }
                            else System.out.println("\n[CLIENT] There are no pieces to swap.");

                            break;
                        case 4:
                            System.out.println("\n[CLIENT] Skipping a turn...");

                            // this.dcInterface.skipTurn(this.pseudonym, this.cipheredSignedSessionID, this.tableID);

                            if (this.playerSessionSymKeys[nextPlayer] != null)
                                deck.symCipher(this.playerSessionSymKeys[nextPlayer]);

                            if (!this.dcInterface.returnDeck(this.pseudonym, this.cipheredSignedSessionID,
                                    this.tableID, deck, DominoesCryptoSym.SymCipher(0,
                                            this.serverSessionSymKey)))
                            {
                                System.out.println("\n[CLIENT] Unexpected Error...");
                                System.exit(703);
                            }

                            break;
                        case 5:
                            System.out.println("\n[CLIENT] Committing your hand...");

                            // TODO: BIT COMMITMENT CORRECTION
                            // int bitCommitment = DominoesCommitData.generateHash(this.bitCommitRandom1,
                            //         this.bitCommitRandom2, this.gamePieces.toArray(new String[0]));

                            // TEMPORARY
                            int bitCommitment = 1;

                            DominoesCommitData commitData = new DominoesCommitData(this.bitCommitRandom1,
                                    bitCommitment);

                            if (this.dcInterface.commitHand(this.pseudonym, this.cipheredSignedSessionID, this.tableID,
                                    commitData)) this.committedPieces = new ArrayList<>(this.gamePieces);
                            else System.out.println("\n[CLIENT You can only commit to full hands.");

                            if (this.playerSessionSymKeys[nextPlayer] != null)
                            {
                                System.out.println("IM CIPHERING THE DECK!!!!!");
                                deck.symCipher(this.playerSessionSymKeys[nextPlayer]);
                            }

                            if (!this.dcInterface.returnDeck(this.pseudonym, this.cipheredSignedSessionID,
                                    this.tableID, deck, DominoesCryptoSym.SymCipher(0,
                                            this.serverSessionSymKey)))
                            {
                                System.out.println("\n[CLIENT] Unexpected Error...");
                                System.exit(703);
                            }

                            break;
                        default:
                            System.out.println("\n[CLIENT] Unexpected Error...");
                            System.exit(703);
                    }
                }
                else
                {
                    System.out.println("\n[CLIENT] Unexpected Error...");
                    System.exit(703);
                }
            }
            else
            {
                if (this.dcInterface.isHandlingStart(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
                {
                    if (!this.knowsCommittedCards)
                    {
                        if (this.dcInterface.haveAllSentDeckProtectionPrivateKeys(this.pseudonym,
                                this.cipheredSignedSessionID, this.tableID))
                        {
                            this.protectionStack = dcInterface.getDeckProtectionKeyStack(this.pseudonym,
                                    this.cipheredSignedSessionID, this.tableID);

                            Stack<?> deckProtection = (Stack<?>) this.protectionStack.clone();

                            while (deckProtection.size() > 1)
                            {
                                byte[] key = (byte[]) deckProtection.pop();

                                for (int i = 0; i < this.gamePieces.size(); i++)
                                    this.gamePieces.set(i, Base64.getEncoder().encodeToString(
                                            (byte[]) DominoesCryptoAsym.AsymDecipher(
                                                    Base64.getDecoder().decode(this.gamePieces.get(i)), key)));
                            }

                            byte[] key = (byte[]) deckProtection.pop();

                            for (int i = 0; i < this.gamePieces.size(); i++)
                                this.gamePieces.set(i, (String) DominoesCryptoAsym.AsymDecipher(
                                        Base64.getDecoder().decode(this.gamePieces.get(i)), key));

                            this.committedPieces = new ArrayList<>(this.gamePieces);
                            System.out.println("PLAYER PIECES: ");
                            System.out.println(this.gamePieces.toString());

                            this.knowsCommittedCards = true;
                        }
                        else if (!this.dcInterface.hasSentDeckProtectionPrivateKey(this.pseudonym,
                                this.cipheredSignedSessionID, this.tableID))
                        {
                            if (!this.dcInterface.sendDeckProtectionPrivateKey(this.pseudonym,
                                    this.cipheredSignedSessionID,this.tableID, this.deckDistributionPrivateKey))
                            {
                                System.out.println("\n[CLIENT] Unexpected Error...");
                                System.exit(703);
                            }
                        }
                    }
                    else
                    {
                        this.dcInterface.stateHighestDouble(this.pseudonym, this.cipheredSignedSessionID, this.tableID,
                                Base64.getEncoder().encodeToString(DominoesCryptoSym.SymCipher(getHighestDouble(),
                                        this.serverSessionSymKey)));

                        while (!this.dcInterface.hasDoubleCheckingEnded(this.pseudonym, this.cipheredSignedSessionID,
                                this.tableID))
                        {
                            this.reentrantLock.lock();
                            try
                            {
                                synchronized (this)
                                {
                                    this.turnCondition.awaitNanos(100000);
                                }
                            }
                            catch (Exception e)
                            {
                                System.out.println("DCThread: gameLogic: " + e.toString());
                                System.exit(704);
                            }
                            finally
                            {
                                this.reentrantLock.unlock();
                            }
                        }

                        if (this.dcInterface.isRedistributionNeeded(this.pseudonym, this.cipheredSignedSessionID,
                                this.tableID)) resetDistribution();
                    }
                }
                else
                {
                    DominoesGameState gameState = this.dcInterface.getGameState(this.pseudonym,
                            this.cipheredSignedSessionID, this.tableID);

                    System.out.println(gameState.toString());
                    if (gameState.getPlayedPieces().isEmpty())
                    {
                        String highestDouble = getHighestDouble();
                        if (this.dcInterface.playPiece(this.pseudonym, this.cipheredSignedSessionID, this.tableID,
                                "First", highestDouble, highestDouble.split("//|")[0]))
                            this.gamePieces.remove(highestDouble);
                        else System.out.println("\n[CLIENT] Error playing the piece.");
                    }
                    else
                    {
                        if (gameState.getWinner() == -1)
                        {
                            int option = clientGameMenu();
                            switch (option)
                            {
                                case 1:
                                    System.out.println("\n[CLIENT] Playing a piece...");

                                    String[] tmpEndPoints = gameState.getEndPoints().toArray(new String[0]);
                                    int endPoint = endPointMenu(tmpEndPoints);
                                    int piece = piecesMenu(gamePieces.toArray(new String[0])) - 1;

                                    String[] pieceEndPoints = this.gamePieces.get(piece).split("\\|");
                                    int pieceEndPoint = pieceEndPointsMenu(pieceEndPoints);

                                    if (this.dcInterface.playPiece(this.pseudonym, this.cipheredSignedSessionID,
                                            this.tableID, tmpEndPoints[endPoint - 1], this.gamePieces.get(piece),
                                            pieceEndPoints[pieceEndPoint - 1]))
                                    {
                                        this.gamePieces.remove(piece);
                                    }
                                    else System.out.println("\n[CLIENT] Error playing the piece.");

                                    System.out.println("PLAYER PIECES: ");
                                    System.out.println(this.gamePieces.toString());

                                    break;
                                case 2:
                                    System.out.println("\n[CLIENT] Drawing a piece...");

                                    String drawnPiece = (String) DominoesCryptoSym.SymDecipher(Base64.getDecoder()
                                            .decode(this.dcInterface.drawPiece(this.pseudonym,
                                                    this.cipheredSignedSessionID, this.tableID)),
                                                        this.serverSessionSymKey);

                                    if (!drawnPiece.equals("Error"))
                                    {
                                        this.gamePieces.add(drawnPiece);
                                        this.committedPieces.add(drawnPiece);
                                    }
                                    else System.out.println("\n[CLIENT] Failed to draw a piece. None remaining.");

                                    randomizeCommitData();

                                    // TODO: BIT COMMITMENT CORRECTION

                                    // int bitCommitment = DominoesCommitData.generateHash(this.bitCommitRandom1,
                                    //         this.bitCommitRandom2, this.committedPieces.toArray(new String[0]));

                                    // TEMPORARY
                                    int bitCommitment = 1;

                                    DominoesCommitData commitData = new DominoesCommitData(this.bitCommitRandom1,
                                            bitCommitment);

                                    while (!this.dcInterface.updateCommitment(this.pseudonym,
                                            this.cipheredSignedSessionID, this.tableID, commitData))
                                        System.out.println("\n[CLIENT] Failed to update hand commitment.");

                                    System.out.println("PLAYER PIECES: ");
                                    System.out.println(this.gamePieces.toString());

                                    break;
                                case 3:
                                    System.out.println("\n[CLIENT] Listing game information...");
                                    System.out.println(gameState.toString());
                                    break;
                                case 4:
                                    System.out.println("\n[CLIENT] Denouncing cheating...");

                                    if (denounceCheatingMenu() == 1)
                                        this.dcInterface.denounceCheating(this.pseudonym, this.cipheredSignedSessionID,
                                                this.tableID);

                                    break;
                                case 5:
                                    System.out.println("\n[CLIENT] Skipping turn...");
                                    this.dcInterface.skipTurn(this.pseudonym, this.cipheredSignedSessionID,
                                            this.tableID);
                                    break;
                                default:
                                    System.out.println("\n[CLIENT] Unexpected Error...");
                                    System.exit(703);
                            }
                        }
                    }
                }
            }
        }

        System.out.println("\n[CLIENT] BEFORE CHEAT HANDLING!");

        if (!this.dcInterface.isHandlingCheating(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
        {
            DominoesGameState gameState = this.dcInterface.getGameState(this.pseudonym, this.cipheredSignedSessionID,
                    this.tableID);
            DominoesTableInfo dominoesTable = this.dcInterface.listTableInfo(this.pseudonym,
                    this.cipheredSignedSessionID, this.tableID);

            System.out.println(gameState.toString());
            System.out.println(dominoesTable.toString());

            if (protestMenu(dominoesTable.getPlayers()[gameState.getWinner()]) == 1)
                this.dcInterface.denounceCheating(this.pseudonym, this.cipheredSignedSessionID, this.tableID);
        }

        this.dcInterface.passedProtestMenu(this.pseudonym, this.cipheredSignedSessionID, this.tableID);
        while (!this.dcInterface.allPassedProtestMenu(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
        {
            this.reentrantLock.lock();
            try
            {
                synchronized (this)
                {
                    this.turnCondition.awaitNanos(100000);
                }
            }
            catch (Exception e)
            {
                System.out.println("DCThread: gameLogic: " + e.toString());
                System.exit(709);
            }
            finally
            {
                this.reentrantLock.unlock();
            }
        }

        while (this.dcInterface.isHandlingCheating(this.pseudonym, this.cipheredSignedSessionID, this.tableID)
                && !this.dcInterface.hasSentCommitData(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
        {
            DominoesCommitData commitData = new DominoesCommitData(this.committedPieces.toArray(new String[0]),
                    this.bitCommitRandom2);

            while(!this.dcInterface.sendCommitData(this.pseudonym, this.cipheredSignedSessionID, this.tableID,
                    commitData))
                System.out.println("\n[CLIENT] Could not send commit data. Retrying...");
        }

        System.out.println("\n[CLIENT] BEFORE CHEAT HANDLE WAITING!");

        while (this.dcInterface.isHandlingCheating(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
        {
            this.reentrantLock.lock();
            try
            {
                synchronized (this)
                {
                    this.turnCondition.awaitNanos(100000);
                }
            }
            catch (Exception e)
            {
                System.out.println("DCThread: gameLogic: " + e.toString());
                System.exit(708);
            }
            finally
            {
                this.reentrantLock.unlock();
            }
        }

        System.out.println("\n[CLIENT] BEFORE ACCOUNTING HANDLING!");

        while (this.dcInterface.isHandlingAccounting(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
        {
            DominoesAccountingInfo accountingInfo = this.dcInterface.getAccountingInfo(this.pseudonym,
                    this.cipheredSignedSessionID, this.tableID);
            int decision = accountingMenu(accountingInfo);

            while (!this.dcInterface.sendAccountingDecision(this.pseudonym, this.cipheredSignedSessionID, this.tableID,
                    decision == 1))
                System.out.println("\n[CLIENT] Could not send decision. Retrying...");

            while (!this.dcInterface.allSentDecision(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
            {
                this.reentrantLock.lock();
                try
                {
                    synchronized (this)
                    {
                        this.turnCondition.awaitNanos(100000);
                    }
                }
                catch (Exception e)
                {
                    System.out.println("DCThread: gameLogic: " + e.toString());
                    System.exit(707);
                }
                finally
                {
                    this.reentrantLock.unlock();
                }
            }

            if (this.dcInterface.allAgreedToAccounting(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
            {
                System.out.println("\n[CLIENT] Handling Accounting!");

                // TODO: HANDLE ACCOUNTING

                break;
            }
            else
            {
                System.out.println("\n[CLIENT] Not every member agreed to the accounting. Game terminating...");
                break;
            }
        }

        this.dcInterface.disbandTable(this.pseudonym, this.cipheredSignedSessionID, this.tableID);
        gameOver();
    }

    private String getTileToReturn()
    {
        return this.gamePieces.get(ThreadLocalRandom.current().nextInt(0,this.gamePieces.size()));
    }

    private String getHighestDouble()
    {
        for (String piece : new String[]{"6|6", "5|5", "4|4", "3|3", "2|2", "1|1", "0|0"})
            if (this.gamePieces.contains(piece)) return piece;
        return "None";
    }

    private void resetDistribution()
    {
        this.gamePieces.clear();
    }

    private void randomizeCommitData()
    {
        this.bitCommitRandom1 = ThreadLocalRandom.current().nextInt(0,32);
        this.bitCommitRandom2 = ThreadLocalRandom.current().nextInt(0,32);
    }

    private void gameOver()
    {
        this.gamePieces.clear();
        this.tableID = -1;
        randomizeCommitData();
        this.committedPieces = null;
    }

    private void establishSession()
    {
        System.out.print("\n[CLIENT] Establishing a session with the server...");

        while (!Arrays.equals(this.sessionPublicKey, this.dcInterface.greetServer(this.pseudonym,
                this.sessionPublicKey)))
        {
            this.reentrantLock.lock();
            try
            {
                synchronized (this)
                {
                    this.turnCondition.awaitNanos(100000);
                }
            }
            catch (Exception e)
            {
                System.out.println("DCThread: establishSession: " + e.toString());
                System.exit(7019);
            }
            finally
            {
                this.reentrantLock.unlock();
            }
        }

        System.out.print("\n[CLIENT] Server received session request.");
        System.out.print("\n[CLIENT] Requesting table credentials...");

        do
        {
            this.serverPublicKey = this.dcInterface.getServerPublicKey();

            this.reentrantLock.lock();
            try
            {
                synchronized (this)
                {
                    this.turnCondition.awaitNanos(100000);
                }
            }
            catch (Exception e)
            {
                System.out.println("DCThread: establishSession: " + e.toString());
                System.exit(707);
            }
            finally
            {
                this.reentrantLock.unlock();
            }
        }
        while (Arrays.equals(this.serverPublicKey, new byte[0]));

        this.cipheredSignedSessionID = DominoesCryptoAsym.AsymCipher(this.signedSessionID, this.serverPublicKey);

        System.out.print("\n[CLIENT] Table credentials obtained.");
        System.out.print("\n[CLIENT] Generating session data...");

        do
        {
            this.serverSessionSymKey = (byte[]) DominoesCryptoAsym.AsymDecipher(this.dcInterface.sendSessionID(
                    this.pseudonym, this.cipheredSignedSessionID), this.sessionPrivateKey);

            System.out.println("SESSION KEY: " + Arrays.toString(this.serverSessionSymKey));

            this.reentrantLock.lock();
            try
            {
                synchronized (this)
                {
                    this.turnCondition.awaitNanos(100000);
                }
            }
            catch (Exception e)
            {
                System.out.println("DCThread: establishSession: " + e.toString());
                System.exit(7337);
            }
            finally
            {
                this.reentrantLock.unlock();
            }
        }
        while (Arrays.equals(this.serverSessionSymKey, new byte[0]));

        this.cipheredSignedSessionID = DominoesCryptoSym.SymCipher(this.signedSessionID, this.serverSessionSymKey);

        System.out.print("\n[CLIENT] Session established successfully.");
    }

    private byte[] signSessionID()
    {
        return DominoesSignature.sign(this.sessionID, this.privateKey);
    }

    private int clientMainMenu()
    {
        while (true)
        {
            DominoesMenus.clientMainMenu();

            Integer option = sextupleCaseMenuSwitch();
            if (option != null) return option;
        }
    }

    private int clientPlayerCapMenu()
    {
        while (true)
        {
            DominoesMenus.clientPlayerCapMenu();

            Integer option = quadrupleCaseMenuSwitch();
            if (option != null) return option;
        }
    }

    private int clientTableMenu(boolean leader)
    {
        while (true)
        {
            if (leader) DominoesMenus.clientTableLeaderMenu();
            else DominoesMenus.clientTableGuestMenu();

            Integer option = tripleCaseMenuSwitch();
            if (option != null) return option;
        }
    }

    private int clientPieceDistributionMenu()
    {
        while (true)
        {
            DominoesMenus.clientPieceDistributionMenu();

            Integer option = quintupleCaseMenuSwitch();
            if (option != null) return option;
        }
    }

    private int clientGameMenu()
    {
        while (true)
        {
            DominoesMenus.clientGameMenu();

            Integer option = quintupleCaseMenuSwitch();
            if (option != null) return option;
        }
    }

    private int endPointMenu(String[] endPoints)
    {
        while (true)
        {
            DominoesMenus.endPointMenu(endPoints);

            int option = getMenuOption();
            if (option >= 1 && option <= endPoints.length) return option;
            else System.out.println("\n[CLIENT] Invalid option.\n" +
                    "[CLIENT] Must be a number within range [1-" + endPoints.length + "].");
        }
    }

    private int piecesMenu(String[] pieces)
    {
        while (true)
        {
            DominoesMenus.piecesMenu(pieces);

            int option = getMenuOption();
            if (option >= 1 && option <= pieces.length) return option;
            else System.out.println("\n[CLIENT] Invalid option.\n" +
                    "[CLIENT] Must be a number within range [1-" + pieces.length + "].");
        }
    }

    private int pieceEndPointsMenu(String[] pieceEndPoints)
    {
        while (true)
        {
            DominoesMenus.pieceEndPointMenu(pieceEndPoints);

            Integer option = doubleCaseMenuSwitch();
            if (option != null) return option;
        }
    }

    private int denounceCheatingMenu()
    {
        while (true)
        {
            DominoesMenus.denounceCheatingMenu();

            Integer option = doubleCaseMenuSwitch();
            if (option != null) return option;
        }
    }

    private int accountingMenu(DominoesAccountingInfo accountingInfo)
    {
        while (true)
        {
            DominoesMenus.accountingMenu(accountingInfo);

            Integer option = doubleCaseMenuSwitch();
            if (option != null) return option;
        }
    }

    private int protestMenu(String winner)
    {
        while (true)
        {
            DominoesMenus.protestMenu(winner);

            Integer option = doubleCaseMenuSwitch();
            if (option != null) return option;
        }
    }

    private Integer sextupleCaseMenuSwitch()
    {
        int option = getMenuOption();
        switch (option)
        {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return option;
            default:
                System.out.println("\n[CLIENT] Invalid option.\n[CLIENT] Must be a number within range [1-6].");
        }
        return null;
    }

    private Integer quintupleCaseMenuSwitch()
    {
        int option = getMenuOption();
        switch (option)
        {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return option;
            default:
                System.out.println("\n[CLIENT] Invalid option.\n[CLIENT] Must be a number within range [1-5].");
        }
        return null;
    }

    private Integer quadrupleCaseMenuSwitch()
    {
        int option = getMenuOption();
        switch (option)
        {
            case 1:
            case 2:
            case 3:
            case 4:
                return option;
            default:
                System.out.println("\n[CLIENT] Invalid option.\n[CLIENT] Must be a number within range [1-4].");
        }
        return null;
    }

    private Integer tripleCaseMenuSwitch()
    {
        int option = getMenuOption();
        switch (option)
        {
            case 1:
            case 2:
            case 3:
                return option;
            default:
                System.out.println("\n[CLIENT] Invalid option.\n[CLIENT] Must be a number within range [1-3].");
        }
        return null;
    }

    private Integer doubleCaseMenuSwitch()
    {
        int option = getMenuOption();
        switch (option)
        {
            case 1:
            case 2:
                return option;
            default:
                System.out.println("\n[CLIENT] Invalid option.\n[CLIENT] Must be a number within range [1-2].");
        }
        return null;
    }

    private static int getMenuOption()
    {
        int option;
        try
        {
            option = sc.nextInt();
        }
        catch (InputMismatchException e)
        {
            sc.next();
            option = -1;
        }
        return option;
    }
}
