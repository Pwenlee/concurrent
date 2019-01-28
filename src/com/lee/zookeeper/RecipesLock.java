package com.lee.zookeeper;

import org.apache.curator.framework.recipes.locks.InterProcessLock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lipan
 * @date 2018/10/8 16:49
 * @description
 */
public class RecipesLock extends ZkClientBase{

    private static String LOCK_PATH = "/lock_path";

    public static void main(String[] args) throws Exception{
//            BaseOperate[] a = new BaseOperate[10240000];
//            for(int i = 0; i<a.length; i++){
//                a[i] = new BaseOperate();
//            }
//            System.out.println("111111111111111111111111");
//            Thread.sleep(60000 * 5);
//            for(int i = 0; i<a.length/2; i++){
//                a[i] = null;
//            }
//            System.gc();
//            System.out.println("2222222222222222222");
//            Thread.sleep(60000 * 5);
                List<Integer> list = new ArrayList<>(10);
                for(int i=0;i<10;i++){
                    list.add(i);
                }
                list.removeIf(ele ->  ele.intValue()> 3);
        System.out.print(list.stream().map(String::valueOf).collect(Collectors.joining(",","{", "}")));
    }
}
