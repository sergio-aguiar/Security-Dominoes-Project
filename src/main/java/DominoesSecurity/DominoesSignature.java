package DominoesSecurity;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.SignedObject;

public class DominoesSignature
{
    public static byte[] sign(Serializable data, Key privateKey)
    {
        Signature signatureEngine = null;

        try
        {
            signatureEngine = Signature.getInstance("SHA256withRSA");
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println("DominoesSignature: sign: " + e.toString());
        }

        SignedObject result = null;
        try
        {
            result = new SignedObject(data, (PrivateKey) privateKey, signatureEngine);
        }
        catch (InvalidKeyException | SignatureException | IOException e)
        {
            System.out.println("DominoesSignature: sign: " + e.toString());
        }

        return Serializer.serialize(result);
    }
    
    public static boolean isValid(byte[] data, Key publicKey ){
    
        SignedObject signedObject = (SignedObject)Serializer.deserialize(data);

        PublicKey pKey = (PublicKey) publicKey;

        Signature signatureEngine = null;
        try
        {
            signatureEngine = Signature.getInstance("SHA256withRSA");
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println("DominoesSignature: isValid: " + e.toString());
        }

        try
        {
            signatureEngine.initVerify(pKey);
        }
        catch (Exception e)
        {
            System.out.println("DominoesSignature: isValid: " + e.toString());
        }

        boolean isVerify = false;
        try
        {
            isVerify = signedObject.verify(pKey, signatureEngine);
        }
        catch (InvalidKeyException | SignatureException e)
        {
            System.out.println("DominoesSignature: isValid: " + e.toString());
        }

        return isVerify;
    }
}
