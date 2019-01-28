package com.lee.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lipan
 */
public class BioTimeServer {

    private static final int PORT = 8088;

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors() * 2 + 1, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(64));
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(PORT);
            while (true){
                Socket socket = serverSocket.accept();
                executorService.submit(new BioTimeServerHandler(socket));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(Objects.nonNull(serverSocket)) {
                serverSocket.close();
            }
        }
    }
}
