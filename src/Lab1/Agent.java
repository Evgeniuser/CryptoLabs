package Lab1;

import java.math.BigInteger;
import java.util.Random;
import static java.math.BigInteger.probablePrime;

import static Lab1.MyMath.*;

public class Agent {

    private long P1;
    private long prkey;
    public long PublicKey;
    private long MyPrimeNum;
    public String name;

    Agent(long P, long G, String name)
    {
        this.P1 = P;
        this.name = name;
        Random rnd = new Random();
        BigInteger a = new BigInteger(String.valueOf(probablePrime(28,rnd)));
        MyPrimeNum = Long.parseLong(a.toString());
        System.out.println(MyPrimeNum);
        createPublicKey(G);
    }

    private void createPublicKey(long g){ PublicKey = modPow2(g,MyPrimeNum, P1);}
    void createPrivateKey(long y){prkey = modPow2(y,MyPrimeNum, P1);
        System.out.println( name + " PrivateKey = " + prkey);
    }
    public long getY1() {return PublicKey;}

}
