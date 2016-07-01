package com.company;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
	// write your code here
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter protocol(1-3), Ws, Wr, p, R, Nf, V, d, sequenceNumberBit, N:");
        int protocol = 1, Ws = 4, Wr = 2;
        int R = 1, Nf = 1, v = 1, d = 1;
        int N = 1;
        double p = 0.3;
        int sequenceNumberBit = 3;

        protocol = sc.nextInt();
        Ws = sc.nextInt();
        Wr = sc.nextInt();
        p = sc.nextDouble();
        R = sc.nextInt();
        Nf = sc.nextInt();
        v = sc.nextInt();
        d = sc.nextInt();
        sequenceNumberBit = sc.nextInt();
        N = sc.nextInt();

        Selector selector = new Selector(protocol, Ws, Wr, p, R, Nf, v, d, sequenceNumberBit, N);
    }
}
