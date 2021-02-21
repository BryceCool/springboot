package com.springboot.frame.debug;

/**
 * debug 小技巧
 */
public class DebugDemo {

    public static void main(String[] args) {
        //conditionDebug();

        //dropFrameDebug();

        //multiThreadDebug();

        dynamicSetValueDebug();
    }

    /**
     * 1. 条件断点: 右击断点处，condition处填入条件，i=100
     */
    public static void conditionDebug() {
        int result = 0;
        for (int i = 0; i < 10000; i++) {
            if (i == 100) {
                result = i % 2 + 1;
                System.out.println(result);
            }
        }
    }

    /**
     * 2. 回到上一步：Drop Frame
     * run to cursor: 运行到光标处
     */
    public static void dropFrameDebug() {
        int i = 99;
        method1(i);
    }

    public static void method1(int i) {
        System.out.println("method1：" + i);
        method2(i);
    }

    public static void method2(int j) {
        j++;
        System.out.println("method2：" + j);
    }

    /**
     * 3. 多线程调试：各Thread 的断点处Suspend 选择为Thread
     */
    public static void multiThreadDebug() {

        new Thread(() -> {
            System.out.println("1 - 山重水复疑无路！");
        }, "Thread 1").start();

        new Thread(() -> {
            System.out.println("2 - 柳暗花明又一村！");
        }, "Thread 2").start();

        System.out.println("3 - 仰天长笑出门去！");
        System.out.println("4 - 我辈岂是蓬蒿人！");
    }

    /**
     * 4. 临时执行表达式/修改变量的运行值: 运行到某个frame上，选定某个参数值，set value即可
     */
    public static void dynamicSetValueDebug() {
        int num = 0;
        for (int i = 0; i < 10; i++) {
            num += i;
        }
        System.out.println(num);
    }
}
