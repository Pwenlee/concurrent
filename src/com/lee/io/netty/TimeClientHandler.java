package com.lee.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author lipan
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private ByteBuf message;

    public TimeClientHandler() {
        byte[] byteMessage = "QUERY TIME".getBytes();
        message = Unpooled.copiedBuffer(byteMessage);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String response = new String(bytes, "UTF-8");
        System.out.println("Now is " + response);
    }
}
