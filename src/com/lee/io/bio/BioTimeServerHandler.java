package com.lee.io.bio;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author lipan
 */
public class BioTimeServerHandler implements Runnable{

    private Socket socket;

    public BioTimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run(){
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), Boolean.TRUE);
            String request;
            String response;
            while(true) {
                request = in.readLine();
                if(StringUtils.isBlank(request)){
                    break;
                }
                System.out.println("server receive order : " + request);
                response = (request.equals("QUERY TIME")) ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) : "BAD ORDER";
                out.println(response);
                System.out.println("server send response to client : " + response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (Objects.nonNull(in)) {
                    in.close();
                }
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
            if(Objects.nonNull(out)){
                out.close();
            }
        }
    }

}
