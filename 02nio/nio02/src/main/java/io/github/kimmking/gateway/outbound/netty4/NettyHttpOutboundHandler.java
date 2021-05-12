package io.github.kimmking.gateway.outbound.netty4;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.github.kimmking.gateway.outbound.OutboundHandler;
import io.github.kimmking.gateway.outbound.httpclient4.NamedThreadFactory;
import io.github.kimmking.gateway.outbound.netty4.client.NettyHttpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Date: 2020-11-03
 *
 * @author chenxi
 */
public class NettyHttpOutboundHandler implements OutboundHandler {

    private ExecutorService proxyService;

    public NettyHttpOutboundHandler() {
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 1000;
        int queueSize = 2048;
        proxyService = new ThreadPoolExecutor(cores, cores, keepAliveTime, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        proxyService.execute(() -> new NettyHttpClient().fetchGet(fullRequest, ctx));
    }
}
