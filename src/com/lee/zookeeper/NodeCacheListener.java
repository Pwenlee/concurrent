package com.lee.zookeeper;

import org.apache.curator.framework.recipes.cache.NodeCache;

/**
 * @author lipan
 * @date 2018/10/8 16:33
 * @description
 */
public class NodeCacheListener extends ZkClientBase{

    private static String path = "/lee";

    public static void main(String[] args) throws Exception{
        NodeCache nodeCache = new NodeCache(client, path, false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(()->
            System.out.println("node data update, new data "  + new String(nodeCache.getCurrentData().getData()))
        );
        Thread.sleep(Integer.MAX_VALUE);
    }
}
