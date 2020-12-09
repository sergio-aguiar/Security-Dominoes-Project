package DominoesAI;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import DominoesClient.DCInterface;
import DominoesClient.DCThread;

public class DAIThread extends DCThread {

    private final DCInterface dcInterface;
    private final ArrayList<String> gamePieces;
    private final String pseudonym;

    private int tableID;
    private ReentrantLock reentrantLock;
    private Condition turnCondition;

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
                    while(!this.dcInterface.isPlayerTurn(this.pseudonym, this.tableID))
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
                            if(this.gamePieces.size() < 7)
                            {
                                System.out.println("[AICLIENT] drawing ...");
                                String piece = this.dcInterface.drawPiece(this.pseudonym, this.tableID);
                                this.gamePieces.add(piece);
                                System.out.println(this.gamePieces.toString());
                            }
                            else
                            {
                                System.out.println("[AICLIENT] commiting hand ...");
                                if(this.dcInterface.commitHand(this.pseudonym, this.tableID, "TMP"))
                                {
                                    System.out.println("[AICLIENT] hand commited");
                                }                                
                                else   
                                    System.out.println("[AICLIENT] couldn't commit hand");
                            }
                        }                        
                    }

                    if(this.dcInterface.hasPlayerCommitted(this.pseudonym, this.tableID))
                    {
                        this.dcInterface.skipTurn(this.pseudonym, this.tableID);
                        System.out.println("[AICLIENT] skip turn");
                    }
                }

            }
        }
    }

    private static String generatePseudonym()
    {
        return "AITHREAD- " + Thread.currentThread().getId();
    }
}
