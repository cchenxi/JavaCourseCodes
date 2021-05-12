package io.github.kimmking.gateway.outbound.netty4.client;

import io.github.kimmking.gateway.inbound.HttpInboundInitializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

/**
 * netty http client , channel initializer
 * Date: 2021-05-12
 *
 * @author chenxi
 */
public class NettyHttpClientOutboundInitializer extends ChannelInitializer<SocketChannel> {
    private ChannelHandlerContext ctx;

    public NettyHttpClientOutboundInitializer(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        /**
         * 响应解码 和 请求编码
         * @see HttpInboundInitializer#initChannel(io.netty.channel.socket.SocketChannel)
         */
        p.addLast(new HttpResponseDecoder());
        p.addLast(new HttpRequestEncoder());
        p.addLast(new NettyHttpClientOutboundHandler(this.ctx));
    }
}
