package DominoesMisc;

import DominoesSecurity.DominoesCryptoAsym;
import DominoesSecurity.DominoesCryptoSym;

import java.io.Serializable;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The deck management system.
 * Manages Draw Swap and Insert
 * @author Ricardo Rodrigues Azevedo
 * @version 1.2
 * @since 2020-10-17
 */

public class DominoesDeck implements Serializable
{
    private static final long serialVersionUID = 1104L;
    private final String[] deck;
    private final int size = 28;
    protected int pointer = 27;
    private int cipherDepth = 0;

    public DominoesDeck(){
        this.deck = new String[size];
        generateSet();
    }

    /**
     * Method for initialize the deck. Also shuffle it
     */
    private void generateSet()
    {
        int count = 0;
        for (int i = 0; i < 7; i++) for (int j=i; j < 7; j++) this.deck[count++] = (i + "|" + j);

        shuffle();
    }

    private void swapTiles(String[] set, int index, int newIndex, int maxSize)
    {
        if(index < 0 || index > maxSize) return;
        if(newIndex < 0 || newIndex > maxSize) return;

        if (index < newIndex)
        {
            String tmp = set[index];
            set[index] = set[newIndex];
            set[newIndex] = tmp;
        }
        else
        {
            String tmp = set[newIndex];
            set[newIndex] = set[index];
            set[index] = tmp;
        }
    }

    private void shuffleSet(String[] set,int times, int maxSize)
    {
        if(maxSize < 1) return;

        for (int i=0; i < times; i++)
        {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            int firstIndex = random.nextInt(0, maxSize);
            int secondIndex = random.nextInt(0, maxSize);
            swapTiles(set, firstIndex, secondIndex, maxSize);
        }
    }

    private void splitShuffle()
    {
        String[] rightSide = getRightSideSet();
        int rightSideSize = rightSide.length;

        String[] leftSide = getLeftSideSet();

        ThreadLocalRandom random = ThreadLocalRandom.current();

        shuffleSet(leftSide,random.nextInt(this.pointer, 2 * (this.pointer + 1)), this.pointer + 1);
        shuffleSet(rightSide, random.nextInt(rightSideSize, 2* (rightSideSize + 1)), rightSideSize);

        if (this.pointer + 1 >= 0) System.arraycopy(leftSide, 0, this.deck, 0, this.pointer + 1);

        if (this.size - this.pointer - 1 > 0)
            System.arraycopy(rightSide, 0,
                    this.deck, this.pointer + 1, this.size - this.pointer - 1);
    }

    /**
     * Method for draw one of the available tile in the set
     * @return a String of the tile
     */
    public String drawPiece()
    {
        String tile = getTile();
        swapTiles(this.deck, 0, this.pointer, this.size);
        this.pointer--;
        if(!isEmpty()) splitShuffle();

        return tile;
    }

    /**
     * Method for swap a player tile with one of the available. The tile to swap is inserted in the deck and top tile of the available's deck is draw
     * @param tile to swap
     * @return tile from the available list
     */
    public String swapTile(String tile)
    {
        String tileToTake = getTile();
        int indexOfOut = getIndex(tile, getRightSideSet());
        if(indexOfOut < 0)
            return null;

        swapTiles(this.deck, 0 , this.pointer + 1 + indexOfOut, this.size);
        splitShuffle();

        return tileToTake;
    }

    /**
     * Method to insert a tile in the deck
     * @param tile to insert
     */
    public void returnTile(String tile)
    {
        addTile(tile);
        this.pointer++;
        splitShuffle();

    }

