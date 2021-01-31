package DominoesAI;

import DominoesClient.DCStub;

public class DAIGuestMain
{
    private static final String serverHostName = "localhost";
    private static final int serverPort = 4000;

    public static void main(String[] args)
    {
        DCStub dcStub = new DCStub(serverHostName, serverPort);
        DAIThread daiThread = new DAIThread(dcStub, false);
        daiThread.start();
    }
}
