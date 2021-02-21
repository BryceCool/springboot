package com.springboot.frame.thread;

/**
 * @author yangguanghui6
 * @date 2021/2/1 21:05
 */
public class DeadLockDemo {

    public static void main(String[] args) {
        DeadLockThread deadLock = new DeadLockThread();
        new Thread(deadLock, "窗口1").start();
        try {
            Thread.sleep(40);
            deadLock.flag = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(deadLock, "窗口2").start();
    }
}


class DeadLockThread implements Runnable {

    private static int count = 100;

    public Boolean flag = true;

    private Object object = new Object();

    @Override
    public void run() {
        if (flag) {
            while (count > 0) {
                synchronized (object) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ticket();
                }
            }
        } else {
            while (count > 0) {
                ticket();
            }
        }
    }

    private synchronized void ticket() {
        synchronized (object) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {

            }
            if (count > 0) {
                System.out.println(Thread.currentThread().getName() + ", 正在开始是出售：" + (100 - count) + 1);
                count--;
            }
        }
    }
}