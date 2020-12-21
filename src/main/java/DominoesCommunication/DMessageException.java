package DominoesCommunication;

public class DMessageException extends Exception
{
    private DMessage errorMessage;

    public DMessageException(String errorMessage)
    {
        super(errorMessage);
    }

    public DMessageException(String errorMessage, DMessage message)
    {
        super(errorMessage);
        this.errorMessage = message;
    }

    public DMessage getErrorMessage()
    {
        return this.errorMessage;
    }
}
