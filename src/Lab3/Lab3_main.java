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
        ByteBuffer fld = ByteBuffer.allocate(ld.limit()*8);
        int t = ld.capacity();

        long tst;
        do {

            tst = (long)ld.get();
            fld.putLong(Alice.DecryptMsg(tst));
            t--;
        }while(t>0);
        fld.position(0);
        t = fld.limit()/8;

        ByteBuffer df = ByteBuffer.allocate(ld.capacity());

        do {
            tst = fld.getLong();
            df.put((byte)Alice.EncryptMsg(Alice.D,Alice.N,tst));
            t--;
        }while(t>0);

        StringBuffer hexString = new StringBuffer();
        byte[] dataByte =  df.array();

        for(int i = 0;i<dataByte.length;i++)
        {

            hexString.append(Integer.toHexString(0xFF & dataByte[i]));
        }
        System.out.println("Hex format : " + hexString.toString());

        GOST_DS A = new GOST_DS(GOSTgenParams(512));
        BigInteger[] RS = A.CreateSign(new BigInteger(ld.array()));
        System.out.println(A.VerifySign(RS,new BigInteger(ld.array())));



    }

}
