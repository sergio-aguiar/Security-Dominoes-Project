package DominoesSecurity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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

/**
 * DominoesCryptoSym
 * Cipher and Decipher with Symetric keys
 * @author Ricardo Rodrigues Azevedo
 * @version 1.0
 * @since 2020-12-18
 */
public class DominoesCryptoSym {


    /**
     * Symetric key generator
     * @param pwd
     * @return SymetricKey
     */
    public static byte[] GenerateSymKeys(String pwd) 
    {
        int keySize = 256;

        String alg = "PBKDF2WithHmacSHA256";

        PBEKeySpec pbeKeySpec = new PBEKeySpec(pwd.toCharArray(), GenerateSalt(), 1000, keySize);

        SecretKeyFactory skf = null;
        try 
        {
            skf = SecretKeyFactory.getInstance(alg);

        } catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Algoritm not valid");
        }

        byte[] key = new byte[keySize];
        try 
        {
            key = skf.generateSecret(pbeKeySpec).getEncoded();

        } catch (InvalidKeySpecException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR:Key Specifications invalid");
        }

        return key;
    }
    
    private static byte[] GenerateSalt() 
    {
        Random rd = new Random();

        byte[] salt = new byte[32];

        rd.nextBytes(salt);

        return salt;
    }

    /**
     * Method for ciphering a message
     * @param msg to cipher
     * @param key : symetric key
     * @return Message ciphered
     */
    public static byte[] SymCipher(Object msg, byte[] key) 
    {
        byte[] msgArray = Serializer.Serialize(msg);

        SecretKey skey = new SecretKeySpec(key, "AES");

        Cipher c = null;
        try 
        {
            c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Algoritm not valid");
        } catch (NoSuchPaddingException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Padding used is invalid");
        }

        byte[] iv = new byte[c.getBlockSize()];

        SecureRandom rd = new SecureRandom();

        rd.nextBytes(iv);

        try 
        {
            c.init(Cipher.ENCRYPT_MODE, skey, new IvParameterSpec(iv));
        } catch (InvalidKeyException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Ivalid key used");
        } catch (InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
            System.err.println("ERROR: Invalid parameter used");
        }

        ByteArrayInputStream in = new ByteArrayInputStream(msgArray);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        out.write(iv, 0, c.getBlockSize());

        return ReadMessage(in, out, c);
    }

    /**
     * Method for deciphering a certain message
     * @param msg to decipher
     * @param key : simetric key
     * @return Message deciphered
     */
    public static Object SymDecipher(byte[] msg, byte[] key) 
    {
        SecretKey skey = new SecretKeySpec(key, "AES");

        Cipher cipher = null;
        try 
        {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Algoritm not valid");
        } catch (NoSuchPaddingException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Padding used is invalid");
        }

        ByteArrayInputStream in = new ByteArrayInputStream(msg);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] iv = new byte[cipher.getBlockSize()];

        try 
        {
            in.read(iv);
        } catch (IOException e1) 
        {
            e1.printStackTrace();
            System.err.println("ERROR: reading iv");
        }

        try 
        {
            cipher.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(iv));
        } catch (InvalidKeyException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Ivalid key used");
        } catch (InvalidAlgorithmParameterException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Invalid parameter used");
        }

        return Serializer.Deserialize(ReadMessage(in, out, cipher));
    }


    private static byte[] ReadMessage(ByteArrayInputStream in, ByteArrayOutputStream out, Cipher c)
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
        } catch (IllegalBlockSizeException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Couldn't finish deciphering");
        } catch (BadPaddingException e) {
            e.printStackTrace();
            System.err.println("ERROR: Padding given is invalid");
        }

        out.write(ciphertext, 0, ciphertext.length);

        return out.toByteArray();
    }   

}