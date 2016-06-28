package com.company;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ali on 6/27/16.
 */
public class SRSender extends Thread {
    Thread t;
    String threadName;
    int Ws, R, Nf, v, d, Wr;
    double p;
    int sendWait;
    int Ns, Nr;
    int sequenceNumberBit;
    Vector<String> Data;
    BlockingQueue<Message> queues, queuer;
    Queue<Integer> badPackets;


    public SRSender(int ws, int wr, int r, int nf, int v, int d, double p, String threadName,
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
        sendWait = (int) Math.ceil(Nf / R + v / d);
        Ns = 0;
        Nr = 0;
        Data = new Vector<>(10);
        for (int i = 0; i < 10; i++) {
            Data.insertElementAt("Data" + (i + 1), i);
        }
        badPackets = new LinkedList<>();
    }

    public void run() {
        int j = 0;
        int limitr = Wr;
        int limits = Ws;
        boolean nack = false;
        int nackPacket = -1;
        boolean bitErr = false, corrupted = false;
        while (j < 10) {
            //System.out.println("Sender start!");
            corrupted = false;
            //badPackets.removeAllElements();
            for (int i = 0; i < limitr; i++) {
                bitErr = false;
                Message msg;
                while ((msg = queuer.poll()) == null) {
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (ThreadLocalRandom.current().nextDouble(0, 1) >= Math.pow((1 - p), Nf)) {
                    bitErr = true;
                    corrupted = true;
                    badPackets.add(msg.sendNumber);
                    System.out.println(threadName + " Recevied: [curropted] " + msg.data + "\t ack: " + msg.ack);
                    Nr = Math.min(Nr, msg.sendNumber);
                } else {
                    System.out.println(threadName + " Recevied: " + msg.data + "\t ack: " + msg.ack);
                    if(!corrupted)
                        Nr++;
                }
                if(msg.ack < 100) {
                    Ns = msg.ack;
                    nack = false;
                } else {
                    nack = true;
                    nackPacket = msg.ack-100;
                }
                /*if (i == Wr - 1 && Nr == msg.sendNumber && !bitErr)
                    Nr++;*/
                Nr %= binaryPower(sequenceNumberBit);
            }
            //msg.data = Data.elementAt(Ns);
            int ack;
            if(!badPackets.isEmpty()) {
                ack = badPackets.poll()+100;//= nack
                limitr = 1;
            } else {
                limitr = Wr;
                ack = Nr;
            }
            int sendNumber;
            if(nack) {
                limits = 1;
                sendNumber = nackPacket;
            } else {
                limits = Ws;
                sendNumber = Ns;
            }
            for (int i = 0; i < limits; i++) {
                Message tmp = new Message();
                tmp.data = ("Data" + sendNumber);
                tmp.ack = ack;
                tmp.sendNumber = sendNumber;
                if(!nack) {
                    Ns++;
                    Ns %= binaryPower(sequenceNumberBit);
                    sendNumber = Ns;
                }
                System.out.println(threadName + " Sent: " + tmp.data + "\t ack: " + tmp.ack);
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
        System.out.println("Sender " + threadName + " started");
        for (int i = 0; i < Ws; i++) {
            Message start = new Message();
            start.ack = 0;
            start.sendNumber = i;
            start.data = "Data" + i;
            System.out.println(threadName + " Sent: " + start.data + "\t ack: " + start.ack);
            try {
                sleep(100); //for print
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                sleep(sendWait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                queues.put(start);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
}
