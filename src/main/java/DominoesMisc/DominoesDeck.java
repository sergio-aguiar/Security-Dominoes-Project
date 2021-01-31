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
    private static final int size = 28;

    private static final long serialVersionUID = 1104L;
    private final String[] deck;
    private int lastPiece = size - 1;
    private int cipherDepth = 0;

    public DominoesDeck()
    {
        this.deck = new String[size];
        generatePieces();
    }


    private void generatePieces()
    {
        int count = 0;
        for (int i = 0; i < 7; i++) for (int j=i; j < 7; j++) this.deck[count++] = (i + "|" + j);

        shuffle();
    }

    private void swapPieces(int piece1, int piece2)
    {
        String tmpPiece = this.deck[piece1];
        this.deck[piece1] = this.deck[piece2];
        this.deck[piece2] = tmpPiece;
    }

    public void shuffle()
    {
        for (int i = 0; i < 100; i++)
        {
            if (this.lastPiece > 0)
                swapPieces(ThreadLocalRandom.current().nextInt(0, this.lastPiece + 1),
                        ThreadLocalRandom.current().nextInt(0, this.lastPiece + 1));

            if (this.lastPiece < 27)
                swapPieces(ThreadLocalRandom.current().nextInt(this.lastPiece + 1, size),
                        ThreadLocalRandom.current().nextInt(this.lastPiece + 1, size));
        }
    }

    public String drawPiece()
    {
        String piece = this.deck[0];
        swapPieces(0, this.lastPiece);
        this.lastPiece--;
        shuffle();
        return piece;
    }

    public void returnPiece(String piece)
    {
        for (int i = this.lastPiece + 1; i < size; i++) if (this.deck[i].equals(piece))
        {
            swapPieces(this.lastPiece + 1, i);
            this.lastPiece++;
            shuffle();
        }
    }

    public String swapPiece(String piece)
    {
        returnPiece(piece);
        return drawPiece();
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

    protected void printAvailableSet()
    {
        String[] availableSet = new String[this.lastPiece + 1];
        if (this.lastPiece + 1 >= 0) System.arraycopy(this.deck, 0, availableSet, 0, this.lastPiece + 1);
        printSet(availableSet);
    }

    protected void printWholeSet()
    {
        printSet(this.deck);
    }
}

