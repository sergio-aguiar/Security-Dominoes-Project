package DominoesClient;

import DominoesMisc.DominoesMenus;

import java.util.Arrays;
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
    private final String pseudonym;
    private final DCInterface dcInterface;

    private int tableID;

    public DCThread(String pseudonym, DCInterface dcInterface)
    {
        this.pseudonym = pseudonym;
        this.dcInterface = dcInterface;
        this.tableID = -1;
    }

    @Override
    public void run()
    {
        System.out.println("[CLIENT] Dominoes Client starting...");

        while (true)
        {
            int option1 = clientMainMenu();
            switch (option1)
            {
                case 1:
                    int option2 = clientPlayerCapMenu();
                    boolean exit1 = false;
                    switch (option2)
                    {
                        case 1:
                            System.out.println("\n[CLIENT] Creating a dominoes table...");
                            this.tableID = this.dcInterface.createTable(this.pseudonym, 2);
                            break;
                        case 2:
                            System.out.println("\n[CLIENT] Creating a dominoes table...");
                            this.tableID = this.dcInterface.createTable(this.pseudonym, 4);
                            break;
                        case 3:
                            System.out.println("\n[CLIENT] Creating a dominoes table...");
                            this.tableID = this.dcInterface.createTable(this.pseudonym, 7);
                            break;
                        case 4:
                            exit1 = true;
                            break;
                        default:
                            System.out.println("\n[CLIENT] Unexpected Error...");
                            System.exit(703);
                    }

                    if (!exit1)
                    {
                        boolean exit2 = false;
                        do
                        {
                            int option3 = clientTableMenu(true);
                            switch (option3)
                            {
                                case 1:
                                    System.out.println("\n[CLIENT] Starting Game...");
                                    System.out.println("Start: " + dcInterface.startGame(this.pseudonym, this.tableID));
                                    break;
                                case 2:
                                    System.out.println("\n[CLIENT] Listing Table Information...");
                                    System.out.println(dcInterface.listTableInfo(this.pseudonym, this.tableID).toString());
                                    break;
                                case 3:
                                    System.out.println("\n[CLIENT] Disbanding Table...");
                                    dcInterface.disbandTable(this.pseudonym, this.tableID);
                                    exit2 = true;
                                    break;
                                default:
                                    System.out.println("\n[CLIENT] Unexpected Error...");
                                    System.exit(703);
                            }
                        }
                        while (!exit2);
                    }
                    break;
                case 2:
                    System.out.println("\n[CLIENT] Listing available dominoes tables...");
                    System.out.println(Arrays.toString(this.dcInterface.listAvailableTables()));
                    break;
                case 3:
                case 4:
                    if (option1 == 3)
                    {
                        System.out.println("\n[CLIENT] Joining a specific dominoes table...");
                        if (this.dcInterface.joinTable(this.pseudonym, 0)) tableID = 0;
                    }
                    else
                    {
                        System.out.println("\n[CLIENT] Joining a random dominoes table...");
                        tableID = this.dcInterface.joinRandomTable(this.pseudonym);
                    }

                    boolean exit3 = false;
                    do
                    {
                        int option4 = clientTableMenu(false);
                        switch (option4)
                        {
                            case 1:
                                // TODO: MARK AS READY LOGIC
                                System.out.println("\n[CLIENT] Marking self as ready...");
                                System.out.println(this.dcInterface.markAsReady(this.pseudonym, this.tableID));
                                break;
                            case 2:
                                // TODO: LIST TABLE INFORMATION LOGIC
                                System.out.println("\n[CLIENT] Listing Table Information...");
                                System.out.println(this.dcInterface.listTableInfo(this.pseudonym, this.tableID));
                                break;
                            case 3:
                                // TODO: LEAVE TABLE LOGIC
                                System.out.println("\n[CLIENT] Leaving Table...");
                                System.out.println(this.dcInterface.leaveTable(this.pseudonym, this.tableID));
                                exit3 = true;
                                break;
                            default:
                                System.out.println("\n[CLIENT] Unexpected Error...");
                                System.exit(703);
                        }
                    }
                    while (!exit3);

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

    private int clientPlayerCapMenu()
    {
        while (true)
        {
            DominoesMenus.clientPlayerCapMenu();

            int option = getMenuOption();
            switch (option)
            {
                case 1:
                case 2:
                case 3:
                case 4:
                    return option;
                default:
                    System.out.println("\n[CLIENT] Invalid option.\n[CLIENT] Must be a number within range [1-4].");
            }
        }
    }

    private int clientTableMenu(boolean leader)
    {
        while (true)
        {
            if (leader) DominoesMenus.clientTableLeaderMenu();
            else DominoesMenus.clientTableGuestMenu();

            int option = getMenuOption();
            switch (option)
            {
                case 1:
                case 2:
                case 3:
                    return option;
                default:
                    System.out.println("\n[CLIENT] Invalid option.\n[CLIENT] Must be a number within range [1-3].");
            }
        }
    }

    private static int getMenuOption()
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
