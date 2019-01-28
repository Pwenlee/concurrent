package com.lee.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Objects;

/**
 * @author lipan
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;

    @Override
    public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception{
        if(msg instanceof FullHttpRequest){
            handleHttpRequest(ctx, (FullHttpRequest)msg);
        }else if(msg instanceof WebSocketFrame){
            handleWebSocketRequest(ctx, (WebSocketFrame)msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception{
        if(!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))){
            sendHttpRequest(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket", null, false);
        handshaker =  wsFactory.newHandshaker(req);
        if(Objects.isNull(handshaker)){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else{
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private void handleWebSocketRequest(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception{
        if(frame instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(), ((CloseWebSocketFrame) frame).retain());
            return;
        }
        if(frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if(frame instanceof PongWebSocketFrame){
            //ie11在没有消息之后的30秒会发送pong，此时的处理是不回复 如果回复了ping 则ie11会回复pong 就陷入发送无效ping/pong的循环
            return;
        }
        if(!(frame instanceof TextWebSocketFrame)){
            throw new UnsupportedOperationException(String.format("%s frame type not support", frame.getClass().getName()));
        }
        String request = ((TextWebSocketFrame)frame).text();
        System.out.println(String.format("%s received %s", ctx.channel().id(), request));
        ChannelFuture channelFuture = ctx.writeAndFlush(new TextWebSocketFrame("11111111111"));
        channelFuture.addListener(future -> {
            if(Objects.nonNull(future.cause())){
                System.out.println("response success");
            }
        });
    }

    private static void sendHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse resp){
        if(resp.status().code() != 200){
            ByteBuf buf = Unpooled.copiedBuffer(resp.status().toString(), CharsetUtil.UTF_8);
            resp.content().writeBytes(buf);
            buf.release();
            HttpHeaderUtil.setContentLength(resp, resp.content().readableBytes());
        }
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(resp);
        if(!HttpHeaderUtil.isKeepAlive(req) || resp.status().code() != 200){
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
