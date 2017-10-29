package Lab2;


import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;


import java.security.MessageDigest;
import static Lab1.MyMath.isPrimitive;
import static Lab1.MyMath.modPow2;
import static java.lang.Math.random;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;
import static java.math.BigInteger.probablePrime;

public class HelpfulFunc {

    private static Random rnd = new Random(17);
    private static SecureRandom Srandom = new SecureRandom();

    public static ByteBuffer ReadFile(String FileName,boolean isSign) throws IOException
    {
        RandomAccessFile as = new RandomAccessFile(FileName,"r");
        FileChannel fl = as.getChannel();
        ByteBuffer InBuffer;
        if(!isSign)
        {
            InBuffer = ByteBuffer.allocate ((int) fl.size () + 1);
            fl.read(InBuffer);
            fl.close();
            InBuffer.position (0);
        }
        else {
            InBuffer = ByteBuffer.allocate ((int) fl.size());
            fl.read(InBuffer);
            fl.close();
            InBuffer.position (0);
        }

        return InBuffer;
    }
    public static String ReadFile(String FileName) throws IOException
    {

        BufferedReader in = new BufferedReader(new FileReader (FileName));
        String Buffer;
        Buffer = in.readLine ();
        return Buffer;
    }

    public static int WriteFile(String FileName,byte[] buffer,boolean wrFlg) throws IOException
    {
        FileOutputStream fout = new FileOutputStream(FileName);
        if(wrFlg)
            fout.write(buffer,0,buffer.length/2-1);
        else
            fout.write(buffer,0,buffer.length-1);

        fout.close();

        return 0;
    }

    public static void WriteFile(String FileName,String buffer) throws IOException
    {
        FileOutputStream fout = new FileOutputStream(FileName);
        BufferedWriter out = new BufferedWriter(new FileWriter(FileName));
        out.write(buffer);
        out.close();
    }

    static ArrayList<Long> GenPG(int BitLen)
    {
        ArrayList<Long> pair = new ArrayList<>();

        BigInteger a = new BigInteger(String.valueOf(probablePrime(BitLen,rnd)));

        long P = 2 * a.longValue() + 1;
        long G;

        while(true)
        {
            if(isPrimitive(P))
            {
                G = 1+(long)(random()*(P-1));

                while(true)
                {
                    if(modPow2(G,a.longValue(),P) != 1)
                        break;
                    else
                        G = 1+(long)(random()*(P-1));
                }

                break;
            }

            else
            {
                a = new BigInteger(String.valueOf(probablePrime(BitLen,rnd)));
                P = 2 * a.longValue() + 1;
            }
        }
        pair.add(P);
        pair.add(G);
        return pair;
    }
    public static ByteBuffer getHash(String filename) throws Exception
    {
        ByteBuffer HashStr;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        FileInputStream fl = new FileInputStream(filename);

        byte[] dataByte = new byte[fl.available()];
        fl.read(dataByte);
        md.update(dataByte,0,dataByte.length);
        dataByte = md.digest();
        HashStr = ByteBuffer.wrap(dataByte);
        StringBuffer hexString = new StringBuffer();
        for (byte aDataByte : dataByte) {
            HashStr.put(aDataByte);
            hexString.append(Integer.toHexString(0xFF & aDataByte));
        }
        System.out.println("Hex format : " + hexString.toString());

        return HashStr;
    }

    //Procedure A
    private static int procedure_A(int x0, int c, BigInteger[] pq, int size)
    {
        //Verify and perform condition: 0<x<2^16; 0<c<2^16; c - odd.
        while (x0 < 0 || x0 > 65536) x0 = Srandom.nextInt() / 32768;

        while ((c < 0 || c > 65536) || (c / 2 == 0)) {
            c = Srandom.nextInt() / 32768 + 1;
        }

        BigInteger C = new BigInteger(Integer.toString(c));
        BigInteger constA16 = new BigInteger("19381");

        //step1
        BigInteger[] y = new BigInteger[1]; // begin length = 1
        y[0] = new BigInteger(Integer.toString(x0));

        //step 2
        int[] t = new int[1]; // t - orders; begin length = 1
        t[0] = size;
        int s = 0;
        for (int i = 0; t[i] >= 17; i++) {
            // extension array t
            int tmp_t[] = new int[t.length + 1];
            System.arraycopy(t, 0, tmp_t, 0, t.length);
            t = new int[tmp_t.length];
            System.arraycopy(tmp_t, 0, t, 0, tmp_t.length);

            t[i + 1] = t[i] / 2;
            s = i + 1;
        }

        //step3
        BigInteger p[] = new BigInteger[s + 1];
        p[s] = new BigInteger("8003", 16); //set min prime number length 16 bit

        int m = s - 1;  //step4

        for (int i = 0; i < s; i++) {
            int rm = t[m] / 16;  //step5

            step6:
            for (; ; ) {
                //step 6
                BigInteger tmp_y[] = new BigInteger[y.length];
                System.arraycopy(y, 0, tmp_y, 0, y.length);
                y = new BigInteger[rm + 1];
                System.arraycopy(tmp_y, 0, y, 0, tmp_y.length);

                for (int j = 0; j < rm; j++) {
                    y[j + 1] = (y[j].multiply(constA16).add(C)).mod(TWO.pow(16));
                }

                //step 7
                BigInteger Ym = new BigInteger("0");
                for (int j = 0; j < rm; j++) {
                    Ym = Ym.add(y[j].multiply(TWO.pow(16 * j)));
                }

                y[0] = y[rm]; //step 8

                //step 9
                BigInteger N = TWO.pow(t[m] - 1).divide(p[m + 1]).
                        add((TWO.pow(t[m] - 1).multiply(Ym)).
                                divide(p[m + 1].multiply(TWO.pow(16 * rm))));

                if (N.mod(TWO).compareTo(ONE) == 0) {
                    N = N.add(ONE);
                }

                int k = 0; //step 10

                step11:
                for (; ; ) {
                    //step 11
                    p[m] = p[m + 1].multiply(N.add(BigInteger.valueOf(k))).add(ONE);

                    if (p[m].compareTo(TWO.pow(t[m])) == 1) {
                        continue step6; //step 12
                    }

                    //step13
                    if ((TWO.modPow(p[m + 1].multiply(N.add(BigInteger.valueOf(k))), p[m]).compareTo(ONE) == 0) &&
                            (TWO.modPow(N.add(BigInteger.valueOf(k)), p[m]).compareTo(ONE) != 0)) {
                        m -= 1;
                        break;
                    } else {
                        k += 2;
                        continue step11;
                    }
                }

                if (m >= 0) {
                    break; //step 14
                } else {
                    pq[0] = p[0];
                    pq[1] = p[1];
                    return y[0].intValue(); //return for procedure B step 2
                }
            }
        }
        return y[0].intValue();
    }

