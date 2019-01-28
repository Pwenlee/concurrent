package com.lee.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * zk节点异步操作
 * @author lipan
 * @date 2018/9/28 15:18
 * @description
 */
public class BackgroundOperate extends ZkClientBase{

    private final static String path = "/lee2";

    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    private static CountDownLatch countDownLatch = new CountDownLatch(2);

    public static void main(String[] args) throws Exception{
        client.create().inBackground((CuratorFramework client, CuratorEvent event) -> {
            System.out.println(Thread.currentThread().getName()+ "-----------" + event.getResultCode() + "----------------" + event.getType());
            countDownLatch.countDown();
        }, executorService).forPath(path);

        client.create().inBackground((CuratorFramework client, CuratorEvent event) -> {
            System.out.println(Thread.currentThread().getName()+ "-----------" + event.getResultCode() + "----------------" + event.getType());
            countDownLatch.countDown();
        }).forPath(path);
        countDownLatch.await();
        executorService.shutdown();
    }
}
