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

public class DominoesSignature {

    public static byte[] sign(Serializable data, Key privateKey) {
        Signature signatureEngine = null;

        try {
            signatureEngine = Signature.getInstance("SHA256withRSA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SignedObject result = null;
        try {
            result = new SignedObject(data, (PrivateKey) privateKey, signatureEngine);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SignatureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Serializer.Serialize(result);
    }
    
    public static boolean isValid(byte[] data, Key publicKey ){
    
        SignedObject signedObject = (SignedObject)Serializer.Deserialize(data);

        PublicKey pKey = (PublicKey) publicKey;

        Signature signatureEngine = null;
        try {
            signatureEngine = Signature.getInstance("SHA256withRSA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            signatureEngine.initVerify(pKey);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        boolean isVerify = false;
        try {
            isVerify = signedObject.verify(pKey, signatureEngine);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SignatureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return isVerify;
    
    }



}
