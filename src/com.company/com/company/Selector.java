package com.company;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

/**
 * Created by ali on 6/27/16.
 */
public class Selector {

    private int Ws, Wr, R, Nf, v, d, sequenceNumberBit;
    private double p;
    int protocol;
    int N;
    int sleepTime;
    double Reff;
    double eta;
    BlockingQueue<Message> queue1, queue2;

    public Selector(int protocol, int ws, int wr, double p, int r, int nf, int v, int d, int sequenceNumberBit, int n) throws FileNotFoundException, UnsupportedEncodingException {
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
        N = n;
        sleepTime = 1000;
        Reff = 0;
        double tmp;
        if(protocol == 1) {
            PrintWriter writer1 = new PrintWriter("/home/ali/SW.txt", "UTF-8");
            for(int i = 0; i < n; i++) {
                tmp = senderReceiverCreate();
                System.out.println(tmp);
                Reff += tmp;

            }
            Reff /= n;
            eta = Reff/R;
            writer1.println(Reff + "\t" + eta);
            writer1.close();
        } else if(protocol == 2) {
            PrintWriter writer2 = new PrintWriter("/home/ali/GBN.txt", "UTF-8");
            for(int i = 0; i < n; i++) {
                Reff += GBNSenderReceiverCreate();
            }
            Reff /= n;
            eta = Reff/R;
            writer2.println(Reff + "\t" + eta);
            writer2.close();
        } else {
            PrintWriter writer3 = new PrintWriter("/home/ali/SR.txt", "UTF-8");
            for(int i = 0; i < n; i++) {
                Reff += SRSenderReceiverCreate();
            }
            Reff /= n;
            eta = Reff/R;
            writer3.println(Reff + "\t" + eta);
            writer3.close();
        }
    }

    double senderReceiverCreate() {
        System.out.println("test");
        Sender sender = new Sender(Ws, R, Nf, v, d, p, "sender1", queue1, queue2, sequenceNumberBit);
        sender.start();
        Receiver receiver = new Receiver(Wr, R, Nf, v, d, p, "receiver1", queue2, queue1, sequenceNumberBit);
        receiver.start();
        try {
            sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println(sender.getReceiveBit());
        System.out.println(sender.getTime() + receiver.getTime());
        return (sender.getReceiveBit() + receiver.getReceiveBit())/(1.*(sender.getTime()+receiver.getTime()));
    }

    double GBNSenderReceiverCreate() {
        System.out.println("GBN Test!");
        GBNSender gbnSender = new GBNSender(Ws, Wr, R, Nf, v, d, p, "gbnsender", queue1, queue2, sequenceNumberBit);
        gbnSender.start();
        GBNReceiver gbnReceiver = new GBNReceiver(Wr, Ws, R, Nf, v, d, p, "gbnreceiver", queue2, queue1, sequenceNumberBit);
        gbnReceiver.start();
        try {
            sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (gbnSender.getReceiveBit()+gbnReceiver.getReceiveBit())/(1.*(gbnReceiver.getTime()+gbnSender.getTime()));
    }

    double SRSenderReceiverCreate() {
        System.out.println("SR Test!");
        SRSender srSender = new SRSender(Ws, Wr, R, Nf, v, d, p, "srSender", queue1, queue2, sequenceNumberBit);
        srSender.start();
        SRReceiver srReceiver = new SRReceiver(Wr, Ws, R, Nf, v, d, p, "srReceiver", queue2, queue1, sequenceNumberBit);
        srReceiver.start();
        try {
            sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (srReceiver.getReceiveBit()+srSender.getReceiveBit())/(1.*(srSender.getTime()+srReceiver.getTime()));
    }
}
