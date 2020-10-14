package DominoesCommunication;

import java.io.Serializable;

public class DMessage implements Serializable
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

    public DMessage(int messageType) throws DMessageException
    {
        this.messageType = messageType;
        this.returnInfo = null;
    }

    public DMessage(int messageType, Object returnInfo) throws DMessageException
    {
        this.messageType = messageType;
        this.returnInfo = returnInfo;
    }

    public int getMessageType()
    {
        return this.messageType;
    }

    public Object getReturnInfo()
    {
        return this.returnInfo;
    }

    @Override
    public String toString()
    {
        return "DMessage{" + "messageType=" + messageType + ", returnInfo=" + returnInfo + '}';
    }
}
