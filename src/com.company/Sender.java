package com.company;

/**
 * Created by ali on 6/27/16.
 */
public class Sender extends Thread {

    private:
        Thread t;
        String threadName;
        int Ws, R, Nf, v, d;
        double p;

    public Sender(int ws, int r, int nf, int v, int d, double p, String threadName) {
        Ws = ws;
        R = r;
        Nf = nf;
        this.v = v;
        this.d = d;
        this.p = p;
        this.threadName = threadName;
    }

    public void run() {

    }

    public void start() {
        System.out.println("Sender " + threadName + " started");
    }
}
