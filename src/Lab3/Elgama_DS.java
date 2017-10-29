
package Lab3;

import java.math.BigInteger;
import java.security.SecureRandom;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class Elgama_DS {

    BigInteger P;
    BigInteger G;
    BigInteger Y;

    private BigInteger X;
    private SecureRandom Srnd = new SecureRandom();
    private BigInteger Fi;
    private BigInteger k;
    private BigInteger r;
    BigInteger invk;

    Elgama_DS(BigInteger p,BigInteger g)
    {
        this.P = p;
        this.G = g;

        X = new BigInteger (P.subtract(ONE).bitLength (),Srnd);
        Y = G.modPow(X,P);
        Fi = P.subtract(ONE);
    }
    private void CrtRK()
    {
        k = new BigInteger(Fi.bitLength (),Srnd);
        while(k.gcd(P).compareTo(ONE)!=0)
        {
            k = new BigInteger(Fi.bitLength (),Srnd);
        }

    }

    public BigInteger[] CreateSign(BigInteger msg)
    {
        BigInteger[] signPair = new BigInteger[2];

        gen:

        try {
            CrtRK();
            invk = k.modInverse(Fi);
        }
        catch (ArithmeticException e)
        {
            System.out.println(e);
            CrtRK();
            break gen;
        }

        r = G.modPow(k,P);
        BigInteger s = msg.subtract(X.multiply(r))
                          .multiply(invk)
                          .mod(Fi);

        if(s.compareTo(ZERO)==0) s.add(Fi);

        signPair[0] = r;
        signPair[1] = s;
        return signPair;
    }

    public boolean VerifySign(BigInteger msg,BigInteger[] pair,BigInteger Y)
    {
        BigInteger Check = Y.modPow(pair[0],P).multiply(pair[0].modPow(pair[1],P)).mod(P);
        return Check.equals(G.modPow(msg,P));
    }

    public BigInteger getY() {
        return Y;
    }
}
