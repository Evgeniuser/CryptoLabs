package Lab2;

import static Lab1.MyMath.*;
import static java.lang.Math.random;

public class Shamir_agent {

    public long C;
    private long D;
    public long P;
    public String name;

    Shamir_agent(long p, String name)
    {
        this.name = name;
        this.P = p;

        D = (long)(random()%(P-1)) + 1;

        while(true)
        {
            if(gcd(P-1,D)==1)
                break;
            else
                D = (long)(random() %P-1) + 1;
        }

        C = AdvanceGcd(P-1,D);
        if(C<0) C = C+P;

    }

    public long getC() {
        return C;
    }

    public long CrtX(long msg,long key)
    {
        return modPow2(msg,key,P);
    }

    public long DerX(long msg)
    {
        return modPow2(msg,D,P);
    }
}
