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
        CAN_DRAW_REQUEST(13),
        DECK_FETCH_REQUEST(14),
        DECK_RETURN_REQUEST(15),
        SKIP_TURN_REQUEST(16),
        COMMIT_HAND_REQUEST(17),
        COMMIT_STATE_REQUEST(18),
        START_HANDLING_STATE_REQUEST(19),
        DOUBLE_STATING_REQUEST(20),
        DOUBLE_CHECKING_STATE_REQUEST(21),
        REDISTRIBUTION_NEEDED_REQUEST(22),
        GAME_STATE_FETCH_REQUEST(23),
        RESET_NEEDED_REQUEST(24),
        PLAY_PIECE_REQUEST(25),
        DRAW_PIECE_REQUEST(26),
        DENOUNCE_CHEATING_REQUEST(27),
        CHEAT_HANDLING_STATE_REQUEST(28),
        COMMIT_UPDATE_REQUEST(29),
        SEND_COMMIT_DATA_REQUEST(30),
        HAS_SENT_COMMIT_REQUEST(31),
        IS_HANDLING_ACCOUNT_REQUEST(32),
        ACCOUNTING_INFO_REQUEST(33),
        ACCOUNTING_DECISION_REQUEST(34),
        ALL_DECISIONS_REQUEST(35),
        ALL_AGREED_REQUEST(36),
        PROTEST_PASSED_REQUEST(37),
        PROTEST_CHECK_REQUEST(38),
        CHECK_USER_REGISTER_REQUEST(39),
        REGISTER_USER_REQUEST(40),
        GREET_SERVER_REQUEST(41),
        SERVER_KEY_REQUEST(42),
        SESSION_ID_SEND_REQUEST(43);

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
    private final byte[] cipheredSessionID;
    private final String pseudonym;
    private final Object returnInfo;
    private final Object firstArgument;
    private final Object secondArgument;
    private final Object thirdArgument;
    private final Object fourthArgument;

    public DMessage(int messageType) throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = null;
        this.cipheredSessionID = null;
        this.returnInfo = null;
        this.firstArgument = null;
        this.secondArgument = null;
        this.thirdArgument = null;
        this.fourthArgument = null;
    }

    public DMessage(int messageType, String pseudonym) throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = pseudonym;
        this.cipheredSessionID = null;
        this.returnInfo = null;
        this.firstArgument = null;
        this.secondArgument = null;
        this.thirdArgument = null;
        this.fourthArgument = null;
    }

    public DMessage(int messageType, String pseudonym, Object firstArgument) throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = pseudonym;
        this.cipheredSessionID = null;
        this.returnInfo = null;
        this.firstArgument = firstArgument;
        this.secondArgument = null;
        this.thirdArgument = null;
        this.fourthArgument = null;
    }

    public DMessage(int messageType, String pseudonym, Object firstArgument, Object secondArgument)
            throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = pseudonym;
        this.cipheredSessionID = null;
        this.returnInfo = null;
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
        this.thirdArgument = null;
        this.fourthArgument = null;
    }

    public DMessage(int messageType, String pseudonym, Object firstArgument, Object secondArgument,
                    Object thirdArgument) throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = pseudonym;
        this.cipheredSessionID = null;
        this.returnInfo = null;
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
        this.thirdArgument = thirdArgument;
        this.fourthArgument = null;
    }

    public DMessage(int messageType, String pseudonym, Object firstArgument, Object secondArgument,
                    Object thirdArgument, Object fourthArgument) throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = pseudonym;
        this.cipheredSessionID = null;
        this.returnInfo = null;
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
        this.thirdArgument = thirdArgument;
        this.fourthArgument = fourthArgument;
    }

    public DMessage(int messageType, Object returnInfo) throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = null;
        this.cipheredSessionID = null;
        this.returnInfo = returnInfo;
        this.firstArgument = null;
        this.secondArgument = null;
        this.thirdArgument = null;
        this.fourthArgument = null;
    }

    public DMessage(int messageType, String pseudonym, byte[] cipheredSessionID) throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = pseudonym;
        this.cipheredSessionID = cipheredSessionID;
        this.returnInfo = null;
        this.firstArgument = null;
        this.secondArgument = null;
        this.thirdArgument = null;
        this.fourthArgument = null;
    }

    public DMessage(int messageType, String pseudonym, byte[] cipheredSessionID, Object firstArgument)
            throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = pseudonym;
        this.cipheredSessionID = cipheredSessionID;
        this.returnInfo = null;
        this.firstArgument = firstArgument;
        this.secondArgument = null;
        this.thirdArgument = null;
        this.fourthArgument = null;
    }

    public DMessage(int messageType, String pseudonym, byte[] cipheredSessionID, Object firstArgument,
                    Object secondArgument) throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = pseudonym;
        this.cipheredSessionID = cipheredSessionID;
        this.returnInfo = null;
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
        this.thirdArgument = null;
        this.fourthArgument = null;
    }

    public DMessage(int messageType, String pseudonym, byte[] cipheredSessionID, Object firstArgument,
                    Object secondArgument, Object thirdArgument) throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = pseudonym;
        this.cipheredSessionID = cipheredSessionID;
        this.returnInfo = null;
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
        this.thirdArgument = thirdArgument;
        this.fourthArgument = null;
    }

    public DMessage(int messageType, String pseudonym, byte[] cipheredSessionID, Object firstArgument,
                    Object secondArgument, Object thirdArgument, Object fourthArgument) throws DMessageException
    {
        this.messageType = messageType;
        this.pseudonym = pseudonym;
        this.cipheredSessionID = cipheredSessionID;
        this.returnInfo = null;
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
        this.thirdArgument = thirdArgument;
        this.fourthArgument = fourthArgument;
    }

    public int getMessageType()
    {
        return this.messageType;
    }

    public byte[] getCipheredSessionID()
    {
        return this.cipheredSessionID;
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

    public Object getThirdArgument()
    {
        return this.thirdArgument;
    }

    public Object getFourthArgument()
    {
        return this.fourthArgument;
    }

    public boolean noCipheredSessionID()
    {
        return this.cipheredSessionID == null;
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

    public boolean noThirdArgument()
    {
        return this.thirdArgument == null;
    }

    public boolean noFourthArgument()
    {
        return this.fourthArgument == null;
    }

    @Override
    public String toString()
    {
        return "DMessage{" + "messageType=" + messageType + ", returnInfo=" + returnInfo + '}';
    }
}
