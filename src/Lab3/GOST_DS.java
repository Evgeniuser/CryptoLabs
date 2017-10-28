package Lab3;


import java.math.BigInteger;
import java.security.SecureRandom;

public class GOST_DS {

    BigInteger P;
    BigInteger Q;
    BigInteger A;
    public BigInteger Y;
    private BigInteger X;
    private SecureRandom Srnd = new SecureRandom();

    GOST_DS(BigInteger[] PQA)
    {

        this.P = PQA[0];
        this.Q = PQA[1];
        this.A = PQA[2];
        X = new BigInteger(Q.bitLength(),Srnd);
        Y = A.modPow(X,P);
    }

    public BigInteger[] CreateSign(BigInteger Hm)
    {
        BigInteger k;

        do {
            k = new BigInteger(Q.bitLength(),Srnd);
        }while(k.compareTo(Q)>=0);

        BigInteger r = A.modPow(k, P).mod(Q);
        BigInteger s = X.multiply(r).add(k.multiply(Hm)).mod(Q);

        BigInteger[] SignMas = new BigInteger[2];

        SignMas[0] = r;
        SignMas[1] = s;

        System.out.println(r.bitLength() + "\t" + s.bitLength());
        return SignMas;
    }

    public boolean VerifySign(BigInteger[] RS,BigInteger Hm)
    {
        BigInteger R = RS[0];
        BigInteger S = RS[1];
        BigInteger v = Hm.modPow(Q.subtract(BigInteger.TWO),Q);
        BigInteger z1 = S.multiply(v).mod(Q);
        BigInteger z2 = Q.subtract(R).multiply(v).mod(Q);

        z1 = A.modPow(z1,P);
        z2 = Y.modPow(z2,P);

        BigInteger u = z1.multiply(z2).mod(P).mod(Q);

        return u.equals(R);
    }
}
