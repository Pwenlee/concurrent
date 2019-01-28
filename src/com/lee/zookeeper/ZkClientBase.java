package com.lee.zookeeper;

import org.apache.curator.framework.CuratorFramework;

/**
 * @author lipan
 * @date 2018/10/8 16:34
 * @description
 */
public class ZkClientBase {

    protected static CuratorFramework client = Instance.getInstance().getCuratorClient();



}
