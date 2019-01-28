package com.lee.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lipan
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String request = (String) msg;
        System.out.println("request is " + request);
        String response = (request.equals("QUERY TIME")) ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) : "BAD ORDER";
        ByteBuf resp = Unpooled.copiedBuffer(response.getBytes());
        ctx.writeAndFlush(resp);
    }
}
