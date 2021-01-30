import DominoesSecurity.DominoesCryptoAsym;
import DominoesSecurity.DominoesCryptoSym;

import java.util.Base64;
import java.util.Map;

public class TestMixedCIpher
{
    public static void main(String[] args)
    {
        Map<String, byte[]> keys1 = DominoesCryptoAsym.GenerateAsymKeys();
        byte[] deckDistributionPrivateKey1 = keys1.get("private");
        byte[] deckDistributionPublicKey1 = keys1.get("public");

        Map<String, byte[]> keys2 = DominoesCryptoAsym.GenerateAsymKeys();
        byte[] deckDistributionPrivateKey2 = keys2.get("private");
        byte[] deckDistributionPublicKey2 = keys2.get("public");

        String testing = "5|5";
        String ciphered = Base64.getEncoder().encodeToString(DominoesCryptoAsym.AsymCipher(testing, deckDistributionPublicKey1));
        ciphered = Base64.getEncoder().encodeToString(DominoesCryptoAsym.AsymCipher(Base64.getDecoder().decode(ciphered), deckDistributionPublicKey2));

        byte[] symKey = DominoesCryptoSym.GenerateSymKeys(Long.toString(System.currentTimeMillis()));

        ciphered = Base64.getEncoder().encodeToString(DominoesCryptoSym.SymCipher(Base64.getDecoder().decode(ciphered), symKey));
        ciphered = Base64.getEncoder().encodeToString((byte[]) DominoesCryptoSym.SymDecipher(Base64.getDecoder().decode(ciphered), symKey));

        ciphered = Base64.getEncoder().encodeToString((byte[]) DominoesCryptoAsym.AsymDecipher(Base64.getDecoder().decode(ciphered), deckDistributionPrivateKey2));
        String result = (String) DominoesCryptoAsym.AsymDecipher(Base64.getDecoder().decode(ciphered), deckDistributionPrivateKey1);

        System.out.println(result);
    }
}
