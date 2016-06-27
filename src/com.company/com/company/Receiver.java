package com.company;


import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ali on 6/27/16.
 */
public class Receiver extends Thread {
    Thread t;
    String threadName;
    int Ws, R, Nf, v, d;
    int Ns, Nr;
    double p;
    int sendWait;
    int sequenceNumberBit;
    Vector<String> Data;
    BlockingQueue<Message> queues, queuer;


    public Receiver(int ws, int r, int nf, int v, int d, double p, String threadName,
                    BlockingQueue<Message> queues, BlockingQueue<Message> queuer, int sequenceNumberBit) {
        Ws = ws;
        R = r;
        Nf = nf;
        this.v = v;
        this.d = d;
        this.p = p;
        this.threadName = threadName;
        this.queues = queues;
        this.queuer = queuer;
        this.sequenceNumberBit = sequenceNumberBit;
        sendWait = (int) Math.ceil(Nf/R + v/d);
        Ns = 0;
        Nr = 0;
        Data = new Vector<>(10);
        for(int i = 0; i < 10; i++) {
            Data.insertElementAt("Data" + (i+1), i);
        }
    }

    public void run() {
        int j = 0;
        boolean bitErr = false;
        while(j < 10) {
           // System.err.println("Receiver start!");
            Message msg;
            bitErr = false;
            while ((msg = queuer.poll()) == null) ;
            if(ThreadLocalRandom.current().nextDouble(0, 1) >= Math.pow((1-p), Nf)) {
                bitErr = true;
                System.err.println(threadName + " Recevied: [curropted] " + msg.data + "\t ack: " + msg.ack);
            } else {
                System.err.println(threadName + " Recevied: " + msg.data + "\t ack: " + msg.ack);
            }
            Ns = msg.ack;
            if (Nr == msg.sendNumber && !bitErr)
                Nr++;
            Nr %= binaryPower(sequenceNumberBit);
//        msg.data = Data.elementAt(Ns);
            Message tmp = new Message();
            tmp.data = ("Data" + Ns);
            tmp.ack = Nr;
            tmp.sendNumber = Ns;
            try {
                sleep(20); // for print!
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.err.println(threadName + " Sent: " + tmp.data + "\t ack: " + tmp.ack);
            try {
                sleep(sendWait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                queues.put(tmp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            j++;
        }
    }

    public void start() {
        System.err.println("Receiver " + threadName + " started");
        if(t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    int binaryPower(int x) {
        int ans = 1;
        for(int i = 0; i < x; i++) {
            ans *= 2;
        }
        return ans;
    }
}

