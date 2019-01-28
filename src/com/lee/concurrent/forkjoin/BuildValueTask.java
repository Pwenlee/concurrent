package com.lee.concurrent.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.RecursiveTask;

/**
 * @author lipan
 */
public class BuildValueTask extends RecursiveTask<List<Long>> {

    private int count;

    private int maxValue;

    private static final  int DEFAULT_SIZE = 100000;

    public BuildValueTask(int count, int maxValue) {
        if(count >= 1) {
            this.count = count;
        }else{
            throw new IllegalArgumentException("count must >= 1");
        }
        this.maxValue = maxValue;
    }

    @Override
    protected List<Long> compute() {
        List<Long> result = new ArrayList<>(count);
        Random random = new Random();
        if(count <= DEFAULT_SIZE){
            for(int i = 1; i <= count; i++){
                result.add(Long.valueOf(random.nextInt(maxValue) + 1));
            }
            return result;
        }else{
            int total = count / DEFAULT_SIZE;
            for(int i = 1; i <= total; i++){
                BuildValueTask buildValueTask = new BuildValueTask(DEFAULT_SIZE, maxValue);
                buildValueTask.fork();
                result.addAll(buildValueTask.join());
            }
            if(count % DEFAULT_SIZE != 0){
                BuildValueTask buildValueTask = new BuildValueTask(count % DEFAULT_SIZE, maxValue);
                buildValueTask.fork();
                result.addAll(buildValueTask.join());
            }
            return result;
        }
    }
}
