package Lab4;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class player {
    private String name;
    private BigInteger C;
    private BigInteger D;
    private BigInteger P;
    private LinkedList<BigInteger> MyCards = new LinkedList<> ();

    player(BigInteger P)
    {
        this.P = P;
        D = BigInteger.probablePrime (P.subtract (BigInteger.ONE).bitLength (),new SecureRandom ());
        C = D.modInverse (P.subtract (BigInteger.ONE));

    }

    public LinkedList<BigInteger> CryptCard(LinkedList<BigInteger> Cards)
    {
        Collections.shuffle (Cards, new SecureRandom ());
        IntStream.range (0, Cards.size ()).forEach (i -> Cards.set (i, Cards.get (i).modPow (C, P)));
        return Cards;
    }

    public LinkedList<BigInteger> DecryptCard(LinkedList<BigInteger> Cards)
    {
        IntStream.range (0, Cards.size ()).forEach (i -> Cards.set (i, Cards.get (i).modPow (D, P)));
        return Cards;
    }

    public void GetCard(LinkedList<BigInteger> Card)
    {
        MyCards.addAll (0,DecryptCard (Card));
    }

    public void PrintCard()
    {
        System.out.println (MyCards);
    }

}
