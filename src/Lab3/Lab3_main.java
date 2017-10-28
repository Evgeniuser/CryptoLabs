package Lab3;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import static Lab2.HelpfulFunc.*;


public class Lab3_main {

    public static void main(String[] args) throws Exception

    {
        RSA_agent Alice = new RSA_agent("Alice");
        String filename = "test.txt";
        ByteBuffer ld = getHash(filename).position(0);
        BigInteger cryptMsg = Alice.DecryptMsg(new BigInteger(ld.array ()));
        BigInteger decryptMsg = Alice.EncryptMsg (Alice.D,Alice.N,cryptMsg);

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



    }

}
