package Lab4;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import static Lab2.HelpfulFunc.GenPG2;

public class Lab4_main {

    public static void main(String[] args) {

        BigInteger P = GenPG2 (20)[0];
        player Krupe = new player(P);
        LinkedList<BigInteger> Cards = new LinkedList<>();
        ArrayList<player> players = new ArrayList<> ();
        LinkedList<BigInteger> plCards = new LinkedList<> ();
        for(int i = 0;i!=52;i++) Cards.add (new BigInteger (Integer.toString (i+2)));
        for(int i = 0;i!=8;i++) players.add (new player (P));
        Cards = Krupe.CryptCard(Cards);
        for (player player1 : players) Cards = player1.CryptCard(Cards);

        for(int i = 0;i<8;i++)
        {
            plCards.add(Cards.pollFirst ());
            Collections.shuffle(Cards,new SecureRandom());
            plCards.add(Cards.pollFirst ());

            plCards = Krupe.DecryptCard(plCards);
            for(int j = 0;j<players.size()-1;j++) plCards = players.get(j).DecryptCard (plCards);
            players.get(players.size ()-1).GetCard (plCards);
            Collections.swap (players,players.size ()-1,i);
            plCards.clear ();

        }

        for (Lab4.player player : players) player.PrintCard();

        plCards.addAll(0,Cards.subList(0,5));

        for (Lab4.player player : players) plCards = player.DecryptCard(plCards);
        Krupe.GetCard(plCards);
        Krupe.PrintCard();
    }

}
