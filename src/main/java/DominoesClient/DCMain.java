package DominoesClient;

public class DCMain
{
    private static final String serverHostName = "localhost";
    private static final int serverPort = 4000;

    public static void main(String[] args)
    {
        DCStub dcStub = new DCStub(serverHostName, serverPort);
        DCThread dcThread = new DCThread(dcStub);
        dcThread.start();
    }
}
