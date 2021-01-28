package DominoesServer;

import DominoesClient.DCInterface;
import DominoesDatabase.DSQLiteConnection;
import DominoesMisc.*;
import DominoesSecurity.DominoesCryptoAsym;
import DominoesSecurity.DominoesCryptoSym;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class DSImplementation implements DCInterface
{
    private final ReentrantLock reentrantLock;
    private final ArrayList<DominoesTable> dominoesTables;

    private final HashMap<String, byte[]> playerSessionIDs;
    private final HashMap<String, byte[]> playerPublicKeys;
    private final HashMap<String, byte[]> playerSessionSymKeys;
    private final byte[] serverPrivateKey;
    private final byte[] serverPublicKey;

    public DSImplementation()
    {
        this.reentrantLock = new ReentrantLock();
        this.dominoesTables = new ArrayList<>();
        this.playerSessionIDs = new HashMap<>();
        this.playerPublicKeys = new HashMap<>();
        this.playerSessionSymKeys = new HashMap<>();

        Map<String, byte[]> keys = DominoesCryptoAsym.GenerateAsymKeys();
        this.serverPrivateKey = keys.get("private");
        this.serverPublicKey = keys.get("public");
    }

    @Override
    public int createTable(String pseudonym, byte[] cipheredSessionID, int playerCap)
    {
        int tableID;
        this.reentrantLock.lock();
        try
        {
            DominoesTable table = new DominoesTable(playerCap, pseudonym);
            tableID = table.getId();
            this.dominoesTables.add(table);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: createTable: " + e.toString());
            tableID = -1;
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return tableID;
    }

    @Override
    public DominoesTableInfo[] listAvailableTables(String pseudonym, byte[] cipheredSessionID)
    {
        DominoesTableInfo[] tables;
        this.reentrantLock.lock();
        try
        {
            tables = new DominoesTableInfo[this.dominoesTables.size()];
            for (int i = 0; i < this.dominoesTables.size(); i++)
                tables[i] = new DominoesTableInfo(this.dominoesTables.get(i).getId(),
                        this.dominoesTables.get(i).getPlayers(), this.dominoesTables.get(i).getReadyStates());
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: listAvailableTables: " + e.toString());
            tables = new DominoesTableInfo[0];
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return tables;
    }

    @Override
    public boolean joinTable(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean joined = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                joined = table.joinTable(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: joinTable: " + e.toString());
            joined = false;
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return joined;
    }

    @Override
    public int joinRandomTable(String pseudonym, byte[] cipheredSessionID)
    {
        int tableID = -1;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (!table.isFull()) if (table.joinTable(pseudonym))
                tableID = table.getId();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: joinRandomTable: " + e.toString());
            tableID = -1;
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return tableID;
    }

    @Override
    public boolean startGame(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        int started = 0;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
            {
                for (boolean ready : table.getReadyStates()) if (!ready)
                {
                    started = -1;
                    break;
                }
                if (started != -1)
                {
                    started = table.getId();
                    table.startGame();
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: startGame: " + e.toString());
            started = -1;
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return started >= 0;
    }

    @Override
    public void disbandTable(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        this.reentrantLock.lock();
        try
        {
            this.dominoesTables.removeIf(table -> table.getId() == tableID);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: disbandTable: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
    }

    @Override
    public boolean markAsReady(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        if (tableID < 0) return false;

        boolean marked = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                for (int i = 0; i < table.getPlayers().length; i++) if (table.getPlayers()[i].equals(pseudonym))
                {
                    table.setReady(pseudonym);
                    marked = true;
                    break;
                }
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: markAsReady: " + e.toString());
            marked = false;
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return marked;
    }

    @Override
    public DominoesTableInfo listTableInfo(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DominoesTableInfo dTable = null;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                dTable = new DominoesTableInfo(table.getId(), table.getPlayers(), table.getReadyStates());
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: listTableInfo: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }

        System.out.println(dTable);

        return dTable;
    }

    @Override
    public void leaveTable(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        if (tableID < 0) return;

        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                for (int i = 0; i < table.getPlayers().length; i++)
                    if (table.getPlayers()[i] != null && table.getPlayers()[i].equals(pseudonym))
                        table.leaveTable(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: leaveTable: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
    }

    @Override
    public boolean isPlayerTurn(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.isTurn(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: isPlayerTurn: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean hasGameEnded(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID) result = table.hasEnded();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: hasGameEnded: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean isDeckSorting(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = true;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = !table.haveAllCommitted();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: isDeckSorting: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean canDraw(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = true;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.canDraw(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: canDraw: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public DominoesDeck getDeck(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DominoesDeck dDeck = null;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID) dDeck = table.getDeck();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: getDeck: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return dDeck;
    }

    @Override
    public boolean returnDeck(String pseudonym, byte[] cipheredSessionID, int tableID, DominoesDeck deck, int pieceDif)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
            {
                table.setDeck(deck);
                table.handleCardDif(pseudonym, pieceDif);
                result = true;
                table.incrementTurn();
            }
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: returnDeck: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public void skipTurn(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID) table.incrementTurn();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: skipTurn: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
    }

    @Override
    public boolean commitHand(String pseudonym, byte[] cipheredSessionID, int tableID, DominoesCommitData commitData)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
            {
                result = table.distributionCommit(pseudonym, commitData);
                if (result) table.incrementTurn();
            }
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: commitHand: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean hasPlayerCommitted(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.hasPlayerCommitted(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: hasPlayerCommitted: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean isHandlingStart(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.getFirstPlayer() == -1;
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: isHandlingStart: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public void stateHighestDouble(String pseudonym, byte[] cipheredSessionID, int tableID, String piece)
    {
        this.reentrantLock.lock();
        try
        {
            System.out.println("Player: " + pseudonym + " highest is " + piece);
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
            {
                table.incrementTurn();
                table.setDouble(pseudonym, piece);
            }
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: stateHighestDouble: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
    }

    @Override
    public boolean hasDoubleCheckingEnded(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.areAllDoublesSubmitted();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: hasDoubleCheckingEnded: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean isRedistributionNeeded(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.isRedistributionNeeded(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: isRedistributionNeeded: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public DominoesGameState getGameState(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DominoesGameState gameState = null;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                gameState = table.getGameState();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: getGameState: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return gameState;
    }

    @Override
    public boolean isResetNeeded(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.isResetNeeded();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: isResetNeeded: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean playPiece(String pseudonym, byte[] cipheredSessionID, int tableID, String targetEndPoint,
                             String piece, String pieceEndPoint)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
            {
                result = table.playPiece(pseudonym, targetEndPoint, piece, pieceEndPoint);
                if (result) table.incrementTurn();
            }
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: playPiece: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public String drawPiece(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        String result = "Error";
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
            {
                result = table.drawPiece(pseudonym);
                if (result == null) result = "Error";
            }
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: playPiece: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public void denounceCheating(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                table.denounceCheating(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: denounceCheating: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
    }

    @Override
    public boolean isHandlingCheating(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.isHandlingCheating();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: playPiece: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean updateCommitment(String pseudonym, byte[] cipheredSessionID, int tableID,
                                    DominoesCommitData commitData)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
            {
                result = table.updateCommit(pseudonym, commitData);
                if (result) table.incrementTurn();
            }
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: updateCommitment: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean sendCommitData(String pseudonym, byte[] cipheredSessionID, int tableID,
                                  DominoesCommitData commitData)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
            {
                table.setCommitGenData(pseudonym, commitData);
                result = true;
                table.incrementTurn();
            }
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: sendCommitData: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean hasSentCommitData(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.hasSentCommitData(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: hasSentCommitData: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean isHandlingAccounting(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.isHandlingAccounting();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: isHandlingAccounting: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public DominoesAccountingInfo getAccountingInfo(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DominoesAccountingInfo accountingInfo = null;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                accountingInfo = table.getAccountingInfo();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: getAccountingInfo: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return accountingInfo;
    }

    @Override
    public boolean sendAccountingDecision(String pseudonym, byte[] cipheredSessionID, int tableID, boolean decision)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
            {
                table.setDecisionMade(pseudonym, decision);
                result = true;
            }
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: sendAccountingDecision: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean allSentDecision(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.haveAllDecided();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: allSentDecision: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean allAgreedToAccounting(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.haveAllAgreedToAccounting();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: allAgreedToAccounting: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public void passedProtestMenu(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                table.passedProtest(pseudonym);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: passedProtestMenu: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
    }

    @Override
    public boolean allPassedProtestMenu(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            for (DominoesTable table : this.dominoesTables) if (table.getId() == tableID)
                result = table.haveAllPassedProtest();
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: allPassedProtestMenu: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean isUserRegistered(String user)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            result = DSQLiteConnection.isUserRegistered(user);
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: isUserRegistered: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean registerUser(String user)
    {
        boolean result = false;
        this.reentrantLock.lock();
        try
        {
            DSQLiteConnection.registerUser(user);
            result = true;
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: isUserRegistered: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public byte[] greetServer(String pseudonym, byte[] publicKey)
    {
        byte[] result;
        this.reentrantLock.lock();
        try
        {
            this.playerPublicKeys.put(pseudonym, publicKey);
            result = publicKey;
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: greetServer: " + e.toString());
            result = new byte[0];
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public byte[] getServerPublicKey()
    {
        byte[] result = new byte[0];
        this.reentrantLock.lock();
        try
        {
            result = this.serverPublicKey;
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: getServerPublicKey: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public byte[] sendSessionID(String pseudonym, byte[] cipheredSessionID)
    {
        byte[] result = new byte[0];
        this.reentrantLock.lock();
        try
        {
            this.playerSessionIDs.put(pseudonym, (byte[]) DominoesCryptoAsym.AsymDecipher(cipheredSessionID,
                    this.serverPrivateKey));

            byte[] sessionSymKey = DominoesCryptoSym.GenerateSymKeys(Long.toString(System.currentTimeMillis()));
            this.playerSessionSymKeys.put(pseudonym, sessionSymKey);
            result = sessionSymKey;
        }
        catch (Exception e)
        {
            System.out.println("DSImplementation: sendSessionID: " + e.toString());
        }
        finally
        {
            this.reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public boolean hasKeySortingStarted(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        return false;
    }

    @Override
    public void startKeySorting(String pseudonym, byte[] cipheredSessionID, int tableID)
    {

    }

    @Override
    public boolean hasKeySortingEnded(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        return false;
    }

    @Override
    public byte[][] getPlayerPublicKeys(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        return new byte[0][];
    }

    @Override
    public DominoesSymKeyMatrix getSymKeyDistributionMatrix(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        return null;
    }

    @Override
    public boolean returnSymKeyDistributionMatrix(String pseudonym, byte[] cipheredSessionID, int tableID,
                                                  DominoesSymKeyMatrix symKeyMatrix)
    {
        return false;
    }

    @Override
    public byte[][] getSessionSymKeys(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        return new byte[0][];
    }
}
