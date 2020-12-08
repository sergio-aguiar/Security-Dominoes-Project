package DominoesCommunication;

import java.io.Serializable;

public class DMessage implements Serializable
{
    public enum MessageType implements Serializable
    {
        CREATE_TABLE_REQUEST(1),
        LIST_TABLES_REQUEST(2),
        JOIN_TABLE_REQUEST(3),
        JOIN_RANDOM_TABLE_REQUEST(4),
        LIST_TABLE_INFO_REQUEST(5),
        DISBAND_TABLE_REQUEST(6),
        LEAVE_TABLE_REQUEST(7),
        START_GAME_REQUEST(8),
        MARK_AS_READY_REQUEST(9),
        TURN_CHECK_REQUEST(10),
        GAME_STATE_REQUEST(11),
        DISTRIBUTION_STATE_REQUEST(12),
        DRAW_PIECE_REQUEST(13),
        RETURN_PIECE_REQUEST(14),
        SWAP_PIECE_REQUEST(15),
        SKIP_TURN_REQUEST(16),
        COMMIT_HAND_REQUEST(17),
        COMMIT_STATE_REQUEST(18);

        private static final long serialVersionUID = 1102L;

        private final int messageCode;

        MessageType(int messageCode)
        {
            this.messageCode = messageCode;
        }

        public int getMessageCode()
        {
            return this.messageCode;
        }

        public static String getNameByMessageCode(int messageCode)
        {
            for (MessageType m : values()) if (m.getMessageCode() == messageCode) return m.name();
            return "Error";
        }

        @Override
        public String toString()
        {
            return this.name();
        }
    }

    private static final long serialVersionUID = 1101L;

    private final int messageType;
    private final String pseudonym;
    private final Object returnInfo;
    private final Object firstArgument;
    private final Object secondArgument;

    public DMessage(int messageType) throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = null;
        this.returnInfo = null;
        this.firstArgument = null;
        this.secondArgument = null;
    }

    public DMessage(int messageType, String pseudonym) throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = pseudonym;
        this.returnInfo = null;
        this.firstArgument = null;
        this.secondArgument = null;
    }

    public DMessage(int messageType, String pseudonym, Object firstArgument) throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = pseudonym;
        this.returnInfo = null;
        this.firstArgument = firstArgument;
        this.secondArgument = null;
    }

    public DMessage(int messageType, String pseudonym, Object firstArgument, Object secondArgument)
            throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = pseudonym;
        this.returnInfo = null;
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
    }

    public DMessage(int messageType, Object returnInfo) throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = null;
        this.returnInfo = returnInfo;
        this.firstArgument = null;
        this.secondArgument = null;
    }

    public int getMessageType()
    {
        return this.messageType;
    }

    public String getPseudonym()
    {
        return this.pseudonym;
    }

    public Object getReturnInfo()
    {
        return this.returnInfo;
    }

    public Object getFirstArgument()
    {
        return this.firstArgument;
    }

    public Object getSecondArgument()
    {
        return this.secondArgument;
    }

    public boolean noReturnInfo()
    {
        return this.returnInfo == null;
    }

    public boolean noFirstArgument()
    {
        return this.firstArgument == null;
    }

    public boolean noSecondArgument()
    {
        return this.secondArgument == null;
    }

    @Override
    public String toString()
    {
        return "DMessage{" + "messageType=" + messageType + ", returnInfo=" + returnInfo + '}';


    }
}
