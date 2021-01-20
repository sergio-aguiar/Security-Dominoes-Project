package DominoesSecurity;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignedObject;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class DominoesCC {

    private static KeyStore keyStore = null;
    private static Provider provider = null;

    static{
        provider = Security.getProvider("SunPKCS11");
        provider = provider.configure("src/DominoesSecurity/CitizenCard.cfg");
        Security.addProvider(provider);

        try {
            keyStore = KeyStore.getInstance("PKCS11", "SunPKCS11-PTeID");

        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            keyStore.load(null, null);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Map<String, Key> getKeys(X509Certificate cert) {

        PublicKey publicKey = null;
        PrivateKey privateKey = null;       

        try {
            privateKey = (PrivateKey) keyStore.getKey("CITIZEN AUTHENTICATION CERTIFICATE", ("").toCharArray());
        } catch (UnrecoverableKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        publicKey = cert.getPublicKey();

        Map<String, Key> keys = new HashMap<String, Key>();

        keys.put("publicKey", publicKey);
        keys.put("privateKey", privateKey);

        return keys;
    }

    public static int getSerial(X509Certificate cert) {

        return cert.getSerialNumber().intValue();
    }

    private static X509Certificate getCert() {

        X509Certificate cert = null;
        try {
            cert = (X509Certificate) keyStore.getCertificate("CITIZEN AUTHENTICATION CERTIFICATE");
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return cert;
    }

}
