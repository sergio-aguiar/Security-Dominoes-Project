package DominoesMisc;

import DominoesCommunication.DMessage;

public class DominoesTableException extends Exception
{
    public DominoesTableException(String errorMessage)
    {
        super(errorMessage);
    }
}
