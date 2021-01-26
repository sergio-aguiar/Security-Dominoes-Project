package DominoesMisc;

import DominoesSecurity.DominoesCryptoAsym;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

public class DominoesTable
{
    private static final long serialVersionUID = 1103L;
    private static int gID = 0;
    private static final int maxPieces = 6;

    private final int id;
    private DominoesDeck deck;
    private final DominoesGameState gameState;
    private final DominoesAccountingInfo accountingInfo;
    private final String[] players;
    private final boolean[] readyStates;
    private final int[] playerPieceCount;
    private final boolean[] bitCommitment;
    private final DominoesCommitData[] commitData;
    private final String[] playerDoubles;
    private final HashSet<Integer> leftToCommit;
    private final HashSet<Integer> leftToReset;
    private final boolean[] illegalMoves;
    private final DominoesCommitData[] commitGenData;
    private final Object[] decisionMade;
    private final boolean[] havePassedProtest;

    private final byte[][] playerPublicKeys;
    private final byte[][] playerSessionIDs;

    private final byte[] tablePrivateKey;
    private final byte[] tablePublicKey;

    private boolean started;
    private boolean ended;
    private int firstPlayer;
    private boolean resetNeeded;
    private boolean handlingCheating;
    private boolean handlingAccounting;
    private int turn;

    public DominoesTable(int playerCap, String tableLeader, byte[] leaderPublicKey, byte[] leaderSessionID)
            throws DominoesTableException
    {
        if (!this.isValidPlayerCap(playerCap)) throw new DominoesTableException("Invalid player capacity value!");

        this.id = gID++;
        this.deck = new DominoesDeck();
        this.gameState = new DominoesGameState(playerCap);
        this.accountingInfo = new DominoesAccountingInfo(playerCap);
        this.players = new String[playerCap];
        this.players[0] = tableLeader;
        this.readyStates = new boolean[playerCap];
        this.readyStates[0] = true;
        this.playerPieceCount = new int[playerCap];
        this.playerPieceCount[0] = 0;
        this.bitCommitment = new boolean[playerCap];
        this.bitCommitment[0] = false;
        this.commitData = new DominoesCommitData[playerCap];
        this.commitData[0] = null;
        this.playerDoubles = new String[playerCap];
        this.playerDoubles[0] = null;
        this.leftToCommit = new HashSet<>();
        this.leftToReset = new HashSet<>();
        this.illegalMoves = new boolean[playerCap];
        this.illegalMoves[0] = false;
        this.commitGenData = new DominoesCommitData[playerCap];
        this.commitGenData[0] = null;
        this.decisionMade = new Object[playerCap];
        this.decisionMade[0] = null;
        this.havePassedProtest = new boolean[playerCap];
        this.havePassedProtest[0] = false;
        this.playerPublicKeys = new byte[playerCap][];
        this.playerPublicKeys[0] = leaderPublicKey;
        this.playerSessionIDs = new byte[playerCap][];
        this.playerSessionIDs[0] = leaderSessionID;

        for (int i = 1; i < playerCap; i++)
        {
            this.players[i] = null;
            this.readyStates[i] = false;
            this.playerPieceCount[i] = 0;
            this.bitCommitment[i] = false;
            this.commitData[i] = null;
            this.playerDoubles[i] = null;
            this.illegalMoves[i] = false;
            this.commitGenData[i] = null;
            this.decisionMade[i] = null;
            this.havePassedProtest[i] = false;
            this.playerPublicKeys[i] = null;
            this.playerSessionIDs[i] = null;
        }

        Map<String, byte[]> keys = DominoesCryptoAsym.GenerateAsymKeys();
        this.tablePrivateKey = keys.get("private");
        this.tablePublicKey = keys.get("public");

        this.started = false;
        this.ended = false;
        this.firstPlayer = -1;
        this.resetNeeded = false;
        this.handlingCheating = false;
        this.handlingAccounting = false;
        this.turn = 0;
    }

    private boolean isValidPlayerCap(int playerCap)
    {
        return playerCap >= 2 && playerCap <= 4;
    }

    public boolean joinTable(String pseudonym)
    {
        boolean joined = false;
        for (int i = 1; i < this.players.length; i++) if (this.players[i] == null) {
            this.players[i] = pseudonym;
            joined = true;
            break;
        }
        return joined;
    }

