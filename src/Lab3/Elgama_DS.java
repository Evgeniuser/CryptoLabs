package Lab3;

import static Lab1.MyMath.AdvanceGcd;
import static Lab1.MyMath.gcd;
import static Lab1.MyMath.modPow2;
import static java.lang.Math.random;

public class Elgama_DS {

    private long X;
    long P;
    long G;
    long Y;
    private long k;
    String name;


    Elgama_DS(long p, long g, String name) {
        P = p;
        G = g;
        X = 1+(long)(random()*(P-1));
        Y = modPow2(G,X,P);
        this.name = name;
    }

    public long generateR()
    {
        k = (long)(random()*(P-2))+1;
        while(true)
        {
            if(gcd(P-1,k)==1) break;
            else k = (long)(random()*(P-2))+1;
        }
        return modPow2(G,k,P);
    }

    long CryptS(long msg,long r)
    {
        long S = (msg-(X*r))*AdvanceGcd(P-1,k)%(P-1);
        if(S<0) S+=(P-1);
        return S;
    }



}
