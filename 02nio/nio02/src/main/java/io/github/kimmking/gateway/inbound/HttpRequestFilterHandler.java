package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.HttpRequestHeaderFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Date: 2020-11-03
 *
 * @author chenxi
 */
public class HttpRequestFilterHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            new HttpRequestHeaderFilter().filter(request, ctx);
        }
        super.channelRead(ctx, msg);
    }
}
