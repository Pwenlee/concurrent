package com.lee.concurrent.forkjoin;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lipan
 */
public class Test {

    public static void main(String[] args) throws Exception{

        System.out.println(2 * Runtime.getRuntime().availableProcessors() + 1);

        int length = 50000000;
        ForkJoinPool forkJoinPool = new ForkJoinPool(2 * Runtime.getRuntime().availableProcessors() + 1);
        long start1 = System.currentTimeMillis();
        BuildValueTask buildValueTask = new BuildValueTask(length, length);
        List<Long> list = forkJoinPool.submit(buildValueTask).get();
        long end1 = System.currentTimeMillis();
        System.out.println("build data cost-----------------" + (end1 - start1));


        MinValueTask minValueTask = new MinValueTask(list, 20);
        long start = System.currentTimeMillis();
        Future<List<Long>> result = forkJoinPool.submit(minValueTask);
        System.out.println(result.get().stream().map(String::valueOf).collect(Collectors.joining(",", "", "")));
        long end = System.currentTimeMillis();
        System.out.println("compute value cost-----------------" + (end - start));
    }
}
