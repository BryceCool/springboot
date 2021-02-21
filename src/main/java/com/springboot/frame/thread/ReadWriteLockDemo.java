package com.springboot.frame.thread;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yangguanghui6
 * @date 2021/2/2 19:37
 */
public class ReadWriteLockDemo {

    public static void main(String[] args) {

        Clerks clerks = new Clerks();
        new Thread(new WriteLockThread(clerks), "窗口A").start();
        for (int i = 0; i < 5; i++) {
            new Thread(new ReadLockThread(clerks), "窗口B").start();
        }
    }
}


class ReadLockThread implements Runnable {

    Clerks clerks;

    public ReadLockThread(Clerks clerks) {
        this.clerks = clerks;
    }

    @Override
    public void run() {
        clerks.save();
    }
}

class WriteLockThread implements Runnable {

    Clerks clerks;

    public WriteLockThread(Clerks clerks) {
        this.clerks = clerks;
    }

    @Override
    public void run() {
        clerks.sale();
    }
}


class Clerks {

    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void save() {
        readWriteLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "存票！");
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void sale() {
        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "售票！");
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}