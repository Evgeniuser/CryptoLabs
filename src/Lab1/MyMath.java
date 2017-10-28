package Lab1;

import java.math.BigInteger;

import static java.lang.Math.random;



public class MyMath {

    public static long modPow2(long a,long b, long n)
    {
        long x=1,y=a;

        while(b > 0){
            if(b%2 == 1){
                x=(x*y)%n;
            }
            y = (y*y)%n;
            b /= 2;
        }

        return x%n;
    }

    public static long gcd(long a,long b)
    {
        if(b==0)
            return a;
        return gcd(b,a%b);
    }

    public static long AdvanceGcd(long a, long b)
    {

        long[] U = {a,1,0};
        long[] V = {b,0,1};
        long[] K = {0,0,0};
        long q;
        while(V[0]!=0)
        {
            q = U[0] / V[0];
            K[0] = U[0] % V[0];
            K[1] = U[1] - (q * V[1]);
            K[2] = U[2] - (q * V[2]);

            System.arraycopy(V,0,U,0,U.length);
            System.arraycopy(K,0,V,0,K.length);
        }

        U[0] = gcd(a,b);

        return U[2];
    }

    public static boolean isPrimitive(long x)
    {
        for (int i = 0; i < 100; i++)
        {
            long a = (long) (random() % (x - 2)) + 2;
            if((gcd(a,x) != 1) && (modPow2(a,x-1,x) != 1))
                return  false;
        }

        return true;
    }

    public static long mulMod(Long x, long y, long n)
    {

        long sum = 0;
        for(int i = 0; i < y; i++)
        {
            sum +=x;
            if(sum >= n)
                sum-=n;
        }

        return sum;
    }

    public static BigInteger powBg(BigInteger base, BigInteger exponent) {
        BigInteger result = BigInteger.ONE;
        while (exponent.signum() > 0) {
            if (exponent.testBit(0)) result = result.multiply(base);
            base = base.multiply(base);
            exponent = exponent.shiftRight(1);
        }
        return result;
    }

}




