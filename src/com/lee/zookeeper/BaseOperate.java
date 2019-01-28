package com.lee.zookeeper;

import org.apache.zookeeper.data.Stat;

/**
 * zk节点基本操作
 * @author lipan
 * @date 2018/9/28 14:39
 * @description
 */
public class BaseOperate extends ZkClientBase{

    private final static String path = "/lee";

    public static void main(String[] args) throws Exception{
        if(null == client.checkExists().forPath(path)) {
            System.out.println("create node");
            client.create().creatingParentsIfNeeded().forPath(path, "init".getBytes());
        }
        System.out.println(new String(client.getData().forPath(path)));
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(path);
        client.setData().withVersion(stat.getVersion()).forPath(path, new StringBuilder("init").append(stat.getVersion() + 1).toString().getBytes());
        //client.delete().forPath(path);
    }
}
