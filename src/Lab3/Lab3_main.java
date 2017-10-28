//TODO: implement saving a signature to a file

package Lab3;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import static Lab2.HelpfulFunc.*;


public class Lab3_main {

    public static void main(String[] args) throws Exception

    {
        RSA_agent Alice = new RSA_agent("Alice",512);
        RSA_agent Bob = new RSA_agent ("Bob",512);
        String filename = "test.txt";
        ByteBuffer ld = getHash(filename).position(0);
        BigInteger cryptMsg = Alice.DecryptMsg(new BigInteger(ld.array ()));
        BigInteger decryptMsg = Bob.EncryptMsg (Alice.getD(),Alice.getN(),cryptMsg);

        StringBuffer hexString = new StringBuffer();

        byte[] dataByte = decryptMsg.toByteArray ();

        for(int i = 0;i<dataByte.length;i++)
        {
            hexString.append(Integer.toHexString(0xFF & dataByte[i]));
        }

        System.out.println("Hex format : " + hexString.toString());
        System.out.println ("RSA test: " + decryptMsg.equals(new BigInteger(ld.array ())));

        GOST_DS A = new GOST_DS(GOSTgenParams(512));
        BigInteger[] RS = A.CreateSign(new BigInteger(ld.array()));
        System.out.println("GOST test: " + A.VerifySign(RS,new BigInteger(ld.array())));

        BigInteger[] pair = GenPG2(128);
        Elgama_DS TST1 = new Elgama_DS(pair[0],pair[1]);
        Elgama_DS TST2 = new Elgama_DS(pair[0],pair[1]);

        BigInteger[] crypt = TST1.CreateSign(new BigInteger(ld.array ()));
        boolean ts = TST2.VerifySign (new BigInteger(ld.array ()),crypt,TST1.getY ());
        System.out.println ("Elgama test: " + ts);
    }

}
