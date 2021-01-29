import DominoesSecurity.DominoesCryptoAsym;

import java.util.Map;

public class CryptoAsymTesting
{
    public static void main(String[] args)
    {
        Map<String, byte[]> keys = DominoesCryptoAsym.GenerateAsymKeys();
        byte[] deckDistributionPrivateKey = keys.get("private");
        byte[] deckDistributionPublicKey = keys.get("public");

        String name = "Nabo";
        byte[] ciphered = DominoesCryptoAsym.AsymCipher(name, deckDistributionPrivateKey);
        String plain = (String) DominoesCryptoAsym.AsymDecipher(ciphered, deckDistributionPublicKey);

        System.out.println(plain);
    }
}
