package DominoesSecurity;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import DominoesMisc.DominoesDeck;

public class DominoesSecurityTeste {

    public static void main(String[] args) throws IOException 
    {
        TestSym();
        TestAsym();
    }

    private static void TestAsym() 
    {

        Map<String,byte[]> keys = DominoesCryptoAsym.GenerateAsymKeys();

        System.out.println("Generating Object");

        DominoesDeck deck = new DominoesDeck();

        String piece = deck.drawPiece();
        String piece2 = deck.drawPiece();
        String piece3 = deck.drawPiece();
        deck.returnPiece(piece);
        String piece4 = deck.swapPiece(piece2);
        String piece5 = deck.drawPiece();
        deck.returnPiece(piece3);
        String piece6 = deck.drawPiece();


        System.out.println(deck);

        byte[] encriptedMsg = DominoesCryptoAsym.AsymCipher(deck, keys.get("public"));

        System.out.println("Mensagem encriptada: " + encriptedMsg.toString());

        Object decriptedMsg = DominoesCryptoAsym.AsymDecipher(encriptedMsg, keys.get("private"));

        System.out.println("Mensagem desicriptada: " + decriptedMsg);    
        
        boolean teste = deck.equals(decriptedMsg);

        System.out.println("Resultados são iguais? " + teste);    

    }

    private static void TestSym() 
    {
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Pwd:");

        String pwd = scan.nextLine();

        byte[] key = DominoesCryptoSym.GenerateSymKeys(pwd);

        // System.out.println("Message:");

        // String msg = scan.nextLine();

        System.out.println("Generating Object");

        DominoesDeck deck = new DominoesDeck();

        String piece = deck.drawPiece();
        String piece2 = deck.drawPiece();
        String piece3 = deck.drawPiece();
        deck.returnPiece(piece);
        String piece4 = deck.swapPiece(piece2);
        String piece5 = deck.drawPiece();
        deck.returnPiece(piece3);
        String piece6 = deck.drawPiece();


        System.out.println(deck);

        scan.close();

        byte[] encriptedMsg = DominoesCryptoSym.SymCipher(deck, key);

        System.out.println("Mensagem encriptada: " + encriptedMsg.toString());

        DominoesDeck decriptedMsg = (DominoesDeck) DominoesCryptoSym.SymDecipher(encriptedMsg, key);

        System.out.println("Mensagem desicriptada: " + decriptedMsg);    
        
        boolean teste = deck.equals(decriptedMsg);

        System.out.println("Resultados são iguais? " + teste);    
    }
}
