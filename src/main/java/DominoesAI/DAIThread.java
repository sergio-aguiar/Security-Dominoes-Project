package DominoesAI;

import DominoesClient.DCInterface;
import DominoesClient.DCThread;
import DominoesMisc.*;
import DominoesSecurity.DominoesCC;
import DominoesSecurity.DominoesCryptoAsym;
import DominoesSecurity.DominoesCryptoSym;
import DominoesSecurity.DominoesSignature;

import java.security.Key;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class DAIThread extends DCThread
{
    private final boolean createTable;
    public DAIThread(DCInterface dcInterface, boolean createTable)
    {
        super(dcInterface);
        this.createTable = createTable;
    }

    @Override
    public void run()
    {
        System.out.println("[CLIENT] Dominoes Client starting...");

        if (!this.dcInterface.isUserRegistered(this.identifier)) this.dcInterface.registerUser(this.identifier);

        establishSession();

        while (true)
        {
            if (this.createTable)
            {
                System.out.println("\n[CLIENT] Creating a dominoes table...");
                this.tableID = this.dcInterface.createTable(this.pseudonym, this.cipheredSignedSessionID, 3);

                while (!this.dcInterface.startGame(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
                {
                    System.out.println("\n[CLIENT] Could not start the game.\n" +
                            "[CLIENT] Some players have yet to ready up.");

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
                        System.out.println("DAIThread: run: " + e.toString());
                        System.exit(759);
                    }
                    finally
                    {
                        this.reentrantLock.unlock();
                    }
                }

                System.out.println("\n[CLIENT] Starting Game...");
                gameLogic();
            }
            else
            {
                System.out.println("\n[CLIENT] Joining a random dominoes table...");
                do
                {
                    this.tableID = this.dcInterface.joinRandomTable(this.pseudonym,
                            this.cipheredSignedSessionID);

                    if (this.tableID == -1)
                    {
                        System.out.println("[CLIENT] No available tables to join...");

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
                            System.out.println("DAIThread: run: " + e.toString());
                            System.exit(709);
                        }
                        finally
                        {
                            this.reentrantLock.unlock();
                        }
                    }
                }
                while (this.tableID == -1);

                System.out.print("\n[CLIENT] Marking self as ready...");

                if (this.dcInterface.markAsReady(this.pseudonym, this.cipheredSignedSessionID,
                        this.tableID))
                {
                    System.out.println("\n[CLIENT] Awaiting game Start...");
                    gameLogic();
                }
                else
                {
                    System.out.println("\n[CLIENT] The table was disbanded.");
                }
            }
        }
    }

    private void gameLogic()
    {
        while (!this.dcInterface.hasGameEnded(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
        {
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
                if (!this.dcInterface.hasKeySortingStarted(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
                    this.dcInterface.startKeySorting(this.pseudonym, this.cipheredSignedSessionID, this.tableID);
                else
                {
                    this.playerPublicKeys = this.dcInterface.getPlayerPublicKeys(this.pseudonym,
                            this.cipheredSignedSessionID, this.tableID);

                    DominoesSymKeyMatrix symKeyMatrix = this.dcInterface.getSymKeyDistributionMatrix(this.pseudonym,
                            this.cipheredSignedSessionID, this.tableID);

                    for (int i = 0; i < symKeyMatrix.getPlayers().length; i++)
                        if (symKeyMatrix.getPlayers()[i].equals(this.pseudonym))
                        {
                            for (int j = i + 1; j < symKeyMatrix.getSymKeyMatrix()[i].length; j++)
                            {
                                byte[] symKey = DominoesCryptoSym.generateSymKeys(Long.toString(
                                        System.currentTimeMillis()));

                                symKeyMatrix.updateKey(i, j, DominoesCryptoAsym.asymCipher(symKey,
                                        this.sessionPublicKey));

                                symKeyMatrix.updateKey(j, i, DominoesCryptoAsym.asymCipher(symKey,
                                        this.playerPublicKeys[j]));
                            }
                        }

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
                this.playerSessionSymKeys = this.dcInterface.getSessionSymKeys(this.pseudonym,
                        this.cipheredSignedSessionID, this.tableID);

                for (int i = 0; i < this.playerSessionSymKeys.length; i++) if (this.playerSessionSymKeys[i] != null)
                    this.playerSessionSymKeys[i] = (byte[]) DominoesCryptoAsym.asymDecipher(
                            this.playerSessionSymKeys[i], this.sessionPrivateKey);
            }

            if (!this.dcInterface.hasPlayerCommitted(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
            {
                if (!this.dcInterface.hasDeckBeenProtected(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
                {
                    DominoesDeck deck = this.dcInterface.getDeck(this.pseudonym, this.cipheredSignedSessionID,
                            this.tableID);

                    Map<String, byte[]> keys = DominoesCryptoAsym.generateAsymKeys();
                    this.deckDistributionPrivateKey = keys.get("private");
                    byte[] deckDistributionPublicKey = keys.get("public");

                    deck.asymCipher(deckDistributionPublicKey);

                    this.dcInterface.notifyDeckProtected(this.pseudonym, this.cipheredSignedSessionID, this.tableID);

                    if (!this.dcInterface.returnDeck(this.pseudonym, this.cipheredSignedSessionID,
                            this.tableID, deck, DominoesCryptoSym.symCipher(0, this.serverSessionSymKey)))
                    {
                        System.out.println("\n[CLIENT] Unexpected Error...");
                        System.exit(703);
                    }
                }
                else if (this.dcInterface.isDeckSorting(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
                {
                    DominoesDeck deck = this.dcInterface.getDeck(this.pseudonym,
                            this.cipheredSignedSessionID, this.tableID);

                    int lastPlayer = (int) DominoesCryptoSym.symDecipher(this.dcInterface.getLastTurn(this.pseudonym,
                            this.cipheredSignedSessionID, this.tableID), this.serverSessionSymKey);

                    int nextPlayer = (int) DominoesCryptoSym.symDecipher(this.dcInterface.getNextTurn(this.pseudonym,
                            this.cipheredSignedSessionID, this.tableID), this.serverSessionSymKey);

                    if (!this.dcInterface.isDeckSentFromServer(this.pseudonym, this.cipheredSignedSessionID,
                            this.tableID) && this.playerSessionSymKeys[lastPlayer] != null)
                        deck.symDecipher(this.playerSessionSymKeys[lastPlayer]);

                    // TODO: BIT COMMITMENT CORRECTION
                    // int bitCommitment = DominoesCommitData.generateHash(this.bitCommitRandom1,
                    //         this.bitCommitRandom2, this.gamePieces.toArray(new String[0]));

                    // TEMPORARY
                    int bitCommitment = 1;

                    DominoesCommitData commitData = new DominoesCommitData(this.bitCommitRandom1,
                            bitCommitment);

                    if (this.dcInterface.commitHand(this.pseudonym, this.cipheredSignedSessionID, this.tableID,
                            commitData))
                    {
                        System.out.println("\n[CLIENT] Committing your hand...");

                        if (this.playerSessionSymKeys[nextPlayer] != null)
                            deck.symCipher(this.playerSessionSymKeys[nextPlayer]);

                        if (!this.dcInterface.returnDeck(this.pseudonym, this.cipheredSignedSessionID,
                                this.tableID, deck, DominoesCryptoSym.symCipher(0,
                                        this.serverSessionSymKey)))
                        {
                            System.out.println("\n[CLIENT] Unexpected Error...");
                            System.exit(703);
                        }
                    }
                    else
                    {
                        double distRand = ThreadLocalRandom.current().nextDouble();

                        if (distRand < 0.05)
                        {
                            System.out.println("\n[CLIENT] Drawing a piece...");

                            if (this.dcInterface.canDraw(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
                            {
                                String tile1 = deck.drawPiece();
                                if (tile1 != null) this.gamePieces.add(tile1);
                                else System.out.println("\n[CLIENT] No pieces left to draw.");

                                if (this.playerSessionSymKeys[nextPlayer] != null)
                                    deck.symCipher(this.playerSessionSymKeys[nextPlayer]);

                                if (!this.dcInterface.returnDeck(this.pseudonym, this.cipheredSignedSessionID,
                                        this.tableID, deck, DominoesCryptoSym.symCipher(1,
                                                this.serverSessionSymKey)))
                                {
                                    System.out.println("\n[CLIENT] Unexpected Error...");
                                    System.exit(703);
                                }
                            }
                            else System.out.println("\n[CLIENT] Could not draw a piece. Hand full.");
                        }
                        else if (distRand < 0.1)
                        {
                            if (this.gamePieces.size() > 0)
                            {
                                String tile2 = getTileToReturn();
                                deck.returnPiece(tile2);
                                this.gamePieces.remove(tile2);

                                if (this.playerSessionSymKeys[nextPlayer] != null)
                                    deck.symCipher(this.playerSessionSymKeys[nextPlayer]);

                                if (!this.dcInterface.returnDeck(this.pseudonym, this.cipheredSignedSessionID,
                                        this.tableID, deck, DominoesCryptoSym.symCipher(-1,
                                                this.serverSessionSymKey)))
                                {
                                    System.out.println("\n[CLIENT] Unexpected Error...");
                                    System.exit(703);
                                }
                            }
                            else System.out.println("\n[CLIENT] There are no pieces to return.");
                        }
                        else if (distRand < 0.55)
                        {
                            System.out.println("\n[CLIENT] Swapping a piece...");
                            if (this.gamePieces.size() > 0)
                            {
                                String tile3 = getTileToReturn();
                                this.gamePieces.add(deck.swapPiece(tile3));
                                this.gamePieces.remove(tile3);

                                if (this.playerSessionSymKeys[nextPlayer] != null)
                                    deck.symCipher(this.playerSessionSymKeys[nextPlayer]);

                                if (!this.dcInterface.returnDeck(this.pseudonym, this.cipheredSignedSessionID,
                                        this.tableID, deck, DominoesCryptoSym.symCipher(0,
                                                this.serverSessionSymKey)))
                                {
                                    System.out.println("\n[CLIENT] Unexpected Error...");
                                    System.exit(703);
                                }
                            }
                            else System.out.println("\n[CLIENT] There are no pieces to swap.");
                        }
                        else
                        {
                            System.out.println("\n[CLIENT] Skipping a turn...");

                            if (this.playerSessionSymKeys[nextPlayer] != null)
                                deck.symCipher(this.playerSessionSymKeys[nextPlayer]);

                            if (!this.dcInterface.returnDeck(this.pseudonym, this.cipheredSignedSessionID,
                                    this.tableID, deck, DominoesCryptoSym.symCipher(0,
                                            this.serverSessionSymKey)))
                            {
                                System.out.println("\n[CLIENT] Unexpected Error...");
                                System.exit(703);
                            }
                        }
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
                                            (byte[]) DominoesCryptoAsym.asymDecipher(
                                                    Base64.getDecoder().decode(this.gamePieces.get(i)), key)));
                            }

                            byte[] key = (byte[]) deckProtection.pop();

                            for (int i = 0; i < this.gamePieces.size(); i++)
                                this.gamePieces.set(i, (String) DominoesCryptoAsym.asymDecipher(
                                        Base64.getDecoder().decode(this.gamePieces.get(i)), key));

                            this.committedPieces = new ArrayList<>(this.gamePieces);
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
                                Base64.getEncoder().encodeToString(DominoesCryptoSym.symCipher(getHighestDouble(),
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
                            System.out.println("\n[CLIENT] Listing game information...");
                            System.out.println(gameState.toString());

                            System.out.println("\n[CLIENT] Game pieces: " + this.gamePieces.toString());

                            System.out.println("\n[CLIENT] Playing a piece...");
                            String[] tmpEndPoints = gameState.getEndPoints().toArray(new String[0]);

                            if (this.dcInterface.playPiece(this.pseudonym, this.cipheredSignedSessionID,
                                    this.tableID, tmpEndPoints[0], this.gamePieces.get(0),
                                    this.gamePieces.get(0).split("\\|")[0]))
                            {
                                this.gamePieces.remove(this.gamePieces.get(0));
                            }
                            else System.out.println("\n[CLIENT] Error playing the piece.");
                        }
                    }
                }
            }
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

        while (this.dcInterface.isHandlingAccounting(this.pseudonym, this.cipheredSignedSessionID, this.tableID))
        {
            DominoesAccountingInfo accountingInfo = this.dcInterface.getAccountingInfo(this.pseudonym,
                    this.cipheredSignedSessionID, this.tableID);

            while (!this.dcInterface.sendAccountingDecision(this.pseudonym, this.cipheredSignedSessionID, this.tableID,
                    true))
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
                X509Certificate cert = DominoesCC.getCert();
                Map<String, Key> keys = DominoesCC.getKeys(cert);

                if (this.dcInterface.proveUserIdentity(this.pseudonym, DominoesCryptoSym.symCipher(
                        DominoesSignature.sign(this.sessionID, keys.get("privateKey")), this.serverSessionSymKey),
                        this.tableID, this.identifier, keys.get("publicKey")))
                {
                    System.out.print("\n[CLIENT] Identity verified. Accounting successful.");
                    this.dcInterface.setPseudonymAsUsed(this.pseudonym, this.identifier);
                }
                else System.out.println("\n[CLIENT] Failed identity verification. Accounting did not proceed.");

                this.sessionID = generateSessionID();
                this.pseudonym = generatePseudonym(this.sessionID, keys.get("privateKey"));
                establishSession();
                this.signedSessionID = signSessionID(keys.get("privateKey"));

                Map<String, byte[]> sessionKeys = DominoesCryptoAsym.generateAsymKeys();
                this.sessionPrivateKey = sessionKeys.get("private");
                this.sessionPublicKey = sessionKeys.get("public");

                while (!this.dcInterface.haveAllFinishedAccounting(this.pseudonym, this.cipheredSignedSessionID,
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
                        System.exit(705);
                    }
                    finally
                    {
                        this.reentrantLock.unlock();
                    }
                }
            }
            else
            {
                System.out.println("\n[CLIENT] Not every member agreed to the accounting. Game terminating...");
            }
            break;
        }

        this.dcInterface.disbandTable(this.pseudonym, this.cipheredSignedSessionID, this.tableID);

        System.out.print("\n[CLIENT] Fetching score...");
        System.out.print("\n[CLIENT] Score: " + DominoesCryptoSym.symDecipher(
                this.dcInterface.getUserScore(this.pseudonym, this.cipheredSignedSessionID,
                        this.identifier), this.serverSessionSymKey));

        X509Certificate cert = DominoesCC.getCert();
        Map<String, Key> keys = DominoesCC.getKeys(cert);

        this.sessionID = generateSessionID();
        this.pseudonym = generatePseudonym(this.sessionID, keys.get("privateKey"));
        establishSession();
        this.signedSessionID = signSessionID(keys.get("privateKey"));

        Map<String, byte[]> sessionKeys = DominoesCryptoAsym.generateAsymKeys();
        this.sessionPrivateKey = sessionKeys.get("private");
        this.sessionPublicKey = sessionKeys.get("public");

        System.exit(0);
    }
}