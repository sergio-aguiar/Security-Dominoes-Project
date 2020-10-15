package DominoesClient;

import DominoesMisc.DominoesMenus;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DCThread extends Thread
{
    public enum DCStates
    {
        AT_MAIN_MENUS("AMM"),
        AT_TABLE_LEADER_MENU("ATLM"),
        AT_TABLE_GUEST_MENU("ATGM"),
        AWAITING_GAME_START("AGS"),
        AT_PIECE_DISTRIBUTION("APD");

        private final String description;

        private DCStates(String description)
        {
            this.description = description;
        }

        @Override
        public String toString() {
            return this.description;
        }
    }

    private static final Scanner sc = new Scanner(System.in);
    private static int gID = 0;

    private final int id;
    private final String pseudonym;
    private final DCInterface dcInterface;

    public DCThread(String pseudonym, DCInterface dcInterface)
    {
        this.id = gID++;
        this.pseudonym = pseudonym;
        this.dcInterface = dcInterface;
    }

    @Override
    public void run()
    {
        System.out.println("[CLIENT] Dominoes Client starting...");

        while (true)
        {
            int option = clientMainMenu();

            switch (option)
            {
                case 1:
                    System.out.println("\n[CLIENT] Creating a dominoes table...");
                    System.out.println(this.dcInterface.createTable(this.pseudonym, 4));
                    // TODO: Add Table Leader Menu
                    break;
                case 2:
                    System.out.println("\n[CLIENT] Listing available dominoes tables...");
                    System.out.println(this.dcInterface.listAvailableTables(this.pseudonym));
                    // TODO: Add Table Listing
                    break;
                case 3:
                case 4:
                    if (option == 3)
                    {
                        System.out.println("\n[CLIENT] Joining a specific dominoes table...");
                        // TODO: Add Table Choosing
                        System.out.println(this.dcInterface.joinTable(this.pseudonym, 0));
                    }
                    else
                    {
                        System.out.println("\n[CLIENT] Joining a random dominoes table...");
                        System.out.println(this.dcInterface.joinRandomTable(this.pseudonym));
                    }
                    // TODO: Add Table Guest Menu
                    break;
                case 5:
                    System.out.println("\n[CLIENT] Shutting down...");
                    System.exit(702);
                default:
                    System.out.println("\n[CLIENT] Unexpected Error...");
                    System.exit(703);
            }
        }
    }

    private int clientMainMenu()
    {
        while (true)
        {
            DominoesMenus.clientMainMenu();

            int option = getMenuOption();
            switch (option)
            {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    return option;
                default:
                    System.out.println("\n[CLIENT] Invalid option.\n[CLIENT] Must be a number within range [1-5].");
            }
        }
    }

    private int getMenuOption()
    {
        int option;
        try
        {
            option = sc.nextInt();
        }
        catch (InputMismatchException e)
        {
            sc.next();
            option = -1;
        }
        return option;
    }
}
