package DominoesServer;

import DominoesCommunication.DMessage;
import DominoesCommunication.DMessageException;

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
            default:
        }

        switch(inMessage.getMessageType())
        {
            default:
        }

        return outMessage;
    }
}
