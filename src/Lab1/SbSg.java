package Lab1;

import java.util.*;


import static java.lang.Math.sqrt;
import static Lab1.MyMath.*;

public class SbSg {

    public static long Sbsg(long P,long G,long Y)
    {
        long K;
        long result;

        K = (long)sqrt(P)+1;
        HashMap<Long,Long> map = new HashMap<>();
        map.clear();
        for(long i = 1; i <= K; i++)
        {
            long key = modPow2(G,(i*K),P);
            map.put(key,i);
            System.out.println(key + " " + i);
        }

        System.out.println();

        for(long j = 1; j <=K; j++)
        {
            long x = Y*modPow2(G,j,P);
            System.out.println(x + " " + j + " " + map.containsKey(x));
            if(map.containsKey(x))
            {
                result = map.get(x)* K - j;
                return result;
            }
        }

        return -1;
    }








}
