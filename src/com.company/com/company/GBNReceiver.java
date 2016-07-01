package com.company;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ali on 6/27/16.
 */
public class GBNReceiver extends Thread {
    Thread t;
    String threadName;
    int Ws, R, Nf, v, d, Wr;
    int Ns, Nr;
    double p;
    int sendWait;
    int sequenceNumberBit;
    int receiveBit;
    double time;
    double timeWait;
    Vector<String> Data;
    BlockingQueue<Message> queues, queuer;


    public GBNReceiver(int ws, int wr, int r, int nf, int v, int d, double p, String threadName,
                       BlockingQueue<Message> queues, BlockingQueue<Message> queuer, int sequenceNumberBit) {
        Ws = ws;
        Wr = wr;
        R = r;
        Nf = nf;
        this.v = v;
        this.d = d;
        this.p = p;
        this.threadName = threadName;
        this.queues = queues;
        this.queuer = queuer;
        this.sequenceNumberBit = sequenceNumberBit;
        sendWait = (int) Math.ceil((Nf+sequenceNumberBit+1)/R + d/v);
        timeWait = (Nf+sequenceNumberBit+1)/(1.*R) + d*1./v;
        System.out.println("timeWait" + timeWait);
        Ns = 0;
        Nr = 0;
        Data = new Vector<>(10);
        for (int i = 0; i < 10; i++) {
            Data.insertElementAt("Data" + (i + 1), i);
        }
        time = 0;
        receiveBit = 0;
    }

    public void run() {
        int j = 0;
        boolean bitErr = false, corrupted = false;
        while (j < 10) {
            //System.err.println("Sender start!");
            corrupted = false;
            for (int i = 0; i < Wr; i++) {
                bitErr = false;
                Message msg;
                while ((msg = queuer.poll()) == null) {
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                time += timeWait;
                if (Math.random() >= Math.pow((1 - p), Nf)) {
                    bitErr = true;
                    corrupted = true;
                    System.err.println(threadName + " Recevied: [curropted] " + msg.data + "\t ack: " + msg.ack);
                    Nr = Math.min(Nr, msg.sendNumber);
                } else {
                    System.err.println(threadName + " Recevied: " + msg.data + "\t ack: " + msg.ack);
                    receiveBit += Nf;
                    if(!corrupted)
                        Nr++;
                }
                Ns = msg.ack;
                /*if (i == Wr - 1 && Nr == msg.sendNumber && !bitErr)
                    Nr++;*/
                Nr %= binaryPower(sequenceNumberBit);
            }
            //msg.data = Data.elementAt(Ns);
            for (int i = 0; i < Ws; i++) {
                Message tmp = new Message();
                tmp.data = ("Data" + Ns);
                tmp.ack = Nr;
                tmp.sendNumber = Ns;
                Ns++;
                Ns %= binaryPower(sequenceNumberBit);
                System.err.println(threadName + " Sent: " + tmp.data + "\t ack: " + tmp.ack);
                try {
                    sleep(100); // for print!
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    int binaryPower(int x) {
        int ans = 1;
        for (int i = 0; i < x; i++) {
            ans *= 2;
        }
        return ans;
    }

    public int getReceiveBit() {
        return receiveBit;
    }

    public double getTime() {
        return time;
    }
}
