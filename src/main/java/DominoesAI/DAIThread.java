/*
package DominoesAI;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import DominoesClient.DCInterface;
import DominoesClient.DCThread;
import DominoesMisc.DominoesDeck;

public class DAIThread extends DCThread {

    private final DCInterface dcInterface;
    private final ArrayList<String> gamePieces;
    private final String pseudonym;

    private int tableID;
    private ReentrantLock reentrantLock;
    private Condition turnCondition;
    private boolean gameStarted = false;

    public DAIThread(DCInterface dcInterface) {
        super(generatePseudonym(), dcInterface);
        this.dcInterface = dcInterface;
        this.gamePieces = new ArrayList<>();
        this.tableID = -1;
        this.pseudonym = generatePseudonym();
        this.reentrantLock = new ReentrantLock(true);
        this.turnCondition = this.reentrantLock.newCondition();
    }

    @Override
    public void run() {
        boolean onTable = false;
        boolean ready = false;
        System.out.println(this.pseudonym);

        // TODO: AGENT LOGIC
        System.out.println("[AICLIENT] Dominoes Ai Client starting...");

        while (true) {
            while (!onTable) {
                System.out.println("\n[AICLIENT] Joining a random dominoes table...");
                do {
                    this.tableID = this.dcInterface.joinRandomTable(this.pseudonym);
                    
                    if(this.tableID != -1) break;
                    System.out.println("[AICLIENT] No available tables to join.");
                    this.reentrantLock.lock();
                    try
                    {
                        synchronized (this)
                        {
                            this.turnCondition.await(1, TimeUnit.SECONDS);
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("[AICLIENT] while waiting for a table, [ERROR]: " + e.toString());
                        System.exit(703);
                    }
                    finally
                    {
                        this.reentrantLock.unlock();
                    }
                }
                while (this.tableID == -1);     

                System.out.println("\n[AICLIENT] AICLIENT join table with id:" + this.tableID);
                onTable = true;    
            }
            if(!ready)
            {
                if(this.dcInterface.markAsReady(this.pseudonym, this.tableID))
                {
                    System.out.println("[AICLIENT] AICLIENT ready");
                    ready = true;
                }
                else
                    System.out.println("[AICLIENT] AICLIENT not ready");
            }
            else
            {
                while(!this.dcInterface.hasGameEnded(this.pseudonym, this.tableID))
                {
                    while(!this.dcInterface.isPlayerTurn(this.pseudonym, this.tableID)
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
                            System.out.println("[AICLIENT] while wainting for turn. [ERROR]: " + e.toString());
                            System.exit(703);
                        }
                        finally
                        {
                            this.reentrantLock.unlock();
                        }
                    }

                    if(!this.dcInterface.hasPlayerCommitted(this.pseudonym, this.tableID))
                    {
                        if(this.dcInterface.isDeckSorting(this.pseudonym, this.tableID))
                        {
                           
                            if(this.dcInterface.canDraw(this.pseudonym, this.tableID))
                            {
                                System.out.println("[AICLIENT] drawing ...");
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
                            else
                            {
                                System.out.println("[AICLIENT] commiting hand ...");

                                // TODO: FIX BIT COMMIT

                                //if(this.dcInterface.commitHand(this.pseudonym, this.tableID, 0))
                                //{
                                //    System.out.println("[AICLIENT] hand commited");
                                //}
                                //else
                                //    System.out.println("[AICLIENT] couldn't commit hand");
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
                            //gameLogic
                            this.dcInterface.skipTurn(this.pseudonym, this.tableID);
                            //System.out.println("[AICLIENT] skip turn");   
                        }
                    }                     
                }

            }
        }
    }

    private static String generatePseudonym()
    {
        return "AITHREAD - " + System.currentTimeMillis();
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
}
*/
