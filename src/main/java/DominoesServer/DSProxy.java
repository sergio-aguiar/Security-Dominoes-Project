package DominoesServer;

import DominoesCommunication.DMessageException;
import DominoesCommunication.DSCommunication;
import DominoesCommunication.DMessage;

import java.util.concurrent.locks.ReentrantLock;

public class DSProxy extends Thread
{
    private static final ReentrantLock reentrantLock = new ReentrantLock();
    private static int nProxy = 0;
    private DSCommunication dsCommunication;
    private DSInterface dsInterface;

    public DSProxy(DSCommunication dsCommunication, DSInterface dsInterface)
    {
        super("DSProxy_" + getProxyID());
        this.dsCommunication = dsCommunication;
        this.dsInterface = dsInterface;
    }

    @Override
    public void run()
    {
        DMessage inMessage = null;
        DMessage outMessage = null;

        inMessage = (DMessage) this.dsCommunication.readObject();
        try
        {
            outMessage = this.dsInterface.processAndReply(inMessage);
        }
        catch(DMessageException e)
        {
            System.out.println("Thread " + getName() + ": " + e.getMessage() + "!");
            System.out.println(e.getErrorMessage().toString());
            System.exit(901);
        }
        this.dsCommunication.writeObject(outMessage);
        this.dsCommunication.close();
    }

    private static int getProxyID()
    {
        int proxyID = -1;

        try
        {
            Class.forName("DominoesServer.DSProxy");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Data type DSProxy was not found!");

            e.printStackTrace();
            System.exit(902);
        }

        reentrantLock.lock();
        try
        {
            proxyID = nProxy;
            nProxy += 1;
        } catch (Exception e)
        {
            System.out.println("DSProxy: getProxyID: " + e.toString());
        }
        finally
        {
            reentrantLock.unlock();
        }

        return proxyID;
    }

    public DSCommunication getDsCommunication()
    {
        return this.dsCommunication;
    }
}
