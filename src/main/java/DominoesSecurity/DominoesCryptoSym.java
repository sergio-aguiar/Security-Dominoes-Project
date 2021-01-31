package DominoesSecurity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import javax.crypto.SecretKeyFactory;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import java.security.SecureRandom;

public class DominoesCryptoSym
{
    public static byte[] generateSymKeys(String pwd)
    {
        int keySize = 256;

        String alg = "PBKDF2WithHmacSHA256";

        PBEKeySpec pbeKeySpec = new PBEKeySpec(pwd.toCharArray(), generateSalt(), 1000, keySize);

        SecretKeyFactory skf = null;
        try 
        {
            skf = SecretKeyFactory.getInstance(alg);
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println("DominoesCryptoSym: generateSymKeys: " + e.toString());
        }

        byte[] key = new byte[keySize];
        try 
        {
            key = skf.generateSecret(pbeKeySpec).getEncoded();

        }
        catch (Exception e)
        {
            System.out.println("DominoesCryptoSym: generateSymKeys: " + e.toString());
        }

        return key;
    }
    
    private static byte[] generateSalt()
    {
        Random rd = new Random();
        byte[] salt = new byte[32];
        rd.nextBytes(salt);
        return salt;
    }

    public static byte[] symCipher(Object msg, byte[] key)
    {
        byte[] msgArray = Serializer.serialize(msg);

        SecretKey skey = new SecretKeySpec(key, "AES");

        Cipher c = null;
        try 
        {
            c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e)
        {
            System.out.println("DominoesCryptoSym: symCipher: " + e.toString());
        }

        byte[] iv = new byte[c.getBlockSize()];
        SecureRandom rd = new SecureRandom();
        rd.nextBytes(iv);

        try 
        {
            c.init(Cipher.ENCRYPT_MODE, skey, new IvParameterSpec(iv));
        }
        catch (InvalidKeyException | InvalidAlgorithmParameterException e)
        {
            System.out.println("DominoesCryptoSym: symCipher: " + e.toString());
        }

        ByteArrayInputStream in = new ByteArrayInputStream(msgArray);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(iv, 0, c.getBlockSize());

        return readMessage(in, out, c);
    }

    public static Object symDecipher(byte[] msg, byte[] key)
    {
        SecretKey skey = new SecretKeySpec(key, "AES");

        Cipher cipher = null;
        try 
        {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e)
        {
            System.out.println("DominoesCryptoSym: symDecipher: " + e.toString());
        }

        ByteArrayInputStream in = new ByteArrayInputStream(msg);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] iv = new byte[cipher.getBlockSize()];

        try 
        {
            in.read(iv);
        }
        catch (IOException e)
        {
            System.out.println("DominoesCryptoSym: symDecipher: " + e.toString());
        }

        try 
        {
            cipher.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(iv));
        }
        catch (InvalidKeyException | InvalidAlgorithmParameterException e)
        {
            System.out.println("DominoesCryptoSym: symDecipher: " + e.toString());
        }

        return Serializer.deserialize(readMessage(in, out, cipher));
    }

    private static byte[] readMessage(ByteArrayInputStream in, ByteArrayOutputStream out, Cipher c)
    {
        byte[] ciphertext = null;
        int amount = 16;

        while(in.available() > amount)
        {
            byte[] msg_chunk = new byte[amount];
            in.read(msg_chunk,0 , amount);
            ciphertext = c.update(msg_chunk, 0, amount);
            out.write(ciphertext, 0 , ciphertext.length);
        }

        byte[] msg_chunk = new byte[in.available()];
        in.read(msg_chunk, 0 , in.available());

        try 
        {
            ciphertext = c.doFinal(msg_chunk);
        }
        catch (IllegalBlockSizeException | BadPaddingException e)
        {
            System.out.println("DominoesCryptoSym: readMessage: " + e.toString());
        }

        out.write(ciphertext, 0, ciphertext.length);

        return out.toByteArray();
    }   

}