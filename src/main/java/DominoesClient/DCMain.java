package DominoesClient;

import DominoesSecurity.DominoesCC;
import DominoesSecurity.DominoesSignature;

import java.security.Key;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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
