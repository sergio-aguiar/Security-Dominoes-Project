package DominoesClient;

import DominoesMisc.DominoesDeck;
import DominoesMisc.DominoesGameState;
import DominoesMisc.DominoesMenus;
import DominoesMisc.DominoesTable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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

        DCStates(String description)
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
                            int tableToJoin;

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
                    System.out.println("\n[CLIENT] Showing score...");
                    break;
                case 6:
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
            while (!this.dcInterface.isPlayerTurn(this.pseudonym, this.tableID)
                    || this.dcInterface.isResetNeeded(this.pseudonym, this.tableID))
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

            if (!this.dcInterface.hasPlayerCommitted(this.pseudonym, this.tableID))
            {
                if (this.dcInterface.isDeckSorting(this.pseudonym, this.tableID))
                {
                    int option = clientPieceDistributionMenu();
                    switch (option)
                    {
                        case 1:
                            System.out.println("\n[CLIENT] Drawing a piece...");

                            if (this.dcInterface.canDraw(this.pseudonym, this.tableID))
                            {
                                DominoesDeck deck1 = this.dcInterface.getDeck(this.pseudonym, this.tableID);

                                String tile1 = deck1.drawPiece();
                                if (tile1 != null) this.gamePieces.add(tile1);
                                else System.out.println("\n[CLIENT] No pieces left to draw.");

                                if (!this.dcInterface.returnDeck(this.pseudonym, this.tableID, deck1, 1))
                                {
                                    System.out.println("\n[CLIENT] Unexpected Error...");
                                    System.exit(703);
                                }

                                System.out.println(this.gamePieces.toString());
                            }
                            else System.out.println("\n[CLIENT] Could not draw a piece. Hand full.");

                            break;
                        case 2:
                            System.out.println("\n[CLIENT] Returning a piece...");
                            if (this.gamePieces.size() > 0)
                            {
                                DominoesDeck deck2 = this.dcInterface.getDeck(this.pseudonym, this.tableID);

                                String tile2 = getTileToReturn();
                                deck2.returnTile(tile2);
                                this.gamePieces.remove(tile2);

                                if (!this.dcInterface.returnDeck(this.pseudonym, this.tableID, deck2, -1))
                                {
                                    System.out.println("\n[CLIENT] Unexpected Error...");
                                    System.exit(703);
                                }

                                System.out.println(this.gamePieces.toString());
                            }
                            else System.out.println("\n[CLIENT] There are no pieces to return.");

                            break;
                        case 3:
                            System.out.println("\n[CLIENT] Swapping a piece...");
                            if (this.gamePieces.size() > 0)
                            {
                                DominoesDeck deck3 = this.dcInterface.getDeck(this.pseudonym, this.tableID);

                                String tile3 = getTileToReturn();
                                this.gamePieces.add(deck3.swapTile(tile3));
                                this.gamePieces.remove(tile3);

                                if (!this.dcInterface.returnDeck(this.pseudonym, this.tableID, deck3, 0))
                                {
                                    System.out.println("\n[CLIENT] Unexpected Error...");
                                    System.exit(703);
                                }

                                System.out.println(this.gamePieces.toString());
                            }
                            else System.out.println("\n[CLIENT] There are no pieces to swap.");

                            break;
                        case 4:
                            System.out.println("\n[CLIENT] Skipping a turn...");
                            this.dcInterface.skipTurn(this.pseudonym, this.tableID);
                            break;
                        case 5:
                            System.out.println("\n[CLIENT] Committing your hand...");
                            // TODO: Finish committing
                            if (!this.dcInterface.commitHand(this.pseudonym, this.tableID, "TMP"))
                                System.out.println("\n[CLIENT You can only commit to full hands.");
                            break;
                        default:
                            System.out.println("\n[CLIENT] Unexpected Error...");
                            System.exit(703);
                    }
                }
                else
                {
                    System.out.println("\n[CLIENT] Unexpected Error...");
                    System.exit(703);
                }
            }
            else
            {
                if (this.dcInterface.isHandlingStart(this.pseudonym, this.tableID))
                {
                    this.dcInterface.stateHighestDouble(this.pseudonym, this.tableID, getHighestDouble());

                    while (!this.dcInterface.hasDoubleCheckingEnded(this.pseudonym, this.tableID))
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
                            System.exit(704);
                        }
                        finally
                        {
                            this.reentrantLock.unlock();
                        }
                    }

                    if (this.dcInterface.isRedistributionNeeded(this.pseudonym, this.tableID)) resetDistribution();
                }
                else
                {
                    DominoesGameState gameState = this.dcInterface.getGameState(this.pseudonym, this.tableID);

                    System.out.println(gameState.toString());
                    if (gameState.getPlayedPieces().isEmpty())
                    {
                        String highestDouble = getHighestDouble();
                        if (this.dcInterface.playPiece(this.pseudonym, this.tableID, "First",
                                highestDouble, highestDouble.split("//|")[0]))
                            this.gamePieces.remove(highestDouble);
                        else System.out.println("\n[CLIENT] Error playing the piece.");
                    }
                    else
                    {
                        if (gameState.getWinner() == -1)
                        {
                            int option = clientGameMenu();
                            switch (option)
                            {
                                case 1:
                                    System.out.println("\n[CLIENT] Playing a piece...");

                                    String[] tmpEndPoints = gameState.getEndPoints().toArray(new String[0]);
                                    int endPoint = endPointMenu(tmpEndPoints);
                                    int piece = piecesMenu(gamePieces.toArray(new String[0])) - 1;

                                    String[] pieceEndPoints = this.gamePieces.get(piece).split("\\|");
                                    int pieceEndPoint = pieceEndPointsMenu(pieceEndPoints);

                                    if (this.dcInterface.playPiece(this.pseudonym, this.tableID, tmpEndPoints[endPoint],
                                            this.gamePieces.get(piece),  pieceEndPoints[pieceEndPoint - 1]))
                                        this.gamePieces.remove(piece);
                                    else System.out.println("\n[CLIENT] Error playing the piece.");

                                    System.out.println(this.gamePieces.toString());

                                    break;
                                case 2:
                                    System.out.println("\n[CLIENT] Drawing a piece...");

                                    String drawnPiece = this.dcInterface.drawCard(this.pseudonym, this.tableID);
                                    if (!drawnPiece.equals("Error")) this.gamePieces.add(drawnPiece);
                                    else System.out.println("\n[CLIENT] Failed to draw a piece. None remaining.");

                                    System.out.println(this.gamePieces.toString());

                                    break;
                                case 3:
                                    System.out.println("\n[CLIENT] Listing game information...");
                                    System.out.println(gameState.toString());
                                    break;
                                case 4:
                                    System.out.println("\n[CLIENT] Denouncing cheating...");
                                    break;
                                case 5:
                                    System.out.println("\n[CLIENT] Skipping turn...");
                                    this.dcInterface.skipTurn(this.pseudonym, this.tableID);
                                    break;
                                default:
                                    System.out.println("\n[CLIENT] Unexpected Error...");
                                    System.exit(703);
                            }
                        }
                    }
                }
            }
        }
    }

    private String getTileToReturn()
    {
        return this.gamePieces.get(ThreadLocalRandom.current().nextInt(0,this.gamePieces.size()));
    }

    private String getHighestDouble()
    {
        for (String piece : new String[]{"6|6", "5|5", "4|4", "3|3", "2|2", "1|1", "0|0"})
            if (this.gamePieces.contains(piece)) return piece;
        return "None";
    }

    private void resetDistribution()
    {
        this.gamePieces.clear();
    }

    private int clientMainMenu()
    {
        while (true)
        {
            DominoesMenus.clientMainMenu();

            Integer option = sextupleCaseMenuSwitch();
            if (option != null) return option;
        }
    }

    private int clientPlayerCapMenu()
    {
        while (true)
        {
            DominoesMenus.clientPlayerCapMenu();

            Integer option = quadrupleCaseMenuSwitch();
            if (option != null) return option;
        }
    }

    private int clientTableMenu(boolean leader)
    {
        while (true)
        {
            if (leader) DominoesMenus.clientTableLeaderMenu();
            else DominoesMenus.clientTableGuestMenu();

            Integer option = tripleCaseMenuSwitch();
            if (option != null) return option;
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

    private int clientGameMenu()
    {
        while (true)
        {
            DominoesMenus.clientGameMenu();

            Integer option = quintupleCaseMenuSwitch();
            if (option != null) return option;
        }
    }

    private int endPointMenu(String[] endPoints)
    {
        while (true)
        {
            DominoesMenus.endPointMenu(endPoints);

            int option = getMenuOption();
            if (option >= 1 && option <= endPoints.length) return option;
            else System.out.println("\n[CLIENT] Invalid option.\n" +
                    "[CLIENT] Must be a number within range [1-" + endPoints.length + "].");
        }
    }

    private int piecesMenu(String[] pieces)
    {
        while (true)
        {
            DominoesMenus.piecesMenu(pieces);

            int option = getMenuOption();
            if (option >= 1 && option <= pieces.length) return option;
            else System.out.println("\n[CLIENT] Invalid option.\n" +
                    "[CLIENT] Must be a number within range [1-" + pieces.length + "].");
        }
    }

    private int pieceEndPointsMenu(String[] pieceEndPoints)
    {
        while (true)
        {
            DominoesMenus.pieceEndPointMenu(pieceEndPoints);

            int option = getMenuOption();
            if (option == 1 || option == 2) return option;
            else System.out.println("\n[CLIENT] Invalid option.\n[CLIENT] Must be a number within range [1-2].");
        }
    }

    private Integer sextupleCaseMenuSwitch()
    {
        int option = getMenuOption();
        switch (option)
        {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return option;
            default:
                System.out.println("\n[CLIENT] Invalid option.\n[CLIENT] Must be a number within range [1-6].");
        }
        return null;
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

    private Integer quadrupleCaseMenuSwitch()
    {
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
        return null;
    }

    private Integer tripleCaseMenuSwitch()
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
        return null;
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
