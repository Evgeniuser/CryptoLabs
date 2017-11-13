package Lab4;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import static Lab2.HelpfulFunc.GenPG2;

public class Lab4_main {

    public static void main(String[] args) {

        BigInteger P = GenPG2 (10)[0];
        LinkedList<BigInteger> Cards = new LinkedList<>();
        ArrayList<player> players = new ArrayList<> ();
        LinkedList<BigInteger> plCards = new LinkedList<> ();
        for(int i = 0;i!=52;i++) Cards.add (new BigInteger (Integer.toString (i+2)));
        for(int i = 0;i!=8;i++) players.add (new player (P));

        for(int i = 0;i<players.size ();i++) Cards = players.get(i).CryptCard (Cards);

        for(int i = 0;i<8;i++)
        {
            plCards.add(Cards.getFirst ());
            Cards.removeFirst ();
            plCards.add(Cards.getFirst ());
            Cards.removeFirst ();
            for(int j = 0;j<players.size()-1;j++) plCards = players.get(j).DecryptCard (plCards);
            players.get(players.size ()-1).GetCard (plCards);
            Collections.swap (players,players.size ()-1,i);
            plCards.clear ();
        }


       for(int i = 0;i<players.size ();i++)
            players.get (i).PrintCard ();

    }

}
