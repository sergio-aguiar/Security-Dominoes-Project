package DominoesClient;

import DominoesSecurity.DominoesCC;
import DominoesSecurity.DominoesSignature;

import java.security.Key;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class DCMain
{
    private static final Scanner sc = new Scanner(System.in);
    private static final String serverHostName = "localhost";
    private static final int serverPort = 4000;

    private static int sessionID;
    private static String pseudonym;

    public static void main(String[] args)
    {
        DCStub dcStub = new DCStub(serverHostName, serverPort);

        generatePseudonym();

        DCThread dcThread = new DCThread(pseudonym, sessionID, dcStub);
        dcThread.start();
    }

    private static void pseudonymQuery()
    {
        do
        {
            System.out.print("\n[CLIENT] Insert Pseudonym: ");
            pseudonym = sc.next();
        }
        while (pseudonym.isEmpty());
    }

    private static void generatePseudonym()
    {
        sessionID = ThreadLocalRandom.current().nextInt(0,131072);
        X509Certificate cert = DominoesCC.getCert();
        Map<String, Key> keys = DominoesCC.getKeys(cert);
        pseudonym = Base64.getEncoder().encodeToString(DominoesSignature.sign(sessionID, keys.get("privateKey")));
    }
}
