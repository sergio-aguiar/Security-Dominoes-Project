package Testing;

import java.io.IOException;
import java.security.InvalidKeyException;
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
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class Card {

    public static void main(String[] args) {

        Provider p = Security.getProvider("SunPKCS11");
        p = p.configure("/home/pioavenger/Desktop/Repositories/Security-Dominoes-Project/src/main/java/Testing/CitizenCard.cfg");
        Security.addProvider(p);

        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("PKCS11", "SunPKCS11-PTeID");
        } catch (KeyStoreException | NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            ks.load(null, null);
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String data = "olá tudo bem";

        PrivateKey privateKey = null;
        Certificate certificate = null;
        PublicKey publicKey = null;

        try {
            privateKey = (PrivateKey) ks.getKey("CITIZEN AUTHENTICATION CERTIFICATE", null);
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.out.println("PrivateKey: " + privateKey);

        try {
            certificate = ks.getCertificate("CITIZEN AUTHENTICATION CERTIFICATE");
        } catch (KeyStoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // System.out.println("Begin Certificate");

        // System.out.println(certificate);

        // System.out.println("End Certificate");

        publicKey = certificate.getPublicKey();

        System.out.println("PublicKey: " + publicKey);

        Signature sig = null;
        try {
            sig = Signature.getInstance("SHA256withRSA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            sig.initSign(privateKey);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            sig.update(data.getBytes());
        } catch (SignatureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        byte[] result = null;
        try {
            result = sig.sign();
        } catch (SignatureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(result);

        boolean isVerify = false;

        try {
            sig.initVerify(publicKey);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            isVerify = sig.verify(result);
        } catch (SignatureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(isVerify);
    }
}