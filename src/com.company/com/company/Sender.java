package com.company;


import java.util.Vector;
import java.util.concurrent.BlockingQueue;

/**
 * Created by ali on 6/27/16.
 */
public class Sender extends Thread {
        Thread t;
        String threadName;
        int Ws, R, Nf, v, d;
        double p;
        int sendWait;
        int Ns, Nr;
        Vector<String> Data;
        BlockingQueue<Message> queues, queuer;

    public Sender(int ws, int r, int nf, int v, int d, double p, String threadName,
                  BlockingQueue<Message> queues, BlockingQueue<Message> queuer) {
        Ws = ws;
        R = r;
        Nf = nf;
        this.v = v;
        this.d = d;
        this.p = p;
        this.threadName = threadName;
        this.queues = queues;
        this.queuer = queuer;
        sendWait = Nf/R + v/d;
        Ns = 0;
        Nr = 0;
        Data = new Vector<>(10);
        for(int i = 0; i < 10; i++) {
            Data.insertElementAt("Data" + (i+1), i);
        }
    }

    public void run() {
        int j = 0;
        while(j < 10) {
            //System.out.println("Sender start!");
            Message msg;
            while ((msg = queuer.poll()) == null) ;
            System.out.println(threadName + " Recevied: " + msg.data + "\t ack:" + msg.ack);
            Ns = msg.ack;
            if (Nr == msg.sendNumber)
                Nr++;
            //msg.data = Data.elementAt(Ns);
            Message tmp = new Message();
            tmp.data = ("Data" + Ns);
            tmp.ack = Nr;
            tmp.sendNumber = Ns;
            System.out.println(threadName + " Sent: " + tmp.data + "\t ack: " + tmp.ack);
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
        System.out.println("Sender " + threadName + " started");
        Message start = new Message();
        start.ack = 0;
        start.sendNumber = 0;
        start.data = "Data0";
        try {
            sleep(sendWait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(threadName + " Sent: " + start.data + "\t ack: " + start.ack);
        try {
            queues.put(start);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
