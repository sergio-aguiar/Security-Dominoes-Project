package DominoesClient;

import DominoesCommunication.DCCommunication;
import DominoesCommunication.DMessage;
import DominoesCommunication.DMessageException;
import DominoesMisc.*;

import java.security.Key;
import java.util.Stack;

public class DCStub implements DCInterface
{
    private final String serverHostName;
    private final int serverHostPort;

    public DCStub(String serverHostName, int serverHostPort)
    {
        this.serverHostName = serverHostName;
        this.serverHostPort = serverHostPort;
    }

    @Override
    public int createTable(String pseudonym, byte[] cipheredSessionID, int playerCap)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: createTable: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.CREATE_TABLE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, playerCap);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: createTable: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.CREATE_TABLE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: createTable: incorrect " +
                    "reply message!");

            System.exit(704);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: createTable: no return " +
                    "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (int) inMessage.getReturnInfo();
    }

    @Override
    public DominoesTableInfo[] listAvailableTables(String pseudonym, byte[] cipheredSessionID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: listAvailableTables: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.LIST_TABLES_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: listAvailableTables: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.LIST_TABLES_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: listAvailableTables: " +
                    "incorrect reply message!");

            System.exit(705);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: listAvailableTables: no " +
                    "return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (DominoesTableInfo[]) inMessage.getReturnInfo();
    }

    @Override
    public boolean joinTable(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: joinTable: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.JOIN_TABLE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: joinTable: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.JOIN_TABLE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: joinTable: incorrect " +
                    "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: joinTable: no return " +
                    "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public int joinRandomTable(String pseudonym, byte[] cipheredSessionID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: joinRandomTable: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.JOIN_RANDOM_TABLE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: joinRandomTable: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.JOIN_RANDOM_TABLE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: joinRandomTable: incorrect " +
                    "reply message!");

            System.exit(707);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: joinRandomTable: no return " +
                    "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (int) inMessage.getReturnInfo();
    }

    @Override
    public boolean startGame(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: startGame: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.START_GAME_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: startGame: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.START_GAME_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: startGame: incorrect " +
                    "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: startGame: no return " +
                    "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public void disbandTable(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: disbandTable: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.DISBAND_TABLE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: disbandTable: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.DISBAND_TABLE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: disbandTable: incorrect " +
                    "reply message!");

            System.exit(706);
        }
    }

    @Override
    public boolean markAsReady(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: markAsReady: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.MARK_AS_READY_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: markAsReady: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.MARK_AS_READY_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: markAsReady: incorrect " +
                    "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: markAsReady: no return " +
                    "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public DominoesTableInfo listTableInfo(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: listTableInfo: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.LIST_TABLE_INFO_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: listTableInfo: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.LIST_TABLE_INFO_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: listTableInfo: incorrect " +
                    "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            return null;
        }
        dcCommunication.close();

        return (DominoesTableInfo) inMessage.getReturnInfo();
    }

    @Override
    public void leaveTable(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: leaveTable: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.LEAVE_TABLE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: leaveTable: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.LEAVE_TABLE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: leaveTable: incorrect " +
                    "reply message!");

            System.exit(706);
        }
    }

    @Override
    public boolean isPlayerTurn(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isPlayerTurn: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.TURN_CHECK_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isPlayerTurn: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.TURN_CHECK_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isPlayerTurn: incorrect " +
                    "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isPlayerTurn: no return " +
                    "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean hasGameEnded(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasGameEnded: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.GAME_STATE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasGameEnded: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.GAME_STATE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasGameEnded: incorrect " +
                    "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasGameEnded: no return " +
                    "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean isDeckSorting(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isDeckSorting: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.DISTRIBUTION_STATE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isDeckSorting: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.DISTRIBUTION_STATE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isDeckSorting: incorrect " +
                    "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isDeckSorting: no return " +
                    "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean canDraw(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isDeckEmpty: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.CAN_DRAW_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isDeckEmpty: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.CAN_DRAW_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isDeckEmpty: incorrect " +
                    "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isDeckEmpty: no return " +
                    "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public DominoesDeck getDeck(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getDeck: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.DECK_FETCH_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getDeck: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.DECK_FETCH_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getDeck: incorrect " +
                    "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            return null;
        }
        dcCommunication.close();

        return (DominoesDeck) inMessage.getReturnInfo();
    }

    @Override
    public boolean returnDeck(String pseudonym, byte[] cipheredSessionID, int tableID, DominoesDeck deck,
                              byte[] pieceDif)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: returnDeck: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.DECK_RETURN_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID, deck, pieceDif);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: returnDeck: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.DECK_RETURN_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: returnDeck: incorrect " +
                    "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: returnDeck: no return " +
                    "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public void skipTurn(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: skipTurn: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.SKIP_TURN_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: skipTurn: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.SKIP_TURN_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: skipTurn: incorrect " +
                    "reply message!");

            System.exit(706);
        }
    }

    @Override
    public boolean commitHand(String pseudonym, byte[] cipheredSessionID, int tableID, DominoesCommitData commitData)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: commitHand: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.COMMIT_HAND_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID, commitData);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: commitHand: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.COMMIT_HAND_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: commitHand: incorrect " +
                    "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: commitHand: no return " +
                    "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean hasPlayerCommitted(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasPlayerCommitted: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.COMMIT_STATE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasPlayerCommitted: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.COMMIT_STATE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasPlayerCommitted: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasPlayerCommitted: no " +
                    "return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean isHandlingStart(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingStart: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.START_HANDLING_STATE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingStart: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.START_HANDLING_STATE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingStart: incorrect " +
                    "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingStart: no return " +
                    "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public void stateHighestDouble(String pseudonym, byte[] cipheredSessionID, int tableID, String piece)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: stateHighestDouble: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.DOUBLE_STATING_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID, piece);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: stateHighestDouble: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.DOUBLE_STATING_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: stateHighestDouble: " +
                    "incorrect reply message!");

            System.exit(706);
        }
    }

    @Override
    public boolean hasDoubleCheckingEnded(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasDoubleCheckingEnded: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.DOUBLE_CHECKING_STATE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasDoubleCheckingEnded: "
                    + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.DOUBLE_CHECKING_STATE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasDoubleCheckingEnded: " +
                    "incorrect " + "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasDoubleCheckingEnded: " +
                    "no return " + "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean isRedistributionNeeded(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isRedistributionNeeded: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.REDISTRIBUTION_NEEDED_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isRedistributionNeeded: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.REDISTRIBUTION_NEEDED_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isRedistributionNeeded: " +
                    "incorrect " + "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isRedistributionNeeded: " +
                    "no return " + "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public DominoesGameState getGameState(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getGameState: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.GAME_STATE_FETCH_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getGameState: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.GAME_STATE_FETCH_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getGameState: incorrect " +
                    "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            return null;
        }
        dcCommunication.close();

        return (DominoesGameState) inMessage.getReturnInfo();
    }

    @Override
    public boolean isResetNeeded(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isResetNeeded: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.RESET_NEEDED_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isResetNeeded: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.RESET_NEEDED_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isResetNeeded: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isResetNeeded: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean playPiece(String pseudonym, byte[] cipheredSessionID, int tableID, String targetEndPoint,
                             String piece, String pieceEndPoint)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: playPiece: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.PLAY_PIECE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID, targetEndPoint, piece, pieceEndPoint);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: playPiece: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.PLAY_PIECE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: playPiece: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: playPiece: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public String drawPiece(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: drawCard: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.DRAW_PIECE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: drawCard: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.DRAW_PIECE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: drawCard: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: drawCard: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (String) inMessage.getReturnInfo();
    }

    @Override
    public void denounceCheating(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: denounceCheating: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.DENOUNCE_CHEATING_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: denounceCheating: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.DENOUNCE_CHEATING_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: denounceCheating: incorrect " +
                    "reply message!");

            System.exit(706);
        }
    }

    @Override
    public boolean isHandlingCheating(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingCheating: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.CHEAT_HANDLING_STATE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingCheating: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.CHEAT_HANDLING_STATE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingCheating: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingCheating: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean updateCommitment(String pseudonym, byte[] cipheredSessionID, int tableID,
                                    DominoesCommitData commitData)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: updateCommitment: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.COMMIT_UPDATE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID, commitData);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: updateCommitment: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.COMMIT_UPDATE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: updateCommitment: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: updateCommitment: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean sendCommitData(String pseudonym, byte[] cipheredSessionID, int tableID,
                                  DominoesCommitData commitData)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: sendCommitData: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.SEND_COMMIT_DATA_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID, commitData);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: sendCommitData: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.SEND_COMMIT_DATA_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: sendCommitData: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: sendCommitData: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean hasSentCommitData(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasSentCommitData: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.HAS_SENT_COMMIT_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasSentCommitData: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.HAS_SENT_COMMIT_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasSentCommitData: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasSentCommitData: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean isHandlingAccounting(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingAccounting: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.IS_HANDLING_ACCOUNT_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingAccounting: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.IS_HANDLING_ACCOUNT_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingAccounting: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingAccounting: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public DominoesAccountingInfo getAccountingInfo(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getAccountingInfo: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.ACCOUNTING_INFO_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getAccountingInfo: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.ACCOUNTING_INFO_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getAccountingInfo: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getAccountingInfo: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (DominoesAccountingInfo) inMessage.getReturnInfo();
    }

    @Override
    public boolean sendAccountingDecision(String pseudonym, byte[] cipheredSessionID, int tableID, boolean decision)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: sendAccountingDecision: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.ACCOUNTING_DECISION_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID, decision);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: sendAccountingDecision: "
                    + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.ACCOUNTING_DECISION_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: sendAccountingDecision: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: sendAccountingDecision: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean allSentDecision(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: allSentDecision: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.ALL_DECISIONS_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: allSentDecision: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.ALL_DECISIONS_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: allSentDecision: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: allSentDecision: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean allAgreedToAccounting(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: allAgreedToAccounting: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.ALL_AGREED_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: allAgreedToAccounting: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.ALL_AGREED_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: allAgreedToAccounting: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: allAgreedToAccounting: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public void passedProtestMenu(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: passedProtestMenu: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.PROTEST_PASSED_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: passedProtestMenu: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.PROTEST_PASSED_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: passedProtestMenu: incorrect "
                    + "reply message!");

            System.exit(706);
        }
    }

    @Override
    public boolean allPassedProtestMenu(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: allPassedProtestMenu: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.PROTEST_CHECK_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: allPassedProtestMenu: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.PROTEST_CHECK_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: allPassedProtestMenu: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: allPassedProtestMenu: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean isUserRegistered(String user)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isUserRegistered: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.CHECK_USER_REGISTER_REQUEST.getMessageCode(), user);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isUserRegistered: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.CHECK_USER_REGISTER_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isUserRegistered: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isUserRegistered: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean registerUser(String user)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: registerUser: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.REGISTER_USER_REQUEST.getMessageCode(), user);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: registerUser: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.REGISTER_USER_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: registerUser: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: registerUser: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public byte[] greetServer(String pseudonym, byte[] publicKey)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: greetServer: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.GREET_SERVER_REQUEST.getMessageCode(), pseudonym,
                    (Object) publicKey);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: greetServer: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.GREET_SERVER_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: greetServer: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: greetServer: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (byte[]) inMessage.getReturnInfo();
    }

    @Override
    public byte[] getServerPublicKey()
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getServerPublicKey: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.SERVER_KEY_REQUEST.getMessageCode());
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getServerPublicKey: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.SERVER_KEY_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getServerPublicKey: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getServerPublicKey: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (byte[]) inMessage.getReturnInfo();
    }

    @Override
    public byte[] sendSessionID(String pseudonym, byte[] cipheredSessionID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: sendSessionID: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.SESSION_ID_SEND_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: sendSessionID: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.SESSION_ID_SEND_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: sendSessionID: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: sendSessionID: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (byte[]) inMessage.getReturnInfo();
    }

    @Override
    public boolean hasKeySortingStarted(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasKeySortingStarted: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.HAS_KEY_SORTING_STARTED_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasKeySortingStarted: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.HAS_KEY_SORTING_STARTED_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasKeySortingStarted: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasKeySortingStarted: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public void startKeySorting(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: startKeySorting: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.START_KEY_SORTING_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: startKeySorting: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.START_KEY_SORTING_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: startKeySorting: incorrect "
                    + "reply message!");

            System.exit(706);
        }
    }

    @Override
    public boolean hasKeySortingEnded(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasKeySortingEnded: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.HAS_KEY_SORTING_ENDED_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasKeySortingEnded: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.HAS_KEY_SORTING_ENDED_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasKeySortingEnded: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasKeySortingEnded: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public byte[][] getPlayerPublicKeys(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getPlayerPublicKeys: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.PLAYER_PUBLIC_KEYS_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getPlayerPublicKeys: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.PLAYER_PUBLIC_KEYS_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getPlayerPublicKeys: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getPlayerPublicKeys: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (byte[][]) inMessage.getReturnInfo();
    }

    @Override
    public DominoesSymKeyMatrix getSymKeyDistributionMatrix(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                        "getSymKeyDistributionMatrix: " + e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.KEY_DISTRIBUTION_MATRIX_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "getSymKeyDistributionMatrix: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.KEY_DISTRIBUTION_MATRIX_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "getSymKeyDistributionMatrix: incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            return null;
        }
        dcCommunication.close();

        return (DominoesSymKeyMatrix) inMessage.getReturnInfo();
    }

    @Override
    public boolean returnSymKeyDistributionMatrix(String pseudonym, byte[] cipheredSessionID, int tableID,
                                                  DominoesSymKeyMatrix symKeyMatrix)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                        "returnSymKeyDistributionMatrix: " + e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.RETURN_KEY_DISTRIBUTION_MATRIX_REQUEST.getMessageCode(),
                    pseudonym, cipheredSessionID, tableID, symKeyMatrix);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "returnSymKeyDistributionMatrix: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.RETURN_KEY_DISTRIBUTION_MATRIX_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "returnSymKeyDistributionMatrix: incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "returnSymKeyDistributionMatrix: no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public byte[][] getSessionSymKeys(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getSessionSymKeys: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.SESSION_SYM_KEYS_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getSessionSymKeys: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.SESSION_SYM_KEYS_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getSessionSymKeys: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getSessionSymKeys: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (byte[][]) inMessage.getReturnInfo();
    }

    @Override
    public boolean hasDeckBeenProtected(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                        "hasDeckBeenProtected: " + e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.HAS_DECK_BEEN_PROTECTED_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "hasDeckBeenProtected: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.HAS_DECK_BEEN_PROTECTED_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "hasDeckBeenProtected: incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "hasDeckBeenProtected: no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean sendDeckProtectionPrivateKey(String pseudonym, byte[] cipheredSessionID, int tableID,
                                                byte[] deckProtectionPrivateKey)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                        "sendDeckProtectionPublicKey: " + e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.SEND_DECK_PROTECTION_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID, deckProtectionPrivateKey);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "sendDeckProtectionPublicKey: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.SEND_DECK_PROTECTION_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "sendDeckProtectionPublicKey: incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "sendDeckProtectionPublicKey: no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public void notifyDeckProtected(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: notifyDeckProtected: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.NOTIFY_DECK_PROTECTED_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: notifyDeckProtected: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.NOTIFY_DECK_PROTECTED_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: notifyDeckProtected: " +
                    "incorrect reply message!");

            System.exit(706);
        }
    }

    @Override
    public boolean haveAllSentDeckProtectionPrivateKeys(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                        "haveAllSentDeckProtectionPrivateKeys: " + e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.HAVE_ALL_SENT_DECK_PROTECTION_REQUEST.getMessageCode(),
                    pseudonym, cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "haveAllSentDeckProtectionPrivateKeys: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.HAVE_ALL_SENT_DECK_PROTECTION_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "haveAllSentDeckProtectionPrivateKeys: incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "haveAllSentDeckProtectionPrivateKeys: no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean hasSentDeckProtectionPrivateKey(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                        "hasSentDeckProtectionPrivateKey: " + e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.HAS_SENT_DECK_PROTECTION_REQUEST.getMessageCode(),
                    pseudonym, cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "hasSentDeckProtectionPrivateKey: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.HAS_SENT_DECK_PROTECTION_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "hasSentDeckProtectionPrivateKey: incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "hasSentDeckProtectionPrivateKey: no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public Stack<byte[]> getDeckProtectionKeyStack(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": " +
                        "DCStub: getDeckProtectionKeyStack: " + e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.DECK_PROTECTION_KEY_STACK_REQUEST.getMessageCode(),
                    pseudonym, cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getDeckProtectionKeyStack: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.DECK_PROTECTION_KEY_STACK_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getDeckProtectionKeyStack: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getDeckProtectionKeyStack: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (Stack<byte[]>) inMessage.getReturnInfo();
    }

    @Override
    public boolean isDeckSentFromServer(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                        "isDeckComingFromServer: " + e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.DECK_SENT_BY_SERVER_REQUEST.getMessageCode(),
                    pseudonym, cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "isDeckComingFromServer: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.DECK_SENT_BY_SERVER_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "isDeckComingFromServer: incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "isDeckComingFromServer: no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public byte[] getLastTurn(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getLastTurn: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.LAST_TURN_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getLastTurn: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.LAST_TURN_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getLastTurn: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getLastTurn: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (byte[]) inMessage.getReturnInfo();
    }

    @Override
    public byte[] getNextTurn(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getNextTurn: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.NEXT_TURN_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getNextTurn: "
                    + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.NEXT_TURN_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getNextTurn: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getNextTurn: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (byte[]) inMessage.getReturnInfo();
    }

    @Override
    public byte[] getUserScore(String pseudonym, byte[] cipheredSessionID, String user)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getUserScore: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.GET_USER_SCORE_REQUEST.getMessageCode(), pseudonym,
                    cipheredSessionID, user);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getUserScore: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.GET_USER_SCORE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getUserScore: " +
                    "incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: getUserScore: " +
                    "no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (byte[]) inMessage.getReturnInfo();
    }

    @Override
    public boolean proveUserIdentity(String pseudonym, byte[] cipheredSessionID, int tableID, String user,
                                     Key userPublicKey)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                        "proveUserIdentity: " + e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.PROVE_IDENTITY_REQUEST.getMessageCode(),
                    pseudonym, cipheredSessionID, tableID, user, userPublicKey);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "proveUserIdentity: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.PROVE_IDENTITY_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "proveUserIdentity: incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "proveUserIdentity: no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean haveAllFinishedAccounting(String pseudonym, byte[] cipheredSessionID, int tableID)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                        "haveAllFinishedAccounting: " + e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.ALL_FINISHED_ACCOUNTING_REQUEST.getMessageCode(),
                    pseudonym, cipheredSessionID, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "haveAllFinishedAccounting: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.ALL_FINISHED_ACCOUNTING_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "haveAllFinishedAccounting: incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "haveAllFinishedAccounting: no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean hasPseudonymBeenUsed(String pseudonym)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                        "hasPseudonymBeenUsed: " + e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.WAS_PSEUDONYM_USED_REQUEST.getMessageCode(),
                    pseudonym);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "hasPseudonymBeenUsed: " + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.WAS_PSEUDONYM_USED_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "hasPseudonymBeenUsed: incorrect reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: " +
                    "hasPseudonymBeenUsed: no return value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public void setPseudonymAsUsed(String pseudonym, String user)
    {
        DCCommunication dcCommunication = new DCCommunication(serverHostName, serverHostPort);
        DMessage inMessage;
        DMessage outMessage = null;

        while (!dcCommunication.open())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: setPseudonymAsUsed: " +
                        e.toString());
            }
        }

        try
        {
            outMessage = new DMessage(DMessage.MessageType.SET_PSEUDONYM_USED_REQUEST.getMessageCode(), pseudonym,
                    user);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: setPseudonymAsUsed: " +
                    e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.SET_PSEUDONYM_USED_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: setPseudonymAsUsed: " +
                    "incorrect reply message!");

            System.exit(706);
        }
    }
}
