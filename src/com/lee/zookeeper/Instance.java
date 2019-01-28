package com.lee.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Objects;

/**
 * @author lipan
 * @date 2018/9/28 14:31
 * @description
 */
public class Instance {

    private static Instance instance = new Instance();

    private static CuratorFramework client;

    private static final String LEADER_PATH = "/LEADER_PATH";

    static {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        client.start();
        LeaderSelector leaderSelector = new LeaderSelector(client, LEADER_PATH, new LeaderSelectorListener() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                Thread.sleep(Integer.MAX_VALUE);
            }

            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {

            }
        });
        leaderSelector.autoRequeue();
        leaderSelector.start();
    }

    public CuratorFramework getCuratorClient() {
        return client;
    }

    public static Instance getInstance(){
        return instance;
    }

    public static class MyThread extends Thread{

        private String content;

        private MyThread myThread;

        public MyThread(String content) {
            this.content = content;
        }

        public MyThread(String content, MyThread myThread) {
            this.content = content;
            this.myThread = myThread;
        }

        @Override
        public void run(){
            if(Objects.nonNull(myThread)) {
                try {
                    myThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print(content);
        }
    }

    public static void main(String[] args) {
        MyThread a = new MyThread("abc");
        MyThread b = new MyThread("abc", a);
        MyThread c = new MyThread("abc", b);
        c.start();
        b.start();
        a.start();

    }
}
