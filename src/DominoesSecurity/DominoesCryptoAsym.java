package DominoesSecurity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource.PSpecified;



/**
 * DominoesCryptoSym
 * Cipher and Decipher with Assymetric keys
 */
public class DominoesCryptoAsym {


    /**
     * Assymetrics Keys Generator
     * @return a Map with a public and a private key
     */
    public static Map<String, byte[]> GenerateAsymKeys() 
    {
        int keySize = 1024;

        KeyPairGenerator kpg = null;
        try 
        {
            kpg = KeyPairGenerator.getInstance("RSA");

        } catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Invalid Algorithm");
        }

        kpg.initialize(keySize);

        KeyPair keyPair = kpg.generateKeyPair();

        byte[] privateKey = keyPair.getPrivate().getEncoded();
        byte[] publicKey = keyPair.getPublic().getEncoded();

        Map<String, byte[]> keys = new HashMap<String, byte[]>();
        keys.put("private", privateKey);
        keys.put("public", publicKey);

        return keys;
    }


    /**
     * Method for ciphering a message
     * @param msg to cipher
     * @param key : asymetric key
     * @return Message ciphered
     */
    public static byte[] AsymCipher(Object msg, byte[] key) 
    {
        byte[] msgArray = Serializer.Serialize(msg);

        if (msgArray.length > 62) 
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ByteArrayInputStream in = new ByteArrayInputStream(msgArray);

            while (in.available() > 62) 
            {
                byte[] chunk = new byte[62];

                in.read(chunk, 0, 62);

                byte[] result = AsymCipherSplit(chunk, key);

                out.write(result, 0, result.length);
            }

            byte[] chunk = new byte[in.available()];

            in.read(chunk, 0, in.available());

            byte[] result = AsymCipherSplit(chunk, key);

            out.write(result, 0, result.length);

            return out.toByteArray();
        }

        return AsymCipherSplit(msgArray, key);
    }

    private static byte[] AsymCipherSplit(byte[] msg, byte[] key) 
    {
        PublicKey skey = null;

        try 
        {
            skey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key, "RSA"));

        } catch (InvalidKeySpecException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: KeySpec not valid");
        } catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Key Algorithm not valid");
        }

        Cipher cipher = null;
        try 
        {
            cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

        } catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Cipher Algorithm not valid");
        } catch (NoSuchPaddingException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Invalid padding used");
        }

        try 
        {
            cipher.init(Cipher.ENCRYPT_MODE, skey);

        } catch (InvalidKeyException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Invalid Key used");
        }

        byte[] cypherResult = null;

        try 
        {
            cypherResult = cipher.doFinal(msg);

        } catch (IllegalBlockSizeException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Couldn't finish ciphering ");
        } catch (BadPaddingException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Padding given is invalid");

        }

        return cypherResult;
    }

    /**
     * Method for deciphering a message
     * @param msg to decipher
     * @param key : assymetric key
     * @return Message deciphered
     */
    public static Object AsymDecipher(byte[] msg, byte[] key) 
    {
        if (msg.length > 128) 
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            ByteArrayInputStream in = new ByteArrayInputStream(msg);

            while (in.available() > 128) 
            {
                byte[] chunk = new byte[128];

                in.read(chunk, 0, 128);

                byte[] result = AsymDecipherSplit(chunk, key);

                out.write(result, 0, result.length);
            }

            byte[] chunk = new byte[in.available()];

            in.read(chunk, 0, in.available());

            byte[] result = AsymDecipherSplit(chunk, key);

            out.write(result, 0, result.length);

            return Serializer.Deserialize(out.toByteArray());
        }

        return Serializer.Deserialize(AsymDecipherSplit(msg, key));
    }

    private static byte[] AsymDecipherSplit(byte[] msg, byte[] key) 
    {
        PrivateKey skey = null;

        try 
        {
            skey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(key, "RSA"));

        } catch (InvalidKeySpecException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: KeySpec not valid");

        } catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Key Algorithm not valid");
        }

        Cipher cipher = null;

        try 
        {
            cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

        } catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Cipher Algorithm not valid");

        } catch (NoSuchPaddingException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Invalid padding used");
        }

        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-1"),
                PSpecified.DEFAULT);

        try 
        {
            cipher.init(Cipher.DECRYPT_MODE, skey, oaepParams);

        } catch (InvalidKeyException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Invalid Key used");

        } catch (InvalidAlgorithmParameterException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Invalid OAEO parameter used");
        }

        byte[] cypherResult = null;

        try
        {
            cypherResult = cipher.doFinal(msg);

        } catch (IllegalBlockSizeException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Couldn't finish deciphering ");

        } catch (BadPaddingException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Padding given is invalid");

        }

        return cypherResult;
    }
}
