package com.company;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
	// write your code here
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter protocol(1-3), Ws, Wr, p, R, Nf, V, d, sequenceNumberBit, N:");
        int protocol = 3, Ws = 4, Wr = 2;
        int R = 10000, Nf = 100, v = 300000000, d = 1000;
        int N = 5;
        double p = 0.001;
        int sequenceNumberBit = 3;

        protocol = sc.nextInt();
        /*
        Ws = sc.nextInt();
        Wr = sc.nextInt();
        p = sc.nextDouble();
        R = sc.nextInt();
        Nf = sc.nextInt();
        v = sc.nextInt();
        d = sc.nextInt();
        sequenceNumberBit = sc.nextInt();
        N = sc.nextInt();
*/
        Selector selector = new Selector(protocol, Ws, Wr, p, R, Nf, v, d, sequenceNumberBit, N);
    }
}
