package DominoesMisc;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The deck management system.
 * Manages Draw Swap and Insert
 * @author Ricardo Rodrigues Azevedo
 * @version 1.0
 * @since 2019-10-17
 */

public class DominoesDeck implements Serializable
{
    private static final long serialVersionUID = 1104L;
    private final String[] deck;
    // private String[] outSet;
    private final int size = 28;
    private int pointer = this.size - 1;

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

        ThreadLocalRandom r = ThreadLocalRandom.current();
        shuffleSet(this.deck, r.nextInt(this.size - 1,this.size * 2), this.size);
    }

    private void shuffleCard(String[] set, int index, int newIndex, int maxSize)
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
        for (int i=0; i < times; i++)
        {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            int firstIndex = random.nextInt(0, maxSize);
            int secondIndex = random.nextInt(0, maxSize);
            shuffleCard(set, firstIndex, secondIndex, maxSize);
        }
    }

    private void splitShuffle()
    {
        String[] rightSide = getRightSideSet();
        int rightSideSize = rightSide.length;

        String[] leftSide = getLeftSideSet();

        ThreadLocalRandom random = ThreadLocalRandom.current();
    
        shuffleSet(leftSide,random.nextInt(this.pointer, 2 * (this.pointer + 1)), this.pointer);
        shuffleSet(rightSide, random.nextInt(rightSideSize, 2* (rightSideSize + 1)), rightSideSize);

        if (this.pointer + 1 >= 0) System.arraycopy(leftSide, 0, this.deck, 0, this.pointer + 1);

        if (rightSideSize - (this.pointer + 1) >= 0)
            System.arraycopy(rightSide, this.pointer + 1,
                    this.deck, this.pointer + 1, rightSideSize - (this.pointer + 1));
    }

    /**
     * Method for draw one of the available tile in the set
     * @return a String of the tile
     */
    public String drawPiece()
    {
        if (isEmpty()) return null;

        String tile = getTile();
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
        int indexOfOut = getOutTileIndex(tile);
        shuffleCard(this.deck, this.pointer, indexOfOut, this.size);
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
        Shuffle();
    }

    private void Shuffle()
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        shuffleSet(this.deck, random.nextInt(this.size - 1,this.size * 2), this.size);
    }

    private int getOutTileIndex(String tile)
    {
        int indexOfOut = getIndex(tile, this.deck);
        if (indexOfOut < this.pointer) return -1;       //TODO: Solução para caso de erro

        return indexOfOut;
    }

    private void addTile(String tile)
    {
        int indexOfOut = getIndex(tile, getRightSideSet());
        if(indexOfOut < 0) return;
        if(this.pointer + 1 >= this.size) shuffleCard(this.deck, this.pointer + 1, indexOfOut, this.size);

        this.pointer++;
    }

    private String getTile()
    {
        return this.deck[this.pointer];
    }

    private int getIndex(String tile, String[] set)
    {
        for (int i = 0; i < set.length ; i++) if (tile.equals(set[i])) return i;

        //TODO: Send exception, tile already on set
        return -1;
    }

    private String[] getRightSideSet()
    {
        int rightSideSize = this.size - this.pointer - 1;

        if(rightSideSize < 1)
            return new String[0];

        String[] rightSide = new String[rightSideSize];

        for (int i=0; i < rightSideSize; i++)
            rightSide[i] = this.deck[this.pointer + 1 + i];

        return rightSide;
    }

    private String[] getLeftSideSet()
    {
        String[] leftSide = new String[this.pointer + 1];
        if (this.pointer + 1 >= 0) System.arraycopy(this.deck, 0, leftSide, 0, this.pointer + 1);

        return leftSide;
    }

    /**
     * Check if there is any tiles in the deck
     * @return true if the deck has tiles. false if not
     */
    public boolean isEmpty()
    {
        return this.pointer < 0;
    }

    private void printSet(String[] set)
    {
        System.out.print("[");
        for (String s : set) System.out.print(s + ";");
        System.out.println("]");
    }

    protected void printOutSet()
    {
        printSet(getRightSideSet());
    }

    protected void printAvailableSet()
    {
        printSet(this.deck);
    }


}

