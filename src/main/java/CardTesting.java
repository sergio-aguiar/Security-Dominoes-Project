import DominoesSecurity.DominoesCC;
import DominoesSecurity.DominoesCryptoAsym;
import DominoesSecurity.DominoesSignature;

import java.security.Key;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;

public class CardTesting
{
    public static void main(String[] args)
    {
        X509Certificate cert = DominoesCC.getCert();

        Map<String, Key>  keys = DominoesCC.getKeys(cert);
        System.out.println("Public key:" + Arrays.toString(keys.get("publicKey").getEncoded()));
        System.out.println("Private key:" + keys.get("privateKey").toString());

        String data = "\nHello, I'm not cyphered.";

        System.out.println("Plain: " + data);
        byte[] cyphered = DominoesSignature.sign(data, keys.get("privateKey"));
        System.out.println("Cyphered: " + Arrays.toString(cyphered));
        System.out.println("Valid sign: " + DominoesSignature.isValid(cyphered, keys.get("publicKey")));
    }
}
