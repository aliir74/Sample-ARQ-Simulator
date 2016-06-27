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
        System.out.println("Sender start!");
        Message msg;
        while((msg = queuer.poll()) == null);
        System.out.println(threadName + " Recevied: data=" + msg.data + "\t ack=" + msg.ack);
        Ns = msg.ack;
        if(Nr == msg.sendNumber)
            Nr++;
        //msg.data = Data.elementAt(Ns);
        msg.data = ("Data"+Ns);
        msg.ack = Nr;
        msg.sendNumber = Ns;
        try {
            sleep(sendWait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            queues.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        System.out.println("Sender " + threadName + " started");
        Message start = new Message();
        start.ack = 0;
        start.sendNumber = 0;
        start.data = "hello";
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

        if(t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