    //Procedure B
    private static void procedure_B(int x0, int c, BigInteger[] pq)
    {
        //Verify and perform condition: 0<x<2^16; 0<c<2^16; c - odd.
        while(x0<0 || x0>65536)
        {
            x0 = Srandom.nextInt()/32768;
        }

        while((c<0 || c>65536) || (c/2==0))
        {
            c = Srandom.nextInt()/32768 + 1;
        }

        BigInteger [] qp = new BigInteger[2];
        BigInteger q, Q, p;
        BigInteger C = new BigInteger(Integer.toString(c));
        BigInteger constA16 = new BigInteger("19381");

        //step1
        x0 = procedure_A(x0, c, qp, 256);
        q = qp[0];

        //step2
        x0 = procedure_A(x0, c, qp, 512);
        Q = qp[0];

        BigInteger[] y = new BigInteger[65];
        y[0] = new BigInteger(Integer.toString(x0));

        int tp = 1024;

        step3: for(;;)
        {
            //step 3
            for (int j=0; j<64; j++)
            {
                y[j+1] = (y[j].multiply(constA16).add(C)).mod(TWO.pow(16));
            }

            //step 4
            BigInteger Y = new BigInteger("0");

            for (int j=0; j<64; j++)
            {
                Y = Y.add(y[j].multiply(TWO.pow(16*j)));
            }

            y[0] = y[64]; //step 5

            //step 6
            BigInteger N = TWO.pow(tp-1).divide(q.multiply(Q)).
                    add((TWO.pow(tp-1).multiply(Y)).
                            divide(q.multiply(Q).multiply(TWO.pow(1024))));

            if (N.mod(TWO).compareTo(ONE)==0)
            {
                N = N.add(ONE);
            }

            int k = 0; //step 7

            step8: for(;;)
            {
                //step 11
                p = q.multiply(Q).multiply(N.add(BigInteger.valueOf(k))).add(ONE);

                if (p.compareTo(TWO.pow(tp))==1)
                {
                    continue step3; //step 9
                }

                //step10
                if ((TWO.modPow(q.multiply(Q).multiply(N.add(BigInteger.valueOf(k))),p).compareTo(ONE)==0) &&
                        (TWO.modPow(q.multiply(N.add(BigInteger.valueOf(k))),p).compareTo(ONE)!=0))
                {
                    pq[0] = p;
                    pq[1] = q;
                    return;
                }
                else
                {
                    k += 2;
                    continue step8;
                }
            }
        }
    }

    //procedure C
    private static BigInteger procedure_C(BigInteger p, BigInteger q)
    {
        BigInteger pSub1 = p.subtract(ONE);
        BigInteger pSub1DivQ = pSub1.divide(q);
        int length = p.bitLength();

        for(;;)
        {
            BigInteger d = new BigInteger(length, Srandom);

            // 1 < d < p-1
            if (d.compareTo(ONE) > 0 && d.compareTo(pSub1) < 0)
            {
                BigInteger a = d.modPow(pSub1DivQ, p);

                if (a.compareTo(ONE) != 0)
                {
                    return a;
                }
            }
        }
    }

    public static BigInteger[] GOSTgenParams(int size)
    {

        BigInteger[] pqa = new BigInteger[3];
        BigInteger q,p;

        int  x0, c;


        x0 = Srandom.nextInt();
        c  = Srandom.nextInt();
        if(size==512)
            procedure_A(x0, c, pqa,512);
        else if(size == 1024)
            procedure_B(x0,c,pqa);

        p = pqa[0];  q = pqa[1];
        pqa[2] = procedure_C(p, q);

        return pqa;
    }

    public static BigInteger[] GenPG2(int bitLen)
    {
        BigInteger[] pair = new BigInteger[2];

        BigInteger P;
        BigInteger G;
        BigInteger Q;

        Q = BigInteger.probablePrime (bitLen,Srandom);
        P = Q.multiply(TWO).add(ONE);

        while(!P.isProbablePrime (50))
        {
            Q = BigInteger.probablePrime (bitLen,Srandom);
            P = Q.multiply(TWO).add(ONE);
        }

        G = new BigInteger(P.subtract(ONE).bitLength (),Srandom);

        while(G.modPow (Q,P).compareTo (ONE)==0)
        {
            G = new BigInteger(P.subtract(ONE).bitLength (),Srandom);
        }

        pair[0] = P;
        pair[1] = G;

        return pair;
    }
}
