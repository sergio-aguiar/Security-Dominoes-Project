package DominoesClient;

import DominoesMisc.DominoesMenus;
import DominoesMisc.DominoesTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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

    private final ReentrantLock reentrantLock;
    private final Condition turnCondition;

    private final String pseudonym;
    private final DCInterface dcInterface;
    private final ArrayList<String> gamePieces;

    private int tableID;

    public DCThread(String pseudonym, DCInterface dcInterface)
    {
        this.reentrantLock = new ReentrantLock(true);
        this.turnCondition = this.reentrantLock.newCondition();
        this.pseudonym = pseudonym;
        this.dcInterface = dcInterface;
        this.gamePieces = new ArrayList<>();
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
                            this.tableID = this.dcInterface.createTable(this.pseudonym, 3);
                            break;
                        case 3:
                            System.out.println("\n[CLIENT] Creating a dominoes table...");
                            this.tableID = this.dcInterface.createTable(this.pseudonym, 4);
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
                                    if (this.dcInterface.startGame(this.pseudonym, this.tableID))
                                    {
                                        System.out.println("\n[CLIENT] Starting Game...");
                                        // TODO: @Fabio, add game logic here
                                        gameLogic();
                                    }
                                    else
                                    {
                                        System.out.println("\n[CLIENT] Could not start the game.\n" +
                                                "[CLIENT] Some players have yet to ready up.");
                                    }
                                    break;
                                case 2:
                                    System.out.println("\n[CLIENT] Fetching Table Information...");
                                    System.out.print(
                                            this.dcInterface.listTableInfo(this.pseudonym, this.tableID).toString());
                                    break;
                                case 3:
                                    System.out.println("\n[CLIENT] Disbanding Table...");
                                    this.dcInterface.disbandTable(this.pseudonym, this.tableID);
                                    this.tableID = -1;
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
                    System.out.println("\n[CLIENT] Fetching available dominoes tables...");

                    DominoesTable[] tmpTables = this.dcInterface.listAvailableTables();
                    if (tmpTables.length == 0) System.out.println("[CLIENT] No available tables found.");
                    else for (DominoesTable table : tmpTables) System.out.print("\n" + table.toString());

                    break;
                case 3:
                case 4:
                    boolean noTables = false;
                    while (this.tableID < 0)
                    {
                        if (option1 == 3)
                        {
                            int tableToJoin = -1;

                            do
                            {
                                System.out.print("\n[CLIENT] Table ID: ");
                                tableToJoin = getMenuOption();

                                if (tableToJoin < 0) System.out.println("\n[CLIENT] Invalid table ID.");
                            }
                            while (tableToJoin < 0);

                            if (this.dcInterface.joinTable(this.pseudonym, tableToJoin))
                            {
                                System.out.println("\n[CLIENT] Joining dominoes table #" + tableToJoin + "...");
                                this.tableID = tableToJoin;
                            }
                            else
                            {
                                System.out.println("\n[CLIENT] Could not join a table with the specified ID.");
                                noTables = true;
                                break;
                            }
                        }
                        else
                        {
                            System.out.println("\n[CLIENT] Joining a random dominoes table...");
                            this.tableID = this.dcInterface.joinRandomTable(this.pseudonym);

                            if (this.tableID == -1)
                            {
                                System.out.println("[CLIENT] No available tables to join.");
                                noTables = true;
                                break;
                            }
                        }
                    }

                    if (noTables) break;

                    boolean exit3 = false;
                    do
                    {
                        int option4 = clientTableMenu(false);
                        switch (option4)
                        {
                            case 1:
                                System.out.print("\n[CLIENT] Marking self as ready...");

                                if (this.dcInterface.markAsReady(this.pseudonym, this.tableID))
                                {
                                    // TODO: @Fabio, add game logic here
                                    System.out.println("\n[CLIENT] Awaiting game Start...");
                                    gameLogic();
                                }
                                else
                                {
                                    System.out.println("\n[CLIENT] The table was disbanded.");
                                    exit3 = true;
                                }
                                break;
                            case 2:
                                System.out.println("\n[CLIENT] Listing Table Information...");
                                DominoesTable tmpTable = this.dcInterface.listTableInfo(this.pseudonym, this.tableID);

                                if (tmpTable != null) System.out.print(tmpTable.toString());
                                else
                                {
                                    System.out.println("[CLIENT] The table was disbanded.");
                                    exit3 = true;
                                }
                                break;
                            case 3:
                                System.out.println("\n[CLIENT] Leaving Table...");
                                this.dcInterface.leaveTable(this.pseudonym, this.tableID);
                                exit3 = true;
                                break;
                            default:
                                System.out.println("\n[CLIENT] Unexpected Error...");
                                System.exit(703);
                        }
                    }
                    while (!exit3);

                    this.tableID = -1;
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

    private void gameLogic()
    {
        while (!this.dcInterface.hasGameEnded(this.pseudonym, this.tableID))
        {
            while (!this.dcInterface.isPlayerTurn(this.pseudonym, this.tableID))
            {
                this.reentrantLock.lock();
                try
                {
                    synchronized (this)
                    {
                        this.turnCondition.awaitNanos(100000);
                    }
                }
                catch (Exception e)
                {
                    System.out.println("DCThread: gameLogic: " + e.toString());
                    System.exit(703);
                }
                finally
                {
                    this.reentrantLock.unlock();
                }
            }

            if (this.dcInterface.isDeckSorting(this.pseudonym, this.tableID))
            {
                int option = clientPieceDistributionMenu();
                switch (option)
                {
                    case 1:
                        System.out.println("\n[CLIENT] Drawing a piece...");
                        this.gamePieces.add(this.dcInterface.drawPiece(this.pseudonym, this.tableID));
                        System.out.println(this.gamePieces.toString());
                        break;
                    case 2:
                        System.out.println("\n[CLIENT] Returning a piece...");
                        // TODO: Finish returning
                        System.out.println(this.gamePieces.toString());
                        break;
                    case 3:
                        System.out.println("\n[CLIENT] Swapping a piece...");
                        // TODO: Finish swapping
                        System.out.println(this.gamePieces.toString());
                        break;
                    case 4:
                        System.out.println("\n[CLIENT] Skipping a turn...");
                        // TODO: Finish skipping
                        break;
                    case 5:
                        System.out.println("\n[CLIENT] Committing your hand...");
                        // TODO: Finish committing
                        break;
                    default:
                        System.out.println("\n[CLIENT] Unexpected Error...");
                        System.exit(703);
                }
            }
            else
            {

            }
        }

        // while game not ended
        //      if not my turn
        //          wait
        //      if my turn
        //          if deck sorting
        //              show deck sort menu
        //          if game
        //              show game menu
        // show game ended menu
    }

    private int clientMainMenu()
    {
        while (true)
        {
            DominoesMenus.clientMainMenu();

            Integer option = quintupleCaseMenuSwitch();
            if (option != null) return option;
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

            int option = tripleCaseMenuSwitch();
            if (option != -1) return option;
        }
    }

    private int clientPieceDistributionMenu()
    {
        while (true)
        {
            DominoesMenus.clientPieceDistributionMenu();

            Integer option = quintupleCaseMenuSwitch();
            if (option != null) return option;
        }
    }

    private Integer quintupleCaseMenuSwitch()
    {
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
        return null;
    }

    private int tripleCaseMenuSwitch()
    {
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
        return -1;
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
