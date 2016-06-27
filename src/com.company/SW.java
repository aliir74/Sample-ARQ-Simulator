package com.company;

/**
 * Created by ali on 6/27/16.
 */
public class SW {
        int Ws, Wr, R, Nf, v, d;
        double p;
        boolean receive;

    public SW(int ws, int wr, double p, int r, int nf, int v, int d) {
        Ws = ws;
        Wr = wr;
        R = r;
        Nf = nf;
        this.v = v;
        this.d = d;
        this.p = p;
        receive = false; // when sender use media -> false and vice versa
    }
}
