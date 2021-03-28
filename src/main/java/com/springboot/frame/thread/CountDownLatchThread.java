package com.springboot.frame.thread;

import java.util.concurrent.CountDownLatch;

/**
 * @author yangguanghui6
 * @date 2021/2/2 20:05
 */
class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);

        long begin = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            new Thread(new CountDownLatchThread(countDownLatch)).start();
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("Time is :" + (end - begin));
    }
}

class CountDownLatchThread implements Runnable {

    CountDownLatch countDownLatch;

    public CountDownLatchThread(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            int num = 0;
            for (int i = 0; i < 200000; i++) {
                if (i % 2 == 0) {
                    num += i;
                }
            }
            System.out.println(Thread.currentThread().getName() + ":" + num);
        } finally {
            countDownLatch.countDown();
        }
    }
}



