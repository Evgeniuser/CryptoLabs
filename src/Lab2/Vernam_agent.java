package Lab2;

import java.util.Random;

public class Vernam_agent {

    private byte[] X;
    private Random rnd;
    public String name;

    Vernam_agent(Random rnd,String name)
    {
        this.rnd = rnd;
        this.name = name;
    }

    public byte[] CryptOrDecrypt(byte[] msg)
    {
        long ms = msg.length,i = 0;
        X = new byte[Math.toIntExact(ms)];
        rnd.nextBytes(X);
        while(i!=ms)
        {
            msg[Math.toIntExact(i)] ^= X[Math.toIntExact(i)];
            i++;
        }
        return  msg;
    }


}