    private void shuffle()
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        shuffleSet(this.deck, random.nextInt(this.size - 1,this.size * 2), this.size);
    }

    private void addTile(String tile)
    {
        String[] rightSide = getRightSideSet();
        int indexOfOut = getIndex(tile, rightSide);

        if(indexOfOut < 0)
        {
            System.err.println("Adição de um tile que n foi retirado(tile " + tile + ")");
            System.exit(1);
        }
        if(this.pointer + 1 < this.size)
            swapTiles(this.deck, this.pointer + 1, this.pointer + 1 + indexOfOut, this.size);
    }

    private String getTile()
    {
        if (isEmpty())
        {
            // System.err.println("Deck vazio n pode retirar mais");
            // System.exit(1);
            return null;
        }

        String tile = this.deck[0];

        return tile;
    }

    private int getIndex(String tile, String[] set)
    {
        if(set.length == 0) return -1;
        for (int i = 0; i < set.length ; i++)
            if (tile.equals(set[i]))
                return i;

        return -1;
    }

    /**
     * Check if there is any tiles in the deck
     * @return true if the deck has tiles. false if not
     */
    public boolean isEmpty()
    {
        return this.pointer < 0;
    }

    public String[] getRightSideSet()
    {

        int rightSideSize = this.size - this.pointer - 1;

        if(rightSideSize < 1 || rightSideSize >= this.size)
            return new String[0];

        String[] rightSide = new String[rightSideSize];

        System.arraycopy(this.deck, this.pointer + 1, rightSide, 0, rightSideSize);

        for (int i=0; i < rightSideSize; i++)
            rightSide[i] = this.deck[this.pointer + i + 1];

        return rightSide;

    }

    public String[] getLeftSideSet()
    {
        int leftSideSize = this.pointer + 1;

        if(leftSideSize < 1)
            return new String[0];

        String[] leftSide = new String[leftSideSize];

        System.arraycopy(this.deck, 0, leftSide, 0, leftSideSize);

        return leftSide;
    }

    private void printSet(String[] set)
    {
        System.out.print("[");
        for (String s : set) System.out.print(s + "; ");
        System.out.println("]");
    }

    public void asymCipher(byte[] asymKey)
    {
        for (int i = 0; i < this.deck.length; i++)
        {
            if (this.cipherDepth == 0)
                this.deck[i] = Base64.getEncoder().encodeToString(DominoesCryptoAsym.AsymCipher(this.deck[i], asymKey));
            else
                this.deck[i] = Base64.getEncoder().encodeToString(DominoesCryptoAsym.AsymCipher(
                        Base64.getDecoder().decode(this.deck[i]), asymKey));
        }
        this.cipherDepth++;
        shuffle();
    }

    public void asymDecipher(byte[] asymKey)
    {
        for (int i = 0; i < this.deck.length; i++)
        {
            if (this.cipherDepth == 1)
                this.deck[i] = (String) DominoesCryptoAsym.AsymDecipher(Base64.getDecoder().decode(this.deck[i]),
                        asymKey);
            else
                this.deck[i] = Base64.getEncoder().encodeToString(
                        (byte[]) DominoesCryptoAsym.AsymDecipher(Base64.getDecoder().decode(this.deck[i]), asymKey));
        }
        this.cipherDepth--;
        shuffle();
    }

    public void symCipher(byte[] symKey)
    {
        for (int i = 0; i < this.deck.length; i++)
        {
            if (this.cipherDepth == 0)
                this.deck[i] = Base64.getEncoder().encodeToString(DominoesCryptoSym.SymCipher(this.deck[i], symKey));
            else
                this.deck[i] = Base64.getEncoder().encodeToString(DominoesCryptoSym.SymCipher(
                        Base64.getDecoder().decode(this.deck[i]), symKey));
        }
        this.cipherDepth++;
        shuffle();
    }

    public void symDecipher(byte[] symKey)
    {
        for (int i = 0; i < this.deck.length; i++)
        {
            if (this.cipherDepth == 1)
                this.deck[i] = (String) DominoesCryptoSym.SymDecipher(Base64.getDecoder().decode(this.deck[i]),
                        symKey);
            else
                this.deck[i] = Base64.getEncoder().encodeToString(
                        (byte[]) DominoesCryptoSym.SymDecipher(Base64.getDecoder().decode(this.deck[i]), symKey));
        }
        this.cipherDepth--;
        shuffle();
    }

    // For debuging
    protected void printAvailableSet()
    {
        printSet(getLeftSideSet());
    }

    protected void printNotAvailableSet()
    {
        printSet(getRightSideSet());
    }

}
