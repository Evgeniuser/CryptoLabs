package Lab2;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import java.util.ArrayList;
import java.util.Random;

import static Lab1.MyMath.isPrimitive;
import static Lab1.MyMath.modPow2;
import static java.lang.Math.random;
import static java.math.BigInteger.probablePrime;

public class HelpfulFunc {

    static Random rnd = new Random(17);

    public static ByteBuffer ReadFile(String FileName) throws IOException
    {
        RandomAccessFile as = new RandomAccessFile(FileName,"r");
        FileChannel fl = as.getChannel();
        System.out.println("Размер файла: " + fl.size()+ " байт(а)");
        ByteBuffer InBuffer = ByteBuffer.allocate((int)fl.size()+1);
        fl.read(InBuffer);
        fl.close();
        return InBuffer;
    }

    public static int WriteFile(String FileName,byte[] buffer,boolean wrFlg) throws IOException
    {
        FileOutputStream fout = new FileOutputStream(FileName);
        if(wrFlg == true)
            fout.write(buffer,0,buffer.length/2-1);
        else
            fout.write(buffer,0,buffer.length);

        fout.close();

        return 0;
    }

    static ArrayList<Long> GenPG(int BitLen)
    {
        ArrayList<Long> pair = new ArrayList<>();

        BigInteger a = new BigInteger(String.valueOf(probablePrime(BitLen,rnd)));

        long P = 2 * a.longValue() + 1;
        long G;

        while(true)
        {
            if(isPrimitive(P) == true)
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


}
