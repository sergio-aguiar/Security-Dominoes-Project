package DominoesMisc;

import java.util.ArrayList;

public class DominoesMenus
{
    public static void clientInitMenu()
    {
        System.out.print("\n[Client] Welcome to the game client!\n"
                + "1- Configure server address\n"
                + "2- Connect to the server\n"
                + "3- Exit\n"
                + "Option: ");
    }

    public static void clientMainMenu()
    {
        System.out.print("\n[Client] Game lobby:\n"
                + "1- Create table\n"
                + "2- List available tables\n"
                + "3- Join table\n"
                + "4- Join random table\n"
                + "5- Exit\n"
                + "Option: ");
    }

    public static void clientPlayerCapMenu()
    {
        System.out.print("\n[Client] Number of Players:\n"
                + "1- 2 players\n"
                + "2- 3 players\n"
                + "3- 4 players\n"
                + "4- Exit\n"
                + "Option: ");
    }

    public static void clientTableLeaderMenu()
    {
        System.out.print("\n[Client] Table Operations:\n"
                + "1- Start game\n"
                + "2- List table information\n"
                + "3- Disband table\n"
                + "Option: ");
    }

    public static void clientTableGuestMenu()
    {
        System.out.print("\n[Client] Table Operations:\n"
                + "1- Mark as ready\n"
                + "2- List table information\n"
                + "3- Leave table\n"
                + "Option: ");
    }

    public static void clientPieceDistributionMenu()
    {
        System.out.print("\n[Client] Piece Distribution Operations:\n"
                + "1- Draw a piece\n"
                + "2- Return a random piece\n"
                + "3- Swap a random piece\n"
                + "4- Skip turn\n"
                + "5- Commit hand\n"
                + "Option: ");
    }

    public static void clientGameMenu()
    {
        System.out.print("\n[Client] Game Operations:\n"
                + "1- Play a piece\n"
                + "2- Draw a piece\n"
                + "3- List game information\n"
                + "4- Denounce cheating\n"
                + "5- Skip Turn\n"
                + "Option: ");
    }

    public static void endPointMenu(String[] endPoints)
    {
        System.out.print("\n[CLIENT] Piece Placement Spot:");

        for (int i = 0; i < endPoints.length; i++) System.out.print("\n" + (i + 1) + "- " + endPoints[i]);
        System.out.print("\nOption: ");
    }

    public static void piecesMenu(String[] pieces)
    {
        System.out.print("\n[CLIENT] Piece Selection:");

        for (int i = 0; i < pieces.length; i++) System.out.print("\n" + (i + 1) + "- " + pieces[i]);
        System.out.print("\nOption: ");
    }

    public static void pieceEndPointMenu(String[] pieceEndPoints)
    {
        System.out.print("\n[CLIENT] End to Match:");

        for (int i = 0; i < pieceEndPoints.length; i++) System.out.print("\n" + (i + 1) + "- " + pieceEndPoints[i]);
        System.out.print("\nOption: ");
    }
}
