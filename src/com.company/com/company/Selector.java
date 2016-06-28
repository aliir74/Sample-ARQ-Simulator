package com.company;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ali on 6/27/16.
 */
public class Selector {

    private int Ws, Wr, R, Nf, v, d, sequenceNumberBit;
    private double p;
    int protocol;
    BlockingQueue<Message> queue1, queue2;

    public Selector(int protocol, int ws, int wr, double p, int r, int nf, int v, int d, int sequenceNumberBit) {
        this.protocol = protocol;
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
        if(protocol == 1)
            senderReceiverCreate();
        else if(protocol == 2)
            GBNSenderReceiverCreate();
        else
            SRSenderReceiverCreate();
    }

    void senderReceiverCreate() {
        System.out.println("test");
        Sender sender = new Sender(Ws, R, Nf, v, d, p, "sender1", queue1, queue2, sequenceNumberBit);
        sender.start();
        Receiver receiver = new Receiver(Wr, R, Nf, v, d, p, "receiver1", queue2, queue1, sequenceNumberBit);
        receiver.start();
    }

    void GBNSenderReceiverCreate() {
        System.out.println("GBN Test!");
        GBNSender gbnSender = new GBNSender(Ws, Wr, R, Nf, v, d, p, "gbnsender", queue1, queue2, sequenceNumberBit);
        gbnSender.start();
        GBNReceiver gbnReceiver = new GBNReceiver(Wr, Ws, R, Nf, v, d, p, "gbnreceiver", queue2, queue1, sequenceNumberBit);
        gbnReceiver.start();
    }

    void SRSenderReceiverCreate() {
        System.out.println("SR Test!");
        SRSender srSender = new SRSender(Ws, Wr, R, Nf, v, d, p, "srSender", queue1, queue2, sequenceNumberBit);
        srSender.start();
        SRReceiver srReceiver = new SRReceiver(Wr, Ws, R, Nf, v, d, p, "srReceiver", queue2, queue1, sequenceNumberBit);
        srReceiver.start();
    }
}
