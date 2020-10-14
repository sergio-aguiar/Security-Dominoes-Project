package DominoesServer;

import DominoesCommunication.DSCommunication;

import java.net.SocketTimeoutException;

public class DSMain
{
    public static  boolean running;
    private  static final int serverPort = 4000;

    public static void main(String[] args)
    {
        DSImplementation dsImplementation = new DSImplementation();
        DSInterface dsInterface = new DSInterface(dsImplementation);
        DSProxy dsProxy;

        DSCommunication dsCommunication = new DSCommunication(serverPort);
        DSCommunication dsCommunicationL;
        dsCommunication.start();

        System.out.println("[Server] DominoesServer now listening on port " + serverPort);

        running = true;
        while (running)
        {
            try
            {
                dsCommunicationL = dsCommunication.accept();
                dsProxy = new DSProxy(dsCommunicationL, dsInterface);
                dsProxy.start();
            }
            catch (SocketTimeoutException ignored) {}
        }

        dsCommunication.end();
        System.out.println("[Server] DominoesServer shutting down...");
    }
}
