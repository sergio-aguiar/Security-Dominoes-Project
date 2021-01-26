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
        int sessionID = ThreadLocalRandom.current().nextInt(0,131072);

        X509Certificate cert = DominoesCC.getCert();
        Map<String, Key> keys = DominoesCC.getKeys(cert);

        String pseudonym = generatePseudonym(sessionID, keys.get("privateKey"));

        String serialID = cert.getSerialNumber().toString();
        if (!dcStub.isUserRegistered(serialID)) dcStub.registerUser(serialID);

        DCThread dcThread = new DCThread(pseudonym, keys.get("privateKey"), keys.get("publicKey"), sessionID, dcStub);
        dcThread.start();
    }

    public static String generatePseudonym(int sessionID, Key privateKey)
    {
        return Base64.getEncoder().encodeToString(DominoesSignature.sign(sessionID, privateKey));
    }
}