    public boolean leaveTable(String pseudonym)
    {
        boolean left = false;
        for (int i = 1; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
        {
            this.players[i] = null;
            left = true;
            break;
        }
        return left;
    }

    public void incrementTurn()
    {
        if (this.haveAllCommitted())
        {
            if (this.turn == this.players.length - 1) this.turn = 0;
            else this.turn++;
        }
        else
        {
            if (this.turn == this.players.length - 1) this.turn = 0;
            else this.turn++;

            while (!this.leftToCommit.contains(this.turn))
            {
                if (this.turn == this.players.length - 1) this.turn = 0;
                else this.turn++;
            }
        }
    }

    public String distributionDrawPiece(String pseudonym)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
            if (this.playerPieceCount[i] < maxPieces)
            {
                this.playerPieceCount[i]++;
                String result = this.deck.drawPiece();
                System.out.println("Table res: " + result);
                return result;
            }
        return "Error";
    }

    public String drawPiece(String pseudonym)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
        {
            this.playerPieceCount[i]++;
            return this.deck.drawPiece();
        }
        return "Error";
    }

    public String distributionSwapPiece(String pseudonym, String piece)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
            if (this.playerPieceCount[i] > 0) return this.deck.swapTile(piece);
        return "Error";
    }

    public void distributionReturnPiece(String pseudonym, String piece)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
            if (this.playerPieceCount[i] > 0)
            {
                this.playerPieceCount[i]--;
                this.deck.returnTile(piece);
            }
    }

    public boolean distributionCommit(String pseudonym, DominoesCommitData commitData)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
            if (this.leftToCommit.contains(i)) if (this.playerPieceCount[i] == maxPieces)
            {
                this.bitCommitment[i] = true;
                this.commitData[i] = commitData;
                this.leftToCommit.remove(i);
                return true;
            }
        return false;
    }

    public boolean updateCommit(String pseudonym, DominoesCommitData commitData)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
        {
            this.commitData[i] = commitData;
            return true;
        }
        return false;
    }

    public boolean hasPlayerCommitted(String pseudonym)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
            return this.bitCommitment[i];
        return false;
    }

    public boolean haveAllCommitted()
    {
        for (boolean b : this.bitCommitment) if (!b) return false;
        return true;
    }

    public boolean isTurn(String pseudonym)
    {
        return this.players[this.turn].equals(pseudonym);
    }

    public boolean isHandlingCheating()
    {
        return this.handlingCheating;
    }

    public boolean isHandlingAccounting()
    {
        return this.handlingAccounting;
    }

    public int getId()
    {
        return this.id;
    }

    public int getPlayerCap()
    {
        return this.players.length;
    }

    public DominoesDeck getDeck()
    {
        return this.deck;
    }

    public String[] getPlayers()
    {
        return this.players;
    }

    public boolean[] getReadyStates()
    {
        return this.readyStates;
    }

    public int getFirstPlayer()
    {
        return this.firstPlayer;
    }

    public DominoesGameState getGameState()
    {
        return this.gameState;
    }

    public DominoesAccountingInfo getAccountingInfo()
    {
        return this.accountingInfo;
    }

    public void setDeck(DominoesDeck deck)
    {
        this.deck = deck;
    }

    public void setReady(String pseudonym)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
        {
            this.readyStates[i] = true;
            break;
        }
    }

    public boolean hasStarted()
    {
        return this.started;
    }

    public boolean hasEnded()
    {
        return this.ended;
    }

    public int getTurn()
    {
        return this.turn;
    }

    public boolean canDraw(String pseudonym)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
            return this.playerPieceCount[i] < maxPieces;
        return false;
    }

    public void handleCardDif(String pseudonym, int cardDif)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
        {
            this.playerPieceCount[i] += cardDif;
            break;
        }
    }

    public void setDouble(String pseudonym, String piece)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
        {
            this.playerDoubles[i] = piece;
            if (areAllDoublesSubmitted()) setFirstPlayer();
            break;
        }
    }

    public boolean areAllDoublesSubmitted()
    {
        for (String piece : this.playerDoubles) if (piece == null) return false;
        System.out.println("Player Doubles: " + Arrays.toString(this.playerDoubles));
        return true;
    }

    private void setFirstPlayer()
    {
        HashSet<String> pieces = new HashSet<>(Arrays.asList(this.playerDoubles));
        for (String piece : new String[]{"6|6", "5|5", "4|4", "3|3", "2|2", "1|1", "0|0"}) if (pieces.contains(piece))
            for (int i = 0; i < this.playerDoubles.length; i++) if (this.playerDoubles[i].equals(piece))
            {
                if (this.firstPlayer == -1)
                {
                    this.firstPlayer = i;
                    this.turn = i;
                    System.out.println("First player: " + i + " with the piece " + this.playerDoubles[i]);
                }
            }

        if (this.firstPlayer == -1) this.resetNeeded = true;
    }

    public boolean isRedistributionNeeded(String pseudonym)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym)) this.leftToReset.remove(i);
        if (this.leftToReset.isEmpty() && this.resetNeeded)
        {
            this.deck = new DominoesDeck();

            Arrays.fill(this.playerPieceCount, 0);
            Arrays.fill(this.bitCommitment, false);
            Arrays.fill(this.playerDoubles, null);

            for (int j = 0; j < this.players.length; j++)
            {
                this.leftToCommit.add(j);
                this.leftToReset.add(j);
            }

            this.turn = 0;
            this.resetNeeded = false;
        }
        return this.firstPlayer == -1;
    }

    public boolean playPiece(String pseudonym, String targetEndPoint, String piece, String pieceEndPoint)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
        {
            if (targetEndPoint.equals("First") && !this.gameState.getPlayedPieces().isEmpty()) return false;

            boolean alreadyPlayed = this.gameState.getPlayedPieces().contains(piece);
            this.illegalMoves[i] = !this.gameState.playPiece(targetEndPoint, piece, pieceEndPoint, i)
                    || alreadyPlayed
                    || this.illegalMoves[i];

            this.playerPieceCount[i]--;
            if (this.playerPieceCount[i] == 0)
            {
                this.gameState.setWinner(i);
                this.ended = true;
                this.handlingAccounting = true;
                calcAllAccounting();
            }

            return true;
        }
        return false;
    }

    public void startGame()
    {
        for (int i = 0; i < this.players.length; i++)
        {
            this.leftToCommit.add(i);
            this.leftToReset.add(i);
        }
        this.started = true;
    }

    public void denounceCheating(String pseudonym)
    {
        this.ended = true;
        this.handlingCheating = true;
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
            this.gameState.setDenounced(i);
    }

    public void setCommitGenData(String pseudonym, DominoesCommitData commitGenData)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
        {
            this.commitGenData[i] = commitGenData;

            boolean allCommitted = true;
            for (DominoesCommitData commitData : this.commitGenData) if (commitData == null)
            {
                allCommitted = false;
                break;
            }

            if (allCommitted)
            {
                computeCheaters();
                calcAllAccounting();

                this.handlingCheating = false;
                this.handlingAccounting = true;
            }
        }
    }

    public void setDecisionMade(String pseudonym, boolean decision)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
            this.decisionMade[i] = decision;
    }

    public boolean haveAllDecided()
    {
        for (Object decision : this.decisionMade) if (decision == null) return false;
        return true;
    }

    public boolean haveAllAgreedToAccounting()
    {
        for (Object decision : this.decisionMade) if (!((boolean) decision)) return false;
        return true;
    }

    public boolean hasSentCommitData(String pseudonym)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
            return this.commitGenData[i] != null;
        return false;
    }

    public void computeCheaters()
    {
        System.out.println(Arrays.toString(this.illegalMoves));
        System.out.println(this.gameState.isCheater(0));
        System.out.println(this.gameState.isCheater(1));

        for (int i = 0; i < this.players.length; i++)
        {
            if (this.illegalMoves[i])
            {
                this.gameState.setCheater(i);
                System.out.println("Setting player " + i + " as a cheater!");
                System.out.println(this.gameState.isCheater(i));
                continue;
            }

            if (this.commitData[i].hasBitCommitment()
                    && this.commitData[i].hasRandom1()
                    && this.commitGenData[i].hasRandom2()
                    && this.commitGenData[i].hasPieces())
            {
                if (this.commitData[i].getBitCommitment() != DominoesCommitData.generateHash(
                        this.commitData[i].getRandom1(), this.commitGenData[i].getRandom2(),
                        this.commitGenData[i].getPieces())) this.gameState.setCheater(i);
            }
            else this.gameState.setCheater(i);
        }
    }

    private int calcAccountingResult(String pseudonym)
    {
        int result = 0;
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
        {
            if (!areThereCheaters())
            {
                if (this.gameState.getDenounced()[i])
                {
                    System.out.println("\nTHERE ARE NO CHEATERS! AND DENOUNCED BY " + i);
                    for (int pieceCount : this.playerPieceCount) result -= pieceCount;
                }
                else if (this.gameState.getWinner() == i)
                {
                    System.out.println("\nTHERE ARE CHEATERS! AND WON BY " + i);
                    for (int pieceCount : this.playerPieceCount) result += pieceCount;
                }
                else if (this.gameState.getWinner() != -1)
                {
                    System.out.println("\nTHERE ARE CHEATERS! AND NOT WON BY " + i);
                    result -= this.playerPieceCount[i];
                }
            }
            else
            {
                System.out.println("\nIS CHEATER: " +this.gameState.isCheater(i));
                if (this.gameState.isCheater(i))
                {
                    System.out.println("\nTHERE ARE CHEATERS! AND ONE IS " + i);
                    for (int pieceCount : this.playerPieceCount) result -= pieceCount;
                }
                else if (this.gameState.getWinner() == i)
                {
                    System.out.println("\nTHERE ARE CHEATERS! AND WON BY " + i);
                    for (int pieceCount : this.playerPieceCount) result += pieceCount;
                }
                else if (this.gameState.getWinner() != -1)
                {
                    System.out.println("\nTHERE ARE CHEATERS! AND NOT WON BY " + i);
                    result -= this.playerPieceCount[i];
                }
            }

            System.out.print("\nPlayer " + this.players[i] + " result: " + result);
        }
        return result;
    }

    private void calcAllAccounting()
    {
        for (int i = 0; i < this.players.length; i++)
            this.accountingInfo.setAccountingInfo(i, this.players[i], calcAccountingResult(this.players[i]));
    }

    private boolean areThereCheaters()
    {
        for (int i = 0; i < this.players.length; i++) if (this.gameState.isCheater(i)) return true;
        return false;
    }

    public void passedProtest(String pseudonym)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
            this.havePassedProtest[i] = true;
    }

    public boolean haveAllPassedProtest()
    {
        for (int i = 0; i < this.players.length; i++) if (!this.havePassedProtest[i]) return false;
        return true;
    }

    public void reportPlayerKey(String pseudonym, byte[] publicKey)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
            this.playerPublicKeys[i] = publicKey;
    }

    public void reportPlayerSessionID(String pseudonym, byte[] cipheredSessionID)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
            this.playerSessionIDs[i] = (byte[]) DominoesCryptoAsym.AsymDecipher(cipheredSessionID,
                    this.tablePrivateKey);
    }

    public byte[] getTablePublicKey()
    {
        return this.tablePublicKey;
    }

    public boolean isFull()
    {
        for (String player : this.players) if (player == null) return false;
        return true;
    }

    public boolean isResetNeeded()
    {
        return this.resetNeeded;
    }

    @Override
    public String toString()
    {
        return "DominoesTable{" +
                "id=" + this.id +
                ", deck=" + this.deck +
                ", gameState=" + this.gameState +
                ", accountingInfo=" + this.accountingInfo +
                ", players=" + Arrays.toString(this.players) +
                ", readyStates=" + Arrays.toString(this.readyStates) +
                ", playerPieceCount=" + Arrays.toString(this.playerPieceCount) +
                ", bitCommitment=" + Arrays.toString(this.bitCommitment) +
                ", commitData=" + Arrays.toString(this.commitData) +
                ", playerDoubles=" + Arrays.toString(this.playerDoubles) +
                ", leftToCommit=" + this.leftToCommit +
                ", leftToReset=" + this.leftToReset +
                ", illegalMoves=" + Arrays.toString(this.illegalMoves) +
                ", commitGenData=" + Arrays.toString(this.commitGenData) +
                ", decisionMade=" + Arrays.toString(this.decisionMade) +
                ", started=" + this.started +
                ", ended=" + this.ended +
                ", firstPlayer=" + this.firstPlayer +
                ", resetNeeded=" + this.resetNeeded +
                ", handlingCheating=" + this.handlingCheating +
                ", handlingAccounting=" + this.handlingAccounting +
                ", turn=" + this.turn +
                '}';
    }
}
