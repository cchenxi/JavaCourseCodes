package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.attribute.Attributes;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 工作线程处理器
 */
public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {

    private String proxyServers;

    public HttpInboundInitializer(String proxyServers) {
        this.proxyServers = proxyServers;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        // 使用 channel的attr传递网关代理的实际应用地址
        ch.attr(Attributes.PROXY_SERVERS).set(this.proxyServers);

        // 请求解码 和 响应编码
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(1024 * 1024));
        // 处理请求头
        p.addLast(new HttpRequestFilterHandler());
        // 处理负载均衡
        p.addLast(new HttpEndpointRouterHandler());
        // 处理实际调用
        p.addLast(new HttpInboundHandler());
    }
}
