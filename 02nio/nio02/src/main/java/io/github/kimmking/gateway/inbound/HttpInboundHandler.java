package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.attribute.Attributes;
import io.github.kimmking.gateway.outbound.netty4.NettyHttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {
    private NettyHttpOutboundHandler handler;

    public HttpInboundHandler() {
        handler = new NettyHttpOutboundHandler();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            handler.handle((FullHttpRequest) msg, ctx);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
