//package com.company;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ali on 6/27/16.
 */
public class SW {

    private int Ws, Wr, R, Nf, v, d;
    private double p;
    BlockingQueue<Message> queue1, queue2;

    public SW(int ws, int wr, double p, int r, int nf, int v, int d) {
        Ws = ws;
        Wr = wr;
        R = r;
        Nf = nf;
        this.v = v;
        this.d = d;
        this.p = p;
        queue1 = new LinkedBlockingQueue<Message>();
        queue2 = new LinkedBlockingQueue<Message>();
    }
}
