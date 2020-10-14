package DominoesCommunication;

import java.io.Serializable;

public class Message implements Serializable
{
    public enum MessageType implements Serializable
    {
        ;

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
            for(MessageType m : values()) if(m.getMessageCode() == messageCode) return m.name();
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
    private final Object returnInfo;

    public Message(int messageType)
    {
        this.messageType = messageType;
        this.returnInfo = null;
    }

    public Message(int messageType, Object returnInfo)
    {
        this.messageType = messageType;
        this.returnInfo = returnInfo;
    }
}
