package DominoesMisc;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The deck management system.
 * Manages Draw Swap and Insert
 * @author Ricardo Rodrigues Azevedo
 * @version 1.2
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
    
        shuffleSet(leftSide,random.nextInt(this.pointer, 2 * (this.pointer + 1)), this.pointer);
        shuffleSet(rightSide, random.nextInt(rightSideSize, 2* (rightSideSize + 1)), rightSideSize);

        if (this.pointer + 1 >= 0) System.arraycopy(leftSide, 0, this.deck, 0, this.pointer + 1);

        if (rightSideSize - (this.pointer + 1) > 0)
            System.arraycopy(rightSide, this.pointer + 1,
                    this.deck, this.pointer + 1, rightSideSize - (this.pointer + 1));
    }

    /**
     * Method for draw one of the available tile in the set
     * @return a String of the tile
     */
    public String drawPiece()
    {
        if (isEmpty())
         return null;

        String tile = getTile();
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
        int indexOfOut = getIndex(tile, this.deck);
        swapTiles(this.deck, this.pointer, indexOfOut, this.size);
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
        String tile = this.deck[0];

        swapTiles(this.deck, 0, this.pointer, this.size);

        this.pointer--;

        return tile;
    }

    private int getIndex(String tile, String[] set)
    {
        for (int i = 0; i < set.length ; i++)
        {
            if (tile.equals(set[i]))
                return i; 
        } 

        //TODO: Send exception, tile already on set
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

    private void printSet(String[] set)
    {
        System.out.print("[");
        for (String s : set) System.out.print(s + "; ");
        System.out.println("]");
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

