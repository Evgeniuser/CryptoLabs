package Lab2;

import java.io.*;
import java.nio.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


import static Lab2.HelpfulFunc.GenPG;
import static Lab2.HelpfulFunc.ReadFile;
import static Lab2.HelpfulFunc.WriteFile;

public class Lab2_main {

    public static void main(String[] args) throws IOException {

        //RSA Begin

        ByteBuffer InBuffer = ReadFile("test.txt",false).position(0);
        ByteBuffer OutBuffer = ByteBuffer.allocate(InBuffer.limit()*8);
        RSA_agent Alice = new RSA_agent("Alice");
        RSA_agent Bob = new RSA_agent("Bob");

        long test = 12345,text;
        System.out.println("RSA test: " + test + " " + Alice.EncryptMsg(Bob.getD(),Bob.getN(),test) + " " + Bob.DecryptMsg(Alice.EncryptMsg(Bob.getD(),Bob.getN(),test)));
        int t = InBuffer.limit();
        do{
            test = (long)InBuffer.getShort();
            text = Alice.EncryptMsg(Bob.getD(),Bob.getN(),test);
            OutBuffer.putLong(text);
            t-=2;

        }while(t>1 || t!=0);


        WriteFile("RSA_crypt.txt",OutBuffer.array(),false);
        InBuffer.clear();
        InBuffer = ReadFile("RSA_crypt.txt",false).position(0);
        OutBuffer= ByteBuffer.allocate(InBuffer.limit()/4);
        OutBuffer.position(0);
        t = InBuffer.capacity();

        do{

            test = InBuffer.getLong();
            text = Bob.DecryptMsg(test);
            OutBuffer.putShort((short) text);
            t-=16;

        }while(t>1 || t>0);
        WriteFile("RSA_detest.txt",OutBuffer.array(),true);

        //RSA end

        //Elgama Begin

        ArrayList<Long> pair = GenPG(20);

        Elgama_agent A = new Elgama_agent(pair.get(0),pair.get(1),"A");
        Elgama_agent B = new Elgama_agent(pair.get(0),pair.get(1),"B");

        long tst = 12345;
        long r = A.CrtSessionKey();
        long crt = A.Crypt(tst,B.getY());
        System.out.println("Elgam test: " + tst + " " + crt + " " + B.Decrypt(r, crt));
        InBuffer = ReadFile("test.txt",false).position(0);
        OutBuffer.clear();

        t = InBuffer.limit();
        OutBuffer = ByteBuffer.allocate(InBuffer.limit()*16);
        do
        {
            r = A.CrtSessionKey();
            test = (long)InBuffer.getShort();
            text = A.Crypt(test,B.getY());
            OutBuffer.putLong(r);
            OutBuffer.putLong(text);
            t-=2;
        }while(t>1 || t>0);


        WriteFile("Elgam_crypt.txt",OutBuffer.array(),false);
        InBuffer.clear();
        InBuffer = ReadFile("Elgam_crypt.txt",false).position(0);
        OutBuffer.clear();
        t = InBuffer.limit();
        OutBuffer = ByteBuffer.allocate(InBuffer.limit()/8);
        long text1, opR;
        do {
            opR = InBuffer.getLong();
            text1 = InBuffer.getLong();
            OutBuffer.putShort((short)B.Decrypt(opR,text1));
            t-=32;
        }while(t>1 || t> 0);
        WriteFile("Elgam_detest.txt",OutBuffer.array(),true);

        //Elgama End


        //Shamir Begin

        Shamir_agent C = new Shamir_agent(pair.get(0),"C");
        Shamir_agent D = new Shamir_agent(pair.get(0),"B");

        long tst1;
        tst1 = C.CrtX(tst,D.getC());
        tst1 = D.CrtX(tst1,C.getC());
        tst1 = C.DerX(tst1);
        tst1 = D.DerX(tst1);
        System.out.println("Shamir test: " + tst + " " + tst1);

        InBuffer.clear();
        InBuffer = ReadFile("test.txt",false).position(0);
        t = InBuffer.limit();
        OutBuffer.clear();
        OutBuffer = ByteBuffer.allocate(InBuffer.limit());
        do
        {
            test = (long)InBuffer.getShort();
            tst1 = C.CrtX(test,D.getC());
            tst1 = D.CrtX(tst1,C.getC());
            tst1 = C.DerX(tst1);
            text = D.DerX(tst1);
            OutBuffer.putShort((short)text);
            t-=2;
        }while(t>1 || t>0);
        WriteFile("Shamir_detest.txt",OutBuffer.array(),false);

        //Shamir End

        //Vernam Begin

        Random rnd = new Random(1);
        Vernam_agent Q = new Vernam_agent(rnd,"Q");
        Vernam_agent W = new Vernam_agent(rnd,"W");

        byte[] qwe = {1,2,3,4,5};

        System.out.println(Arrays.equals(qwe,W.CryptOrDecrypt(Q.CryptOrDecrypt(qwe))));
        System.out.println(Arrays.equals(InBuffer.array(),W.CryptOrDecrypt(Q.CryptOrDecrypt(InBuffer.array()))));

        //Vernam End

    }
}
