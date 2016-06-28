package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter protocol(1-3), Ws, Wr, p, R, Nf, V, d, sequenceNumberBit:");
        int protocol = 1, Ws = 2, Wr = 2;
        int R = 1, Nf = 1, v = 1, d = 1;
        double p = 0;
        int sequenceNumberBit = 3;
        /*
        protocol = sc.nextInt(), Ws = sc.nextInt(), Wr = sc.nextInt();
        p = sc.nextDouble();
        R = sc.nextInt();
        Nf = sc.nextInt();
        v = sc.nextInt();
        d = sc.nextInt();
        sequenceNumberBit = sc.nextInt();
        */
        if(protocol == 1) {
            SW sw = new SW(Ws, Wr, p, R, Nf, v, d, sequenceNumberBit);
        }

    }
}
