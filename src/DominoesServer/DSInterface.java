package DominoesServer;

import DominoesCommunication.DMessage;
import DominoesCommunication.DMessageException;
import DominoesMisc.DominoesTable;

public class DSInterface
{
    private final DSImplementation dsImplementation;

    public DSInterface(DSImplementation dsImplementation)
    {
        this.dsImplementation = dsImplementation;
    }

    public DMessage processAndReply(DMessage inMessage) throws DMessageException
    {
        DMessage outMessage = null;

        switch(inMessage.getMessageType())
        {
            case 1:
                if (inMessage.noFirstArgument())
                    throw new DMessageException("Argument \"playerCap\" was not given", inMessage);
                if ((int) inMessage.getFirstArgument() != 2
                        && (int) inMessage.getFirstArgument() != 4
                        && (int) inMessage.getFirstArgument() != 7)
                    throw new DMessageException("Argument \"playerCap\" was given an incorrect value", inMessage);
                break;
            case 2:
            case 4:
                break;
            case 3:
                if (inMessage.noFirstArgument())
                    throw new DMessageException("Argument \"tableID\" was not given.", inMessage);
                if ((int) inMessage.getFirstArgument() < 0)
                    throw new DMessageException("Argument \"tableID\" was given an incorrect value", inMessage);
                break;
            default:
                throw new DMessageException("Invalid message type: " + inMessage.getMessageType());
        }

        switch(inMessage.getMessageType())
        {
            case 1:
                int return1 = this.dsImplementation.createTable(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.CREATE_TABLE_REQUEST.getMessageCode(), return1);
                break;
            case 2:
                DominoesTable[] return2 = this.dsImplementation.listAvailableTables();
                outMessage = new DMessage(DMessage.MessageType.LIST_TABLES_REQUEST.getMessageCode(), return2);
                break;
            case 3:
                boolean return3 = this.dsImplementation.joinTable(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.JOIN_TABLE_REQUEST.getMessageCode(), return3);
                break;
            case 4:
                int return4 = this.dsImplementation.joinRandomTable(inMessage.getPseudonym());
                outMessage = new DMessage(DMessage.MessageType.JOIN_RANDOM_TABLE_REQUEST.getMessageCode(), return4);
                break;
        }

        return outMessage;
    }
}
