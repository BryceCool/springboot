package com.springboot.frame.thread;

import java.util.Arrays;
import java.util.List;

/**
 * @author yangguanghui6
 * @date 2021/2/22 14:09
 */
public class Test {

    public static void main(String[] args) {

        List<String> strings = Arrays.asList("1", "2", "3");
        strings.forEach(System.out::println);

    }

}
