package Lab2;

import static java.lang.Math.random;
import static Lab1.MyMath.*;

public class Elgama_agent {

    private long X;
    long P;
    long G;
    long Y;
    private long k;
    String name;


    public Elgama_agent(long p, long g, String name) {
        P = p;
        G = g;
        X = 1+(long)(random()*(P-1));
        Y = modPow2(G,X,P);
        this.name = name;
    }

    public long CrtSessionKey()
    {
        k = (long)(random()*(P-2))+1;
        return modPow2(G,k,P);
    }

    public long Crypt(long msg,long Y)
    {
        return msg*(modPow2(Y,k,P))%P;
    }

    public long Decrypt(long r, long msg)
    {
        return msg*modPow2(r,P-1-X,P)%P;
    }

    public long getY() {
        return Y;
    }
}
