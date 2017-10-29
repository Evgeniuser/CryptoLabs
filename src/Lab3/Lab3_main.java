//TODO: implement saving a signature to a file

package Lab3;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.StringTokenizer;

import static Lab2.HelpfulFunc.*;


public class Lab3_main {

    public static void main(String[] args) throws Exception

    {
        RSA_agent Alice = new RSA_agent("Alice",256);
        RSA_agent Bob = new RSA_agent ("Bob",256);

        GOST_DS A = new GOST_DS(GOSTgenParams(512));

        BigInteger[] pair = GenPG2(128);
        Elgama_DS TST1 = new Elgama_DS(pair[0],pair[1]);
        Elgama_DS TST2 = new Elgama_DS(pair[0],pair[1]);

        String filename = "test.txt";
        ByteBuffer ld = getHash(filename).position(0);

        BigInteger cryptMsg = Alice.DecryptMsg(new BigInteger(ld.array()));

        WriteFile ("RSA_sign",cryptMsg.toString ());

        cryptMsg = new BigInteger (ReadFile("RSA_sign"));
        BigInteger decryptMsg = Bob.EncryptMsg (Alice.getD(),Alice.getN(),cryptMsg);
        StringBuffer hexString = new StringBuffer();

        byte[] dataByte = decryptMsg.toByteArray ();

        for(int i = 0;i<dataByte.length;i++)
        {
            hexString.append(Integer.toHexString(0xFF & dataByte[i]));
        }

        System.out.println("Hex format : " + hexString.toString());
        System.out.println ("RSA test: " + decryptMsg.equals(new BigInteger(ld.array ())));



        BigInteger[] RS = A.CreateSign(new BigInteger(ld.array()));
        String test = RS[0].toString() + "\t" + RS[1].toString();

        WriteFile("GostSign",test);
        test = ReadFile("GostSign");

        StringTokenizer st = new StringTokenizer (test,"\n\t., ");
        BigInteger[] Q = new BigInteger[2];
        int i = 0;
        while(st.hasMoreTokens()){
            Q[i] = new BigInteger(st.nextToken ());
            i++;
        }

        System.out.println("GOST test: " + A.VerifySign(RS,new BigInteger(ld.array())));
        System.out.println("GOST-FILE test: " + A.VerifySign(Q,new BigInteger(ld.array())));


        BigInteger[] crypt = TST1.CreateSign(new BigInteger(ld.array ()));
        test = crypt[0].toString() + "\t" + crypt[1].toString();
        WriteFile("ElgamaSign",test);
        test = ReadFile("ElgamaSign");

        st = new StringTokenizer (test,"\n\t., ");
        Q = new BigInteger[2];
        i = 0;
        while(st.hasMoreTokens()){
            Q[i] = new BigInteger(st.nextToken ());
            i++;
        }
        boolean ts = TST2.VerifySign (new BigInteger(ld.array ()),crypt,TST1.getY ());
        System.out.println ("Elgama test: " + ts);
        ts = TST2.VerifySign (new BigInteger(ld.array ()),Q,TST1.getY ());
        System.out.println ("Elgama file-test: " + ts);
    }

}
