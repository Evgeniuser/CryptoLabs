package Lab1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import static Lab1.MyMath.*;

import static java.lang.Math.random;
import static java.math.BigInteger.probablePrime;

public class lab1 {
    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Random rnd = new Random();
        BigInteger a = new BigInteger(String.valueOf(probablePrime(28,rnd)));
        long P = Long.parseLong(a.toString());
        long G = (long)(1 + random()*(P-1));
        System.out.println("Prime P: " + isPrimitive(P));

        Agent Alice = new Agent(P,G,"Alice");
        Agent Bob = new Agent(P,G,"Bob");
        Alice.createPrivateKey(Bob.getY1());
        Bob.createPrivateKey(Alice.getY1());

        long Zs = SbSg.Sbsg(23,5,3);
        System.out.println(Zs);

        System.out.println((AdvanceGcd(28,19)));
    }
}
