package DominoesMisc;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

public class DominoesTable implements Serializable
{
    private static final long serialVersionUID = 1103L;
    private static int gID = 0;
    private static final int maxPieces = 6;

    private final int id;

    private DominoesDeck deck;
    private DominoesGameState gameState;

    private final String[] players;
    private final boolean[] readyStates;
    private final int[] playerPieceCount;
    private final boolean[] bitCommits;
    private final String[] playerDoubles;
    private final HashSet<Integer> leftToCommit;
    private final HashSet<Integer> leftToReset;
    private final boolean[] illegalMoves;

    private boolean started;
    private int firstPlayer;
    private boolean resetNeeded;
    private int turn;

    public DominoesTable(int playerCap, String tableLeader) throws DominoesTableException
    {
        if (!this.isValidPlayerCap(playerCap)) throw new DominoesTableException("Invalid player capacity value!");

        this.id = gID++;
        this.deck = new DominoesDeck();
        this.gameState = new DominoesGameState(playerCap);
        this.players = new String[playerCap];
        this.players[0] = tableLeader;
        this.readyStates = new boolean[playerCap];
        this.readyStates[0] = true;
        this.playerPieceCount = new int[playerCap];
        this.playerPieceCount[0] = 0;
        this.bitCommits = new boolean[playerCap];
        this.bitCommits[0] = false;
        this.playerDoubles = new String[playerCap];
        this.playerDoubles[0] = null;
        this.leftToCommit = new HashSet<>();
        this.leftToReset = new HashSet<>();
        this.illegalMoves = new boolean[playerCap];
        this.illegalMoves[0] = false;

        for (int i = 1; i < playerCap; i++)
        {
            this.players[i] = null;
            this.readyStates[i] = false;
            this.playerPieceCount[i] = 0;
            this.bitCommits[i] = false;
            this.playerDoubles[i] = null;
            this.illegalMoves[i] = false;
        }

        this.started = false;
        this.firstPlayer = -1;
        this.resetNeeded = false;
        this.turn = 0;
    }

    public static int getMaxPieces()
    {
        return maxPieces;
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

    public boolean distributionCommit(String pseudonym)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym))
            if (this.leftToCommit.contains(i)) if (this.playerPieceCount[i] == maxPieces)
            {
                this.bitCommits[i] = true;
                this.leftToCommit.remove(i);
                return true;
            }
        return false;
    }

    public boolean hasPlayerCommitted(String pseudonym)
    {
        for (int i = 0; i < this.players.length; i++) if (this.players[i].equals(pseudonym)) return this.bitCommits[i];
        return false;
    }

    public boolean haveAllCommitted()
    {
        for (boolean b : this.bitCommits) if (!b) return false;
        return true;
    }

    public boolean isTurn(String pseudonym)
    {
        return this.players[this.turn].equals(pseudonym);
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
        // TODO: Add game termination
        return false;
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
            Arrays.fill(this.bitCommits, false);
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
            System.out.println("DST: play piece: " + targetEndPoint + " ," + piece + " ," + pieceEndPoint);
            if (targetEndPoint.equals("First") && !this.gameState.getPlayedPieces().isEmpty()) return false;
            this.illegalMoves[i] = !this.gameState.playPiece(targetEndPoint, piece, pieceEndPoint, i)
                    || this.illegalMoves[i];
            System.out.println("DST: play piece: " + true);
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
        StringBuilder toReturn = new StringBuilder("Table ID = " + this.id + "\n");
        for (int i = 0; i < this.players.length; i++) if (this.players[i] != null)
        {
            toReturn.append((this.readyStates[i]) ? "<READY>" : "<NOT READY>");
            toReturn.append(" ").append(this.players[i]).append("\n");
        }

        return toReturn.toString();
    }
}
