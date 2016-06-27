package com.company;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ali on 6/27/16.
 */
public class SW {

    private int Ws, Wr, R, Nf, v, d, sequenceNumberBit;
    private double p;
    BlockingQueue<Message> queue1, queue2;

    public SW(int ws, int wr, double p, int r, int nf, int v, int d, int sequenceNumberBit) {
        Ws = ws;
        Wr = wr;
        R = r;
        Nf = nf;
        this.v = v;
        this.d = d;
        this.p = p;
        this.sequenceNumberBit = sequenceNumberBit;
        queue1 = new LinkedBlockingQueue<Message>();
        queue2 = new LinkedBlockingQueue<Message>();
        senderReceiverCreate();
    }

    void senderReceiverCreate() {
        System.out.println("test");
        Sender sender = new Sender(Ws, R, Nf, v, d, p, "sender1", queue1, queue2, sequenceNumberBit);
        sender.start();
        Receiver receiver = new Receiver(Wr, R, Nf, v, d, p, "receiver1", queue2, queue1, sequenceNumberBit);
        receiver.start();
    }
}
