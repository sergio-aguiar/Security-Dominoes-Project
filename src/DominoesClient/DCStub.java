package DominoesClient;

import DominoesCommunication.DCCommunication;
import DominoesCommunication.DMessage;
import DominoesCommunication.DMessageException;
import DominoesMisc.DominoesDeck;
import DominoesMisc.DominoesGameState;
import DominoesMisc.DominoesTable;

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
    public int createTable(String pseudonym, int playerCap)
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
            outMessage = new DMessage(DMessage.MessageType.CREATE_TABLE_REQUEST.getMessageCode(), pseudonym, playerCap);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: createTable: " +
                    e.toString());
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
    public DominoesTable[] listAvailableTables()
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
            outMessage = new DMessage(DMessage.MessageType.LIST_TABLES_REQUEST.getMessageCode());
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

        return (DominoesTable[]) inMessage.getReturnInfo();
    }

    @Override
    public boolean joinTable(String pseudonym, int tableID)
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
            outMessage = new DMessage(DMessage.MessageType.JOIN_TABLE_REQUEST.getMessageCode(), pseudonym, tableID);
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
    public int joinRandomTable(String pseudonym)
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
            outMessage = new DMessage(DMessage.MessageType.JOIN_RANDOM_TABLE_REQUEST.getMessageCode(), pseudonym);
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
    public boolean startGame(String pseudonym, int tableID)
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
            outMessage = new DMessage(DMessage.MessageType.START_GAME_REQUEST.getMessageCode(), pseudonym, tableID);
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
    public void disbandTable(String pseudonym, int tableID)
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
            outMessage = new DMessage(DMessage.MessageType.DISBAND_TABLE_REQUEST.getMessageCode(), pseudonym, tableID);
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
    public boolean markAsReady(String pseudonym, int tableID)
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
            outMessage = new DMessage(DMessage.MessageType.MARK_AS_READY_REQUEST.getMessageCode(), pseudonym, tableID);
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
    public DominoesTable listTableInfo(String pseudonym, int tableID)
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
            outMessage = new DMessage(DMessage.MessageType.LIST_TABLE_INFO_REQUEST.getMessageCode(), pseudonym, tableID);
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

        return (DominoesTable) inMessage.getReturnInfo();
    }

    @Override
    public void leaveTable(String pseudonym, int tableID)
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
            outMessage = new DMessage(DMessage.MessageType.LEAVE_TABLE_REQUEST.getMessageCode(), pseudonym, tableID);
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
    public boolean isPlayerTurn(String pseudonym, int tableID)
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
            outMessage = new DMessage(DMessage.MessageType.TURN_CHECK_REQUEST.getMessageCode(), pseudonym, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isPlayerTurn: "
                    + e.toString());
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
    public boolean hasGameEnded(String pseudonym, int tableID)
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
            outMessage = new DMessage(DMessage.MessageType.GAME_STATE_REQUEST.getMessageCode(), pseudonym, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasGameEnded: "
                    + e.toString());
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
    public boolean isDeckSorting(String pseudonym, int tableID)
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
                    tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isDeckSorting: "
                    + e.toString());
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
    public boolean canDraw(String pseudonym, int tableID)
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
                    tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isDeckEmpty: "
                    + e.toString());
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
    public DominoesDeck getDeck(String pseudonym, int tableID)
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
            outMessage = new DMessage(DMessage.MessageType.DECK_FETCH_REQUEST.getMessageCode(), pseudonym, tableID);
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
    public boolean returnDeck(String pseudonym, int tableID, DominoesDeck deck, int cardDif)
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
                    tableID, deck, cardDif);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: returnDeck: "
                    + e.toString());
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
    public void skipTurn(String pseudonym, int tableID)
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
            outMessage = new DMessage(DMessage.MessageType.SKIP_TURN_REQUEST.getMessageCode(), pseudonym, tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: skipTurn: " +
                    e.toString());
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
    public boolean commitHand(String pseudonym, int tableID, String bitCommitment)
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
                    tableID, bitCommitment);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: commitHand: "
                    + e.toString());
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
    public boolean hasPlayerCommitted(String pseudonym, int tableID)
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
                    tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasPlayerCommitted: "
                    + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.COMMIT_STATE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasPlayerCommitted: incorrect "
                    + "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: hasPlayerCommitted: no return "
                    + "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean isHandlingStart(String pseudonym, int tableID)
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
                    tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingStart: "
                    + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.START_HANDLING_STATE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingStart: incorrect "
                    + "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isHandlingStart: no return "
                    + "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public void stateHighestDouble(String pseudonym, int tableID, String piece)
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
            outMessage = new DMessage(DMessage.MessageType.DOUBLE_STATING_REQUEST.getMessageCode(), pseudonym, tableID,
                    piece);
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
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: stateHighestDouble: incorrect "
                    + "reply message!");

            System.exit(706);
        }
    }

    @Override
    public boolean hasDoubleCheckingEnded(String pseudonym, int tableID)
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
                    tableID);
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
    public boolean isRedistributionNeeded(String pseudonym, int tableID)
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
                    tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isRedistributionNeeded: "
                    + e.toString());
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
    public DominoesGameState getGameState(String pseudonym, int tableID)
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
                    tableID);
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
    public boolean isResetNeeded(String pseudonym, int tableID)
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
                    tableID);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isResetNeeded: "
                    + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.RESET_NEEDED_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isResetNeeded: " +
                    "incorrect " + "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: isResetNeeded: " +
                    "no return " + "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }

    @Override
    public boolean playPiece(String pseudonym, int tableID, String endPoint, String piece)
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
                    tableID, endPoint, piece);
        }
        catch (DMessageException e)
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: playPiece: "
                    + e.toString());
        }

        dcCommunication.writeObject(outMessage);
        inMessage = (DMessage) dcCommunication.readObject();

        if (inMessage.getMessageType() != DMessage.MessageType.PLAY_PIECE_REQUEST.getMessageCode())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: playPiece: " +
                    "incorrect " + "reply message!");

            System.exit(706);
        }

        if (inMessage.noReturnInfo())
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": DCStub: playPiece: " +
                    "no return " + "value!");

            System.exit(704);
        }
        dcCommunication.close();

        return (boolean) inMessage.getReturnInfo();
    }
}
