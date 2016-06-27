package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter protocol(1-3), Ws, Wr, p, R, Nf, V, d:");
        int protocol = sc.nextInt(), Ws = sc.nextInt(), Wr = sc.nextInt(), R, Nf, v, d;
        double p = sc.nextDouble();
        R = sc.nextInt();
        Nf = sc.nextInt();
        v = sc.nextInt();
        d = sc.nextInt();
        if(protocol == 1) {
            SW sw = new SW(Ws, Wr, p, R, Nf, v, d);
        }

    }
}
