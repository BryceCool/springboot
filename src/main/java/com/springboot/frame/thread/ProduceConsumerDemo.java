package com.springboot.frame.thread;

/**
 * @author yangguanghui6
 * @date 2021/2/1 16:43
 */
public class ProduceConsumerDemo {

    public static void main(String[] args) {
        Clerk clerk = new Clerk();
        new Thread(new ProductThread(clerk),"生产者A").start();
        new Thread(new ProductThread(clerk),"生产者B").start();
        new Thread(new ConsumerThread(clerk),"消费者A").start();
        new Thread(new ConsumerThread(clerk),"消费者B").start();
    }
}


class ProductThread implements Runnable {

    Clerk clerk;

    public ProductThread(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            clerk.save();
        }
    }
}

class ConsumerThread implements Runnable {

    Clerk clerk;

    public ConsumerThread(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            clerk.sale();
        }
    }
}

/**
 * 共享变量
 */
class Clerk {

    private Integer ticketNum = 0;

    public synchronized void save() {
        try {
            while (ticketNum >= 2) {
                System.out.println("仓库已满！");
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ++ticketNum;
        System.out.println(Thread.currentThread().getName() + "- 存货数 -" + ticketNum);
        notifyAll();
    }

    public synchronized void sale() {
        try {
            while (ticketNum <= 0) {
                System.out.println("仓库为空！");
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        --ticketNum;
        System.out.println(Thread.currentThread().getName() + "- 存货数 -" + ticketNum);
        notifyAll();
    }
}