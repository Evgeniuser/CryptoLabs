package Lab3;


import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import static Lab1.MyMath.*;
import static java.lang.Math.random;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.probablePrime;

public class RSA_agent
{

    private BigInteger C;
    private BigInteger Fi;

    private BigInteger Q;
    private BigInteger P;

    private SecureRandom rnd = new SecureRandom();

    public BigInteger D;
    public BigInteger N;
    public String name;

    RSA_agent(String name,int sizeKey)
    {
        this.name = name;
        Q = new BigInteger(String.valueOf(probablePrime(sizeKey , rnd)));
        P = new BigInteger(String.valueOf(probablePrime(sizeKey , rnd)));

        while(true)
        {
            if(Q.compareTo(P)==0)
            {
                Q = new BigInteger(String.valueOf(probablePrime(sizeKey , rnd)));
                P = new BigInteger(String.valueOf(probablePrime(sizeKey , rnd)));
            }
            break;
        }

        N = Q.multiply(P);
        Fi = P.subtract(ONE).multiply(Q.subtract(ONE));
        D = new BigInteger(Fi.bitLength (),rnd);
        while(true)
        {
            if(Fi.gcd(D).equals (ONE))
                break;
            else
                D = new BigInteger(Fi.bitLength(),rnd);
        }


        C = D.modInverse(Fi);

        if(C.compareTo(ZERO)==0) C = C.add(Fi);
    }

    public BigInteger getN() {return N;}

    public BigInteger getD() {return D;}

    public BigInteger VerifySign(BigInteger D, BigInteger N, BigInteger msg)
    {
        return msg.modPow(D,N);
    }

    public BigInteger CreateSign(BigInteger msg)
    {
        return msg.modPow(this.C, this.N);
    }

}


