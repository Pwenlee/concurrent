package com.lee.concurrent.forkjoin;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * @author lipan
 */
public class MinValueTask extends RecursiveTask<List<Long>> {

    private List<Long> dataList;

    private int count;

    private final static int DEFAULT_SIZE = 10000;

    public MinValueTask(List<Long> dataList, int count) {
        if(CollectionUtils.isEmpty(dataList)){
            throw new IllegalArgumentException("dataList must not be null");
        }
        if(count >= 1 && count <= dataList.size()){
            this.dataList = dataList;
            this.count = count;
        }else{
            throw new IllegalArgumentException("count must >= 1 and <= dataList.size()");
        }
    }

    @Override
    protected List<Long> compute() {
        if(dataList.size() <= DEFAULT_SIZE){
            return getMinDataList(dataList, count);
        }else{
//            int total = dataList.size() / DEFAULT_SIZE;
//            List<MinValueTask> minValueTaskList = new ArrayList<>(total + 1);
//            for(int i = 1; i <= total; i++){
//                int startIndex = (i - 1) * DEFAULT_SIZE;
//                int endIndex = i * DEFAULT_SIZE;
//                minValueTaskList.add(new MinValueTask(dataList.subList(startIndex, endIndex), 20));
//            }
//            int leftCount = dataList.size() % DEFAULT_SIZE;
//            if(leftCount != 0){
//                leftCount = leftCount >= count ? count : leftCount;
//                minValueTaskList.add(new MinValueTask(dataList.subList(total * DEFAULT_SIZE - 1, dataList.size()), leftCount));
//            }
//            List<Long> result = new ArrayList<>((total + 1) * count);
//            minValueTaskList.forEach(ele -> {
//                ele.fork();
//                result.addAll(ele.join());
//            });
//            MinValueTask minValue = new MinValueTask(result, count);
//            minValue.fork();
//            return minValue.join();


            int total = dataList.size() / DEFAULT_SIZE;
            List<Long> result = new ArrayList<>((total + 1) * count);
            for(int i = 1; i <= total; i++){
                int startIndex = (i - 1) * DEFAULT_SIZE;
                int endIndex = i * DEFAULT_SIZE;
                MinValueTask minValueTask = new MinValueTask(dataList.subList(startIndex, endIndex), 20);
                minValueTask.fork();
                result.addAll(minValueTask.join());
            }
            int leftCount = dataList.size() % DEFAULT_SIZE;
            if(leftCount != 0){
                MinValueTask minValueTask = new MinValueTask(dataList.subList(total * DEFAULT_SIZE - 1, dataList.size()), leftCount);
                minValueTask.fork();
                result.addAll(minValueTask.join());
            }
            MinValueTask minValue = new MinValueTask(result, count);
            minValue.fork();
            return minValue.join();
        }
    }

    private static List<Long> getMinDataList(List<Long> dataList, int count){
        if(CollectionUtils.isEmpty(dataList)){
            throw new IllegalArgumentException("dataList must not be null");
        }
        if(count >= 1 && count <= dataList.size()){
            List<Long> resultList = dataList.subList(0, count);
            resultList.sort(Comparator.naturalOrder());
            for(int i = count; i < dataList.size(); i++){
                Long value = dataList.get(i);
                int maxValueIndex = count - 1;
                if(value.compareTo(dataList.get(maxValueIndex)) < 0){
                    resultList.set(maxValueIndex, value);
                    resultList.sort(Comparator.naturalOrder());
                }
            }
            return resultList;
        }else{
            throw new IllegalArgumentException("count must >= 1 and <= dataList.size()");
        }
    }

}
