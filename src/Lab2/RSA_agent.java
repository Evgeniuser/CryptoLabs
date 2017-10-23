package Lab2;


import java.math.BigInteger;
import java.util.Random;


import static Lab1.MyMath.*;


import static java.lang.Math.random;
import static java.math.BigInteger.probablePrime;

public class RSA_agent
{
    private long P;
    private long Q;
    private long C;
    private long Fi;

    public long N;
    public long D;
    public String name;

    RSA_agent(String name)
    {
        this.name = name;

        Random rnd = new Random();
        BigInteger a = new BigInteger(String.valueOf(probablePrime(16 , rnd)));
        P = a.longValue();
        Q = a.longValue();

        while(true)
        {
            if(P == Q)
            {
                a = new BigInteger(String.valueOf(probablePrime(16, rnd)));
                Q = a.longValue();
            }
            break;
        }

        N = P*Q;
        Fi = (P-1)*(Q-1);
        D = (long)(random() * Fi) + 1;
        while(true)
        {
            if(gcd(Fi,D) == 1)
                break;
            else
                D = (long)(random() * Fi) + 1;
        }


        C = AdvanceGcd(Fi,D);

        if(C<0) C = C+Fi;
    }

    public long getN() {return N;}

    public long getD() {return D;}

    public long EncryptMsg(long D, long N, long msg)
    {
        return modPow2(msg,D,N);
    }

    public long DecryptMsg(long msg)
    {
        return modPow2(msg, this.C, this.N);
    }

}


