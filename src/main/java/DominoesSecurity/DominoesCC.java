package DominoesSecurity;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class DominoesCC {

    private static KeyStore keyStore = null;

    static
    {
        Provider provider = Security.getProvider("SunPKCS11");
        provider = provider.configure("src/main/java/DominoesSecurity/CitizenCard.cfg");
        Security.addProvider(provider);

        try
        {
            keyStore = KeyStore.getInstance("PKCS11", "SunPKCS11-PTeID");
        }
        catch (KeyStoreException | NoSuchProviderException e)
        {
            System.out.println("DominoesCC: init: " + e.toString());
        }

        try
        {
            keyStore.load(null, null);
        }
        catch (NoSuchAlgorithmException | CertificateException | IOException e)
        {
            System.out.println("DominoesCC: init: " + e.toString());
        }
    }

    public static Map<String, Key> getKeys(X509Certificate cert) {

        PublicKey publicKey;
        PrivateKey privateKey = null;       

        try
        {
            privateKey = (PrivateKey) keyStore.getKey("CITIZEN AUTHENTICATION CERTIFICATE", ("").toCharArray());
        }
        catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e)
        {
            System.out.println("DominoesCC: init: " + e.toString());
        }

        publicKey = cert.getPublicKey();

        Map<String, Key> keys = new HashMap<>();

        keys.put("publicKey", publicKey);
        keys.put("privateKey", privateKey);

        return keys;
    }

    public static X509Certificate getCert()
    {
        X509Certificate cert = null;
        try
        {
            cert = (X509Certificate) keyStore.getCertificate("CITIZEN AUTHENTICATION CERTIFICATE");
        }
        catch (KeyStoreException e)
        {
            System.out.println("DominoesCC: init: " + e.toString());
        }

        return cert;
    }
}
