package DominoesSecurity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

public class DominoesCryptoAsym
{
    public static Map<String, byte[]> generateAsymKeys()
    {
        int keySize = 1024;

        KeyPairGenerator kpg = null;
        try 
        {
            kpg = KeyPairGenerator.getInstance("RSA");
        }
        catch (Exception e)
        {
            System.out.println("DominoesCryptoAsym: generateAsymKeys: " + e.toString());
        }

        Map<String, byte[]> keys = null;
        try
        {
            kpg.initialize(keySize);

            KeyPair keyPair = kpg.generateKeyPair();

            byte[] privateKey = keyPair.getPrivate().getEncoded();
            byte[] publicKey = keyPair.getPublic().getEncoded();

            keys = new HashMap<String, byte[]>();
            keys.put("private", privateKey);
            keys.put("public", publicKey);
        }
        catch (Exception e)
        {
            System.out.println("DominoesCryptoAsym: generateAsymKeys: " + e.toString());
        }

        return keys;
    }

    public static byte[] asymCipher(Object msg, byte[] key)
    {
        byte[] msgArray = Serializer.serialize(msg);

        if (msgArray.length > 62) 
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(msgArray);

            while (in.available() > 62) 
            {
                byte[] chunk = new byte[62];
                in.read(chunk, 0, 62);
                byte[] result = asymCipherSplit(chunk, key);
                out.write(result, 0, result.length);
            }

            byte[] chunk = new byte[in.available()];
            in.read(chunk, 0, in.available());
            byte[] result = asymCipherSplit(chunk, key);
            out.write(result, 0, result.length);

            return out.toByteArray();
        }

        return asymCipherSplit(msgArray, key);
    }

    private static byte[] asymCipherSplit(byte[] msg, byte[] key)
    {
        PublicKey skey = null;

        try 
        {
            skey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key, "RSA"));

        }
        catch (InvalidKeySpecException | NoSuchAlgorithmException e)
        {
            System.out.println("DominoesCryptoAsym: asymCipherSplit: " + e.toString());
        }

        Cipher cipher = null;
        try 
        {
            cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e)
        {
            System.out.println("DominoesCryptoAsym: asymCipherSplit: " + e.toString());
        }

        try 
        {
            cipher.init(Cipher.ENCRYPT_MODE, skey);
        }
        catch (Exception e)
        {
            System.out.println("DominoesCryptoAsym: asymCipherSplit: " + e.toString());
        }

        byte[] cypherResult = null;

        try 
        {
            cypherResult = cipher.doFinal(msg);
        }
        catch (IllegalBlockSizeException | BadPaddingException e)
        {
            System.out.println("DominoesCryptoAsym: asymCipherSplit: " + e.toString());
        }

        return cypherResult;
    }

    public static Object asymDecipher(byte[] msg, byte[] key)
    {
        if (msg.length > 128) 
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(msg);

            while (in.available() > 128) 
            {
                byte[] chunk = new byte[128];
                in.read(chunk, 0, 128);
                byte[] result = asymDecipherSplit(chunk, key);
                out.write(result, 0, result.length);
            }

            byte[] chunk = new byte[in.available()];
            in.read(chunk, 0, in.available());
            byte[] result = asymDecipherSplit(chunk, key);
            out.write(result, 0, result.length);

            return Serializer.deserialize(out.toByteArray());
        }

        return Serializer.deserialize(asymDecipherSplit(msg, key));
    }

    private static byte[] asymDecipherSplit(byte[] msg, byte[] key)
    {
        PrivateKey skey = null;

        try 
        {
            skey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(key, "RSA"));

        }
        catch (InvalidKeySpecException | NoSuchAlgorithmException e)
        {
            System.out.println("DominoesCryptoAsym: asymCipherSplit: " + e.toString());
        }

        Cipher cipher = null;

        try 
        {
            cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e)
        {
            System.out.println("DominoesCryptoAsym: asymCipherSplit: " + e.toString());
        }

        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1",
                new MGF1ParameterSpec("SHA-1"), PSpecified.DEFAULT);

        try 
        {
            cipher.init(Cipher.DECRYPT_MODE, skey, oaepParams);

        }
        catch (Exception e)
        {
            System.out.println("DominoesCryptoAsym: asymCipherSplit: " + e.toString());
        }

        byte[] cypherResult = null;

        try
        {
            cypherResult = cipher.doFinal(msg);
        }
        catch (IllegalBlockSizeException | BadPaddingException e)
        {
            System.out.println("DominoesCryptoAsym: asymCipherSplit: " + e.toString());
        }

        return cypherResult;
    }
}
