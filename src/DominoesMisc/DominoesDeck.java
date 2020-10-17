package DominoesMisc;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The deck management system.
 * Manages Draw Swap and Insert
 * @author Ricardo Rodrigues Azevedo
 * @version 1.0
 * @since 2019-10-17
 */

public class DominoesDeck {

    private String[] deck;

    // private String[] outSet;

    private int size = 28;

    private int pointer = size-1;

    public DominoesDeck(){
        this.deck = new String[size];
        GenerateSet(this.size);
    }


    /**
     * Method for initialize the deck. Also shuffle it
     * @param size Size for the deck
     */
    private void GenerateSet(int size){

        int count = 0;

        for (int i = 0; i < 7; i++)
            for (int j=i; j < 7; j++)
                this.deck[count++]=(i + "|" + j);

        ThreadLocalRandom r = ThreadLocalRandom.current();
        ShuffleSet(this.deck, r.nextInt(this.size - 1,this.size * 2), this.size);

    }

    private void ShuffleCard(String[] set,int index, int newIndex,int maxSize){
        if(index < 0 || index > maxSize)
            return;
        
        if(newIndex < 0 || newIndex > maxSize)
            return;

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

    private void ShuffleSet(String[] set,int times, int maxSize)
    {
        for (int i=0; i < times; i++)
        {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            int firstIndex = random.nextInt(0,maxSize);
            int secondInfex = random.nextInt(0,maxSize);
            ShuffleCard(set,firstIndex,secondInfex,maxSize);
        }
    }

    private void SplitShufle(){

        String[] rigthSide = getRigthSideSet();
        int rigthSideSize = rigthSide.length;

        String[] leftSide = getLeftSideSet();

        ThreadLocalRandom random = ThreadLocalRandom.current();
    
        ShuffleSet(leftSide,random.nextInt(this.pointer, 2 * (this.pointer + 1)), this.pointer);
        ShuffleSet(rigthSide, random.nextInt(rigthSideSize, 2* (rigthSideSize + 1)),rigthSideSize);

        for (int i=0; i < this.pointer + 1; i++)
            this.deck[i] = leftSide[i];

        for (int i=0; i < rigthSideSize; i++)
            this.deck[i + this.pointer + 1] = rigthSide[i];
        
    }

    /**
     * Method for draw one of the available tile in the set
     * @return a String of the tile
     */
    public String TakeTile()
    {
        if (isEmpty())
            return null;

        String tile_to_take = getTile();

        this.pointer--;

        if(!isEmpty()) SplitShufle();
        
        return tile_to_take;
    }

    /**
     * Method for swap a player tile with one of the available. The tile to swap is inserted in the deck and top tile of the available's deck is draw
     * @param tile to swap
     * @return tile from the available's list
     */
    public String SwapTile(String tile){

        String tileToTake = getTile();

        int indexOfOut = getOutTileIndex(tile);

        ShuffleCard(this.deck, this.pointer, indexOfOut, this.size);

        SplitShufle();

        return tileToTake;

    }

    /**
     * Method to insert a tile in the deck
     * @param tile to insert
     */
    public void InsertTile(String tile){

        addTile(tile);

        Shuffle();

    }

    private void Shuffle(){

        ThreadLocalRandom random = ThreadLocalRandom.current();

        ShuffleSet(this.deck, random.nextInt(this.size - 1,this.size * 2), this.size);

    }

    private int getOutTileIndex(String tile){

        int indexOfOut = getIndex(tile, this.deck);

        if (indexOfOut < this.pointer)
            return -1; //TODO: Solução para caso de erro

        return indexOfOut;

    }

    private void addTile(String tile){

        int indexOfOut = getIndex(tile, getRigthSideSet());

        if(indexOfOut < 0) return;

        if(this.pointer + 1 >= this.size)
            ShuffleCard(this.deck, this.pointer + 1, indexOfOut, this.size);

        this.pointer++;
    }

    private String getTile(){

        return this.deck[this.pointer];

    }

    private int getIndex(String tile, String[] set){

        for (int i = 0; i < set.length ; i++){
            if (tile.equals(set[i]))
                return i;
        }

        //TODO: Send exeption, tile already on set
        return -1;

    }

    private String[] getRigthSideSet(){
        int rigthSideSize = this.size - this.pointer - 1;

        if(rigthSideSize < 1)
            return new String[0];

        String[] rigthSide = new String[rigthSideSize];

        for (int i=0; i < rigthSideSize; i++)
            rigthSide[i] = this.deck[this.pointer + 1 + i];    

        return rigthSide;
    }

    private String[] getLeftSideSet(){
        
        String[] leftSide = new String[this.pointer + 1];

        for (int i=0; i<this.pointer + 1; i++)
            leftSide[i] = this.deck[i];

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

    private void PrintSet(String[] set)
    {
        System.out.print("[");
        for (int i=0; i < set.length; i++){
            System.out.print(set[i] + ";");
        }

        System.out.println("]");
    }

    protected void PrintOutSet(){

        PrintSet(getRigthSideSet());

    }

    protected void PrintAvailableSet(){
        PrintSet(this.deck);
    }


}

