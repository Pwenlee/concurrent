package com.lee.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

/**
 * @author lipan
 */
public class BioTimeClient {

    private static final int PORT = 8088;

    public static void main(String[] args) throws IOException {
        Socket socket;
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            socket = new Socket("127.0.0.1", PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), Boolean.TRUE);
            out.println("QUERY TIME");
            System.out.println("send order to server success");
            String response = in.readLine();
            System.out.println("server response : " + response);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(Objects.nonNull(in)){
                in.close();
            }
            if(Objects.nonNull(out)){
                out.close();
            }
        }
    }
}
