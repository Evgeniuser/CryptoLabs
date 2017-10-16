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

        byte[] buffer = ReadFile("test.txt");

        //RSA Begin
        ByteBuffer InBuffer; ByteBuffer OutBuffer;
        RSA_agent Alice = new RSA_agent("Alice");
        RSA_agent Bob = new RSA_agent("Bob");

        InBuffer = ByteBuffer.wrap(buffer,0,buffer.length);
        OutBuffer = ByteBuffer.allocate(InBuffer.limit());
        long test,text;
        int t = InBuffer.limit();
        do{
            test = (long)InBuffer.getShort();
            text = Bob.DecryptMsg(Alice.EncryptMsg(Bob.getD(),Bob.getN(),test));
            OutBuffer.putShort((short)text);
            t-=2;

        }while(t>1 || t!=0);

        byte[] outBuffer = OutBuffer.array();
        WriteFile("E://4course//Crypto//RSA_detest.txt",outBuffer);
        //RSA end

        //Elgama Begin

        ArrayList<Long> pair = GenPG(20);

        Elgama_agent A = new Elgama_agent(pair.get(0),pair.get(1),"A");
        Elgama_agent B = new Elgama_agent(pair.get(0),pair.get(1),"B");

        long tst = 123456;
        long r = A.CrtSessionKey();
        long crt = A.Crypt(tst,B.getY());
        System.out.println(tst + " " + crt + " " + B.Decrypt(r, crt));
        t = InBuffer.limit();
        InBuffer.position(0);
        OutBuffer.clear();
        OutBuffer = ByteBuffer.allocate(InBuffer.limit());
        r = A.CrtSessionKey();
        do
        {
            test = (long)InBuffer.getShort();
            text = B.Decrypt(r,A.Crypt(test,B.getY()));
            OutBuffer.putShort((short)text);
            t-=2;
        }while(t>1 || t>0);
        outBuffer = OutBuffer.array();
        WriteFile("E://4course//Crypto//Elgam_detest.txt",outBuffer);



        //Elgama End

        //Shamir Begin

        Shamir_agent C = new Shamir_agent(pair.get(0),"C");
        Shamir_agent D = new Shamir_agent(pair.get(0),"B");

        long tst1;
        tst1 = C.CrtX(tst,D.getC());
        tst1 = D.CrtX(tst1,C.getC());
        tst1 = C.DerX(tst1);
        tst1 = D.DerX(tst1);
        System.out.println(tst + " " + tst1);

        t = InBuffer.limit();
        InBuffer.position(0);
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
        outBuffer = OutBuffer.array();
        WriteFile("E://4course//Crypto//Shamir_detest.txt",outBuffer);

        //Shamir End

        //Vernam Begin

        Random rnd = new Random(1);
        Vernam_agent Q = new Vernam_agent(rnd,"Q");
        Vernam_agent W = new Vernam_agent(rnd,"W");

        byte[] qwe = {1,2,3,4,5};

        System.out.println(Arrays.equals(qwe,W.CryptOrDecrypt(Q.CryptOrDecrypt(qwe))));
        System.out.println(Arrays.equals(buffer,W.CryptOrDecrypt(Q.CryptOrDecrypt(buffer))));

        //Vernam End

    }
}
